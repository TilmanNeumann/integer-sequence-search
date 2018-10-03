package de.tilman_neumann.math.app.oeis.transform;

import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.math.base.bigint.combinatorics.Stirling;

import java.math.BigInteger;

/**
 * Inverse Stirling transform.
 * 
 * @see http://mathworld.wolfram.com/StirlingTransform.html
 * @author Tilman Neumann
 */
// tested 2011-09-18, works
public class StirlingTransformation_Inverse extends Transformation_SimpleImpl {
	private static final Logger LOG = Logger.getLogger(StirlingTransformation_Inverse.class);

	public String getName() {
		return "invStirling";
	}
	
	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		BigInteger b_n = BigInteger.ZERO;
		for (int k=0; k<=n; k++) {
			// Get signed Stirling numbers of first kind:
			BigInteger stirling1 = Stirling.stirling1(n, k);
			//LOG.debug("stirling1(" + n + ", " + k + ") = " + stirling1);
			BigInteger elem = a.get(k).multiply(stirling1);
			b_n = b_n.add(elem);
		}
		return b_n;
	}
}
