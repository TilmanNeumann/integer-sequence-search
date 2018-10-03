package de.tilman_neumann.iss.sequenceComparison;

public interface SequenceMatchCondition {

	/**
	 * @return the minimum number of matching values for a valid sequence match
	 */
	public int getMinNumberOfMatches();
	
	/**
	 * Checks if this match condition is satisfied by the given number of matches.
	 * @param s1Size length of first sequence
	 * @param s2Size length of second sequence
	 * @param matchCount
	 * @return satisfied
	 */
	public abstract boolean isSatisfied(int s1Size, int s2Size, int matchCount);

}