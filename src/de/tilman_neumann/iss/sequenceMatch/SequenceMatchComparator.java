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

import java.util.Comparator;

public 	class SequenceMatchComparator implements Comparator<SequenceMatch> {
	/**
	 * Compares two sequence matches, first by match score, second by refName.
	 * @param o1 first match
	 * @param o2 second match
	 * @return <0/0/>0 if match1 shall be displayed before match2
	 */
	public int compare(SequenceMatch o1, SequenceMatch o2) {
		int scoreCmp = Float.valueOf(o1.getMatchCount()).compareTo(Float.valueOf(o2.getMatchCount()));
		if (scoreCmp != 0) {
			return scoreCmp;
		}
		// smaller A-numbers shall give bigger score to list first
		return (-o1.getRefName().compareTo(o2.getRefName()));
	}
}
