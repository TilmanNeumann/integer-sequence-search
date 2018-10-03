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

import de.tilman_neumann.jml.combinatorics.Stirling;

import java.math.BigInteger;

/**
 * Stirling transform.
 * 
 * @see http://mathworld.wolfram.com/StirlingTransform.html
 * @author Tilman Neumann
 */
public class StirlingTransformation extends Transformation_SimpleImpl {
	private static final Logger LOG = Logger.getLogger(StirlingTransformation.class);

	public String getName() {
		return "stirling";
	}
	
	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		BigInteger b_n = BigInteger.ZERO;
		for (int k=0; k<=n; k++) {
			BigInteger stirling2 = Stirling.stirling2(n, k);
			//LOG.debug("stirling2(" + n + ", " + k + ") = " + stirling2);
			BigInteger elem = a.get(k).multiply(stirling2);
			b_n = b_n.add(elem);
		}
		return b_n;
	}
}
