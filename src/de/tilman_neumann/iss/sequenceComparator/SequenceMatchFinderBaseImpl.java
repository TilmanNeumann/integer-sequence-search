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

import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchCondition;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchLinear;

/**
 * A comparator for sequences.
 * @author Tilman Neumann
 */
abstract public class SequenceMatchFinderBaseImpl {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SequenceMatchFinderBaseImpl.class);

	// inputs
	protected SequenceMatchCondition matchCondition;
	
	public SequenceMatchFinderBaseImpl(SequenceMatchCondition matchCondition) {
		this.matchCondition = matchCondition;
		//LOG.debug("\n" + seq1.name + "\n & " + seq2.name);
	}
	
	/**
	 * Returns the first position of the given value in values, or null if not contained.
	 * @param values
	 * @param value
	 * @return first position or null
	 */
	public static Integer indexOf(List<Integer> values, Integer value) {
		if (values==null) return null;
		int index = values.indexOf(value);
		return (index>=0) ? Integer.valueOf(index) : null;
	}
		
	/**
	 * Returns the first position of the given value in values, or null if not contained.
	 * @param values
	 * @param value
	 * @return first position or null
	 */
	public static Integer indexOf(int[] values, int value) {
		if (values==null) {
			return null;
		}
		for (int pos=0; pos<values.length; pos++) {
			if (values[pos]==value) {
				return Integer.valueOf(pos);
			}
		}
		return null;
	}
	
	abstract public void compare(OEISSequence s1, OEISSequence s2) throws SequenceMatchLinear;
}
