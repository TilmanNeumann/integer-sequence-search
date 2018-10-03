package de.tilman_neumann.db.where;

import de.tilman_neumann.db.sql.SqlIdentifier;

public class NotNull extends UnitaryComparisonConstraint {

	public NotNull(SqlIdentifier arg) {
		super(arg, " IS NOT NULL");
	}

}
