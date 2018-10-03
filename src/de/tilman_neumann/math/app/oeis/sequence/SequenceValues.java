package de.tilman_neumann.math.app.oeis.sequence;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import de.tilman_neumann.types.SortedMultiset;

/**
 * Interface for and partial implementation of integer sequences.
 * @author Tilman Neumann
 * @since 2008-12-31
 */
public abstract class SequenceValues implements Serializable {

	private static final long serialVersionUID = 9145548512830736326L;

	/**
	 * Immutable hashcode computed from initial values.
	 * The hashcode of a once instantiated SequenceValues object must not change
	 * because otherwise it will not be found anymore by hashmap lookups. This is also
	 * the reason why there is no addValue() or addValues() method...
	 */
	private final int hashcode;
	
	/**
	 * Default constructor, computes immutable hashcode from initial values.
	 */
	public SequenceValues(List<BigInteger> values) {
		this.hashcode = values.hashCode();
	}

	abstract public BigInteger getValue(int i);
	
	/**
	 * @return the values of this sequence in their natural order
	 */
	abstract public List<BigInteger> getValues();

	/**
	 * @return the _unsigned_ values of this sequence in their natural order
	 */
	abstract public List<BigInteger> getUnsignedValues();

	/**
	 * @return the indices of the _unsigned_ values from a global valuesStore.
	 */
	// TODO: Outcomment this, create Sequence.compare()-method that encapsulates this functionality
	abstract public int[] getUnsignedValueIndices();
	
	/**
	 * @return the indices and their multiplicities (unordered) of the _unsigned_ values
	 */
	// TODO: Outcomment this, create Sequence.compare()-method that encapsulates this functionality
	abstract public SortedMultiset<Integer> getUnsignedValueIndexMultiset();
	
	/**
	 * @return the number of values this sequence contains
	 */
	abstract public int size();

	/**
	 * @return this sequence as a String
	 */
	@Override
	public String toString() {
		return this.getValues().toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof SequenceValues) {
			SequenceValues other = (SequenceValues) o;
			if (this.size()==other.size()) {
				//return this.getValues().equals(other.getValues());
				// this implementation seems to be slightly faster...
				if (this.hashcode != other.hashcode) return false;
				Iterator<BigInteger> otherIter = other.getValues().iterator();
				for (BigInteger value : this.getValues()) {
					Object oo = otherIter.next();
					if (!value.equals(oo)) {
						return false;
					}
				}
				// all values are equal
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return pre-computed hash code
	 */
	@Override
	public int hashCode() {
		return this.hashcode;
	}
}
