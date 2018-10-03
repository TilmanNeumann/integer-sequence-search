package de.tilman_neumann.math.app.oeis.main;

import org.apache.log4j.Logger;

import de.tilman_neumann.math.app.oeis.sequence.OEISSequence;
import de.tilman_neumann.math.app.oeis.transform.Transformation;
import de.tilman_neumann.math.app.oeis.transform.TransformationException;

/**
 * Computation of sequence transforms, unthreaded or threaded.

 * @author Tilman Neumann
 * @since 2017-02-19
 */
public class TransformThread implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(TransformThread.class);

	private final TransformThreadController listener;

	private Transformation[] transformations;
	private int maxNumberOfValues;
	private boolean allowSimilarResults;

	private OEISSequence inputSeq;

	/**
	 * Full constructor for threaded transform computations.
	 * @param transformations
	 * @param maxNumberOfValues
	 * @param allowSimilarResults
	 * @param listener
	 */
	public TransformThread(Transformation[] transformations, int maxNumberOfValues, boolean allowSimilarResults, TransformThreadController listener) {
		this.transformations = transformations;
		this.maxNumberOfValues = maxNumberOfValues;
		this.allowSimilarResults = allowSimilarResults;
		this.listener = listener;
	}

	/**
	 * Set the sequence to transform.
	 * Must be called before invoking the run() method.
	 * @param inputSeq
	 */
	public void setInputSeq(OEISSequence inputSeq) {
		this.inputSeq = inputSeq;
	}
	
	/**
	 * Computes transforms of the input sequence in a new thread.
	 */
	public void run() {
		if (inputSeq == null) throw new NullPointerException("input sequence");
		//LOG.info("input seq = " + inputSeq.nameAndValuesString());
		
		// compute transforms:
		for (Transformation transformation : this.transformations) {
			try {
				OEISSequence seq = transformation.compute(inputSeq, maxNumberOfValues, !allowSimilarResults);
				this.listener.addPartialResult(seq);
			} catch (TransformationException e) {
				LOG.info("Could not compute " + transformation.getName() + " transform of sequence " + inputSeq.getName());
			}
		}
		this.listener.notifyFinish(this); // wake up control thread
	}

	/**
	 * Computes transforms of the input sequence without threads.
	 * 
	 * @return computed transforms
	 */
	public static LookupSequenceStore computeTransforms(OEISSequence inputSeq, Transformation[] transformations, int maxNumberOfValues, int minNumberOfMatches, boolean allowSimilarResults) {
		if (inputSeq == null) throw new NullPointerException("input sequence");
		//LOG.info("input seq = " + inputSeq.nameAndValuesString());
		
		// do not log intermediate results
		LookupSequenceStore sequences = new LookupSequenceStore(transformations.length, minNumberOfMatches, allowSimilarResults, false);
		// compute simple transforms:
		for (Transformation transformation : transformations) {
			try {
				sequences.add(transformation.compute(inputSeq, maxNumberOfValues, !allowSimilarResults));
			} catch (TransformationException te) {
				LOG.info("Discard " + transformation.getName() + " transform of sequence " + inputSeq.getName() + ". Reason: " + te.getMessage());
			}
		}
		return sequences;
	}
}
