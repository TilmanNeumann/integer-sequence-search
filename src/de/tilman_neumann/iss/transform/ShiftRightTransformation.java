package de.tilman_neumann.iss.transform;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues;
import de.tilman_neumann.iss.sequence.SequenceValues_BigIntListImpl;
import de.tilman_neumann.iss.sequence.SequenceValues_UnsignedIndexListImpl;

import java.math.BigInteger;

/**
 * Computes a right shift of the original sequence by inserting
 * the new elements at the beginning.
 * 
 * @author Tilman Neumann
 * @since 2008-11-30
 */
public class ShiftRightTransformation extends Transformation {
	
	private static final Logger LOG = Logger.getLogger(ShiftRightTransformation.class);

	private int[] sElems;
	private String name;
	
	public ShiftRightTransformation(int[] sElems) {
		this.sElems = sElems;
		this.name = "shiftRight";
		for (int sElem : sElems) {
			this.name += sElem;
		}
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
		return sElems.length;
	}

	/**
	 * {@inheritDoc}
	 */
	public Transform compute(OEISSequence inputSeq, int maxNumberOfInputValues, boolean forLookup) {
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);
		
		int sElemsCount = sElems!=null ? sElems.length : 0;
		List<BigInteger> b = new ArrayList<BigInteger>(sElemsCount + a.size());
		// add start elements
		if (sElemsCount>0) {
			for (int sElem : sElems) {
				BigInteger elem = BigInteger.valueOf(sElem);
				b.add(elem);
			}
		}
		// add all values from a
		b.addAll(a);
		
		String name = this.getName(inputSeq.getName());
		SequenceValues outputValues = forLookup ? new SequenceValues_UnsignedIndexListImpl(b) : new SequenceValues_BigIntListImpl(b);
		return new Transform(name, outputValues, this, inputSeq, numberOfInputValues);
	}

	Transform expand(Transform oldTransform, int maxNumberOfInputValues) {
		OEISSequence inputSeq = oldTransform.getInputSequence();
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);
		
		// copying all elements again from the beginning is probably as efficient
		// as first copying old elements and then adding new ones
		int sElemsCount = sElems!=null ? sElems.length : 0;
		List<BigInteger> b = new ArrayList<BigInteger>(sElemsCount + a.size());
		// add start elements
		if (sElemsCount>0) {
			for (int sElem : sElems) {
				BigInteger elem = BigInteger.valueOf(sElem);
				b.add(elem);
			}
		}
		// add all values from a
		b.addAll(a);
		
		SequenceValues oldTransformValues = oldTransform.getAbstractValues();
		SequenceValues outputValues =
			oldTransformValues instanceof SequenceValues_UnsignedIndexListImpl ?
				new SequenceValues_UnsignedIndexListImpl(b) :
			    new SequenceValues_BigIntListImpl(b);
		return new Transform(oldTransform.getName(), outputValues, this, inputSeq, numberOfInputValues);
	}
}
