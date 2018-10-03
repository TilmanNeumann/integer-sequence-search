package de.tilman_neumann.db.where;

abstract public class BooleanConstraint extends Constraint {

	protected String op = null;
	
	public BooleanConstraint(String op) {
		this.op = op;
	}
}
