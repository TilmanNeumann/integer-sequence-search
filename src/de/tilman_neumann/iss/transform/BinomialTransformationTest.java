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

import org.apache.log4j.Logger;
import java.math.BigInteger;

import de.tilman_neumann.util.ConfigUtil;

/**
 * Test of binomial transforms and their inverses.
 * 
 * @see http://mathworld.wolfram.com/BinomialTransform.html
 * @see http://en.wikipedia.org/wiki/Binomial_transform
 * @author Tilman Neumann
 */
// 1 1 2 5 14 42 132 429 1430 4862 16796 58786 208012
public class BinomialTransformationTest {
	private static final Logger LOG = Logger.getLogger(BinomialTransformationTest.class);

	private BinomialTransformationTest() {
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

    	BinomialTransformation forwardTransform = new BinomialTransformation();
    	BinomialTransformation_Inverse inverseTransform = new BinomialTransformation_Inverse();
    	BinomialTransformation_SelfInverse selfInverseTransform = new BinomialTransformation_SelfInverse();
    	List<BigInteger> forward = forwardTransform.compute(x);
    	LOG.info("Sloane's forward binomial transform = " + forward);
    	List<BigInteger> inverse = inverseTransform.compute(x);
    	LOG.info("Sloane's inverse binomial transform = " + inverse);
    	List<BigInteger> id = inverseTransform.compute(forward);
    	LOG.info("Sloane's id transform = " + id);
    	List<BigInteger> selfInverse = selfInverseTransform.compute(x);
    	LOG.info("selfInverse transform = " + selfInverse);
    	List<BigInteger> selfInverseId = selfInverseTransform.compute(selfInverse);
    	LOG.info("selfInverse id transform = " + selfInverseId);
	}
}
