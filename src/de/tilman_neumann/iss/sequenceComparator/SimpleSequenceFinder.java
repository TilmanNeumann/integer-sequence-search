package de.tilman_neumann.iss.sequenceComparator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchArithmetic;

/**
 * Algorithm that recognizes "simple sequences".
 * So far, these include arithmetic series of constants or step functions of arbitrary order.
 * @author Tilman Neumann
 * @since ~2008-12-20
 */
public class SimpleSequenceFinder {
	
	private static final Logger LOG = Logger.getLogger(SimpleSequenceFinder.class);

	// the number of fitting numbers required to identify a "simple sequence" 
	private int minMatchCount;
	
	/**
	 * Full constructor.
	 * @param minMatchCount
	 */
	public SimpleSequenceFinder(int minMatchCount) {
		this.minMatchCount = minMatchCount;
	}
	
	/**
	 * @return the minimum number of numbers of a sequence fitting a "simple sequence" hypothesis
	 */
	public int getMinMatchCount() {
		return this.minMatchCount;
	}
	
	/**
	 * Checks if the given sequence is "simple", i.e. an arithmetic progression
	 * of constants or a step function of arbitrary order. The first three values
	 * are ignored.
     *
	 * Note that even arithmetic progressions of order 10 or so may be interesting
	 * if we investigate a family of sequences; therefore a maximum order threshold
	 * should not be considered.
	 * 
	 * @throws SequenceMatchArithmetic if arithmetic progression
	 */
	public void testForSimpleSequence(OEISSequence lookupSeq) throws SequenceMatchArithmetic {
		List<List<BigInteger>> allValueLists = new LinkedList<List<BigInteger>>();
		// ignore first three values...
		List<BigInteger> vals = lookupSeq.getValues();
		int len = vals.size();
		if (len < minMatchCount) return; // not enough evidence...
		
		int customizedMinMatchCount = Math.max(minMatchCount, 4); // must be one more than ignored start values
		while (len>=customizedMinMatchCount) {
			// remember value list for printing
			allValueLists.add(vals);
			
			SimpleSequenceType type = SimpleSequenceType.CONSTANT;
			BigInteger[] segmentValues = new BigInteger[3];
			int[] segmentSizes = new int[3];
			//LOG.debug("vals = " + vals);
			Iterator<BigInteger> importantValsIter = vals.subList(3, len).iterator();
			segmentValues[0] = importantValsIter.next().abs();
			segmentSizes[0] = 1;
			int segmentCount = 1;
			while (importantValsIter.hasNext()) {
				BigInteger unsignedNext = importantValsIter.next().abs();
				if (unsignedNext.equals(segmentValues[segmentCount-1])) {
					segmentSizes[segmentCount-1]++;
					continue;
				}
				// the new value doesn't match the last segment...
				if (segmentCount == 1) {
					// start of new segment
					segmentValues[1] = unsignedNext;
					segmentSizes[1] = 1;
					segmentCount = 2;
					type = SimpleSequenceType.STEP_FUNCTION;
					continue;
				}
				if (segmentCount==2 && unsignedNext.equals(segmentValues[0])) {
					segmentValues[2] = unsignedNext;
					segmentSizes[2] = 1;
					segmentCount = 3;
					type = SimpleSequenceType.SINGLE_PEAK;
					continue;
				}
				// too many distinct values, check next order
				type = null;
				break;
			}
			
			if (type!=null) {
				// found some kind of arithmetic progression!
				int significance = len - 3;
				if (vals.get(2).equals(segmentValues[0])) {
					significance++;
					if (vals.get(1).equals(segmentValues[0])) {
						significance++;
						if (vals.get(0).equals(segmentValues[0])) {
							significance++;
						}
					}
				}
				// reduce significance by 1 for step function and by 2 for peak function
				significance = significance - segmentCount + 1;
				if (significance >= customizedMinMatchCount) {
					int order = allValueLists.size() - 1;
					throw new SequenceMatchArithmetic(lookupSeq, type, order, significance, allValueLists);
				} // else: not significant enough
			}
			
			// compute next differences:
			List<BigInteger> newVals = new ArrayList<BigInteger>(len-1);
			Iterator<BigInteger> valsIter2 = vals.iterator();
			BigInteger last = valsIter2.next();
			int i=1;
			while (valsIter2.hasNext()) {
				BigInteger next = valsIter2.next();
				if (next==null) {
					LOG.debug("vals = " + vals);
					LOG.debug("next()=vals[" + i + "] is null!");
				}
				i++;
				newVals.add(next.subtract(last));
				last = next;
			}
			vals = newVals;
			len--;
		}
	}
}
