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

import java.math.BigInteger;

/**
 * Transform that negates each second element, starting at the given offset (0 or 1).
 * 
 * @author Tilman Neumann
 */
public class NegateAlternatingTransformation extends Transformation_SimpleImpl {

	private static String[] names = new String[]{"negateEven", "negateOdd"};
	
	private int offset;
	
	public NegateAlternatingTransformation(int offset) {
		if (offset<0 || offset>1) throw new IllegalArgumentException("offset must be 0 or 1 but is " + offset);
		this.offset = offset;
	}

	public String getName() {
		return names[offset];
	}
	
	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		BigInteger b_n = a.get(n);
		// add first offset elements without change
		// negate a[n] for even n if offset == 0
		// negate a[n] for odd n if offset == 1
		int nMinusOffset = n - offset;
		if (nMinusOffset>=0 && nMinusOffset%2==0) {
			b_n = b_n.negate();
		}
		return b_n;
	}
}
