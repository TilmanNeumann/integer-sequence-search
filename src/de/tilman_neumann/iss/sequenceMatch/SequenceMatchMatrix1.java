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
package de.tilman_neumann.iss.sequenceMatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.tilman_neumann.iss.sequence.OEISSequence;

/**
 * A match matrix for two sequences.
 * @author Tilman Neumann
 */
public class SequenceMatchMatrix1 extends SequenceMatchMatrix {

	private static final long serialVersionUID = 6177298374247406064L;
	
	private Map<Integer, Set<Short>> yValuesToPositions;
	private Map<Integer, Set<Short>> yValues2xPositions;
	private boolean[][] matches;
	
	public SequenceMatchMatrix1(OEISSequence s1, OEISSequence s2) {
		super(s1, s2);
		
		// Search procedure:
		// 1. Compute match matrix. s1 will be the x- and s2 the y-axis; 
		// the indices are labeled correspondingly x and y.
		// With this we get all y-indices for a certain value:
		Map<Integer, Set<Short>> xValuesToPositions = hashSequenceValues2Positions(s1.getUnsignedValueIndices());
		//LOG.debug("xValuesToPositions = " + xValuesToPositions);
		int[] s2UnsignedValueIndices = s2.getUnsignedValueIndices();
		yValuesToPositions = hashSequenceValues2Positions(s2UnsignedValueIndices);
	
		// initialize arrays:
		matches = new boolean[xDim][yDim]; // false-initialized
		this.yValues2xPositions = new HashMap<Integer, Set<Short>>();

		for (int y=0; y<yDim; y++) {
			// this is the value of seq2 for row y:
			Integer seq2Value = s2UnsignedValueIndices[y];
			// where does it occur in seq1? these are the matches for row y:
			Set<Short> xPositions = xValuesToPositions.get(seq2Value);
			if (xPositions!=null && xPositions.size()>0) {
				this.yValues2xPositions.put(seq2Value, xPositions);
				for (Short xPosition : xPositions) {
					short x = xPosition.shortValue();
					matches[x][y] = true;
				}
			}
		}
		//LOG.debug("match matrix:\n" + this);
	}
	
	public boolean isMatch(short x, short y) {
		return matches[x][y];
	}

	@Override
	public Map<Integer, Set<Short>> getyValues2Positions() {
		return yValuesToPositions;
	}

	@Override
	public Map<Integer, Set<Short>> getyValues2xPositions() {
		return yValues2xPositions;
	}
}
