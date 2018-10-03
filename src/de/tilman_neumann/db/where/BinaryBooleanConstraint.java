package de.tilman_neumann.db.where;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.sql.SqlStringWithParams;

public class BinaryBooleanConstraint extends BooleanConstraint {

	protected List<Constraint> subConstraints = null;
	
	public BinaryBooleanConstraint(String op, List<Constraint> subConstraints) {
		super(op);
		this.subConstraints = subConstraints;
	}

	@Override
	public SortedSet<String> getTableNames() {
		TreeSet<String> tableNames = new TreeSet<String>();
		if (subConstraints != null) {
			for (Constraint subConstraint : subConstraints) {
				tableNames.addAll(subConstraint.getTableNames());
			}
		}
		return tableNames;
	}

	@Override
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms) {
		StringBuilder totalSql = new StringBuilder();
		List<Object> totalValues = new LinkedList<Object>();
		if (subConstraints!=null && subConstraints.size()>0) {
			for (Constraint subConstraint : subConstraints) {
				SqlStringWithParams subSql = subConstraint.toSqlStringWithParams(dbms);
				totalSql.append('(' + subSql.getSqlString() + ')' + op);
				totalValues.addAll(subSql.getValues());
			}
			// remove last op from SQL string:
			int len = totalSql.length();
			totalSql.delete(len-op.length(), len);
		}
		return new SqlStringWithParams(totalSql.toString(), totalValues);
	}
}
