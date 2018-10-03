package de.tilman_neumann.math.app.oeis.transform;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.math.base.bigint.BigIntConstants;
import de.tilman_neumann.util.ConfigUtil;

/**
 * Test of the Moebius transform for integer series.
 * 
 * @author Tilman Neumann
 */
public class MoebiusTransformationTest {
	private static final Logger LOG = Logger.getLogger(MoebiusTransformationTest.class);
	
	private MoebiusTransformationTest() {
		// static class
	}
	
	/**
	 * Tests
	 * 
	 * @param args Ignored
	 */
	public static void main(String[] args) throws TransformationException {
    	ConfigUtil.initProject();
    	List<BigInteger> series = new ArrayList<BigInteger>();
//		series.add(BigInt.ZERO);
		series.add(BigIntConstants.ONE);
//		series.add(BigInt.TWO);
		for (int i=1; i<10; i++) {
//			BigInt f = BigInt.factorial(i);
//			BigInt f = BigInt.ONE;
			BigInteger f = BigIntConstants.ZERO;
			series.add(f);
		}
		MoebiusTransformation moebiusTransform = new MoebiusTransformation();
		MoebiusTransformation_Inverse inverseMoebiusTransform = new MoebiusTransformation_Inverse();
		LOG.info("series (original) = " + series);
		series = moebiusTransform.compute(series);
		LOG.info("series (forward)   = " + series);
		series = moebiusTransform.compute(series);
		LOG.info("series (forward)   = " + series);
		series = inverseMoebiusTransform.compute(series);
		LOG.info("series (backward)   = " + series);
		series = inverseMoebiusTransform.compute(series);
		LOG.info("series (backward)   = " + series);
		series = inverseMoebiusTransform.compute(series);
		LOG.info("series (backward)   = " + series);
		series = inverseMoebiusTransform.compute(series);
		LOG.info("series (backward)   = " + series);
		series = moebiusTransform.compute(series);
		LOG.info("series (forward)   = " + series);
	}
}
