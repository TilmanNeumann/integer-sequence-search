package de.tilman_neumann.iss.sequenceMatch;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tilman_neumann.iss.sequence.OEISSequence;

abstract public class SequenceMatchMatrix implements Serializable {

	private static final long serialVersionUID = 812531421976438151L;
	
	protected OEISSequence s1;
	protected OEISSequence s2;
	protected short xDim;
	protected short yDim;

	public SequenceMatchMatrix(OEISSequence s1, OEISSequence s2) {
		this.s1 = s1;
		this.s2 = s2;
		xDim = (short)s1.size();
		yDim = (short)s2.size();
		//LOG.debug("xDim = " + xDim + ", yDim = " + yDim);
	}
	
	public short getXDim() {
		return xDim;
	}

	public short getYDim() {
		return yDim;
	}

	@Override
	// TODO: Mark chosen matches in a special way
	public String toString() {
		StringBuffer bu = new StringBuffer();
		for (short y=0; y<yDim; y++) {
			// Debugging: print match matrix and row/column sums
			for (short x=0; x<xDim; x++) {
				bu.append( isMatch(x,y) ? "X" : "-");
			}
			bu.append(/*" " + matchesRowSums[y] +*/ "\n");
		}
//		StringBuffer bu = new StringBuffer();
//		for (int x=0; x<xDim; x++) {
//			bu.append( matchesColumnSums[x] );
//		}
//		LOG.debug(bu.toString());
		return bu.toString();
	}
	
	// TODO: Should we compute this already in the sequence constructors?
	protected Map<Integer, Set<Short>> hashSequenceValues2Positions(List<Integer> values) {
		short numberOfValues = (short)values.size();
		Map<Integer, Set<Short>> values2Positions = new HashMap<Integer, Set<Short>>(numberOfValues);
		Iterator<Integer> valuesIter = values.iterator();
		for (short i=0; i<numberOfValues; i++) {
			Integer value = valuesIter.next();
			//LOG.debug("value " + i + " = " + value);
			this.addToValues2PositionsMap(values2Positions, value, i);
		}
		return values2Positions;
	}
	
	protected Map<Integer, Set<Short>> hashSequenceValues2Positions(int[] values) {
		short numberOfValues = (short)values.length;
		Map<Integer, Set<Short>> values2Positions = new HashMap<Integer, Set<Short>>(numberOfValues);
		for (short i=0; i<numberOfValues; i++) {
			Integer value = Integer.valueOf(values[i]);
			//LOG.debug("value " + i + " = " + value);
			this.addToValues2PositionsMap(values2Positions, value, i);
		}
		return values2Positions;
	}

	protected void addToValues2PositionsMap(Map<Integer, Set<Short>> values2Positions, Integer value, short position) {
		Set<Short> positions = values2Positions.get(value);
		if (positions == null) {
			positions = new HashSet<Short>();
		}
		positions.add(Short.valueOf(position));
		//LOG.debug("indices = " + indices);
		values2Positions.put(value, positions);
	}

	abstract public boolean isMatch(short x, short y);

	abstract public Map<Integer, Set<Short>> getyValues2Positions();

	abstract public Map<Integer, Set<Short>> getyValues2xPositions();
}