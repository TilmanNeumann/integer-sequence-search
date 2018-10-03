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
package de.tilman_neumann.iss.transform;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues;
import de.tilman_neumann.jml.base.BigIntTriangle;

public class TriangleTransform extends Transform {

	private static final long serialVersionUID = -6197126686419557640L;

	private BigIntTriangle triangle;
	
	public TriangleTransform(String name, SequenceValues values,  BigIntTriangle triangle, Transformation transformation, OEISSequence inputSequence, int consideredInputValues) {
		super(name, values, transformation, inputSequence, consideredInputValues);
		this.triangle = triangle;
	}

	public BigIntTriangle getTriangle() {
		return this.triangle;
	}
}
