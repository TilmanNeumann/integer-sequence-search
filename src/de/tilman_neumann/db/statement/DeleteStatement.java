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
