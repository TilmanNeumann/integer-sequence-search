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

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues_BigIntListImpl;
import de.tilman_neumann.jml.base.BigIntList;
import de.tilman_neumann.test.junit.ClassTest;

/**
 * Test of Sloane's Euler transform and its inverse.
 * 
 * @see http://mathworld.wolfram.com/EulerTransform.html
 * @author Tilman Neumann
 */
public class EulerTransformationTest extends ClassTest {
	private static final Logger LOG = Logger.getLogger(EulerTransformationTest.class);
	
	public void testA000292() throws TransformationException {
		BigIntList a000292 = BigIntList.valueOf(/*0, */"1, 4, 10, 20, 35, 56, 84, 120, 165, 220, 286, 364, 455, 560, 680, 816, 969, 1140, 1330, 1540, 1771, 2024, 2300, 2600, 2925, 3276"/*, 3654, 4060, 4495, 4960, 5456, 5984, 6545, 7140, 7770, 8436, 9139, 9880, 10660, 11480, 12341*/);
		OEISSequence seqA000292 = new OEISSequence("A000292", new SequenceValues_BigIntListImpl(a000292));
		LOG.info("A000292 = " + a000292);
		
		// forward transformation: result should be
		BigIntList a000335 = BigIntList.valueOf("1,5,15,45,120,331,855,2214,5545,13741,33362,80091,189339,442799,1023192,2340904,5302061,11902618,26488454,58479965,128120214,278680698,602009786,1292027222,2755684669,5842618668");
		LOG.info("A000335 = " + a000335);
		Transformation eulerT = new EulerTransformation();
		Transform result = eulerT.compute(seqA000292, 10, true).expand(a000292.size());
		LOG.info("euler(A000292) = " + result.getValues());
		assertEquals(a000335, result.getValues());
		
		// inverse transform: result should again be A000292 as above
		Transformation invEulerT = new EulerTransformation_Inverse();
		Transform invResult = invEulerT.compute(result, 10, true).expand(a000292.size());
		LOG.info("invEuler(euler(A000292)) = " + invResult.getValues());
		assertEquals(a000292, invResult.getValues());
		// 2011-09-19: works now :)
	}
}
