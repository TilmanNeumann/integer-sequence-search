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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.Db;
import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.StatementBaseImpl;
import de.tilman_neumann.db.WherePart;
import de.tilman_neumann.db.sql.SqlColumn;
import de.tilman_neumann.db.sql.SqlStringWithParams;
import de.tilman_neumann.util.TimeUtil;

/**
 * An SQL "SELECT" statement.
 * @author Tilman Neumann
 */
public class SelectStatement extends StatementBaseImpl {
	
	private static final Logger LOG = Logger.getLogger(SelectStatement.class);
	
	/**
	 * Constructor for an SQL "SELECT" statement without modifier.
	 * @param db The database that wants this statement
	 * @param selectColumns The columns that shall be selected
	 * @param where constraints
	 * @throws SQLException
	 */
	public SelectStatement(Db db, List<SqlColumn> selectColumns, WherePart where) throws SQLException {
		this(db, null, selectColumns,  where);
	}
	
	/**
	 * Creates an SQL "SELECT" statement.
	 * @param db The database that wants this statement
	 * @param qualifier "UNIQUE" or "DISTINCT"
	 * @param selectColumns The columns that shall be selected
	 * @param where constraints
	 * @throws SQLException on illegal statements or more basic database errors
	 * ("no connection" etc.)
 	 */
	public SelectStatement(Db db, String qualifier, List<SqlColumn> selectColumns, WherePart where) throws SQLException {
		super(db); // remember connection
		//LOG.debug("X0");
		if (selectColumns==null || selectColumns.size()==0) {
			throw new IllegalArgumentException("null or empty select list");
		}
		
		Dbms dbms = db.getDBMS();
		String quote = dbms.getSqlIdentifierQuote();

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT ");
		// unique/distinct
		String trimmedQualifier = (qualifier!=null) ? qualifier.trim().toUpperCase() : "";
		if (trimmedQualifier.length()>0) {
			if (!trimmedQualifier.equals("UNIQUE") && !trimmedQualifier.equals("DISTINCT")) {
				throw new IllegalArgumentException("Illegal SELECT modifier: " + qualifier);
			}
			if (dbms.equals(Dbms.POSTGRES) && trimmedQualifier.equals("UNIQUE")) {
				throw new IllegalArgumentException("Illegal SELECT modifier: PostgreSQL does not support UNIQUE modifier for SELECT statements");
				// TODO: Improve class model. I don't want any "if(dbms=xxx)" distinctions!!
			}
			
			sqlBuilder.append(trimmedQualifier + " ");
		}
		//LOG.debug("X1");
		
		// Build Select list, remember table names by the way
		String columnsStr = "";
		SortedSet<String> tableNames = new TreeSet<String>();
		for (SqlColumn column : selectColumns) {
			String columnName = column.getColumnName();
			if (columnName==null || columnName.trim().length()==0) {
				throw new IllegalArgumentException("null or empty column name");
			}
			String tableName = column.getTableName();
			if (tableName==null || tableName.trim().length()==0) {
				throw new IllegalArgumentException("null or empty table name");
			}
			tableNames.add(tableName);
			columnsStr += /*quote +*/ tableName /*+ quote*/ + "." + /*quote +*/ columnName /*+ quote*/ + ", ";
//			columnsStr += quote + tableName + quote + "." + quote + columnName + quote + ", ";
			// PostgreSQL supports quoting of both table and column
			// TODO: Others?
		}
		// remove last comma and space
		sqlBuilder.append(columnsStr.substring(0, columnsStr.length()-2));
		//LOG.debug("X2");

		// Add FROM part
		SortedSet<String> whereTables = where.getTableNames();
		if (whereTables != null) {
			tableNames.addAll(where.getTableNames());
		}
		String tablesStr = "";
		if (tableNames != null) {
			for (String tableName : tableNames) {
				tablesStr += /*quote +*/ tableName /*+ quote*/ + ", ";
//				tablesStr += quote + tableName + quote + ", ";
				// PostgreSQL supports quoting of both table and column
				// TODO: Others?
			}
		}
		tablesStr = tablesStr.substring(0, tablesStr.length()-2);
		//LOG.debug("tablesStr = " + tablesStr);
		sqlBuilder.append(" FROM " + tablesStr);

		// Add WHERE part
		SqlStringWithParams whereSql = where.toSqlStringWithParams(dbms);
		sqlBuilder.append(whereSql.getSqlString());
		sqlBuilder.append(';');
        String sql = sqlBuilder.toString();
        this.sqlStringAndParams = new SqlStringWithParams(sql, whereSql.getValues());
        //LOG.debug("sqlStringAndParams = " + sqlStringAndParams);
	}

	/**
	 * Executes this SQL "SELECT" statement.
	 * @return Iterator over result rows
	 * @throws SQLException
	 */
	// TODO: fetch size and/or direction parameter(s)?
	public ResultSetIterator execute() throws SQLException {
		this.prepareExecution(); // prepare() and setValues()
//		this.preparedStatement.setFetchSize(1);
//		this.preparedStatement.setFetchDirection(ResultSet.FETCH_FORWARD);
		LOG.debug("executeSQL(" + preparedStatement + ")");
		long start = System.currentTimeMillis();
		ResultSet resultSet = this.preparedStatement.executeQuery();
		long end = System.currentTimeMillis();
		LOG.debug("executeSQL() required " + TimeUtil.timeDiffStr(start, end));
		// store result set iterator in superclass for closing before next command execution
		this.resultSetItr = new ResultSetIterator(resultSet);
		return this.resultSetItr;
	}
}
