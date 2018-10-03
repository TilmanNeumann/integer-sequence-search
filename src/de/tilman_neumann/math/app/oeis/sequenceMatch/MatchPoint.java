package de.tilman_neumann.math.app.oeis.sequenceMatch;

import de.tilman_neumann.types.Pair;

/**
 * A match of one value each of two sequences,
 * represented by its sequence positions.
 * @author Tilman Neumann
 * @since 2009-01-22
 */
public class MatchPoint extends Pair<Short, Short> {

	private static final long serialVersionUID = -2897646298951181487L;

	public MatchPoint(Short u, Short v) {
		super(u, v);
	}
	
	public MatchPoint(short u, short v) {
		super(Short.valueOf(u), Short.valueOf(v));
	}
}
