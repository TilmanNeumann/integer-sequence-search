package de.tilman_neumann.iss.transform;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import java.math.BigInteger;

import de.tilman_neumann.util.ConfigUtil;

/**
 * Test of the Stirling transform and its inverse.
 * 
 * @author Tilman Neumann
 */
// 1 1 2 5 14 42 132 429 1430 4862 16796 58786 208012
public class StirlingTransformationTest {
	private static final Logger LOG = Logger.getLogger(StirlingTransformationTest.class);

	private StirlingTransformationTest() {
		// static class
	}

	public static void main(String[] args) throws TransformationException {
    	ConfigUtil.initProject();
    	int n = args.length;
    	LOG.info("n = " + n);
    	ArrayList<BigInteger> x = new ArrayList<BigInteger>(n);
    	for (int i=0;i<n;i++) {
    		String arg = args[i];
    		x.add(new BigInteger(arg));
    	}
    	LOG.info("x = " + x + "\n");

    	List<BigInteger> forward = new StirlingTransformation().compute(x);
    	LOG.info("Stirling transform = " + forward);
    	StirlingTransformation_Inverse inverseStirlingTransform = new StirlingTransformation_Inverse();
    	List<BigInteger> inverse = inverseStirlingTransform.compute(x);
    	LOG.info("inverse Stirling transform = " + inverse);
    	List<BigInteger> id = inverseStirlingTransform.compute(forward);
    	LOG.info("id transform = " + id);
	}
}
