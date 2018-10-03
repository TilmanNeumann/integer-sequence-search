package de.tilman_neumann.math.app.oeis.sequenceComparator;

import org.apache.log4j.Logger;

import de.tilman_neumann.math.app.oeis.sequence.OEISSequence;
import de.tilman_neumann.math.app.oeis.sequenceComparison.SequenceMatchCondition;
import de.tilman_neumann.math.app.oeis.sequenceMatch.SequenceMatchLinear;
import de.tilman_neumann.util.SortedMultiset;

/**
 * A comparator for sequences.
 * @author Tilman Neumann
 * @since 2009-01-24
 */
public class SequenceDuplicateFinder3 extends SequenceMatchFinderBaseImpl {
	
	private static final Logger LOG = Logger.getLogger(SequenceDuplicateFinder3.class);
	
	OEISSequence s1;
	OEISSequence s2;
	int[] s1UnsignedIndices;
	int[] s2UnsignedIndices;

	public SequenceDuplicateFinder3(SequenceMatchCondition matchCondition) {
		super(matchCondition);
	}
	
	public void compare(OEISSequence s1, OEISSequence s2) throws SequenceMatchLinear {
		this.s1 = s1;
		this.s2 = s2;

		// step 1: find matches on the last row/column
		
//		if (!matchCondition.isSatisfied(Math.min(xSize, ySize))) {
//			// at least one of the sequences is too short
//			return;
//		}
//		
		SortedMultiset<Integer> s1IndexMultiset = s1.getUnsignedValueIndexMultiset();
		SortedMultiset<Integer> s2IndexMultiset = s2.getUnsignedValueIndexMultiset();
		SortedMultiset<Integer> valueIndexIntersection = s1IndexMultiset.intersect(s2IndexMultiset);
		int maxPossibleMatchCount = valueIndexIntersection.totalCount();
		int xSize = s1.size();
		int ySize = s2.size();
		if (!matchCondition.isSatisfied(xSize, ySize, maxPossibleMatchCount)) {
			return;
		}

		s1UnsignedIndices = s1.getUnsignedValueIndices();
		s2UnsignedIndices = s2.getUnsignedValueIndices();
		int xMax = xSize-1;
		int yMax = ySize-1;
		this.checkDiagonal(xMax, yMax);
		
		int xMaxValueIndex = s1UnsignedIndices[xMax];
		int yMaxValueIndex = s2UnsignedIndices[yMax];		
		for (int y=yMax-1; y>-1;) {
			int yValueIndex = s2UnsignedIndices[y];
			if (xMaxValueIndex == yValueIndex) {
				this.checkDiagonal(xMax, y);
			}
			y--;
		}
		for (int x=xMax-1; x>-1;) {
			int xValueIndex = s1UnsignedIndices[x];
			if (yMaxValueIndex == xValueIndex) {
				this.checkDiagonal(x, yMax);
			}
			x--;
		}
	}
	
	public void checkDiagonal(int xMax, int yMax) throws SequenceMatchLinear {
		int matchCount = 0;
		int x = xMax;
		int y = yMax;
		while (x>2 && y>2) {
			int xValueIndex = s1UnsignedIndices[x];
			int yValueIndex = s2UnsignedIndices[y];
			if (xValueIndex != yValueIndex) {
				return; // no duplicate
			}
			matchCount++;
			x--;
			y--;
		}
		
		// we had matches down to the fourth comparable values.
		// this means we have found a duplicate. Augment matchScore if there are further hits:
		if (s1UnsignedIndices[x--] == s2UnsignedIndices[y--]) {
			matchCount++;
		}
		if (s1UnsignedIndices[x--] == s2UnsignedIndices[y--]) {
			matchCount++;
		}
//		if (s1UnsignedIndices[x] == s2UnsignedIndices[y]) {
//			matchCount++;
//		}
		throw new SequenceMatchLinear(s1, s2, matchCount);
	}
}
