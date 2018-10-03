package de.tilman_neumann.math.app.oeis.transform;

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
 * @since 2011-09-16
 */
public class EulerTransformation_Inverse_Step2 extends Transformation_SimpleImpl {
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
