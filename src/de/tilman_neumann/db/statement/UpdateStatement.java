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
package de.tilman_neumann.db.statement;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.Db;
import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.WherePart;
import de.tilman_neumann.db.sql.SqlStringWithParams;

public class UpdateStatement extends OtherThanSelectStatementBaseImpl {
	
	private static final Logger LOG = Logger.getLogger(UpdateStatement.class);

	public UpdateStatement(Db db, String tabName, SortedMap<String, Object> columnsAndValues, WherePart where) throws SQLException {
		super(db); // remember connection
        
        if (columnsAndValues==null || columnsAndValues.size()==0) {
        	throw new IllegalArgumentException("no insert columns and values specified");
        }

		Dbms dbms = db.getDBMS();
		String quote = dbms.getSqlIdentifierQuote();
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("UPDATE " + quote + tabName + quote + " SET ");
		
        List<Object> values = new LinkedList<Object>();
        for (Map.Entry<String, Object> columnAndValue : columnsAndValues.entrySet()) {
        	String column = columnAndValue.getKey();
        	if (column==null || column.trim().length()==0) {
            	throw new IllegalArgumentException("null or empty column name");
        	}
        	sqlBuilder.append(column + "=?, ");
        	values.add(columnAndValue.getValue());
        }

        // remove last comma and space...
        int len = sqlBuilder.length();
        sqlBuilder.delete(len-2, len);

        SqlStringWithParams whereSql = where.toSqlStringWithParams(dbms);
        sqlBuilder.append(whereSql.getSqlString());
        sqlBuilder.append(";");
        String sql = sqlBuilder.toString();
        values.addAll(whereSql.getValues());
        this.sqlStringAndParams = new SqlStringWithParams(sql, values);
        LOG.debug("sqlStringAndParams = " + sqlStringAndParams);
	}
}
