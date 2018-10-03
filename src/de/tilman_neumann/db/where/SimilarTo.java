package de.tilman_neumann.db.where;

import de.tilman_neumann.db.sql.SqlIdentifier;

public class SimilarTo extends BinaryComparisonConstraint {

	// TODO: rhs must be regular expression / string pattern ?
	public SimilarTo(SqlIdentifier lhs, String op, SqlIdentifier rhs) {
		super(lhs, op, rhs); // TODO: Operator name depends on DBMS
	}
}
