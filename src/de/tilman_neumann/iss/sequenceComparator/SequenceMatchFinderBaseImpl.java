package de.tilman_neumann.iss.sequenceComparator;

import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchCondition;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchLinear;

/**
 * A comparator for sequences.
 * @author Tilman Neumann
 * @since 2009-01-24
 */
abstract public class SequenceMatchFinderBaseImpl {
	
	private static final Logger LOG = Logger.getLogger(SequenceMatchFinderBaseImpl.class);

	// inputs
	protected SequenceMatchCondition matchCondition;
	
	public SequenceMatchFinderBaseImpl(SequenceMatchCondition matchCondition) {
		this.matchCondition = matchCondition;
		//LOG.debug("\n" + seq1.name + "\n & " + seq2.name);
	}
	
	/**
	 * Returns the first position of the given value in values, or null if not contained.
	 * @param values
	 * @param value
	 * @return first position or null
	 */
	public static Integer indexOf(List<Integer> values, Integer value) {
		if (values==null) return null;
		int index = values.indexOf(value);
		return (index>=0) ? Integer.valueOf(index) : null;
	}
		
	/**
	 * Returns the first position of the given value in values, or null if not contained.
	 * @param values
	 * @param value
	 * @return first position or null
	 */
	public static Integer indexOf(int[] values, int value) {
		if (values==null) {
			return null;
		}
		for (int pos=0; pos<values.length; pos++) {
			if (values[pos]==value) {
				return Integer.valueOf(pos);
			}
		}
		return null;
	}
	
	abstract public void compare(OEISSequence s1, OEISSequence s2) throws SequenceMatchLinear;
}
