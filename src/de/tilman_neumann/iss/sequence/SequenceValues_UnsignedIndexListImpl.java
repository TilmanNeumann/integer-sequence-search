package de.tilman_neumann.iss.sequence;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.util.SortedMultiset;
import de.tilman_neumann.util.SortedMultiset_BottomUp;

/**
 * An integer sequence, implemented as a list of value indices.
 * @author Tilman Neumann
 * @since 2008-12-28
 */
public class SequenceValues_UnsignedIndexListImpl extends SequenceValues {

	private static final long serialVersionUID = 7488775714926601721L;

	private static final Logger LOG = Logger.getLogger(SequenceValues_UnsignedIndexListImpl.class);
	
	private int[] unsignedValueIndices; // faster then List<Integer>, but higher memory requirements because unsignedValueIndexMultiset still needs Integer
	private boolean[] negSigns; // same memory requirement as byte[] with (-1/0/1) values
	
	private SortedMultiset<Integer> unsignedValueIndexMultiset;
	// Note: Replacement with Map<ValueIndex, List<Position>> gave no improvement!

	/**
	 * Full constructor.
	 * @param values sequence values
	 */
	public SequenceValues_UnsignedIndexListImpl(List<BigInteger> values) {
		super(values);
        //LOG.debug("values = " + values);
		int numberOfValues = values.size();
		List<BigInteger> unsignedValues = new ArrayList<BigInteger>(numberOfValues);
		negSigns = new boolean[numberOfValues];
		int i=0;
		for (BigInteger value : values) {
			BigInteger unsignedValue = value.abs();
			unsignedValues.add(unsignedValue);
			int sign = value.signum();
			negSigns[i] = (sign<0);
			i++;
		}
		//LOG.debug("unsignedValues = " + unsignedValues);
		this.unsignedValueIndices = ValuesStore.get().addAll(unsignedValues);
		//LOG.debug("unsignedValueIndices = " + toString(unsignedValueIndices));
		this.unsignedValueIndexMultiset = new SortedMultiset_BottomUp<Integer>();
		for (int unsignedValueIndex : unsignedValueIndices) {
			unsignedValueIndexMultiset.add(Integer.valueOf(unsignedValueIndex));
		}
	}
	
	public BigInteger getValue(int i) {
		BigInteger unsignedValue = ValuesStore.get().getValue(this.unsignedValueIndices[i]);
		boolean isNegative = negSigns[i];
		return isNegative ? unsignedValue.negate() : unsignedValue;
	}

	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getValues()
	 */
	public List<BigInteger> getValues() {
		List<BigInteger> unsignedValues = ValuesStore.get().getValues(this.unsignedValueIndices); // throws NPE if one of the values is null
		List<BigInteger> values = new ArrayList<BigInteger>(unsignedValues.size());
		int i=0;
		for (BigInteger unsignedValue : unsignedValues) {
			boolean isNegative = negSigns[i];
			BigInteger value = isNegative ? unsignedValue.negate() : unsignedValue;
			values.add(value);
			i++;
		}
		return values;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getUnsignedValues()
	 */
	public List<BigInteger> getUnsignedValues() {
		return ValuesStore.get().getValues(this.unsignedValueIndices);
	}

	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#size()
	 */
	public int size() {
		return this.unsignedValueIndices.length;
	}

	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getUnsignedIndices()
	 */
	@Override
	public int[] getUnsignedValueIndices() {
		return this.unsignedValueIndices;
	}

	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequence.Sequence#getUnsignedIndexMultiset()
	 */
	@Override
	public SortedMultiset<Integer> getUnsignedValueIndexMultiset() {
		return this.unsignedValueIndexMultiset;
	}
}
