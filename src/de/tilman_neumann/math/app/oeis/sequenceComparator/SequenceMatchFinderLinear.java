package de.tilman_neumann.math.app.oeis.sequenceComparator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.math.app.oeis.sequence.OEISSequence;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues_IndexListImpl;
import de.tilman_neumann.math.app.oeis.sequenceComparison.SequenceMatchCondition;
import de.tilman_neumann.math.app.oeis.sequenceComparison.SequenceMatchHypothesis;
import de.tilman_neumann.math.app.oeis.sequenceComparison.SequenceMatchHypothesisLinear;
import de.tilman_neumann.math.app.oeis.sequenceMatch.MatchPoint;
import de.tilman_neumann.math.app.oeis.sequenceMatch.SequenceMatchLinear;
import de.tilman_neumann.math.app.oeis.sequenceMatch.SequenceMatchMatrix;
import de.tilman_neumann.math.app.oeis.sequenceMatch.SequenceMatchMatrix2;
import de.tilman_neumann.math.app.oeis.sequenceMatch.UniqueMatchPointIterator;
import de.tilman_neumann.util.SortedMultiset;

/**
 * A comparator for sequences, finding linear match patterns like a[i]=b[2i].
 * @author Tilman Neumann
 * @since ~2008-12-15
 */
public class SequenceMatchFinderLinear extends SequenceMatchFinderBaseImpl {
	
	private static final Logger LOG = Logger.getLogger(SequenceMatchFinderLinear.class);
	
	public SequenceMatchFinderLinear(SequenceMatchCondition matchCondition) {
		super(matchCondition);
	}
	
	public void compare(OEISSequence s1, OEISSequence s2) throws SequenceMatchLinear {
		// 0. Compare index sets, abort if the possible number of matches is less than the wanted percentage.
		// Implementation notes: Creating multisets in a double loop is quite expensive, thus we better
		// prepare them when sequences are created (factor 4 performance improvement!)
		SortedMultiset<Integer> s1IndexMultiset = s1.getUnsignedValueIndexMultiset();
		SortedMultiset<Integer> s2IndexMultiset = s2.getUnsignedValueIndexMultiset();
		SortedMultiset<Integer> jointValueIndices = s1IndexMultiset.intersect(s2IndexMultiset);
		// TODO: use jointValueIndices in later steps
		int maxPossibleMatchCount = jointValueIndices.totalCount();
		if (!matchCondition.isSatisfied(s1.size(), s2.size(), maxPossibleMatchCount)) {
			return;
		}
		
		//LOG.debug("s1 = " + s1.nameAndValuesString());
		//LOG.debug("s2 = " + s2.nameAndValuesString());
		
		// 1. Compute match matrix. s1 will be the x- and s2 the y-axis; the indices are labeled 
		// x and y correspondingly. We compare the unsigned values to get sign-insensitive matches.
		// Implementation notes: Comparing (integer) indices is faster than comparing the proper big integer values.
//		SequenceMatchMatrix mm = new SequenceMatchMatrix1(s1, s2);
		SequenceMatchMatrix mm = new SequenceMatchMatrix2(s1, s2); // 2s faster
		//LOG.debug("match matrix:\n" + mm);
		
		// 2. Get first two unique match points (those that are alone in both their row and column).
		// Abort if there are less than two such points, because then a linear hypothesis can not be computed
		UniqueMatchPointIterator umpIter = new UniqueMatchPointIterator(mm);
		if (!umpIter.hasNext()) {
			return;
		}
		MatchPoint mp1 = umpIter.next();
		if (!umpIter.hasNext()) {
			return;
		}
		MatchPoint mp2 = umpIter.next();
		
		// 3. Compute linear hypothesis y=a*x+b
		SequenceMatchHypothesisLinear smh = new SequenceMatchHypothesisLinear(mp1, mp2);
		//LOG.debug("\n" + seq1.name + " & " + seq2.name + ": linear hypothesis: y = " + a + "*x + " + b);
		
		// 4. if all grid entries lying exactly on the line of the linear hypothesis, exit with success
		int matchCount = smh.check(mm);
		//LOG.debug("matchCount = " + matchCount);
		if (matchCondition.isSatisfied(s1.size(), s2.size(), matchCount)) {
			// found match with sufficient significance! this must be displayed
			// to show which oeis sequence is related to the lookup sequence.
			throw new SequenceMatchLinear(s1, s2, smh, mm);
			// by the way: if the match sequence is simple, then a lookup of
			// those values will show it :)
		}
		
		// TODO?
		// 5. if number of unique points < 3, then exit with error
		// 6. compute exponential hypothesis
		// 7. if all grid entries lying exactly on the curve of the exponential hypothesis, exit with success
		// 8. exit with error
	}
	
	OEISSequence getMatchSequence(OEISSequence s1, SequenceMatchHypothesis smh) {
		List<BigInteger> unsignedValues = s1.getUnsignedValues();
		List<BigInteger> unsignedMatchValues = new ArrayList<BigInteger>();
		for (MatchPoint position : smh.getHypothesisMatches()) {
			int xPosition = position.getFirst().intValue();
			BigInteger unsignedValue = unsignedValues.get(xPosition);
			unsignedMatchValues.add(unsignedValue);
		}
//		Sequence matchSequence = new SequenceBigIntListImpl("matchSequence", unsignedMatchValues);
//		Sequence matchSequence = new SequenceUnsignedIndexListImpl("matchSequence", unsignedMatchValues);
		OEISSequence matchSequence = new OEISSequence("matchSequence", new SequenceValues_IndexListImpl(unsignedMatchValues));
		// Note: No big speed difference, maybe SequenceIndexListImpl is slightly faster (2s of 4:17)...
		return matchSequence;
	}
}
