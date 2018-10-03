package de.tilman_neumann.math.app.oeis.sequenceMatch;

import java.util.Comparator;

public 	class SequenceMatchComparator implements Comparator<SequenceMatch> {
	/**
	 * Compares two sequence matches, first by match score, second by refName.
	 * @param o1 first match
	 * @param o2 second match
	 * @return <0/0/>0 if match1 shall be displayed before match2
	 */
	public int compare(SequenceMatch o1, SequenceMatch o2) {
		int scoreCmp = Float.valueOf(o1.getMatchCount()).compareTo(Float.valueOf(o2.getMatchCount()));
		if (scoreCmp != 0) {
			return scoreCmp;
		}
		// smaller A-numbers shall give bigger score to list first
		return (-o1.getRefName().compareTo(o2.getRefName()));
	}
}
