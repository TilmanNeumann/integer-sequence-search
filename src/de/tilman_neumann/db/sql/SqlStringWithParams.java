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
package de.tilman_neumann.db.sql;

import java.util.List;

/**
 * Parametrized SQL string suited for execution as a prepared statement.
 * @author Tilman Neumann
 */
public class SqlStringWithParams {

	private String sql = null;
	private List<Object> values = null;
	
	/**
	 * Combination of an SQL string with place holders suited for prepared statements
	 * and the values to insert for the place holders.
	 * 
	 * @param sql
	 * @param columnsAndValues
	 */
	public SqlStringWithParams(String sql, List<Object> values) {
		this.sql = sql;
		this.values = values;
	}
	
	public String getSqlString() {
		return this.sql;
	}
	
	public List<Object> getValues() {
		return this.values;
	}
	
	@Override
	public String toString() {
		return this.sql + ", values = " + this.values;
	}
}
