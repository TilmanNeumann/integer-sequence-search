package de.tilman_neumann.db.where;

import java.util.List;

public class And extends BinaryBooleanConstraint {

	public And(List<Constraint> subConstraints) {
		super(" AND ", subConstraints);
	}
}
