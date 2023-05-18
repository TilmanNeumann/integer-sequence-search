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
package de.tilman_neumann.iss.sequenceComparator;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchCondition;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchLinear;
import de.tilman_neumann.util.SortedMultiset;

/**
 * A comparator for sequences.
 * @author Tilman Neumann
 */
public class SequenceDuplicateFinder extends SequenceMatchFinderBaseImpl {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SequenceDuplicateFinder.class);
	
	public SequenceDuplicateFinder(SequenceMatchCondition matchCondition) {
		super(matchCondition);
	}
	
	public void compare(OEISSequence s1, OEISSequence s2) throws SequenceMatchLinear {
		SortedMultiset<Integer> s1IndexMultiset = s1.getUnsignedValueIndexMultiset();
		SortedMultiset<Integer> s2IndexMultiset = s2.getUnsignedValueIndexMultiset();
		SortedMultiset<Integer> valueIndexIntersection = s1IndexMultiset.intersect(s2IndexMultiset);
		int maxPossibleMatchCount = valueIndexIntersection.totalCount();
		if (!matchCondition.isSatisfied(s1.size(), s2.size(), maxPossibleMatchCount)) {
			return;
		}
		//LOG.debug(s1.nameAndValuesString());
		//LOG.debug(s2.nameAndValuesString());
		//LOG.debug("valueIndexIntersection = " + valueIndexIntersection);
		
		// Note: valueIndexIntersection is an intersection. Therefore, joint
		// multiplicity 1 doesn't mean that the value is unique in each of both
		// sequences. It might occur more often in one of the two...
		Integer uniqueValueIndex = null;
		for (Integer valueIndex : valueIndexIntersection.keySet()) {
			if (s1IndexMultiset.get(valueIndex).intValue()==1 && s2IndexMultiset.get(valueIndex).intValue()==1) {
				uniqueValueIndex = valueIndex;
				break;
			}
		}
		//LOG.debug("uniqueValueIndex = " + uniqueValueIndex);
		if (uniqueValueIndex==null) {
			// computing hypotheses is still too stupid to do the job if there are no unique matches...
			return;
		}
		int matchCount = 0;
		int[] s1UnsignedIndices = s1.getUnsignedValueIndices();
		int[] s2UnsignedIndices = s2.getUnsignedValueIndices();
		Integer xPosition = indexOf(s1UnsignedIndices, uniqueValueIndex);
		Integer yPosition = indexOf(s2UnsignedIndices, uniqueValueIndex);
		int offset = xPosition.intValue() - yPosition.intValue();
		//LOG.debug("offset = " + offset);
		int xOffset = Math.max(0, offset);
		int yOffset = Math.max(0, -offset);
		int numSteps = Math.min(s1.size()-xOffset, s2.size()-yOffset);
		int xEndPos = numSteps+xOffset;
		for (int xPos=xOffset; xPos<xEndPos; xPos++) {
			int yPos = xPos-offset;
			boolean isMatch = (s1UnsignedIndices[xPos]==s2UnsignedIndices[yPos]);
			if (isMatch) {
				matchCount++;
			} else {
				if (xPos>2 && yPos>2) {
					// can not ignore mismatches at higher positions
					return;
				}
			}
		}
		
		//LOG.debug("matchCount = " + matchCount);
		if (matchCondition.isSatisfied(s1.size(), s2.size(), matchCount)) {
			// found match with sufficient significance!
			throw new SequenceMatchLinear(s1, s2, matchCount);
		}
	}
}
