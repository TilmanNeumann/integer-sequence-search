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
package de.tilman_neumann.db.where;

import java.util.SortedSet;

import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.sql.SqlStringWithParams;

/**
 * An arbitrary complex constraint that may be used as part of
 * an SQL WHERE part.
 * @author Tilman Neumann
 */
public abstract class Constraint {
	
	/**
	 * @param dbms the DBMS for which this shall be prepared as a string
	 * @return this constraint as an SQL snippet
	 */
	abstract public SqlStringWithParams toSqlStringWithParams(Dbms dbms);
	
	/**
	 * @return the table names in this constraint
	 */
	abstract public SortedSet<String> getTableNames();

//	@Override
//	abstract public String toString();
}
