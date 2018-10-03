package de.tilman_neumann.math.app.oeis.transform;

import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.jml.combinatorics.Binomial;

import java.math.BigInteger;

/**
 * Computes the inverse binomial transform according to Sloane, which is the
 * forward binomial transform in the terminology of Wolfram MathWorld.
 * 
 * @see http://mathworld.wolfram.com/BinomialTransform.html
 * @see http://en.wikipedia.org/wiki/Binomial_transform
 * @author Tilman Neumann
 */
// 1 1 2 5 14 42 132 429 1430 4862 16796 58786 208012
public class BinomialTransformation_Inverse extends Transformation_SimpleImpl {
	private static final Logger LOG = Logger.getLogger(BinomialTransformation_Inverse.class);

	public String getName() {
		return "invBinomial";
	}
	
	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		BigInteger b_n = BigInteger.ZERO;
		for (int k=0; k<=n; k++) {
			BigInteger binomial = Binomial.nk(n, k);
			//LOG.debug("binomial(" + n + ", " + k + ") = " + binomial);
			BigInteger elem = a.get(k).multiply(binomial);
			if ((n-k)%2!=0) {
				elem = elem.negate();
			}
			b_n = b_n.add(elem);
		}
		return b_n;
	}
}
