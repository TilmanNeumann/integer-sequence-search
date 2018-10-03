package de.tilman_neumann.db.where;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.sql.SqlIdentifier;
import de.tilman_neumann.db.sql.SqlStringWithParams;

/**
 * Base class for comparison constraints.
 * @author Tilman Neumann
 * @since 2008-12-03
 */
public class BinaryComparisonConstraint extends ComparisonConstraint {
	private static final Logger LOG = Logger.getLogger(BinaryComparisonConstraint.class);
	
	private SqlIdentifier lhs = null;
	private SqlIdentifier rhs = null;
	
	/**
	 * Full constructor.
	 * @param lhs left hand size
	 * @param op comparison operator
	 * @param rhs right hand side
	 */
	public BinaryComparisonConstraint(SqlIdentifier lhs, String op, SqlIdentifier rhs) {
		super(op);
		this.lhs = lhs;
		this.rhs = rhs;
	}

	/**
	 * @return the table names possibly contained in this comparison
	 */
	@Override
	public SortedSet<String> getTableNames() {
		TreeSet<String> tableNames = new TreeSet<String>();
		//LOG.debug("XX");
		String leftTable = lhs.getTableName();
		if (leftTable!=null) {
			tableNames.add(leftTable);
		}
		String rightTable = rhs.getTableName();
		if (rightTable!=null) {
			tableNames.add(rightTable);
		}
		return tableNames;
	}

	/**
	 * @return this as an SQL string with place holders and the values to insert,
	 * suited for an prepared statement.
	 */
	@Override
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms) {
		SqlStringWithParams lhsSql = lhs.toSqlStringWithParams(dbms);
		SqlStringWithParams rhsSql = rhs.toSqlStringWithParams(dbms);
		String totalSql = lhsSql.getSqlString() + op + rhsSql.getSqlString();
		List<Object> totalValues = lhsSql.getValues();
		totalValues.addAll(rhsSql.getValues());
		return new SqlStringWithParams(totalSql, totalValues);
	}
}
