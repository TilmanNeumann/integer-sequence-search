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
package de.tilman_neumann.db.where;

import java.util.SortedSet;
import java.util.TreeSet;

import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.sql.SqlIdentifier;
import de.tilman_neumann.db.sql.SqlStringWithParams;

/**
 * Base class for comparison constraints.
 * @author Tilman Neumann
 */
public class UnitaryComparisonConstraint extends ComparisonConstraint {
	private SqlIdentifier arg = null;
	
	/**
	 * Full constructor.
	 * @param arg argument
	 * @param op comparison operator
	 */
	public UnitaryComparisonConstraint(SqlIdentifier arg, String op) {
		super(op);
		this.arg = arg;
	}

	/**
	 * @return the table name possibly contained in this comparison argument
	 */
	@Override
	public SortedSet<String> getTableNames() {
		TreeSet<String> tableNames = new TreeSet<String>();
		tableNames.add(arg.getTableName());
		return tableNames;
	}

	/**
	 * @return this as an SQL string with place holders and the values to insert,
	 * suited for an prepared statement.
	 */
	@Override
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms) {
		SqlStringWithParams argSql = arg.toSqlStringWithParams(dbms);
		return new SqlStringWithParams(argSql + op, argSql.getValues());
	}
}
