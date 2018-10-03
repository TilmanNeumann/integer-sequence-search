package de.tilman_neumann.iss.transform;

import java.util.List;

import java.math.BigInteger;

/**
 * Transform that negates each second element, starting at the given offset (0 or 1).
 * 
 * @author Tilman Neumann
 * @since 2008-11-30
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
