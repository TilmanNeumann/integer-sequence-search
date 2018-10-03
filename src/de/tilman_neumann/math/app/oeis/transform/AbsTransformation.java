package de.tilman_neumann.math.app.oeis.transform;

import java.util.List;

import java.math.BigInteger;

/**
 * Computes the absolute transform, i.e. the sequence containing the absolute values
 * of the values in the original sequence.
 * 
 * @author Tilman Neumann
 * @since 2008-11-30
 */
public class AbsTransformation extends Transformation_SimpleImpl {

	public String getName() {
		return "abs";
	}
	
	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		return a.get(n).abs();
	}
}
