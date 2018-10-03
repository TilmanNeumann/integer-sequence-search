package de.tilman_neumann.db.where;

import de.tilman_neumann.db.sql.SqlIdentifier;

public class Equals extends BinaryComparisonConstraint {

	public Equals(SqlIdentifier lhs, SqlIdentifier rhs) {
		super(lhs, "=", rhs);
	}
}
