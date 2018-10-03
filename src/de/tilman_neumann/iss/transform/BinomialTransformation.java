package de.tilman_neumann.iss.transform;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import java.math.BigInteger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues;
import de.tilman_neumann.iss.sequence.SequenceValues_BigIntListImpl;
import de.tilman_neumann.iss.sequence.SequenceValues_UnsignedIndexListImpl;
import de.tilman_neumann.jml.combinatorics.Binomial;

/**
 * Computes the (forward) binomial transform according to Sloane, which is the
 * inverse binomial transform in the terminology of Wolfram MathWorld.
 * 
 * @see http://mathworld.wolfram.com/BinomialTransform.html
 * @see http://en.wikipedia.org/wiki/Binomial_transform
 * @author Tilman Neumann
 */
// 1 1 2 5 14 42 132 429 1430 4862 16796 58786 208012
public class BinomialTransformation extends Transformation_SimpleImpl {
	private static final Logger LOG = Logger.getLogger(BinomialTransformation.class);

	public String getName() {
		return "binomial";
	}
	
	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		BigInteger b_n = BigInteger.ZERO;
		for (int k=0; k<=n; k++) {
			BigInteger binomial = Binomial.nk(n, k);
			//LOG.debug("binomial(" + n + ", " + k + ") = " + binomial);
			BigInteger elem = a.get(k).multiply(binomial);
			b_n = b_n.add(elem);
		}
		return b_n;
	}
}
