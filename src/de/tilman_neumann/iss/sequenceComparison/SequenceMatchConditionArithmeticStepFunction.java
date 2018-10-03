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
package de.tilman_neumann.iss.sequenceComparison;

public class SequenceMatchConditionArithmeticStepFunction implements SequenceMatchCondition {

	private int minNumberOfMatches;

	public SequenceMatchConditionArithmeticStepFunction(int minNumberOfMatches) {
		this.minNumberOfMatches = minNumberOfMatches;
	}
	
	public int getMinNumberOfMatches() {
		return this.minNumberOfMatches;
	}

	/* (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequenceComparison.XXX#isSatisfied(int, int, int)
	 */
	public boolean isSatisfied(int s1Size, int s2Size, int numberOfMatches) {
		int minSeqLen = Math.min(s1Size, s2Size);
		return ((numberOfMatches-minNumberOfMatches) >= requiredNumberOfMatches(minSeqLen-minNumberOfMatches));
	}
	
	private int requiredNumberOfMatches(int n) {
		return ( (int) (Math.sqrt(2*n+1/4) - 1/2) );
	}
}
