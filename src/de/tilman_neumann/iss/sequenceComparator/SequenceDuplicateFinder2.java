package de.tilman_neumann.iss.sequenceComparator;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchCondition;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchLinear;
import de.tilman_neumann.util.SortedMultiset;

/**
 * A comparator for sequences.
 * @author Tilman Neumann
 * @since 2009-01-24
 */
public class SequenceDuplicateFinder2 extends SequenceMatchFinderBaseImpl {
	
	private static final Logger LOG = Logger.getLogger(SequenceDuplicateFinder2.class);
	
	private OEISSequence s1;
	private OEISSequence s2;
	private int[] s1UnsignedIndices;
	private int[] s2UnsignedIndices;

	public SequenceDuplicateFinder2(SequenceMatchCondition matchCondition) {
		super(matchCondition);
	}
	
	public void compare(OEISSequence s1, OEISSequence s2) throws SequenceMatchLinear {
		this.s1 = s1;
		this.s2 = s2;
		int xSize = s1.size();
		int ySize = s2.size();
		SortedMultiset<Integer> s1IndexMultiset = s1.getUnsignedValueIndexMultiset();
		SortedMultiset<Integer> s2IndexMultiset = s2.getUnsignedValueIndexMultiset();
		SortedMultiset<Integer> valueIndexIntersection = s1IndexMultiset.intersect(s2IndexMultiset);
		int maxPossibleMatchCount = valueIndexIntersection.totalCount();
		if (!matchCondition.isSatisfied(xSize, ySize, maxPossibleMatchCount)) {
			return;
		}

		s1UnsignedIndices = s1.getUnsignedValueIndices();
		s2UnsignedIndices = s2.getUnsignedValueIndices();
		if (!matchCondition.isSatisfied(xSize, ySize, Math.min(xSize, ySize))) {
			// at least one of the sequences is too short
			return;
		}

		int xMax = xSize-1;
		int yMax = ySize-1;
		this.checkDiagonal(xMax, yMax);
		int x = xMax;
		int y = yMax;
		boolean isSatisfiedForX = matchCondition.isSatisfied(x, ySize, Math.min(x, ySize));
		boolean isSatisfiedForY = matchCondition.isSatisfied(xSize, y, Math.min(xSize, y));
		while (isSatisfiedForX || isSatisfiedForY) {
			if (isSatisfiedForX) {
				checkDiagonal(--x, yMax);
				isSatisfiedForX = matchCondition.isSatisfied(x, ySize, Math.min(x, ySize));
			}
			if (isSatisfiedForY) {
				checkDiagonal(xMax, --y);
				isSatisfiedForY = matchCondition.isSatisfied(xSize, y, Math.min(xSize, y));
			}
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
		if (s1UnsignedIndices[x] == s2UnsignedIndices[y]) {
			matchCount++;
		}
		throw new SequenceMatchLinear(s1, s2, matchCount);
	}
}
