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

import java.util.LinkedList;

import de.tilman_neumann.db.Dbms;

/**
 * An SQL column.
 * @author Tilman Neumann
 */
public class SqlColumn implements SqlIdentifier {
	private String table = null;
	private String column = null;
	
	/**
	 * Full constructor.
	 * @param table the name of the table this column belongs to
	 * @param column the unqualified name of this column
	 */
	public SqlColumn(String table, String column) {
		this.table = table;
		this.column = column;
	}
	
	/**
	 * @return the (unquoted) unqualified name of this column
	 */
	public String getColumnName() {
		return this.column;
	}
	
	/**
	 * @return the (unquoted) name of the table this column belongs to
	 */
	public String getTableName() {
		return this.table;
	}

	/**
	 * @return full qualified name (table and column) quoted according to the
	 * DBMS conventions so that we can use keywords as identifiers, too
	 */
	public String toString(Dbms dbms) {
		String quote = dbms.getSqlIdentifierQuote();
		String ret = /*quote +*/ table + /*quote +*/ "." + /*quote +*/ column /*+ quote*/;
//		String ret = quote + table + quote + "." + quote + column + quote;
		// PostgreSQL supports quoting of both table and column
		// TODO: Others?
		return ret;
	}
	
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms) {
		// a column description has no place holders
		return new SqlStringWithParams(this.toString(dbms), new LinkedList<Object>());
	}
	
	@Override
	public String toString() {
		return table + "." + column;
	}
}
