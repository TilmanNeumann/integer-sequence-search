package de.tilman_neumann.db.statement;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.Db;
import de.tilman_neumann.db.sql.SqlStringWithParams;

public class InsertStatement extends OtherThanSelectStatementBaseImpl {
	
	private static final Logger LOG = Logger.getLogger(InsertStatement.class);
	
	public InsertStatement(Db db, String tabName, SortedMap<String, Object> columnsAndValues) throws SQLException {
		super(db); // remember connection
        
        if (columnsAndValues==null || columnsAndValues.size()==0) {
        	throw new IllegalArgumentException("no insert columns and values specified");
        }

		String quote = db.getSqlIdentifierQuote();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO " + quote + tabName + quote);
        
        String columnsStr = "";
        String valuesStr = "";
        List<Object> values = new LinkedList<Object>();
        for (Map.Entry<String, Object> columnAndValue : columnsAndValues.entrySet()) {
        	String column = columnAndValue.getKey();
        	if (column==null || column.trim().length()==0) {
            	throw new IllegalArgumentException("null or empty column name");
        	}
        	columnsStr += ", " + column;
            valuesStr += ", ?";
            values.add(columnAndValue.getValue());
        }
        // cut off leading comma and space chars
        columnsStr = columnsStr.substring(2);
        valuesStr = valuesStr.substring(2);
        
        sqlBuilder.append(" (" + columnsStr + ") VALUES (" + valuesStr + ");");
        
        String sql = sqlBuilder.toString();
        this.sqlStringAndParams = new SqlStringWithParams(sql, values);
        LOG.debug("sqlStringAndParams = " + sqlStringAndParams);
	}
}
