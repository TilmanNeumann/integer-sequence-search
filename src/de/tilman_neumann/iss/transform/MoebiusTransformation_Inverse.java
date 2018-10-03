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

import java.math.BigInteger;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

import de.tilman_neumann.jml.Divisors;

/**
 * Sloane's inverse Moebius transform.
 * Is sometimes also taken as the forward Moebius transform.
 * 
 * @author Tilman Neumann
 */
public class MoebiusTransformation_Inverse extends Transformation_SimpleImpl {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MoebiusTransformation_Inverse.class);

	public String getName() {
		return "invMoebius";
	}
	
	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		// first compute divisors
		SortedSet<BigInteger> divisors = Divisors.getDivisors(BigInteger.valueOf(n+1));
		//LOG.debug("divisors(" + n + ") = " + divisors);
		
		// transform current list element
		BigInteger b_n = BigInteger.ZERO;
		for (BigInteger d : divisors) {
			BigInteger a_d = a.get(d.intValue()-1);
			b_n = b_n.add(a_d);
		}
		return b_n;
	}
}
