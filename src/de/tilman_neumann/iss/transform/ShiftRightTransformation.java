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

import java.util.ArrayList;
import java.util.List;

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
 */
public class ShiftRightTransformation extends Transformation {

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
