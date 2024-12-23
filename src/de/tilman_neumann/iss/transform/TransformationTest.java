/*
 * integer-sequence-search (ISS) is an offline OEIS sequence search engine.
 * Copyright (C) 2018 Tilman Neumann - tilman.neumann@web.de
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, see <http://www.gnu.org/licenses/>.
 */
package de.tilman_neumann.iss.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import java.math.BigInteger;

import de.tilman_neumann.jml.base.BigIntTriangle;
import de.tilman_neumann.util.ConfigUtil;

public class TransformationTest {
	
	private static final Logger LOG = Logger.getLogger(TransformationTest.class);

	private TransformationTest() {
		// static class
	}

	/**
	 * Test.
	 * @param args Command line arguments = sequence of numbers
	 */
    public static void main(String[] args) throws TransformationException {
    	ConfigUtil.initProject();
    	long start = System.currentTimeMillis();
    	int n = args.length;
    	LOG.info("n = " + n);
    	ArrayList<BigInteger> x = new ArrayList<BigInteger>(n);
    	for (int i=0;i<n;i++) {
    		String arg = args[i];
    		StringTokenizer tok = new StringTokenizer(arg, ",");
    		while (tok.hasMoreTokens()) {
    			String token = tok.nextToken().trim();
    			if (token.length()>0) {
    	    		x.add(new BigInteger(token));
    			}
    		}
    	}
    	// recompute number of entries:
    	n = x.size();
       	LOG.info("n = " + n);
    	LOG.info("x = " + x + "\n");
    	
    	List<BigInteger> euler = new EulerTransformation().compute(x);
    	LOG.info("euler(x) = \n" + euler);
    	
    	List<BigInteger> euler_inverse = new EulerTransformation_Inverse().compute(x);
    	LOG.info("euler_inverse(x) = \n" + euler_inverse);
    	
    	BigIntTriangle euler2Triangle = new PartitionTransformation(false, false).computeTriangle(x);
    	LOG.info("euler2Triangle(x) = \n" + euler2Triangle);
    	List<BigInteger> euler2 = euler2Triangle.getRowSums();
    	LOG.info("euler2(x) = \n" + euler2);
    	
    	BigIntTriangle euler2InverseTriangle = new PartitionTransformation_Inverse(false, false).computeTriangle(x);
    	LOG.info("euler2InverseTriangle(x) = \n" + euler2InverseTriangle);
    	List<BigInteger> euler2_inverse = euler2InverseTriangle.getRowSums();
    	LOG.info("euler2_inverse(x) = \n" + euler2_inverse);

    	BigIntTriangle exponentialTriangle = new PartitionTransformation(true, false).computeTriangle(x);
    	LOG.info("exponentialTriangle(x) = \n" + exponentialTriangle);
    	List<BigInteger> exponential = exponentialTriangle.getRowSums();
    	LOG.info("exponential(x) = \n" + exponential);

    	BigIntTriangle logarithmicTriangle = new PartitionTransformation_Inverse(true, false).computeTriangle(x);
    	LOG.info("logarithmicTriangle(x) = \n" + logarithmicTriangle);
    	List<BigInteger> logarithmic = logarithmicTriangle.getRowSums();
    	LOG.info("logarithmic(x) = \n" + logarithmic);

    	BigIntTriangle centralExponentialTriangle = new PartitionTransformation(true, true).computeTriangle(x);
    	LOG.info("centralExponentialTriangle(x) = \n" + centralExponentialTriangle);
    	List<BigInteger> centralExponential = centralExponentialTriangle.getRowSums();
    	LOG.info("centralExponential(x) = \n" + centralExponential);

    	BigIntTriangle centralExponentialInverseTriangle = new PartitionTransformation_Inverse(true, true).computeTriangle(x);
    	LOG.info("centralExponentialInverseTriangle(x) = \n" + centralExponentialInverseTriangle);
    	List<BigInteger> centralExponential_inverse = centralExponentialInverseTriangle.getRowSums();
    	LOG.info("centralExponential_inverse(x) = \n" + centralExponential_inverse);

    	List<BigInteger> binomial = new BinomialTransformation().compute(x);
    	LOG.info("binomial(x) = \n" + binomial);

    	List<BigInteger> binomial_inverse = new BinomialTransformation_Inverse().compute(x);
    	LOG.info("binomial_inverse(x) = \n" + binomial_inverse);

    	List<BigInteger> binomial_selfInverse = new BinomialTransformation_SelfInverse().compute(x);
    	LOG.info("binomial_selfInverse(x) = \n" + binomial_selfInverse);

    	List<BigInteger> moebius = new MoebiusTransformation().compute(x);
    	LOG.info("moebius(x) = \n" + moebius);

    	List<BigInteger> moebius_inverse = new MoebiusTransformation_Inverse().compute(x);
    	LOG.info("moebius_inverse(x) = \n" + moebius_inverse);

    	List<BigInteger> stirling = new StirlingTransformation().compute(x);
    	LOG.info("stirling(x) = \n" + stirling);

    	List<BigInteger> stirling_inverse = new StirlingTransformation_Inverse().compute(x);
    	LOG.info("stirling_inverse(x) = \n" + stirling_inverse);
  	
        LOG.info("duration = " + (System.currentTimeMillis()-start) + "ms");
    }
}
