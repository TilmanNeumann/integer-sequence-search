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

import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.sql.SqlStringWithParams;

public class Not extends BooleanConstraint {

	private Constraint subConstraint = null;
	
	public Not(Constraint subConstraint) {
		super("NOT ");
		this.subConstraint = subConstraint;
	}

	@Override
	public SortedSet<String> getTableNames() {
		return subConstraint.getTableNames();
	}

	@Override
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms) {
		SqlStringWithParams subSql = subConstraint.toSqlStringWithParams(dbms);
		return new SqlStringWithParams(op + subSql.getSqlString(), subSql.getValues());
	}
}
