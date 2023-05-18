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
package de.tilman_neumann.iss.sequenceComparison;

import java.util.List;

import de.tilman_neumann.iss.sequenceMatch.MatchPoint;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchMatrix;

public interface SequenceMatchHypothesis {
	/**
	 * Checks a sequence match hypothesis
	 * @param mm sequence match matrix 
	 * @return match score (0 if hypothesis is discarded)
	 */
	int check(SequenceMatchMatrix mm);
	
	int getMatchScore();
	
	/**
	 * @return the position pairs of the matches of this hypothesis
	 */
	List<MatchPoint> getHypothesisMatches();
	
	boolean needsXExpansion(SequenceMatchMatrix mm);
	
	/**
	 * @return the hypothesis as a String
	 */
	String toString();
}
