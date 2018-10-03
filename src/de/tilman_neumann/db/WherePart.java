package de.tilman_neumann.db;

import java.util.SortedSet;

import de.tilman_neumann.db.sql.SqlStringWithParams;
import de.tilman_neumann.db.where.Constraint;

/**
 * The WHERE part of an SQL statement.
 * @author Tilman Neumann
 * @since 2008-12-03
 */
public class WherePart {
	
	private Constraint constraint = null;
	
	/**
	 * Constructor from an arbitrary complex constraint
	 * @param c
	 */
	public WherePart(Constraint c) {
		this.constraint = c;
	}
	
	/**
	 * @param dbms The DBMS for which the WHERE part shall be prepared as a string
	 * @return this as an SQL string with place holders
	 */
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms) {
		SqlStringWithParams constraintSQL = this.constraint.toSqlStringWithParams(dbms);
		return new SqlStringWithParams(" WHERE " + constraintSQL.getSqlString(), constraintSQL.getValues());
	}
	
	/**
	 * @return the table names in this WHERE-part
	 */
	public SortedSet<String> getTableNames() {
		return this.constraint.getTableNames();
	}
	
	@Override
	public String toString() {
		return this.constraint.toString();
	}
}
