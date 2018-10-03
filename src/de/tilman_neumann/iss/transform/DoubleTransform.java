package de.tilman_neumann.iss.transform;

import de.tilman_neumann.iss.sequence.SequenceValues;

/**
 * A two-step transform that keeps the intermediate result, too.
 * @author Tilman Neumann
 * @since 2011-09-17
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
