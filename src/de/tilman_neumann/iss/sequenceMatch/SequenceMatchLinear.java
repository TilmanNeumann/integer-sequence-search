/*
 * integer-sequence-search (ISS) is an offline OEIS sequence search engine.
 * Copyright (C) 2018 Tilman Neumann - tilman.neumann@web.de
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, see <http://www.gnu.org/licenses/>.
 */
package de.tilman_neumann.iss.sequenceMatch;

import java.util.List;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchHypothesis;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchHypothesisLinear;

/**
 * A sequence match.
 * @author Tilman Neumann
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
