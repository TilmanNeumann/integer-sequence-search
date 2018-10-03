package de.tilman_neumann.math.app.oeis.transform;

import java.util.ArrayList;
import java.util.List;

import de.tilman_neumann.math.app.oeis.sequence.OEISSequence;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues_BigIntListImpl;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues_UnsignedIndexListImpl;
import de.tilman_neumann.math.base.bigint.BigIntConstants;

import java.math.BigInteger;

/**
 * Divides all elements of the given sequence by the first value, so that the
 * result sequence is normalized to a first value of 1.
 * 
 * @author Tilman Neumann
 * @since 2017-02-19
 */
public class NormTransformation extends Transformation {

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "norm";
	}

	/**
	 * {@inheritDoc}
	 */
	public int getNumberOfAdditionalOutputValues() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Transform compute(OEISSequence inputSeq, int maxNumberOfInputValues, boolean forLookup) throws TransformationException {
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);

		List<BigInteger> b = new ArrayList<BigInteger>(numberOfInputValues);
		if (numberOfInputValues>0) {
			BigInteger dividend = a.get(0);
			if (dividend.equals(BigIntConstants.ZERO)) throw new TransformationException("The input sequence starts with 0 -> The norm transform does not exist (division by zero)");
			if (dividend.equals(BigIntConstants.ONE)) throw new TransformationException("The input sequence starts with 1 -> The norm transform is equal to the input sequence");
			b.add(BigIntConstants.ONE); // a_0 / a_0
			for (int n=1; n<numberOfInputValues; n++) {
				b.add(computeNthValue(n, a, b, dividend));
			}
		}
		
		String name = this.getName(inputSeq.getName());
		SequenceValues outputValues = forLookup ? new SequenceValues_UnsignedIndexListImpl(b) : new SequenceValues_BigIntListImpl(b);
		return new Transform(name, outputValues, this, inputSeq, numberOfInputValues);
	}

	Transform expand(Transform oldTransform, int maxNumberOfInputValues) throws TransformationException {
		OEISSequence inputSeq = oldTransform.getInputSequence();
		int oldNumberOfInputValues = oldTransform.getNumberOfConsideredInputValues();
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);
		
		List<BigInteger> b = new ArrayList<BigInteger>(numberOfInputValues);
		// copy old values and get dividend
		// we don't need to check dividend values 0 and 1 because then the transform
		// would not exist and this method could not have been called
		BigInteger dividend = null;
		if (oldNumberOfInputValues>0) {
			b.addAll(oldTransform.getValues());
			dividend = a.get(0);
		} else if (numberOfInputValues>0) {
			b.add(BigIntConstants.ONE); // a_0 / a_0
			dividend = a.get(0);
			oldNumberOfInputValues = 1;
		}
		// compute new values
		for (int n=oldNumberOfInputValues; n<numberOfInputValues; n++) {
			b.add(computeNthValue(n, a, b, dividend));
		}
		
		SequenceValues oldTransformValues = oldTransform.getAbstractValues();
		SequenceValues outputValues =
			oldTransformValues instanceof SequenceValues_UnsignedIndexListImpl ?
				new SequenceValues_UnsignedIndexListImpl(b) :
			    new SequenceValues_BigIntListImpl(b);
		return new Transform(oldTransform.getName(), outputValues, this, inputSeq, numberOfInputValues);
	}
	
	/**
	 * Compute transform of n.th input value, for n>=1.
	 * @param n
	 * @param a
	 * @param b
	 * @param dividend
	 * @return
	 */
	private BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b, BigInteger dividend) throws TransformationException {
		BigInteger a_n = a.get(n);
		if (a_n.mod(dividend.abs()).equals(BigIntConstants.ZERO)) return a_n.divide(dividend);
		throw new TransformationException("The " + n + ".th result value is not integer: " + a_n + "/" + dividend);
	}
}
