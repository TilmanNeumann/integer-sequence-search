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
 * Computation of the Euler transform of the given integer sequence.
 * 
 * @see http://mathworld.wolfram.com/EulerTransform.html
 * @author Tilman Neumann
 */
public class EulerTransformation extends Transformation {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(EulerTransformation.class);

	/** 
	 * 1st step of Euler transform.
	 * Note: Similar to Sloane's inverse Moebius transform, the only difference being the weight by d.
	 */
	private final Transformation step1Transformation = new EulerTransformation_Step1();
	
	public String getName() {
		return "euler";
	}
	
	public int getNumberOfAdditionalOutputValues() {
		return 0;
	}
	
	public Transform compute(OEISSequence inputSeq, int maxNumberOfInputValues, boolean forLookup) {
		// compute intermediate series c, optimized for further computations:
		Transform cTransform;
		try {
			cTransform = step1Transformation.compute(inputSeq, maxNumberOfInputValues, false);
		} catch (TransformationException e) {
			throw new IllegalStateException("Euler step 1 transformation should not throw TransformationException", e);
		}
		List<BigInteger> c = cTransform.getValues();
		int numberOfInputValues	= cTransform.getNumberOfConsideredInputValues();
		
		// compute second step inline, optimized for computation or lookup:
		List<BigInteger> b = new ArrayList<BigInteger>(numberOfInputValues);
		if (numberOfInputValues>0) b.add(c.get(0)); // b_1 = c_1
		for (int n=1; n<numberOfInputValues; n++) {
			b.add(computeNthValueOfStep2(n, c, b));
		}
		
		// do not mention intermediate step in the name of the transform
		String name = this.getName(inputSeq.getName());
		SequenceValues outputValues = forLookup ? new SequenceValues_UnsignedIndexListImpl(b) : new SequenceValues_BigIntListImpl(b);
		return new DoubleTransform(name, outputValues, this, cTransform);
	}

	Transform expand(Transform oldTransform, int maxNumberOfInputValues) {
		// expand intermediate transform
		Transform oldIntermediateTransform = ((DoubleTransform)oldTransform).getIntermediateTransform();
		Transform cTransform;
		try {
			cTransform = oldIntermediateTransform.expand(maxNumberOfInputValues);
		} catch (TransformationException e) {
			throw new IllegalStateException("Euler step 1 transformation should not throw TransformationException", e);
		}
		List<BigInteger> c = cTransform.getValues();
		int numberOfInputValues	= cTransform.getNumberOfConsideredInputValues();

		// expand second transform:
		List<BigInteger> b = new ArrayList<BigInteger>(numberOfInputValues);
		// copy old values
		int oldNumberOfInputValues = oldTransform.getNumberOfConsideredInputValues();
		if (oldNumberOfInputValues>0) {
			b.addAll(oldTransform.getValues());
		} else if (numberOfInputValues>0) {
			b.add(c.get(0));
			oldNumberOfInputValues = 1;
		}
		// compute new values
		for (int n=oldNumberOfInputValues; n<numberOfInputValues; n++) {
			b.add(computeNthValueOfStep2(n, c, b));
		}
		
		SequenceValues oldTransformValues = oldTransform.getAbstractValues();
		SequenceValues outputValues =
			oldTransformValues instanceof SequenceValues_UnsignedIndexListImpl ?
				new SequenceValues_UnsignedIndexListImpl(b) :
			    new SequenceValues_BigIntListImpl(b);
		return new DoubleTransform(oldTransform.getName(), outputValues, this, cTransform);
	}
	
	private BigInteger computeNthValueOfStep2(int n, List<BigInteger> c, List<BigInteger> b) {
		BigInteger b_n = c.get(n);
		for (int k=1; k<=n; k++) {
			b_n = b_n.add(c.get(k-1).multiply(b.get(n-k)));
		}
		b_n = b_n.divide(BigInteger.valueOf(n+1));
		return b_n;
	}
}
