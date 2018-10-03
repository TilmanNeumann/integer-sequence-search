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

import de.tilman_neumann.util.Pair;

/**
 * A match of one value each of two sequences,
 * represented by its sequence positions.
 * @author Tilman Neumann
 */
public class MatchPoint extends Pair<Short, Short> {

	private static final long serialVersionUID = -2897646298951181487L;

	public MatchPoint(Short u, Short v) {
		super(u, v);
	}
	
	public MatchPoint(short u, short v) {
		super(Short.valueOf(u), Short.valueOf(v));
	}
}
