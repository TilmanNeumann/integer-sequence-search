package de.tilman_neumann.math.app.oeis.transform;

import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.math.base.bigint.combinatorics.Stirling;

import java.math.BigInteger;

/**
 * Stirling transform.
 * 
 * @see http://mathworld.wolfram.com/StirlingTransform.html
 * @author Tilman Neumann
 */
// tested 2011-09-18, works
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
