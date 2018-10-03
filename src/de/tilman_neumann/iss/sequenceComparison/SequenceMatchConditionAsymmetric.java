package de.tilman_neumann.iss.sequenceComparison;

public class SequenceMatchConditionAsymmetric implements SequenceMatchCondition {

	private static final float log2 = (float) Math.log(2);
	private int desiredMatchCount;
	
	public SequenceMatchConditionAsymmetric(int desiredMatchCount) {
		this.desiredMatchCount = desiredMatchCount;
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
		final int allowedDeviationFromDesiredMatchCount = (s1Size>3) ? (int) (Math.log(s1Size-3)/log2) : 0;
		final int requiredMatchCount = s1Size - allowedDeviationFromDesiredMatchCount;
		return (matchCount >= requiredMatchCount);
	}
}
