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
package de.tilman_neumann.db.statement;

import java.sql.SQLException;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.Db;
import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.sql.SqlStringWithParams;

public class CreateTableStatement extends OtherThanSelectStatementBaseImpl {
	
	private static final Logger LOG = Logger.getLogger(CreateTableStatement.class);

	// Create table statement must be a complete SQL string, no place holders allowed
	// columnNamesAndTypes is SortedMap to maintain the order in which columns were added
	public CreateTableStatement(Db db, String tableName, SortedMap<String, String> columnNamesAndTypes) throws SQLException {
		super(db); // remember connection
		
		String quote = db.getSqlIdentifierQuote();
		String lineSeparator = this.getLineSeparator();
		
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE " + quote + tableName + quote);

        if (columnNamesAndTypes!=null && columnNamesAndTypes.size() > 0) {
            // there are some columns defined...
            sqlBuilder.append(" (" + lineSeparator);
            for (Map.Entry<String, String> columnNameAndType : columnNamesAndTypes.entrySet()) {
            	String columnName = columnNameAndType.getKey();
            	String typeName = columnNameAndType.getValue();
            	// TODO check non-nullity of column name and type
                // TODO: Map abstract to real database types?
                String dbmsTypeName = typeName;

                // Add column to CREATE TABLE statement.
                // Quote column names to avoid problems with DBMS keywords...
                sqlBuilder.append("\t" + quote + columnName + quote + "\t" + dbmsTypeName + "," + lineSeparator);
            }
            
            // remove last comma and line end...
            int len = sqlBuilder.length();
            //LOG.debug("lineSeparator.length = " + lineSeparator.length());
            sqlBuilder.delete(len-lineSeparator.length()-1, len);

            // close column definitions:
            sqlBuilder.append(")");
        } else {
        	// table without columns
        	if (db.getDBMS().equals(Dbms.POSTGRES)) {
                // PostgreSQL needs parentheses for tables without columns...
        		// TODO: Avoid distinction of implementations
               sqlBuilder.append("()");
        	}
        }

        // Postgres >= 8.1 doesn't create oids automatically, 
        // but pdAdmin needs them because it doesn't recognize serials as primary keys
    	if (db.getDBMS().equals(Dbms.POSTGRES)) {
            sqlBuilder.append("\t WITH OIDS");
            // TODO: Avoid distinction of implementations
        }

        // terminate CREATE TABLE statement:
        sqlBuilder.append(";" + lineSeparator);
        
        String sql = sqlBuilder.toString();
        this.sqlStringAndParams = new SqlStringWithParams(sql, null);
        LOG.debug("sqlStringAndParams = " + sqlStringAndParams);
	}
}
