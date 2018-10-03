package de.tilman_neumann.math.app.oeis.sequenceComparison;

import java.util.List;

import de.tilman_neumann.math.app.oeis.sequenceMatch.MatchPoint;
import de.tilman_neumann.math.app.oeis.sequenceMatch.SequenceMatchMatrix;

public interface SequenceMatchHypothesis {
	/**
	 * Checks a sequence match hypothesis
	 * @param mm sequence match matrix 
	 * @return match score (0 if hypothesis is discarded)
	 */
	int check(SequenceMatchMatrix mm);
	
	int getMatchScore();
	
	/**
	 * @return the position pairs of the matches of this hypothesis
	 */
	List<MatchPoint> getHypothesisMatches();
	
	boolean needsXExpansion(SequenceMatchMatrix mm);
	
	/**
	 * @return the hypothesis as a String
	 */
	String toString();
}
