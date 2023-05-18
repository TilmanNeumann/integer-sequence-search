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

import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

import de.tilman_neumann.jml.Divisors;
import de.tilman_neumann.jml.MoebiusFunction;

import java.math.BigInteger;

/**
 * Computation of the second part of the inverse Euler transform of the given integer sequence.
 * 
 * Note: Equal to Sloane's forward Moebius transform divided by n.
 * 
 * @see http://mathworld.wolfram.com/EulerTransform.html
 * @author Tilman Neumann
 */
public class EulerTransformation_Inverse_Step2 extends Transformation_SimpleImpl {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(EulerTransformation_Inverse_Step2.class);

	public String getName() {
		return "invEuler2";
	}
	
	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		// first compute divisors
		BigInteger nBig = BigInteger.valueOf(n+1);
		SortedSet<BigInteger> divisors = Divisors.getDivisors(nBig);
		//LOG.debug("divisors(" + n + ") = " + divisors);
		
		// transform current list element
		BigInteger b_n = BigInteger.ZERO;
		for (BigInteger d : divisors) {
			BigInteger moebiusArg = nBig.divide(d);
			BigInteger moebiusVal = BigInteger.valueOf(MoebiusFunction.moebius(moebiusArg));
			//LOG.debug("moebius(" + moebiusArg + ") = " + moebiusVal);
			BigInteger elem = moebiusVal.multiply(a.get(d.intValue()-1));
			b_n = b_n.add(elem);
		}
		b_n = b_n.divide(nBig);
		return b_n;
	}
}
