/*
 * integer-sequence-search (ISS) is an offline OEIS sequence search engine.
 * Copyright (C) 2018 Tilman Neumann - tilman.neumann@web.de
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
import java.util.SortedSet;

import org.apache.log4j.Logger;
import java.math.BigInteger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues;
import de.tilman_neumann.iss.sequence.SequenceValues_BigIntListImpl;
import de.tilman_neumann.iss.sequence.SequenceValues_UnsignedIndexListImpl;
import de.tilman_neumann.jml.Divisors;
import de.tilman_neumann.jml.MoebiusFunction;

/**
 * Computation of the inverse Euler transform of the given integer sequence.
 * 
 * @see http://mathworld.wolfram.com/EulerTransform.html
 * @author Tilman Neumann
 */
public class EulerTransformation_Inverse extends Transformation {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(EulerTransformation_Inverse.class);

	private final Transformation step1Transformation = new EulerTransformation_Inverse_Step1();

	public String getName() {
		return "invEuler";
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
			throw new IllegalStateException("InvEuler step 1 transformation should not throw TransformationException", e);
		}
		List<BigInteger> c = cTransform.getValues();
		int numberOfInputValues	= cTransform.getNumberOfConsideredInputValues();
		
		// compute second step inline, optimized for computation or lookup:
		List<BigInteger> b = new ArrayList<BigInteger>(numberOfInputValues);
		for (int n=0; n<numberOfInputValues; n++) {
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
			throw new IllegalStateException("InvEuler step 1 transformation should not throw TransformationException", e);
		}
		List<BigInteger> c = cTransform.getValues();
		int numberOfInputValues	= cTransform.getNumberOfConsideredInputValues();

		// expand second transform:
		List<BigInteger> b = new ArrayList<BigInteger>(numberOfInputValues);
		// copy old values
		b.addAll(oldTransform.getValues());
		// compute new values
		int oldNumberOfInputValues = oldTransform.getNumberOfConsideredInputValues();
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
	
	/**
	 * Compute n.th value of the second step.
	 * Note: Equal to Sloane's forward Moebius transform divided by n.
	 * 
	 * @param n
	 * @param c
	 * @param b
	 * @return
	 */
	private BigInteger computeNthValueOfStep2(int n, List<BigInteger> c, List<BigInteger> b) {
		BigInteger nBig = BigInteger.valueOf(n+1);
		SortedSet<BigInteger> divisors = Divisors.getDivisors(nBig);
		//LOG.debug("divisors(" + n + ") = " + divisors);
		BigInteger b_n = BigInteger.ZERO;
		//String b_n_str = "";
		for (BigInteger d : divisors) {
			BigInteger moebiusArg = nBig.divide(d);
			BigInteger moebiusVal = BigInteger.valueOf(MoebiusFunction.moebius(moebiusArg));
			//LOG.debug("moebius(" + moebiusArg + ") = " + moebiusVal);
			BigInteger elem = moebiusVal.multiply(c.get(d.intValue()-1));
			b_n = b_n.add(elem);
			//b_n_str += (elem.compareTo(BigIntConstants.ZERO)<0) ? elem : "+"+elem;
		}
		b_n = b_n.divide(nBig);
		//LOG.debug("b_" + n + " = ( " + b_n_str + " ) / " + nBig + " = " + b_n);
		return b_n;
	}
}
