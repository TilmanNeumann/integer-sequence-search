package de.tilman_neumann.math.app.oeis.sequenceComparison;

public class SequenceMatchConditionSymmetric implements SequenceMatchCondition {

	private static final float log2 = (float) Math.log(2);
	private int desiredMatchCount;
	
	public SequenceMatchConditionSymmetric(int minMatchCount) {
		this.desiredMatchCount = minMatchCount;
	}
	
	public int getMinNumberOfMatches() {
		return this.desiredMatchCount;
	}

	/* (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequenceComparison.XXX#isSatisfied(int, int, int)
	 */
	public boolean isSatisfied(int s1Size, int s2Size, int matchCount) {
		if (matchCount > desiredMatchCount) {
			// this is always enough
			return true;
		}
		final int maxPossibleMatchCount = Math.min(s1Size, s2Size);
		final int allowedDeviationFromDesiredMatchCount = (maxPossibleMatchCount>3) ? (int) (Math.log(maxPossibleMatchCount-3)/log2) : 0;
		final int requiredMatchCount = maxPossibleMatchCount - allowedDeviationFromDesiredMatchCount;
		return (matchCount >= requiredMatchCount);
	}
}
