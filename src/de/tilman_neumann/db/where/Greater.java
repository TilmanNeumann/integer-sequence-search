package de.tilman_neumann.db.where;

import de.tilman_neumann.db.sql.SqlIdentifier;

public class Greater extends BinaryComparisonConstraint {

	public Greater(SqlIdentifier lhs, SqlIdentifier rhs) {
		super(lhs, ">", rhs);
	}
}
