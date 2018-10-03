package de.tilman_neumann.db.where;

import de.tilman_neumann.db.sql.SqlIdentifier;

public class GreaterEquals extends BinaryComparisonConstraint {

	public GreaterEquals(SqlIdentifier lhs, SqlIdentifier rhs) {
		super(lhs, ">=", rhs);
	}
}
