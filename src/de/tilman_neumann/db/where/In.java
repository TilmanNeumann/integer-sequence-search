package de.tilman_neumann.db.where;

import de.tilman_neumann.db.sql.SqlIdentifier;
import de.tilman_neumann.db.sql.SqlValueList;

public class In extends BinaryComparisonConstraint {

	public In(SqlIdentifier lhs, SqlValueList rhs) {
		super(lhs, " IN ", rhs);
	}
}
