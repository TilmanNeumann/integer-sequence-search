package de.tilman_neumann.db.sql;

import java.util.List;

/**
 * Parametrized SQL string suited for execution as a prepared statement.
 * @author Tilman Neumann
 * @since 2008-12-03
 */
public class SqlStringWithParams {

	private String sql = null;
	private List<Object> values = null;
	
	/**
	 * Combination of an SQL string with place holders suited for prepared statements
	 * and the values to insert for the place holders.
	 * 
	 * @param sql
	 * @param columnsAndValues
	 */
	public SqlStringWithParams(String sql, List<Object> values) {
		this.sql = sql;
		this.values = values;
	}
	
	public String getSqlString() {
		return this.sql;
	}
	
	public List<Object> getValues() {
		return this.values;
	}
	
	@Override
	public String toString() {
		return this.sql + ", values = " + this.values;
	}
}
