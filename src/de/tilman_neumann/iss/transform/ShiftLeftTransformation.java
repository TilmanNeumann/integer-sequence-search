package de.tilman_neumann.iss.transform;

import java.util.ArrayList;
import java.util.List;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues;
import de.tilman_neumann.iss.sequence.SequenceValues_BigIntListImpl;
import de.tilman_neumann.iss.sequence.SequenceValues_UnsignedIndexListImpl;

import java.math.BigInteger;

/**
 * Computes a left shift of the sequence by 'offset' elements:
 * The first elements of the original list are dropped and for the rest
 * we have b(n)=a(n+steps).
 * 
 * @author Tilman Neumann
 * @since 2008-11-30
 */
public class ShiftLeftTransformation extends Transformation {

	private int offset;
	private String name;
	
	public ShiftLeftTransformation(int offset) {
		this.offset = offset;
		this.name = "shift" + offset + "Left";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getNumberOfAdditionalOutputValues() {
		return -offset;
	}

	/**
	 * {@inheritDoc}
	 */
	public Transform compute(OEISSequence inputSeq, int maxNumberOfInputValues, boolean forLookup) {
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);
		
		// skip <offset> start values of the input sequence
		List<BigInteger> b = (numberOfInputValues > offset) ? new ArrayList<BigInteger>(a.subList(offset, numberOfInputValues)) : new ArrayList<BigInteger>();
		
		String name = this.getName(inputSeq.getName());
		SequenceValues outputValues = forLookup ? new SequenceValues_UnsignedIndexListImpl(b) : new SequenceValues_BigIntListImpl(b);
		return new Transform(name, outputValues, this, inputSeq, numberOfInputValues);
	}

	Transform expand(Transform oldTransform, int maxNumberOfInputValues) {
		OEISSequence inputSeq = oldTransform.getInputSequence();
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);
		
		// skip <offset> start values of the input sequence
		List<BigInteger> b = (numberOfInputValues > offset) ? new ArrayList<BigInteger>(a.subList(offset, numberOfInputValues)) : new ArrayList<BigInteger>();
		
		SequenceValues oldTransformValues = oldTransform.getAbstractValues();
		SequenceValues outputValues =
			oldTransformValues instanceof SequenceValues_UnsignedIndexListImpl ?
				new SequenceValues_UnsignedIndexListImpl(b) :
			    new SequenceValues_BigIntListImpl(b);
		return new Transform(oldTransform.getName(), outputValues, this, inputSeq, numberOfInputValues);
	}
}
