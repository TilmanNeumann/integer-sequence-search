package de.tilman_neumann.math.app.oeis.transform;

import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

import de.tilman_neumann.jml.Divisors;

import java.math.BigInteger;

/**
 * Computation of the first part of the Euler transform of the given integer sequence.
 * 
 * Note: Similar to Sloane's inverse Moebius transform, the only difference being the weight by d.
 * 
 * @see http://mathworld.wolfram.com/EulerTransform.html
 * @author Tilman Neumann
 * @since 2011-09-16
 */
public class EulerTransformation_Step1 extends Transformation_SimpleImpl {
	private static final Logger LOG = Logger.getLogger(EulerTransformation_Step1.class);

	public String getName() {
		return "euler1";
	}
	
	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		// first compute divisors
		SortedSet<BigInteger> divisors = Divisors.getDivisors(BigInteger.valueOf(n+1));
		//LOG.debug("divisors(" + n + ") = " + divisors);
		
		// transform current list element
		BigInteger b_n = BigInteger.ZERO;
		for (BigInteger d : divisors) {
			BigInteger a_d = a.get(d.intValue()-1); // list indices start with 0, a_i with 1
			b_n = b_n.add(d.multiply(a_d));
		}
		return b_n;
	}
}
