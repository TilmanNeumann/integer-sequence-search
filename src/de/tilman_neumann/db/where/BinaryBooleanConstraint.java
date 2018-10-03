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

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.sql.SqlStringWithParams;

public class BinaryBooleanConstraint extends BooleanConstraint {

	protected List<Constraint> subConstraints = null;
	
	public BinaryBooleanConstraint(String op, List<Constraint> subConstraints) {
		super(op);
		this.subConstraints = subConstraints;
	}

	@Override
	public SortedSet<String> getTableNames() {
		TreeSet<String> tableNames = new TreeSet<String>();
		if (subConstraints != null) {
			for (Constraint subConstraint : subConstraints) {
				tableNames.addAll(subConstraint.getTableNames());
			}
		}
		return tableNames;
	}

	@Override
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms) {
		StringBuilder totalSql = new StringBuilder();
		List<Object> totalValues = new LinkedList<Object>();
		if (subConstraints!=null && subConstraints.size()>0) {
			for (Constraint subConstraint : subConstraints) {
				SqlStringWithParams subSql = subConstraint.toSqlStringWithParams(dbms);
				totalSql.append('(' + subSql.getSqlString() + ')' + op);
				totalValues.addAll(subSql.getValues());
			}
			// remove last op from SQL string:
			int len = totalSql.length();
			totalSql.delete(len-op.length(), len);
		}
		return new SqlStringWithParams(totalSql.toString(), totalValues);
	}
}
