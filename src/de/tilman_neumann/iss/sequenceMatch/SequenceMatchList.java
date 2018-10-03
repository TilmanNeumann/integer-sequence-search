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

import de.tilman_neumann.util.SortOrder;
import de.tilman_neumann.util.SortedList;

/**
 * List of matches of one lookup with nicer toString() implementation than
 * standard list.
 * @author Tilman Neumann
 */
public class SequenceMatchList extends SortedList<SequenceMatch> {

	private static final long serialVersionUID = 7515592729171336037L;

	private static SequenceMatchComparator comp = new SequenceMatchComparator();

	public SequenceMatchList() {
		// matches with bigger matchscore or smaller A-number are listed first
		super(comp, SortOrder.DESCENDING);
	}
	
	@Override
	public String toString() {
		StringBuilder bu = new StringBuilder();
		if (this.size()==0) {
			return "nothing found...";
		}
		
		float lastScore = -1;
		for (SequenceMatch match : this) {
			float newScore = match.getMatchCount();
			if (newScore!=lastScore) {
				bu.append("matchScore = " + newScore + ":\n");
			}
			bu.append("\t" + match.getRefName() + ", " + match.getLookupSequence().getName() + "\n");
			lastScore = newScore;
		}
		return bu.toString();
	}
}
