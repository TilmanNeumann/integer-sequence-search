package de.tilman_neumann.db.where;

import java.util.List;

public class Or extends BinaryBooleanConstraint {

	public Or(List<Constraint> subConstraints) {
		super(" OR ", subConstraints);
	}
}
