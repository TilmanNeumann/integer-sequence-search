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

import org.apache.log4j.Logger;

import de.tilman_neumann.db.Db;
import de.tilman_neumann.db.WherePart;
import de.tilman_neumann.db.sql.SqlStringWithParams;

public class DeleteStatement extends OtherThanSelectStatementBaseImpl {
	
	private static final Logger LOG = Logger.getLogger(DeleteStatement.class);
	
	public DeleteStatement(Db db, String tableName, WherePart where) throws SQLException {
		super(db); // remember connection
		
		String quote = db.getSqlIdentifierQuote();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("DELETE FROM " + quote + tableName + quote);
        
        SqlStringWithParams whereSql = where.toSqlStringWithParams(db.getDBMS());
        sqlBuilder.append(whereSql.getSqlString());
        sqlBuilder.append(";");
        String sql = sqlBuilder.toString();
        this.sqlStringAndParams = new SqlStringWithParams(sql, whereSql.getValues());
        LOG.debug("sqlStringAndParams = " + sqlStringAndParams);
	}
}
