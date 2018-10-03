package de.tilman_neumann.db.where;

import de.tilman_neumann.db.sql.SqlIdentifier;

public class Null extends UnitaryComparisonConstraint {

	public Null(SqlIdentifier arg) {
		super(arg, " IS NULL");
	}

}
