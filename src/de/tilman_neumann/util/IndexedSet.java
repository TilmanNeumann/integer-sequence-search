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
package de.tilman_neumann.util;

import java.util.Collection;
import java.util.List;

/**
 * Indexed sets allow the storage of data by the indices of their elements.
 * Thus, if the average size of such a data element is bigger than that of the
 * indices, this may save a lot of memory.
 * 
 * @author Tilman Neumann
 * 
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
