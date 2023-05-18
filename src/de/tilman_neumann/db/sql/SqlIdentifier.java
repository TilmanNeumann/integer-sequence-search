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

import de.tilman_neumann.db.Dbms;

/**
 * Base interface for SQL identifiers
 * @author Tilman Neumann
 */
public interface SqlIdentifier {
	/**
	 * @return the table name included by this SQLIdentifier, if there is one.
	 * Remember to quote this according to the DBMS conventions before putting
	 * the result into an SQL string.
	 */
	// TODO: Check references + quoting
	public String getTableName();
	
	/**
	 * Must be overridden by implementing classes such that (fully qualified)
	 * SQL descriptions are returned.
	 * @param dbms The DBMS for which this SQL identifier shall be formatted
	 * @return fully qualified SQL description
	 */
	public String toString(Dbms dbms);
	
	/**
	 * @param dbms The DBMS for which this SQL identifier shall be formatted
	 * @return this as an SQL string with place holders and the values to insert.
	 */
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms);
}
