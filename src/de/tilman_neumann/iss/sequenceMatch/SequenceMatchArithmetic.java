/*
 * integer-sequence-search (ISS) is an offline OEIS sequence search engine.
 * Copyright (C) 2018 Tilman Neumann (www.tilman-neumann.de)
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

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import de.tilman_neumann.util.StringUtil;
import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequenceComparator.SimpleSequenceType;

/**
 * Exception thrown when a sequence has been detected to be "simple".
 * @author Tilman Neumann
 */
public class SequenceMatchArithmetic extends SequenceMatch {

	private static final long serialVersionUID = -4265606482514330537L;

	private OEISSequence seq;
	private SimpleSequenceType type;
	private int order;
	private int significance;
	private List<List<BigInteger>> allValueLists;
	
	/**
	 * Complete constructor.
	 * @param name
	 * @param type the kind of simple series
	 * @param order
	 */
	public SequenceMatchArithmetic(OEISSequence seq, SimpleSequenceType type, int order, int significance, List<List<BigInteger>> allValueLists) {
		this.seq = seq;
		this.type = type;
		this.order = order;
		this.significance = significance;
		this.allValueLists = allValueLists;
	}
	
	@Override
	public String toString() {
		String ret = this.getRefName();
		ret += " (significance=" + significance + ")";
		if (allValueLists!=null && allValueLists.size()>0) {
			ret += "\n";
			Iterator<List<BigInteger>> valueListIter = allValueLists.iterator();
			List<BigInteger> valueList = valueListIter.next();
			String valueListStr = valueList.toString();
			int maxSize = valueListStr.length();
			ret += (valueListStr + "\n");
			while (valueListIter.hasNext()) {
				valueList = valueListIter.next();
				valueListStr = valueList.toString();
				int size = valueListStr.length();
				int offset = Math.max(0, (maxSize-size)/2);
				ret += (StringUtil.repeat(" ", offset) + valueListStr + "\n");
			}
		}
		return ret;
	}

	@Override
	public OEISSequence getLookupSequence() {
		return seq;
	}

	@Override
	public int getMatchCount() {
		return this.significance;
	}

	@Override
	public String getRefName() {
		String ret = String.valueOf(order);
		int modOrder = order % 10;
		switch (modOrder) {
		case 1: ret += "st"; break;
		case 2: ret += "nd"; break;
		case 3: ret += "rd"; break;
		default: ret += "th";
		}
		ret += "-order arithmetic progression";
		
		switch (type) {
		case CONSTANT:
			break;
		case STEP_FUNCTION:
			ret += " of a step function";
			break;
		case SINGLE_PEAK:
			ret += " of a single peak function";
			break;
		}
		return ret;
	}
}
