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

/**
 * an integer sequence that is the result of a transformation.
 * @author Tilman Neumann
 */
public class Transform extends OEISSequence {

	private static final long serialVersionUID = 8518176890956482312L;
	
	private Transformation transformation;
	private OEISSequence inputSequence;
	private int consideredInputValues;
	
	public Transform(String name, SequenceValues values, Transformation transformation, OEISSequence inputSequence, int consideredInputValues) {
		super(name, values);
		this.transformation = transformation;
		this.inputSequence = inputSequence;
		this.consideredInputValues = consideredInputValues;
	}

	public void setInputSequence(Transform expandedInputTransform) {
		this.inputSequence = expandedInputTransform;
	}

	public OEISSequence getInputSequence() {
		return this.inputSequence;
	}
	
	public Transformation getTransformation() {
		return this.transformation;
	}

	public void setNumberOfConsideredInputValues(int numberOfValues) {
		this.consideredInputValues = numberOfValues;
	}

	public int getNumberOfConsideredInputValues() {
		return consideredInputValues;
	}
	
	public int getComplexityScore() {
		int score = getTransformation().getComplexityScore();
		OEISSequence inputSequence = getInputSequence();
		if (inputSequence instanceof Transform) {
			Transform inputTransform = (Transform) inputSequence;
			score += inputTransform.getComplexityScore();
		}
		return score;
	}

	/**
	 * Expands the this transform to up to maxNumberOfValues values.
	 * 
	 * @param wantedNumberOfInputValues
	 * @return this expanded
	 * @throws TransformationException if the transform could not be computed
	 */
	public Transform expand(int wantedNumberOfInputValues) throws TransformationException {
		// first expand input sequence if necessary and possible
		OEISSequence inputSeq = getInputSequence();
		if (wantedNumberOfInputValues>inputSeq.size()) {
			// input sequence needs expansion
			if (inputSeq instanceof Transform) {
				// expansion seems to be possible.
				// if the input transform produces extra output values,
				// then it itself needs less inputs...
				Transform inputTransform = (Transform) inputSeq;
				Transformation inputTransformation = inputTransform.getTransformation();
				int numberOfAdditionalOutputValues = inputTransformation.getNumberOfAdditionalOutputValues();
				Transform expandedInputTransform = inputTransform.expand(wantedNumberOfInputValues - numberOfAdditionalOutputValues);
				setInputSequence(expandedInputTransform);
			}
		}
		
		// now expand the given sequence
		return transformation.expand(this, wantedNumberOfInputValues);
	}
}
