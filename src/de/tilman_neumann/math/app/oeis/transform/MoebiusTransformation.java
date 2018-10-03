package de.tilman_neumann.math.app.oeis.transform;

import java.math.BigInteger;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

import de.tilman_neumann.jml.Divisors;
import de.tilman_neumann.jml.MoebiusFunction;

/**
 * Sloane's forward Moebius transform.
 * Is sometimes also called the inverse Moebius transform.
 * 
 * @author Tilman Neumann
 */
public class MoebiusTransformation extends Transformation_SimpleImpl {
	private static final Logger LOG = Logger.getLogger(MoebiusTransformation.class);

	public String getName() {
		return "moebius";
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
		return b_n;
	}
}
