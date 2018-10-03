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
