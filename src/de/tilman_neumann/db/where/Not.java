package de.tilman_neumann.db.where;

import java.util.SortedSet;

import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.sql.SqlStringWithParams;

public class Not extends BooleanConstraint {

	private Constraint subConstraint = null;
	
	public Not(Constraint subConstraint) {
		super("NOT ");
		this.subConstraint = subConstraint;
	}

	@Override
	public SortedSet<String> getTableNames() {
		return subConstraint.getTableNames();
	}

	@Override
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms) {
		SqlStringWithParams subSql = subConstraint.toSqlStringWithParams(dbms);
		return new SqlStringWithParams(op + subSql.getSqlString(), subSql.getValues());
	}
}
