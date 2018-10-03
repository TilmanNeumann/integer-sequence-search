/*
 * integer-sequence-search (ISS) is an offline OEIS sequence search engine.
 * Copyright (C) 2018 Tilman Neumann (www.tilman-neumann.de)
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, see <http://www.gnu.org/licenses/>.
 */
package de.tilman_neumann.iss.transform;

import java.util.ArrayList;
import java.util.List;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues;
import de.tilman_neumann.iss.sequence.SequenceValues_BigIntListImpl;
import de.tilman_neumann.iss.sequence.SequenceValues_UnsignedIndexListImpl;
import de.tilman_neumann.jml.base.BigIntConstants;

import java.math.BigInteger;

/**
 * Divides all elements of the given sequence by the first value, so that the
 * result sequence is normalized to a first value of 1.
 * 
 * @author Tilman Neumann
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
