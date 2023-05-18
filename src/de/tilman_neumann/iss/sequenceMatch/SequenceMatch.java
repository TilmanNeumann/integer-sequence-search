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
package de.tilman_neumann.iss.sequenceMatch;

import de.tilman_neumann.iss.sequence.OEISSequence;

/**
 * A probable match of two sequences.
 * @author Tilman Neumann
 */
abstract public class SequenceMatch extends Exception {

	private static final long serialVersionUID = -2797052573592024437L;

	/**
	 * @return the number of matching values
	 */
	abstract public int getMatchCount();
	
	/**
	 * @return the name of the database sequence
	 */
	abstract public String getRefName();
	
	/**
	 * @return the sequence (often a transform) that was compared to the database
	 */
	abstract public OEISSequence getLookupSequence();
}
