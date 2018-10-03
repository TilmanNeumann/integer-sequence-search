package de.tilman_neumann.iss.main;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchList;
import de.tilman_neumann.iss.transform.AbsTransformation;
import de.tilman_neumann.iss.transform.BinomialTransformation;
import de.tilman_neumann.iss.transform.BinomialTransformation_Inverse;
import de.tilman_neumann.iss.transform.BinomialTransformation_SelfInverse;
import de.tilman_neumann.iss.transform.ConvolutionTransformation;
import de.tilman_neumann.iss.transform.ConvolutionTransformationType;
import de.tilman_neumann.iss.transform.EulerTransformation;
import de.tilman_neumann.iss.transform.EulerTransformation_Inverse;
import de.tilman_neumann.iss.transform.EulerTransformation_Inverse_Step1;
import de.tilman_neumann.iss.transform.EulerTransformation_Inverse_Step2;
import de.tilman_neumann.iss.transform.EulerTransformation_Step1;
import de.tilman_neumann.iss.transform.EulerTransformation_Step2;
import de.tilman_neumann.iss.transform.IdTransformation;
import de.tilman_neumann.iss.transform.MoebiusTransformation;
import de.tilman_neumann.iss.transform.MoebiusTransformation_Inverse;
import de.tilman_neumann.iss.transform.NegateAllTransformation;
import de.tilman_neumann.iss.transform.NegateAlternatingTransformation;
import de.tilman_neumann.iss.transform.NormTransformation;
import de.tilman_neumann.iss.transform.PartitionTransformation;
import de.tilman_neumann.iss.transform.PartitionTransformation_Inverse;
import de.tilman_neumann.iss.transform.ShiftLeftTransformation;
import de.tilman_neumann.iss.transform.ShiftRightTransformation;
import de.tilman_neumann.iss.transform.StirlingTransformation;
import de.tilman_neumann.iss.transform.StirlingTransformation_Inverse;
import de.tilman_neumann.iss.transform.Transformation;

/**
 * Controller for lookup of various transforms of a sequence with the whole OEIS database.
 * @author Tilman Neumann
 * @since 2011-08-16
 */
public class LookupCentral {

	private static final long serialVersionUID = -9201914563761527886L;
	
	private static final Logger LOG = Logger.getLogger(LookupCentral.class);
	
	static Transformation[] simpleTransformations = new Transformation[]{
		new IdTransformation(), // truncates the source sequence
		new ShiftLeftTransformation(1),
		new ShiftLeftTransformation(2),
		new ShiftRightTransformation(new int[] {0}),
		new ShiftRightTransformation(new int[] {1}),
		new ShiftRightTransformation(new int[] {0, 1}),
		new ShiftRightTransformation(new int[] {1, 1}),
		new AbsTransformation(),
		new NegateAllTransformation(),
		new NegateAlternatingTransformation(0),
		new NegateAlternatingTransformation(1),
		new NormTransformation()
		// XXX: More transforms: mulPow2, mulFactorial, ...
	};

	static Transformation[] complexTransformations = new Transformation[]{
		// the id transformation converts transforms
		// from bigInt- to unsignedValueIndex-enconding
		new IdTransformation(),
		new BinomialTransformation(),
		new BinomialTransformation_Inverse(),
		new BinomialTransformation_SelfInverse(),
		new MoebiusTransformation(),
		new MoebiusTransformation_Inverse(),
		new StirlingTransformation(),
		new StirlingTransformation_Inverse(),
		new EulerTransformation(),
		new EulerTransformation_Step1(),
		new EulerTransformation_Step2(),
		new EulerTransformation_Inverse(),
		new EulerTransformation_Inverse_Step1(),
		new EulerTransformation_Inverse_Step2(),
		new PartitionTransformation(false, false),
		new PartitionTransformation_Inverse(false, false),
		new PartitionTransformation(false, true),
		new PartitionTransformation_Inverse(false, true),
		new PartitionTransformation(true, false),
		new PartitionTransformation_Inverse(true, false),
		new PartitionTransformation(true, true),
		new PartitionTransformation_Inverse(true, true),
		new ConvolutionTransformation(ConvolutionTransformationType.STANDARD),
		new ConvolutionTransformation(ConvolutionTransformationType.EXP),
		new ConvolutionTransformation(ConvolutionTransformationType.LCM),
		new ConvolutionTransformation(ConvolutionTransformationType.GCD),
		new ConvolutionTransformation(ConvolutionTransformationType.STIRLING1),
		new ConvolutionTransformation(ConvolutionTransformationType.ABS_STIRLING1),
		new ConvolutionTransformation(ConvolutionTransformationType.STIRLING2),
		new ConvolutionTransformation(ConvolutionTransformationType.DOUBLE_STIRLING1),
		new ConvolutionTransformation(ConvolutionTransformationType.DOUBLE_ABS_STIRLING1),
		new ConvolutionTransformation(ConvolutionTransformationType.DOUBLE_STIRLING2),
		new ConvolutionTransformation(ConvolutionTransformationType.STIRLING1_STIRLING2),
		new ConvolutionTransformation(ConvolutionTransformationType.ABS_STIRLING1_STIRLING2),
		new ConvolutionTransformation(ConvolutionTransformationType.STIRLING2_STIRLING1),
		new ConvolutionTransformation(ConvolutionTransformationType.STIRLING2_ABS_STIRLING1)
		// XXX: More transforms: exponential, logarithmic, Hankel, number theoretic, ...
	};
	
	private final int minNumberOfMatches;
	
	/**
	 * Standard constructor.
	 */
	public LookupCentral(int minNumberOfMatches) {
		this.minNumberOfMatches = minNumberOfMatches;
	}

	/**
	 * Prepares lookup sequences and returns the "simple" sequences found
	 * by the way.
	 * @param lookupSeq The sequence to lookup
	 * @param lookupMode What to lookup
	 * @param maxNumberOfValues Max. number of values of the lookup sequences to consider
	 * @return list of simple matches found
	 */
	public LookupSequenceStore prepareLookupSequences(OEISSequence lookupSeq, OeisLookupMode lookupMode, int maxNumberOfValues) {
		switch (lookupMode) {
		case DUPLICATES:
			// We only want to find simple matches of the lookupSeq.
			// Under this we understand duplicates (ignoring signs) and simple
			// transforms like shifts. Since the match algorithm works with 
			// unsigned values, the wanted matches will be found from the
			// lookup sequence alone; transforms are not required.
			LookupSequenceStore lookupSequences = new LookupSequenceStore(1, minNumberOfMatches, true, true); // add sequence without checks
			lookupSequences.add(lookupSeq);
			return lookupSequences;
		case COMPLEX_RELATIONS:
			// In this run mode, we try to find any matches.
			// first compute some simple transforms without multi-threading:
			LookupSequenceStore simpleTransforms = TransformThread.computeTransforms(lookupSeq, simpleTransformations, maxNumberOfValues, minNumberOfMatches, true);
			
			// now compute complex transforms of simple transforms,
			// and add only sequences that are substantially different:
			TransformThreadController threadPool = new TransformThreadController(complexTransformations, maxNumberOfValues, minNumberOfMatches, false);
			int i = 1;
			for (final OEISSequence simpleTransform : simpleTransforms) {
				//LOG.info("start lookup of sequence " + i + " ...");
				//LOG.info("simpleTransform = " + simpleTransform.nameAndValuesString());
				TransformThread transformer2 = threadPool.get(); // waits until there is a free thread!
				transformer2.setInputSeq(simpleTransform);
				Thread thread = new Thread(threadPool, transformer2, "TransformThread-"+i);
				thread.start(); // invokes Runnable.run()
				i++;
			}
			// waits for last threads to finish
			return threadPool.getResult();
		default:
			throw new IllegalStateException("lookupMode " + lookupMode + " still needs to be implemented.");
		}
	}
	
	public LookupSequenceStore expandLookupSequences(SequenceStore oldLookupSequences, int maxNumberOfValues) {
		ExpandThreadController threadPool = new ExpandThreadController(maxNumberOfValues, minNumberOfMatches, false);
		int i = 1;
		for (final OEISSequence oldLookupSequence : oldLookupSequences) {
			//LOG.info("start lookup of sequence " + i + " ...");
			//LOG.info("oldLookupSequence = " + simpleTransform.nameAndValuesString());
			ExpandThread transformer = threadPool.get(); // waits until there is a free thread!
			transformer.setInputSeq(oldLookupSequence);
			Thread thread = new Thread(threadPool, transformer, "ExpandThread-"+i);
			thread.start(); // invokes Runnable.run()
			i++;
		}
		// waits for last threads to finish
		return threadPool.getResult();
	}
	
	/**
	 * lookup of the given set of sequences.
	 * @param lookupSequences
	 * @return match report
	 */
	public SequenceMatchList lookup(LookupSequenceStore lookupSequences, SequenceStore oeisSequences) {
		// _this_ should not be the thread controller because then all
		// member variables would be synchronized, or not?
		LookupThreadController threadPool = new LookupThreadController(oeisSequences, minNumberOfMatches);
		
		int i = 1;
		for (final OEISSequence lookupSeq : lookupSequences) {
			//LOG.info("start lookup of sequence " + i + " ...");
			LookupThread lookupRunnable = threadPool.get(); // waits until there is a free thread!
			lookupRunnable.setLookupSeq(lookupSeq);
			Thread lookupThread = new Thread(threadPool, lookupRunnable, "LookupThread-"+i);
			lookupThread.start(); // invokes lookupRunnable.run()
			i++;
		}
		// waits for last threads to finish
		return threadPool.getResult();
	}
}
