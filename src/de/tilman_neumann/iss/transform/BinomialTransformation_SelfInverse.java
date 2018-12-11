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

import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.jml.combinatorics.Binomial;

import java.math.BigInteger;

/**
 * Computation of the _self-inverse_ binomial transform
 * (first definition given by Wikipedia). If the sequence has n elements,
 * then this is just the n-th forward difference of the sequence.
 * 
 * @see http://mathworld.wolfram.com/BinomialTransform.html
 * @see http://en.wikipedia.org/wiki/Binomial_transform
 * @author Tilman Neumann
 */
// 1 1 2 5 14 42 132 429 1430 4862 16796 58786 208012
public class BinomialTransformation_SelfInverse extends Transformation_SimpleImpl {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(BinomialTransformation_SelfInverse.class);

	public String getName() {
		return "selfInvBinomial";
	}
	
	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		BigInteger b_n = BigInteger.ZERO;
		for (int k=0; k<=n; k++) {
			BigInteger binomial = Binomial.binomial(n, k);
			//LOG.debug("binomial(" + n + ", " + k + ") = " + binomial);
			BigInteger elem = a.get(k).multiply(binomial);
			if (k%2!=0) {
				elem = elem.negate();
			}
			b_n = b_n.add(elem);
		}
		return b_n;
	}
}
