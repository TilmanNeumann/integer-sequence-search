package de.tilman_neumann.math.app.oeis.sequenceMatch;

import de.tilman_neumann.util.SortOrder;
import de.tilman_neumann.util.SortedList;

/**
 * List of matches of one lookup with nicer toString() implementation than
 * standard list.
 * @author Tilman Neumann
 * @since 2008-12-17
 */
public class SequenceMatchList extends SortedList<SequenceMatch> {

	private static final long serialVersionUID = 7515592729171336037L;

	private static SequenceMatchComparator comp = new SequenceMatchComparator();

	public SequenceMatchList() {
		// matches with bigger matchscore or smaller A-number are listed first
		super(comp, SortOrder.DESCENDING);
	}
	
	@Override
	public String toString() {
		StringBuilder bu = new StringBuilder();
		if (this.size()==0) {
			return "nothing found...";
		}
		
		float lastScore = -1;
		for (SequenceMatch match : this) {
			float newScore = match.getMatchCount();
			if (newScore!=lastScore) {
				bu.append("matchScore = " + newScore + ":\n");
			}
			bu.append("\t" + match.getRefName() + ", " + match.getLookupSequence().getName() + "\n");
			lastScore = newScore;
		}
		return bu.toString();
	}
}
