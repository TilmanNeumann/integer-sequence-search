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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An iterator that delivers unique matches of two sequences represented by the given match matrix.
 * @author Tilman Neumann
 */
public class UniqueMatchPointIterator implements Iterator<MatchPoint> {

	private Map<Integer, Set<Short>> yValues2Positions;
	private Iterator<Map.Entry<Integer, Set<Short>>> yValues2xPositionsIter;
	private MatchPoint nextMp = null;

	public UniqueMatchPointIterator(SequenceMatchMatrix mm) {
		this.yValues2xPositionsIter = mm.getyValues2xPositions().entrySet().iterator();
		this.yValues2Positions = mm.getyValues2Positions();
		this.nextMp = findNextMp();
	}

	private MatchPoint findNextMp() {
		while(yValues2xPositionsIter.hasNext()) {
			Map.Entry<Integer, Set<Short>> yValue2xPositions = yValues2xPositionsIter.next();
			Set<Short> xPositions = yValue2xPositions.getValue();
			if (xPositions!=null && xPositions.size()==1) {
				Integer value = yValue2xPositions.getKey();
				Set<Short> yPositions = yValues2Positions.get(value);
				if (yPositions!=null && yPositions.size()==1) {
					return new MatchPoint(xPositions.iterator().next(), yPositions.iterator().next());
				}
			}
		}
		return null;
	}
	
	public boolean hasNext() {
		return (nextMp!=null);
	}

	public MatchPoint next() {
		MatchPoint mp = nextMp;
		if (mp!=null) {
			this.nextMp = findNextMp();
		}
		return mp;
	}

	public void remove() {
		throw new IllegalStateException("remove() operation is unsupported for class UniqueMatchPointIterator");
	}
}
