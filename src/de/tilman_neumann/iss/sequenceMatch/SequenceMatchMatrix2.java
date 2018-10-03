package de.tilman_neumann.iss.sequenceMatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;

/**
 * A match matrix for two sequences.
 * @author Tilman Neumann
 * @since ~2008-12-20
 */
public class SequenceMatchMatrix2 extends SequenceMatchMatrix {

	private static final Logger LOG = Logger.getLogger(SequenceMatchMatrix2.class);
	
	private static final long serialVersionUID = 6177298374247406064L;
	
	private Map<Integer, Set<Short>> yValues2Positions;
	private Map<Integer, Set<Short>> yValues2xPositions;
	private int[] s2UnsignedIndices;
	
	public SequenceMatchMatrix2(OEISSequence s1, OEISSequence s2) {
		super(s1, s2);

		// With this we get all y-indices for a certain value:
		Map<Integer, Set<Short>> xValuesToPositions = hashSequenceValues2Positions(s1.getUnsignedValueIndices());
		//LOG.debug("xValuesToPositions = " + xValuesToPositions);
		
		// initialize arrays:
		this.yValues2xPositions = new HashMap<Integer, Set<Short>>();
		this.yValues2Positions = new HashMap<Integer, Set<Short>>();
		this.s2UnsignedIndices = s2.getUnsignedValueIndices();

		for (short y=0; y<yDim; y++) {
			// this is the value of seq2 for row y:
			Integer seq2Value = s2UnsignedIndices[y];
			this.addToValues2PositionsMap(yValues2Positions, seq2Value, y);
			// where does it occur in seq1? these are the matches for row y:
			Set<Short> xPositions = xValuesToPositions.get(seq2Value);
			if (xPositions!=null && xPositions.size()>0) {
				yValues2xPositions.put(seq2Value, xPositions);
			}
		}
		
		//LOG.debug("yValues2xPositions = " + yValues2xPositions);
	}
	
	/* (non-Javadoc)
	 * @see de.tilman_neumann.math.app.oeis.sequenceComparison.SequenceMatchMatrix#isMatch(int, int)
	 */
	public boolean isMatch(short xPos, short yPos) {
		Integer yValue = s2UnsignedIndices[yPos];
		Set<Short> xPositionsForYValue = yValues2xPositions.get(yValue);
		//LOG.debug("xPositionsForYValue(" + yValue + ") = " + xPositionsForYValue);
		if (xPositionsForYValue==null) {
			return false;
		}
		return xPositionsForYValue.contains(Short.valueOf(xPos));
	}

	@Override
	public Map<Integer, Set<Short>> getyValues2Positions() {
		return yValues2Positions;
	}

	@Override
	public Map<Integer, Set<Short>> getyValues2xPositions() {
		return yValues2xPositions;
	}
}
