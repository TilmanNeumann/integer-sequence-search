package de.tilman_neumann.db.where;

/**
 * Base class for comparison constraints.
 * @author Tilman Neumann
 * @since 2008-12-03
 */
abstract public class ComparisonConstraint extends Constraint {
	protected String op = null;
	
	/**
	 * Full constructor.
	 * @param op comparison operator
	 */
	public ComparisonConstraint(String op) {
		this.op = op;
	}
}
