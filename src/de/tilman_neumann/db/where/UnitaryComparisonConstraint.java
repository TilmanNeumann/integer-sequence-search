package de.tilman_neumann.db.where;

import java.util.SortedSet;
import java.util.TreeSet;

import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.sql.SqlIdentifier;
import de.tilman_neumann.db.sql.SqlStringWithParams;

/**
 * Base class for comparison constraints.
 * @author Tilman Neumann
 * @since 2008-12-03
 */
public class UnitaryComparisonConstraint extends ComparisonConstraint {
	private SqlIdentifier arg = null;
	
	/**
	 * Full constructor.
	 * @param arg argument
	 * @param op comparison operator
	 */
	public UnitaryComparisonConstraint(SqlIdentifier arg, String op) {
		super(op);
		this.arg = arg;
	}

	/**
	 * @return the table name possibly contained in this comparison argument
	 */
	@Override
	public SortedSet<String> getTableNames() {
		TreeSet<String> tableNames = new TreeSet<String>();
		tableNames.add(arg.getTableName());
		return tableNames;
	}

	/**
	 * @return this as an SQL string with place holders and the values to insert,
	 * suited for an prepared statement.
	 */
	@Override
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms) {
		SqlStringWithParams argSql = arg.toSqlStringWithParams(dbms);
		return new SqlStringWithParams(argSql + op, argSql.getValues());
	}
}
