package de.tilman_neumann.db.where;

import de.tilman_neumann.db.sql.SqlIdentifier;

// TODO: For parsing we need to consider <> also
public class NotEquals extends BinaryComparisonConstraint {

	public NotEquals(SqlIdentifier lhs, SqlIdentifier rhs) {
		super(lhs, "!=", rhs);
	}

}
