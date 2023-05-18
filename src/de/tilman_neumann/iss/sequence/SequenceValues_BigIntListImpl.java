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
package de.tilman_neumann.iss.sequence;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.tilman_neumann.util.SortedMultiset;
import de.tilman_neumann.util.SortedMultiset_BottomUp;

/**
 * An integer sequence, implemented as a list of BigIntegers.
 * @author Tilman Neumann
 */
public class SequenceValues_BigIntListImpl extends SequenceValues {

	private static final long serialVersionUID = 7488775714926601721L;

	// TODO: Convert to BigInteger[]
	private ArrayList<BigInteger> values;

	/**
	 * Full constructor.
	 * @param values sequence values
	 */
	public SequenceValues_BigIntListImpl(List<BigInteger> values) {
		super(values);
		this.values = new ArrayList<BigInteger>(values);
	}

	public BigInteger getValue(int i) {
		return values.get(i);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getValues()
	 */
	public List<BigInteger> getValues() {
		return values;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getUnsignedValues()
	 */
	public List<BigInteger> getUnsignedValues() {
		List<BigInteger> unsignedValues = new ArrayList<BigInteger>(this.size());
		for (BigInteger value : values) {
			BigInteger unsignedValue = value.abs();
			unsignedValues.add(unsignedValue);
		}
		return unsignedValues;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getUnsignedIndices()
	 */
	public int[] getUnsignedValueIndices() {
		return ValuesStore.get().addAll(this.getUnsignedValues());
	}

	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getUnsignedIndexMultiset()
	 */
	public SortedMultiset<Integer> getUnsignedValueIndexMultiset() {
		SortedMultiset<Integer> unsignedValueIndexMultiset = new SortedMultiset_BottomUp<Integer>();
		for (int unsignedValueIndex : this.getUnsignedValueIndices()) {
			unsignedValueIndexMultiset.add(Integer.valueOf(unsignedValueIndex));
		}
		return unsignedValueIndexMultiset;
	}

	/* (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#size()
	 */
	public int size() {
		return values.size();
	}
}
