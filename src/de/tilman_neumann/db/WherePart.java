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
package de.tilman_neumann.db;

import java.util.SortedSet;

import de.tilman_neumann.db.sql.SqlStringWithParams;
import de.tilman_neumann.db.where.Constraint;

/**
 * The WHERE part of an SQL statement.
 * @author Tilman Neumann
 */
public class WherePart {
	
	private Constraint constraint = null;
	
	/**
	 * Constructor from an arbitrary complex constraint
	 * @param c
	 */
	public WherePart(Constraint c) {
		this.constraint = c;
	}
	
	/**
	 * @param dbms The DBMS for which the WHERE part shall be prepared as a string
	 * @return this as an SQL string with place holders
	 */
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms) {
		SqlStringWithParams constraintSQL = this.constraint.toSqlStringWithParams(dbms);
		return new SqlStringWithParams(" WHERE " + constraintSQL.getSqlString(), constraintSQL.getValues());
	}
	
	/**
	 * @return the table names in this WHERE-part
	 */
	public SortedSet<String> getTableNames() {
		return this.constraint.getTableNames();
	}
	
	@Override
	public String toString() {
		return this.constraint.toString();
	}
}
