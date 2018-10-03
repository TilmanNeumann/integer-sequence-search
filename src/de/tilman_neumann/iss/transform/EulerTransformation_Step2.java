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

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues;
import de.tilman_neumann.iss.sequence.SequenceValues_BigIntListImpl;
import de.tilman_neumann.iss.sequence.SequenceValues_UnsignedIndexListImpl;

import java.math.BigInteger;

/**
 * Computation of the second part of the Euler transform of the given integer sequence.
 * 
 * @see http://mathworld.wolfram.com/EulerTransform.html
 * @author Tilman Neumann
 */
public class EulerTransformation_Step2 extends Transformation {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(EulerTransformation_Step2.class);

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "euler2";
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
	public Transform compute(OEISSequence inputSeq, int maxNumberOfInputValues, boolean forLookup) {
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);
		
		List<BigInteger> b = new ArrayList<BigInteger>(numberOfInputValues);
		if (numberOfInputValues>0) b.add(a.get(0)); // b_1 = a_1
		for (int n=1; n<numberOfInputValues; n++) {
			b.add(computeNthValue(n, a, b));
		}
		
		String name = this.getName(inputSeq.getName());
		SequenceValues outputValues = forLookup ? new SequenceValues_UnsignedIndexListImpl(b) : new SequenceValues_BigIntListImpl(b);
		return new Transform(name, outputValues, this, inputSeq, numberOfInputValues);
	}

	Transform expand(Transform oldTransform, int maxNumberOfInputValues) {
		OEISSequence inputSeq = oldTransform.getInputSequence();
		int oldNumberOfInputValues = oldTransform.getNumberOfConsideredInputValues();
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);
		
		List<BigInteger> b = new ArrayList<BigInteger>(numberOfInputValues);
		// copy old values
		if (oldNumberOfInputValues>0) {
			b.addAll(oldTransform.getValues());
		} else if (numberOfInputValues>0) {
			b.add(a.get(0)); // b_1 = a_1
			oldNumberOfInputValues = 1;
		}
		// compute new values
		//LOG.debug("oldNumberOfInputValues = " + oldNumberOfInputValues);
		for (int n=oldNumberOfInputValues; n<numberOfInputValues; n++) {
			//LOG.debug("computeNthValue(" + n + ", " + a + ", " + b + ")");
			b.add(computeNthValue(n, a, b));
		}
		
		SequenceValues oldTransformValues = oldTransform.getAbstractValues();
		SequenceValues outputValues =
			oldTransformValues instanceof SequenceValues_UnsignedIndexListImpl ?
				new SequenceValues_UnsignedIndexListImpl(b) :
			    new SequenceValues_BigIntListImpl(b);
		return new Transform(oldTransform.getName(), outputValues, this, inputSeq, numberOfInputValues);
	}
	
	private BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		BigInteger b_n = a.get(n); // list indices start with 0, c_i with 1
		for (int k=1; k<=n; k++) {
			BigInteger b_nk = b.get(n-k);
			b_n = b_n.add(a.get(k-1).multiply(b_nk));
		}
		b_n = b_n.divide(BigInteger.valueOf(n+1));
		return b_n;
	}
}
