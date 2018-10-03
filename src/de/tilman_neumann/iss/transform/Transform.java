package de.tilman_neumann.iss.transform;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues;

/**
 * an integer sequence that is the result of a transformation.
 * @author Tilman Neumann
 * @since 2011-09-14
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
