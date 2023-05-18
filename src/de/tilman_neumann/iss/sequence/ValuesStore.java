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

import de.tilman_neumann.util.IndexedSetDefaultImpl;

/**
 * A global server class for big integer values stored by indices.
 * 
 * Is thread-safe because IndexedSetDefaultImpl.addValue() is internally synchronized.
 * 
 * @author Tilman Neumann
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
