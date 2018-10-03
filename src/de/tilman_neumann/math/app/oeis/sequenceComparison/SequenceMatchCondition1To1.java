package de.tilman_neumann.math.app.oeis.sequenceComparison;

public class SequenceMatchCondition1To1 implements SequenceMatchCondition {

	private int minNumberOfMatches;

	public SequenceMatchCondition1To1(int minNumberOfMatches) {
		this.minNumberOfMatches = minNumberOfMatches;
	}
	
	public int getMinNumberOfMatches() {
		return this.minNumberOfMatches;
	}
	
	/* (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequenceComparison.XXX#isSatisfied(int, int, int)
	 */
	public boolean isSatisfied(int s1Size, int s2Size, int matchCount) {
		int minSeqLen = Math.min(s1Size, s2Size);
		int requiredNumberOfMatches = Math.max(minSeqLen-4, minNumberOfMatches); // tolerate offset up to 4
		return (matchCount >= requiredNumberOfMatches);
	}
}
