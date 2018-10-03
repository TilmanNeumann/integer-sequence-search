package de.tilman_neumann.iss.sequenceComparison;

public class SequenceMatchConditionArithmeticStepFunction implements SequenceMatchCondition {

	private int minNumberOfMatches;

	public SequenceMatchConditionArithmeticStepFunction(int minNumberOfMatches) {
		this.minNumberOfMatches = minNumberOfMatches;
	}
	
	public int getMinNumberOfMatches() {
		return this.minNumberOfMatches;
	}

	/* (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequenceComparison.XXX#isSatisfied(int, int, int)
	 */
	public boolean isSatisfied(int s1Size, int s2Size, int numberOfMatches) {
		int minSeqLen = Math.min(s1Size, s2Size);
		return ((numberOfMatches-minNumberOfMatches) >= requiredNumberOfMatches(minSeqLen-minNumberOfMatches));
	}
	
	private int requiredNumberOfMatches(int n) {
		return ( (int) (Math.sqrt(2*n+1/4) - 1/2) );
	}
}
