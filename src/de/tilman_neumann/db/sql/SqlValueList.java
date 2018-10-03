/*
 * integer-sequence-search (ISS) is an offline OEIS sequence search engine.
 * Copyright (C) 2018 Tilman Neumann (www.tilman-neumann.de)
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
package de.tilman_neumann.db.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * An SQL values list.
 * @author Tilman Neumann
 */
abstract public class SqlValueList extends ArrayList<SqlValue> implements SqlIdentifier {

	private static final long serialVersionUID = -1380954408462369438L;

	/**
	 * Constructor for an empty list.
	 */
	public SqlValueList() {
		super();
	}

	/**
	 * Copy constructor.
	 * @param values original list of values
	 */
	public SqlValueList(List<SqlValue> values) {
		super(values);
	}
	
	/**
	 * @return null (value lists don't contain a table name)
	 */
	public String getTableName() {
		return null;
	}

	/**
	 * @return this value list as an SQL string.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('(');
		if (this.size()>0) {
			for (SqlValue value : this) {
				b.append(value.toString() + ",");
			}
			// remove last comma
			int len = b.length();
			b.delete(len-1, len); 
		}
		// terminate
		b.append(')');
		return b.toString();
	}
}
