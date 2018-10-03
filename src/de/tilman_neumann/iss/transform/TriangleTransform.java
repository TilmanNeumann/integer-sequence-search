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
