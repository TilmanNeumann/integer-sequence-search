package de.tilman_neumann.util;

import java.util.Collection;
import java.util.List;

/**
 * Indexed sets allow the storage of data by the indices of their elements.
 * Thus, if the average size of such a data element is bigger than that of the
 * indices, this may save a lot of memory.
 * @author Tilman Neumann
 * @since 2008-12-15
 * @param <T> type of entries
 */
public interface IndexedSet<T> {
	
	/**
	 * Adds a new value to this indexed set.
	 * @param val The value to add
	 * @return the index of the value
	 */
	Integer addValue(T val);
	
	/**
	 * Adds several values at once.
	 * @param values
	 * @return the indices of the values
	 */
	int[] addAll(Collection<T> values);
	
	/**
	 * Returns the value stored at the given index.
	 */
	T getValue(int index);
	
	/**
	 * Returns a list of values for the given indices.
	 * @param indices
	 * @return
	 */
	List<T> getValues(Collection<Integer> indices);
	
	/**
	 * Returns the value stored at the given index.
	 */
	String getValueString(int index);
	
	/**
	 * Returns the index of the given value if already registered.
	 * @return found index, or null if value is unknown
	 */
	Integer getIndex(T value);
	
	/**
	 * Returns the indices of the values in the given collection
	 * @param values
	 * @return list of indices of the given values (contains nulls for non-existing entries)
	 */
	IndexList getIndices(Collection<T> values);
}
