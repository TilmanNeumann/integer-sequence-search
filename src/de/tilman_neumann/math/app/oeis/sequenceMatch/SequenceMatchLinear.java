package de.tilman_neumann.math.app.oeis.sequenceMatch;

import java.util.List;

import de.tilman_neumann.math.app.oeis.sequence.OEISSequence;
import de.tilman_neumann.math.app.oeis.sequenceComparison.SequenceMatchHypothesis;
import de.tilman_neumann.math.app.oeis.sequenceComparison.SequenceMatchHypothesisLinear;

/**
 * A sequence match.
 * @author Tilman Neumann
 * @since ~2008-12-15
 */
public class SequenceMatchLinear extends SequenceMatch {

	private static final long serialVersionUID = -1256662855916287080L;

	private OEISSequence lookupSeq;
	private OEISSequence refSeq;
	private int matchCount = 0;
	private SequenceMatchHypothesisLinear matchHypothesis = null;
	private SequenceMatchMatrix matchMatrix = null;
	
	/**
	 * Simplified constructor without hypothesis or match matrix.
	 * @param lookupSeq
	 * @param refSeq
	 * @param matchCount
	 */
	public SequenceMatchLinear(OEISSequence lookupSeq, OEISSequence refSeq, int matchCount) {
		this.lookupSeq = lookupSeq;
		this.refSeq = refSeq;
		this.matchCount = matchCount;
	}

	/**
	 * Full constructor.
	 * @param lookupSeq
	 * @param refSeq
	 * @param hyp match hypothesis
	 * @param matchMatrix match matrix
	 */
	public SequenceMatchLinear(OEISSequence lookupSeq, OEISSequence refSeq, SequenceMatchHypothesisLinear hyp, SequenceMatchMatrix matchMatrix) {
		this.lookupSeq = lookupSeq;
		this.refSeq = refSeq;
		this.matchCount = hyp.getMatchScore();
		this.matchHypothesis = hyp;
		this.matchMatrix = matchMatrix;
	}
	
	public int getMatchCount() {
		return this.matchCount;
	}

	public OEISSequence getRefSequence() {
		return this.refSeq;
	}

	public String getRefName() {
		return this.refSeq.getName();
	}
	
	public OEISSequence getLookupSequence() {
		return this.lookupSeq;
	}
	
	public SequenceMatchHypothesis getHypothesis() {
		return this.matchHypothesis;
	}
	
	public boolean isAlmostExact() {
		if (!(matchHypothesis instanceof SequenceMatchHypothesisLinear)) {
			return false;
		}
		SequenceMatchHypothesisLinear lh = (SequenceMatchHypothesisLinear) matchHypothesis;
		return (lh.getSlope() == 1);
	}

	public boolean needsXExpansion() {
		return matchHypothesis.needsXExpansion(matchMatrix);
	}
	
	@Override
	// TODO: Align sequences in a sensible way ?
	// TODO: print match coordinates ?
	public String toString() {
		StringBuffer bu = new StringBuffer();
		bu.append("Potential match!\n");
		bu.append(lookupSeq.nameAndValuesString() + "\n");
		bu.append("& " + refSeq.nameAndValuesString() + "\n");
		bu.append(getMatrixWithHypothesisString());
		bu.append(matchHypothesis + "\n");
		return bu.toString();
	}
	
	private String getMatrixWithHypothesisString() {
		StringBuffer bu = new StringBuffer();
		List<MatchPoint> hypothesisMatches = matchHypothesis.getHypothesisMatches();
		int xDim = matchMatrix.getXDim();
		int yDim = matchMatrix.getYDim();
		for (short y=0; y<yDim; y++) {
			for (short x=0; x<xDim; x++) {
				char matchMark = '-';
				if (matchMatrix.isMatch(x,y)) {
					if (hypothesisMatches.contains(new MatchPoint(x, y))) {
						matchMark = 'X';
					} else {
						matchMark = 'x';
					}
				}
				bu.append(matchMark);
			}
			bu.append("\n");
		}

		return bu.toString();
	}
}
