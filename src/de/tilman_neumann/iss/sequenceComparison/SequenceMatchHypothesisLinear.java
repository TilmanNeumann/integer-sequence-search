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
package de.tilman_neumann.iss.sequenceComparison;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequenceMatch.MatchPoint;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchMatrix;

/**
 * Linear sequence index match hypothesis a*x+b.
 * Mismatches in the first three values of any of the sequence are ignored.
 * @author Tilman Neumann
 */
public class SequenceMatchHypothesisLinear implements SequenceMatchHypothesis {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SequenceMatchHypothesisLinear.class);

	private double a;
	private double b;
	private int matchScore = 0;
	List<MatchPoint> hypothesisMatches;
	
	/**
	 * Constructor for linear match hypothesis: s2_index = a * s1_index + b.
	 * The match score still needs to be computed.
	 * @param a
	 * @param b
	 */
	public SequenceMatchHypothesisLinear(double a, double b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Constructor for linear match hypothesis: s2_index = a * s1_index + b
	 * where the match score is already known.
	 * @param a
	 * @param b
	 */
	public SequenceMatchHypothesisLinear(double a, double b, int matchScore) {
		this.a = a;
		this.b = b;
		this.matchScore = matchScore;
	}
	
	/**
	 * Constructor for linear match hypothesis: s2_index = a * s1_index + b.
	 * The match score still needs to be computed.
	 * @param mp1 first match point (x1, y1)
	 * @param mp2 second match point (x2, y2)
	 */
	public SequenceMatchHypothesisLinear(MatchPoint mp1, MatchPoint mp2) {
		int x1 = mp1.getFirst().intValue();
		int y1 = mp1.getSecond().intValue();
		int x2 = mp2.getFirst().intValue();
		int y2 = mp2.getSecond().intValue();

		this.a = (y2-y1) / (double) (x2-x1);
		this.b = y1 - a*x1;
	}

	/**
	 * Checks if the hypothesis is satisfied by the given match matrix.
	 * @return match score, or 0 if the hypothesis is not satisfied.
	 */
	public int check(SequenceMatchMatrix mm) {
		hypothesisMatches = new LinkedList<MatchPoint>();
		
		short xDim = mm.getXDim();
		short yDim = mm.getYDim();
		short xMaxFromY = (short) ((a>=0) ? (yDim-1-b)/a : -(0-b)/a);
		short xMax = (short) Math.min(xDim-1, xMaxFromY);
		for (short x=0; x<=xMax; x++) {
			double y = a*x + b;
			if (y > yDim-1) {
				// index exceeds number of values in sequence 2 -> finish
				break;
			}
			if (y>=0) {
				// y is in the valid index range of sequence 2
				short i_y = (short) y;
				if (i_y == y) {
					// y hits an exact integer -> hypothesis test required !
					boolean isMatch = mm.isMatch(x, i_y);
					//LOG.debug("isMatch(" + x + "," + i_y + ") = " + isMatch);
					if (isMatch) {
						hypothesisMatches.add(new MatchPoint(Short.valueOf(x), Short.valueOf(i_y)));
					} else {
						// (x,y) is not a match!
						if (x>2 && i_y>2) {
							// we can't ignore mismatches in higher regions
							matchScore = 0;
							return 0;
						}
						// ignore mismatch in first few values
					}
				}
			}
		}
		
		matchScore = hypothesisMatches.size();
		return matchScore;
	}

	public List<MatchPoint> getHypothesisMatches() {
		return this.hypothesisMatches;
	}

	public int getMatchScore() {
		return matchScore;
	}
	
	public boolean needsXExpansion(SequenceMatchMatrix mm) {
		short xDim = mm.getXDim();
		short yDim = mm.getYDim();
		short xMaxFromY = (short) ((a>=0) ? (yDim-1-b)/a : -(0-b)/a);
		return (xDim-1 < xMaxFromY);
	}
	
	/**
	 * @return the slope a of the linear function a*x+b that is the index match hypothesis
	 */
	public double getSlope() {
		return a;
	}
	
	/**
	 * @return the abscissa b of the linear function a*x+b that is the index match hypothesis
	 */
	public double getAbscissa() {
		return b;
	}
	
	@Override
	public String toString() {
		return "linear hypothesis: y = " + a + "*x + " + b;
	}
}
