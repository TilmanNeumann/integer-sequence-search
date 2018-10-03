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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import java.math.BigInteger;

import de.tilman_neumann.jml.base.BigIntTriangle;
import de.tilman_neumann.util.ConfigUtil;

/**
 * Test of partition transforms.
 * 
 * @author Tilman Neumann
 */
public class PartitionTransformationTest {
	
	private static final Logger LOG = Logger.getLogger(PartitionTransformationTest.class);

	private PartitionTransformationTest() {
		// static class
	}

    public static void main(String[] args) { 
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
    	
    	BigIntTriangle t1 = new PartitionTransformation(false, false).computeTriangle(x);
//    	LOG.info("T1[n,k] =\n" + t1.toString());
//    	LOG.info("T1[n,k] as list =\n" + t1.toList());
    	List<BigInteger> a1 = t1.getRowSums();
    	LOG.info("a1(n) =\n" + a1);
    	BigIntTriangle t2 = new PartitionTransformation(true, false).computeTriangle(x);
    	LOG.info("T2[n,k] =\n" + t2.toString());
//    	LOG.info("T2[n,k] as list =\n" + t2.toList());
    	List<BigInteger> a2 = t2.getRowSums();
    	LOG.info("a2(n) =\n" + a2);
    	BigIntTriangle t3 = new PartitionTransformation(true, true).computeTriangle(x);
//    	LOG.info("T3[n,k] =\n" + t3.toString());
//    	LOG.info("T3[n,k] as list =\n" + t3.toList());
    	List<BigInteger> a3 = t3.getRowSums();
    	LOG.info("a3(n) =\n" + a3 + "\n");

    	PartitionTransformation_Inverse inverseUnweightedRaw = new PartitionTransformation_Inverse(false, false);
    	PartitionTransformation_Inverse inverseWeightedRaw = new PartitionTransformation_Inverse(true, false);
    	PartitionTransformation_Inverse inverseWeightedCentral = new PartitionTransformation_Inverse(true, true);
    	
    	BigIntTriangle it1 = inverseUnweightedRaw.computeTriangle(x);
//    	LOG.info("iT1[n,k] =\n" + it1.toString());
//    	LOG.info("iT1[n,k] as list =\n" + it1.toList());
    	List<BigInteger> ia1 = it1.getRowSums();
    	LOG.info("ia1(n) =\n" + ia1);
    	BigIntTriangle it2 = inverseWeightedRaw.computeTriangle(x);
    	LOG.info("iT2[n,k] =\n" + it2.toString());
//    	LOG.info("iT2[n,k] as list =\n" + it2.toList());
    	List<BigInteger> ia2 = it2.getRowSums();
    	LOG.info("ia2(n) =\n" + ia2);
    	BigIntTriangle it3 = inverseWeightedCentral.computeTriangle(x);
//    	LOG.info("iT3[n,k] =\n" + it3.toString());
//    	LOG.info("iT3[n,k] as list =\n" + it3.toList());
    	List<BigInteger> ia3 = it3.getRowSums();
    	LOG.info("ia3(n) =\n" + ia3 + "\n");
  
    	List<BigInteger> id1 = inverseUnweightedRaw.computeTriangle(a1).getRowSums();
    	LOG.info("id1(n) =\n" + id1);
    	if (!x.equals(id1)) {
    		LOG.warn("transform 1 and inverse do not yield identity!");
    	}
    	List<BigInteger> id2 = inverseWeightedRaw.computeTriangle(a2).getRowSums();
    	LOG.info("id2(n) =\n" + id2);
    	if (!x.equals(id2)) {
    		LOG.warn("transform 2 and inverse do not yield identity!");
    	}
    	List<BigInteger> id3 = inverseWeightedCentral.computeTriangle(a3).getRowSums();
    	LOG.info("id3(n) =\n" + id3);
    	if (!x.equals(id3)) {
    		LOG.warn("transform 3 and inverse do not yield identity!");
    	}

        LOG.info("duration = " + (System.currentTimeMillis()-start) + "ms");
    }
}
