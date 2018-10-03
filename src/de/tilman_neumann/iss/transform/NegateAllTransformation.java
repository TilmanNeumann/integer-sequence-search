package de.tilman_neumann.iss.transform;

import java.util.List;

import java.math.BigInteger;

/**
 * Computes the sequence containing all negated entries of the input sequence.
 * @author Tilman Neumann
 * @since 2008-11-30
 */
public class NegateAllTransformation extends Transformation_SimpleImpl {

	public String getName() {
		return "negate";
	}
	
	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		return a.get(n).negate();
	}
}
