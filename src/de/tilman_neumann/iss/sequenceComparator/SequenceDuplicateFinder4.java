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
package de.tilman_neumann.iss.sequenceComparator;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchCondition;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchHypothesisLinear;
import de.tilman_neumann.iss.sequenceMatch.MatchPoint;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchLinear;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchMatrix;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchMatrix2;
import de.tilman_neumann.iss.sequenceMatch.UniqueMatchPointIterator;
import de.tilman_neumann.util.SortedMultiset;

/**
 * A comparator for sequences.
 * @author Tilman Neumann
 */
public class SequenceDuplicateFinder4 extends SequenceMatchFinderBaseImpl {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SequenceDuplicateFinder4.class);
	
	public SequenceDuplicateFinder4(SequenceMatchCondition matchCondition) {
		super(matchCondition);
	}
	
	public void compare(OEISSequence s1, OEISSequence s2) throws SequenceMatchLinear {
		// 0. Compare index sets, abort if the possible number of matches is less than the wanted percentage.
		// Implementation notes: Creating multisets in a double loop is quite expensive, thus we better
		// prepare them when sequences are created (factor 4 performance improvement!)
		SortedMultiset<Integer> s1IndexMultiset = s1.getUnsignedValueIndexMultiset();
		SortedMultiset<Integer> s2IndexMultiset = s2.getUnsignedValueIndexMultiset();
		SortedMultiset<Integer> valueIndexIntersection = s1IndexMultiset.intersect(s2IndexMultiset);
		// TODO: use valueIndexIntersection in later steps
		int maxPossibleMatchCount = valueIndexIntersection.totalCount();
		if (!matchCondition.isSatisfied(s1.size(), s2.size(), maxPossibleMatchCount)) {
			return;
		}
		
		//LOG.debug("s1 = " + s1.nameAndValuesString());
		//LOG.debug("s2 = " + s2.nameAndValuesString());
		
		// 1. Compute match matrix. s1 will be the x- and s2 the y-axis; the indices are labeled 
		// x and y correspondingly. We compare the unsigned values to get sign-insensitive matches.
		// Implementation notes: Comparing (integer) indices is faster than comparing the proper big integer values.
//		SequenceMatchMatrix mm = new SequenceMatchMatrix1(s1, s2);
		SequenceMatchMatrix mm = new SequenceMatchMatrix2(s1, s2);
		//LOG.debug("match matrix:\n" + mm);
		
		// 2. Get unique match point, abort if there is no such point, because 
		// then a 1:1 hypothesis can not be computed
		UniqueMatchPointIterator umpIter = new UniqueMatchPointIterator(mm);
		if (!umpIter.hasNext()) {
			return;
		}
		MatchPoint mp1 = umpIter.next();
		
		// 3. Compute linear hypothesis y=1.0*x+b
		int b =  mp1.getSecond().intValue()-mp1.getFirst().intValue();
		SequenceMatchHypothesisLinear smh = new SequenceMatchHypothesisLinear(1, b);
		//LOG.debug("\n" + seq1.name + " & " + seq2.name + ": linear hypothesis: y = " + a + "*x + " + b);
		
		// 4. if all grid entries lying exactly on the line of the linear hypothesis, exit with success
		int matchCount = smh.check(mm);
		//LOG.debug("matchCount = " + matchCount);
		if (matchCondition.isSatisfied(s1.size(), s2.size(), matchCount)) {
			// found match with sufficient significance!
			throw new SequenceMatchLinear(s1, s2, smh, mm);
		}
	}
}
