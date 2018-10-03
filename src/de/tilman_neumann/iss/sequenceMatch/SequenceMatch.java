package de.tilman_neumann.iss.sequenceMatch;

import de.tilman_neumann.iss.sequence.OEISSequence;

/**
 * A probable match of two sequences.
 * @author Tilman Neumann
 * @since ~2009-01-01
 */
abstract public class SequenceMatch extends Exception {

	private static final long serialVersionUID = -2797052573592024437L;

	/**
	 * @return the number of matching values
	 */
	abstract public int getMatchCount();
	
	/**
	 * @return the name of the database sequence
	 */
	abstract public String getRefName();
	
	/**
	 * @return the sequence (often a transform) that was compared to the database
	 */
	abstract public OEISSequence getLookupSequence();
}
