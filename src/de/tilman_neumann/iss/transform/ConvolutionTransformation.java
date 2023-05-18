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

import org.apache.log4j.Logger;
import java.math.BigInteger;

import de.tilman_neumann.jml.combinatorics.Binomial;
import de.tilman_neumann.jml.combinatorics.Stirling;
import de.tilman_neumann.jml.gcd.Gcd;

/**
 * Implementation of convolution type transforms.
 * 
 * @author Tilman Neumann
 */
public class ConvolutionTransformation extends Transformation_SimpleImpl {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ConvolutionTransformation.class);

	private ConvolutionTransformationType type;
	
	public ConvolutionTransformation(ConvolutionTransformationType type) {
		this.type = type;
	}

	public String getName() {
		return this.type.getName();
	}
	
	public int getComplexityScore() {
		return super.getComplexityScore() + type.getComplexityScore();
	}

	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		BigInteger b_n = BigInteger.ZERO;
		for (int k=0; k<=n; k++) {
			BigInteger elem = null;
			switch (type) {
			case STANDARD: {
				elem = a.get(k).multiply(a.get(n-k));
				break;
			}
			case EXP: {
				BigInteger coeff = Binomial.binomial(n, k);
				//LOG.debug("binomial(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case LCM: {
				elem = Gcd.lcm(a.get(k), a.get(n-k));
				break;
			}
			case GCD: {
				elem = a.get(k).gcd(a.get(n-k));
				break;
			}
			case STIRLING1: {
				BigInteger coeff = Stirling.stirling1(n, k);
				//LOG.debug("stirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case ABS_STIRLING1: {
				BigInteger coeff = Stirling.absStirling1(n, k);
				//LOG.debug("absStirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case STIRLING2: {
				BigInteger coeff = Stirling.stirling2(n, k);
				//LOG.debug("stirling2(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case DOUBLE_STIRLING1: {
				BigInteger coeff = Stirling.stirling1(n, k).multiply(Stirling.stirling1(n, n-k));
				//LOG.debug("double stirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case DOUBLE_ABS_STIRLING1: {
				BigInteger coeff = Stirling.stirling1(n, k).multiply(Stirling.stirling1(n, n-k)).abs();
				//LOG.debug("double absStirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case DOUBLE_STIRLING2: {
				BigInteger coeff = Stirling.stirling2(n, k).multiply(Stirling.stirling2(n, n-k));
				//LOG.debug("double stirling2(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case STIRLING1_STIRLING2: {
				BigInteger coeff = Stirling.stirling1(n, k).multiply(Stirling.stirling2(n, n-k));
				//LOG.debug("stirling1*stirling2(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case ABS_STIRLING1_STIRLING2: {
				BigInteger coeff = Stirling.absStirling1(n, k).multiply(Stirling.stirling2(n, n-k));
				//LOG.debug("absStirling1*stirling2(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case STIRLING2_STIRLING1: {
				BigInteger coeff = Stirling.stirling2(n, k).multiply(Stirling.stirling1(n, n-k));
				//LOG.debug("stirling2*stirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case STIRLING2_ABS_STIRLING1: {
				BigInteger coeff = Stirling.stirling2(n, k).multiply(Stirling.absStirling1(n, n-k));
				//LOG.debug("stirling2*absStirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			default:
				throw new IllegalArgumentException("Illegal convolution transformation type: " + type);
			}
			b_n = b_n.add(elem);
		}
		return b_n;
	}
}
