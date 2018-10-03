package de.tilman_neumann.math.app.oeis.transform;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tilman_neumann.math.app.oeis.sequence.OEISSequence;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues_BigIntListImpl;

/**
 * Base class for transformations of integer sequences.
 * @author Tilman Neumann
 * @since ~2011-08-15
 */
abstract public class Transformation {
	
	private static final Map<Class<? extends Transformation>, Integer> priorities = new HashMap<Class<? extends Transformation>, Integer>(); static {
		priorities.put(IdTransformation.class, 0);
		
		// two simple transforms are simpler than one complex transform
		priorities.put(AbsTransformation.class, 100);
		priorities.put(NegateAllTransformation.class, 101);
		priorities.put(NegateAlternatingTransformation.class, 102);
		priorities.put(ShiftLeftTransformation.class, 103);
		priorities.put(ShiftRightTransformation.class, 104);
		priorities.put(NormTransformation.class, 105);
		
		// each complex transform alone is simpler than a complex plus a simple transform
		priorities.put(BinomialTransformation.class, 1000);
		priorities.put(BinomialTransformation_Inverse.class, 1001);
		priorities.put(BinomialTransformation_SelfInverse.class, 1002);
		priorities.put(EulerTransformation.class, 1003);
		priorities.put(EulerTransformation_Inverse.class, 1004);
		priorities.put(MoebiusTransformation.class, 1005);
		priorities.put(MoebiusTransformation_Inverse.class, 1006);
		priorities.put(StirlingTransformation.class, 1007);
		priorities.put(StirlingTransformation_Inverse.class, 1008);
		priorities.put(PartitionTransformation.class, 1009);
		priorities.put(PartitionTransformation_Inverse.class, 1010);
		priorities.put(EulerTransformation_Step1.class, 1011);
		priorities.put(EulerTransformation_Step2.class, 1012);
		priorities.put(EulerTransformation_Inverse_Step1.class, 1013);
		priorities.put(EulerTransformation_Inverse_Step2.class, 1014);
		// convolutions are ranked by type too, see class ConvolutionTransform
		priorities.put(ConvolutionTransformation.class, 1015);
	}
	
	/**
	 * @return complexity score (low values = low complexity = high result priority)
	 */
	public int getComplexityScore() {
		Integer score = priorities.get(this.getClass());
		if (score==null) throw new IllegalStateException("Transformation " + this.getName() + " has no attached complexity score!");
		return score.intValue();
	}
	
	/**
	 * Create a nested name composed of the name of this transform and the
	 * given inner name.
	 * @param innerName
	 * @return composed name
	 */
	public String getName(String innerName) {
		return this.getName() + "(" + innerName + ")";
	}
	
	/**
	 * Returns the (simple) name of this transform.
	 * @return name
	 */
	abstract public String getName();

	/**
	 * @return the number of output values generated from some input values
	 * minus the number of the input values.
	 */
	abstract public int getNumberOfAdditionalOutputValues();

	/**
	 * Compute the transform of the given values.
	 * Keep signature for backward compatibility with older tests...
	 * @throws TransformationException if the transform could not be computed
	 */
	List<BigInteger> compute(List<BigInteger> a) throws TransformationException {
		return compute(new OEISSequence("test", new SequenceValues_BigIntListImpl(a))).getValues();
	}

	/**
	 * Compute transform of all values of the input sequence, optimized for further computations.
	 * @param inputSeq
	 * @return
	 * @throws TransformationException if the transform could not be computed
	 */
	public Transform compute(OEISSequence inputSeq) throws TransformationException {
		return this.compute(inputSeq, inputSeq.size(), false);
	}
	
	/**
	 * Compute transform of all values of the input sequence.
	 * The result values data type is optimized for comparisons if forLookup==true,
	 * or for computations if forLookup==false.
	 * @param inputSeq
	 * @return
	 * @throws TransformationException if the transform could not be computed
	 */
	public Transform compute(OEISSequence inputSeq, boolean forLookup) throws TransformationException {
		return this.compute(inputSeq, inputSeq.size(), forLookup);
	}

	/**
	 * Compute the transform of up to maxNumberOfValues of the given input sequence.
	 * The result values data type is optimized for comparisons if forLookup==true,
	 * or for computations if forLookup==false.
	 * 
	 * @param inputSeq
	 * @param maxNumberOfInputValues
	 * @param forLookup
	 * @return
	 * @throws TransformationException if the transform could not be computed
	 */
	abstract public Transform compute(OEISSequence inputSeq, int maxNumberOfInputValues, boolean forLookup) throws TransformationException;

	/**
	 * Internal method required to expand existing transforms.
	 * @param oldTransform
	 * @param maxNumberOfInputValues
	 * @return expanded transform
	 * @throws TransformationException if the transform could not be computed
	 */
	abstract Transform expand(Transform oldTransform, int maxNumberOfInputValues) throws TransformationException;
}
