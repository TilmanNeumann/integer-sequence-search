package de.tilman_neumann.db.sql;

import java.util.LinkedList;

import de.tilman_neumann.db.Dbms;

public class SqlValue implements SqlIdentifier {

	enum Type {
		STRING {
			public String format(Object value) {
				return "'" + value.toString() + "'";
			}
		};
		
		abstract String format(Object value);
	}
	private Type type = null;
	private Object value = null;

	public SqlValue(String str) {
		this.value = str;
		this.type = Type.STRING;
	}
	
	/**
	 * @return null (values don't contain a table name)
	 */
	public String getTableName() {
		return null;
	}

	/**
	 * @return this value as an SQL string.
	 * Depends on the type of the value and on the database system.
	 */
	public String toString(Dbms dbms) {
		return type.format(value);
	}
	
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms) {
		LinkedList<Object> values = new LinkedList<Object>();
		values.add(/*type.format(*/this.value/*)*/);
		// Format values in parametrized SQL string according to their type
		return new SqlStringWithParams(/*type.format(*/"?"/*)*/, values);
	}
}
