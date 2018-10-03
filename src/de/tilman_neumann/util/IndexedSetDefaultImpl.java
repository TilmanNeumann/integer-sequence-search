package de.tilman_neumann.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of an indexed set, using a hashmap and an array list.
 * 
 * Because of the synchronization of the addValue() method this class may also
 * be used as a singleton in a multi-threaded environment.
 * 
 * @author Tilman Neumann
 * @since 2008-12-15
 */
public class IndexedSetDefaultImpl<T> implements IndexedSet<T> {

	/** Used to synchronize addValue(). */
	private final Boolean syncObject = new Boolean(true);
	
	private Map<T, Integer> values2Indices = null;
	private ArrayList<T> values = null;
	private ArrayList<Integer> frequencies = null;
	private boolean collectFrequencies = true;
	private int totalFrequency = 0;
	private ArrayList<Double> informations;
	private double entropy = 0D;
	
	public IndexedSetDefaultImpl() {
		values2Indices = new HashMap<T, Integer>();
		values = new ArrayList<T>();
		frequencies = new ArrayList<Integer>();
	}
	
	/**
	 * Adds the given value to the values table, if not already known.
	 * @param val the value to add
	 * @return the index of the value in the table, null excluded
	 * @see de.tilman_neumann.util.IndexedSet#addValue(java.lang.Object)
	 */
	public Integer addValue(T val) {
		Integer index = values2Indices.get(val);
		// pass by the synchronized block if the value exists already
		if (index==null) {
			// value does not exist yet
			Integer oldIndex = null;
			synchronized (syncObject) {
				// adding new values must be synchronized because else several
				// values might get the same index in the map, and worse, all
				// values from the first double index will be wrong.
				// a thread A that enters the synchronized block asks again if
				// the value still does not exist because another thread might
				// have added the value while A was waiting outside
				index = values2Indices.get(val);
				if (index==null) {
					// value still not there -> add
					index = Integer.valueOf(values.size());
					boolean added = this.values.add(val);
					if (!added) throw new IllegalStateException("class " + this.getClass().getSimpleName() + ": value " + val + " has not been added to list!");
					oldIndex = values2Indices.put(val, index);
					frequencies.add(Integer.valueOf(0)); // new entry
				}
			}
			if (oldIndex!=null) throw new IllegalStateException("class " + this.getClass().getSimpleName() + ": value " + val + " has replaced old index " + oldIndex + " !");
		}
		if (collectFrequencies) {
			frequencies.set(index, frequencies.get(index)+1);
			totalFrequency++;
		}
		return index;
	}

	public void finishStatisticCollection() {
		this.collectFrequencies = false;
		// compute entropy per value
		informations = new ArrayList<Double>();
		for (int freq : frequencies) {
			double p = ((double)freq)/totalFrequency;
			double inf = -Math.log(p);
			informations.add(inf);
			entropy += p*inf;
		}
	}
	
	/**
	 * @return number of distinct values
	 */
	public int size() {
		return values.size();
	}

	/**
	 * @return total sum of (values*their frequencies)
	 */
	public int getTotalFrequency() {
		return totalFrequency;
	}
	
	/**
	 * @return average information per value over the complete database.
	 */
	public double getEntropy() {
		return entropy;
	}
	
	/**
	 * Information contained in the value with the given index.
	 * @param index
	 * @return
	 */
	public double getInformation(int index) {
		return informations.get(index);
	}
	
	/**
	 * Adds the values from the given collection to the values table, if not already known.
	 * @param values the values to add
	 * @return the indices of the values in the table, null excluded
	 * @see de.tilman_neumann.util.IndexedSet#addAll(java.util.Collection)
	 */
	public int[] addAll(Collection<T> values) {
		if (values != null) {
			int[] indices = new int[values.size()];
			int i=0;
			for (T val : values) {
				indices[i] = this.addValue(val).intValue(); // null index excluded!
				i++;
			}
			return indices;
		}
		return new int[0];
	}

	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.types.IndexedSet#getValue(java.lang.Integer)
	 */
	public T getValue(int index) {
		T value = this.values.get(index);
		if (value == null) throw new NullPointerException("class " + this.getClass().getSimpleName() + ": value for index " + index + " is null!");
		return value;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.types.IndexedSet#getValues(java.util.Collection)
	 */
	public List<T> getValues(Collection<Integer> indices) {
		if (indices == null) {
			return new ArrayList<T>(0);
		}
		List<T> ret = new ArrayList<T>(indices.size());
		for (Integer index : indices) {
			ret.add(getValue(index.intValue()));
		}
		return ret;
	}
	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.types.IndexedSet#getValues(java.util.Collection)
	 */
	public List<T> getValues(int[] indices) {
		if (indices == null) {
			return new ArrayList<T>(0);
		}
		List<T> ret = new ArrayList<T>(indices.length);
		for (int index : indices) {
			ret.add(getValue(index));
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.types.IndexedSet#getValueString(int)
	 */
	public String getValueString(int index) {
		return getValue(index).toString();
	}

	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.types.IndexedSet#getIndex(java.lang.Object)
	 */
	public Integer getIndex(T value) {
		return this.values2Indices.get(value);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.tilman_neumann.math.types.IndexedSet#getIndices(java.util.Collection)
	 */
	public IndexList getIndices(Collection<T> values) {
		if (values == null) {
			return new IndexList(0);
		}
		IndexList ret = new IndexList(values.size());
		for (T value : values) {
			ret.add(getIndex(value));
		}
		return ret;
	}
}
