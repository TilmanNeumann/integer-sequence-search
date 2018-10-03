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

public class SequenceMatchConditionAsymmetric implements SequenceMatchCondition {

	private static final float log2 = (float) Math.log(2);
	private int desiredMatchCount;
	
	public SequenceMatchConditionAsymmetric(int desiredMatchCount) {
		this.desiredMatchCount = desiredMatchCount;
	}	
	
	public int getMinNumberOfMatches() {
		return this.desiredMatchCount;
	}

	/* (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequenceComparison.XXX#isSatisfied(int, int, int)
	 */
	public boolean isSatisfied(int s1Size, int s2Size, int matchCount) {
		if (matchCount > desiredMatchCount) {
			// this is always enough
			return true;
		}
		final int allowedDeviationFromDesiredMatchCount = (s1Size>3) ? (int) (Math.log(s1Size-3)/log2) : 0;
		final int requiredMatchCount = s1Size - allowedDeviationFromDesiredMatchCount;
		return (matchCount >= requiredMatchCount);
	}
}
