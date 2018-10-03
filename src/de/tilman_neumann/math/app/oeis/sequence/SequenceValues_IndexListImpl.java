package de.tilman_neumann.math.app.oeis.sequence;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.tilman_neumann.util.SortedMultiset;
import de.tilman_neumann.util.SortedMultiset_BottomUp;

/**
 * An integer sequence, implemented as a list of value indices.
 * @author Tilman Neumann
 * @since 2008-12-28
 */
public class SequenceValues_IndexListImpl extends SequenceValues {

	private static final long serialVersionUID = 7488775714926601721L;

	private int[] indices;

	/**
	 * Full constructor.
	 * @param values sequence values
	 */
	public SequenceValues_IndexListImpl(List<BigInteger> values) {
		super(values);
		this.indices = ValuesStore.get().addAll(values);
	}
	
	public BigInteger getValue(int i) {
		return ValuesStore.get().getValue(this.indices[i]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getValues()
	 */
	public List<BigInteger> getValues() {
		return ValuesStore.get().getValues(this.indices);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getUnsignedValues()
	 */
	public List<BigInteger> getUnsignedValues() {
		List<BigInteger> unsignedValues = new ArrayList<BigInteger>(this.size());
		for (BigInteger value : this.getValues()) {
			BigInteger unsignedValue = value.abs();
			unsignedValues.add(unsignedValue);
		}
		return unsignedValues;
	}

	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#size()
	 */
	public int size() {
		return this.indices.length;
	}

	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getUnsignedIndices()
	 */
	// Implementation notes: getUnsignedValues() is quite unefficient.
	// Better use class SequenceUnsignedIndexListImpl ...
	@Override
	public int[] getUnsignedValueIndices() {
		return ValuesStore.get().addAll(this.getUnsignedValues());
	}

	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getUnsignedIndexMultiset()
	 */
	@Override
	public SortedMultiset<Integer> getUnsignedValueIndexMultiset() {
		SortedMultiset<Integer> unsignedValueIndexMultiset = new SortedMultiset_BottomUp<Integer>();
		for (int unsignedValueIndex : this.getUnsignedValueIndices()) {
			unsignedValueIndexMultiset.add(Integer.valueOf(unsignedValueIndex));
		}
		return unsignedValueIndexMultiset;
	}
}
