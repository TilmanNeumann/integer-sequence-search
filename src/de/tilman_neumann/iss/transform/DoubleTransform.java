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
package de.tilman_neumann.iss.transform;

import de.tilman_neumann.iss.sequence.SequenceValues;

/**
 * A two-step transform that keeps the intermediate result, too.
 * @author Tilman Neumann
 */
public class DoubleTransform extends Transform {

	private static final long serialVersionUID = 4372543537864681394L;
	
	private Transform step1Transform;
	
	public DoubleTransform(String name, SequenceValues values, Transformation transformation, Transform intermediateTransform) {
		super(name, values, transformation, intermediateTransform.getInputSequence(), intermediateTransform.getNumberOfConsideredInputValues());
		this.step1Transform = intermediateTransform;
	}
	
	public Transform getIntermediateTransform() {
		return step1Transform;
	}
}
