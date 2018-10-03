package de.tilman_neumann.math.app.oeis.transform;

import java.math.BigInteger;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

import de.tilman_neumann.math.base.bigint.Divisors;

/**
 * Sloane's inverse Moebius transform.
 * Is sometimes also taken as the forward Moebius transform.
 * 
 * @author Tilman Neumann
 */
public class MoebiusTransformation_Inverse extends Transformation_SimpleImpl {
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
