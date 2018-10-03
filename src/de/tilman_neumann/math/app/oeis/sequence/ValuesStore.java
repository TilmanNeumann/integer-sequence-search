package de.tilman_neumann.math.app.oeis.sequence;

import java.math.BigInteger;

import de.tilman_neumann.math.types.IndexedSetDefaultImpl;

/**
 * A global server class for big integer values stored by indices.
 * 
 * Is thread-safe because IndexedSetDefaultImpl.addValue() is internally synchronized.
 * 
 * @author Tilman Neumann
 * @since 2008-12-22
 */
public class ValuesStore extends IndexedSetDefaultImpl<BigInteger> {

	private static ValuesStore singleton = new ValuesStore();
	
	private ValuesStore() {
		// singleton, use get() method
	}
	
	/**
	 * @return The single values store
	 */
	public static ValuesStore get() {
		return singleton;
	}
}
