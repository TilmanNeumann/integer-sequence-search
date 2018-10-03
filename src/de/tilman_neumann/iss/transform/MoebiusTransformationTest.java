/*
 * integer-sequence-search (ISS) is an offline OEIS sequence search engine.
 * Copyright (C) 2018 Tilman Neumann (www.tilman-neumann.de)
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.jml.base.BigIntConstants;
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
