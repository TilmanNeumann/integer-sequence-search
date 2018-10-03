package de.tilman_neumann.iss.sequence;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tilman_neumann.util.SortedMultiset;

/**
 * Interface for and partial implementation of OEIS sequences.
 * @author Tilman Neumann
 * @since 2008-12-31
 */
public class OEISSequence implements Serializable {

	private static final long serialVersionUID = -6791054288383769845L;
	
	private String name = null;
	private int hashcode;
	private SequenceValues values = null;
	private Set<OEISSequence> duplicates = null; // Set avoids multiple entries
	
	/**
	 * Default sequence constructor.
	 */
	public OEISSequence(String name, SequenceValues values) {
		this.setName(name);
		this.values = values;
		this.duplicates = new HashSet<OEISSequence>();
	}

	private void setName(String name) {
		// trimming is important, otherwise sequences may not be recognized!
		String trimmedName = name.trim();
		this.name = trimmedName;
		this.hashcode = trimmedName.hashCode();
	}
	
	/**
	 * @return name of the sequence (OEIS A-number if this is an original OEIS sequence)
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the values of this sequence
	 */
	public SequenceValues getAbstractValues() {
		return values;
	}

	public void addDuplicate(OEISSequence duplicate) {
		this.duplicates.add(duplicate);
	}
	
	public void addDuplicates(Collection<OEISSequence> duplicates) {
		this.duplicates.addAll(duplicates);
	}
	
	public Set<OEISSequence> getDuplicates() {
		return duplicates;
	}
	
	public Set<OEISSequence> removeDuplicates() {
		Set<OEISSequence> dups = duplicates;
		this.duplicates = new HashSet<OEISSequence>();
		return dups;
	}
	
	/**
	 * @return this sequence as a String
	 */
	public String nameAndValuesString() {
		return this.name.toString() + " = " + this.values.toString();
	}
	
	/**
	 * Two sequences are equal if they have the same name.
	 * 
	 * Values can not be taken into account because we can only
	 * consider a finite number of values, but in reality the sequences
	 * have an infinite number of values.
	 */
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof OEISSequence) {
			OEISSequence other = (OEISSequence) o;
			return this.name.equals(other.name);
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
	
	// delegator methods to values -------------------------------------------------
	
	public List<BigInteger> getValues() {
		return values.getValues();
	}
	
	public List<BigInteger> getUnsignedValues() {
		return values.getUnsignedValues();
	}
	
	public int[] getUnsignedValueIndices() {
		return values.getUnsignedValueIndices();
	}
	
	public SortedMultiset<Integer> getUnsignedValueIndexMultiset() {
		return values.getUnsignedValueIndexMultiset();
	}
	
	public int size() {
		return values.size();
	}
}
