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
package de.tilman_neumann.iss.transform;

/**
 * Variations of convolution transforms.
 * @author Tilman Neumann
 */
public enum ConvolutionTransformationType {
	STANDARD("conv", 0),
	EXP("expConv", 1),
	LCM("lcmConv", 2),
	GCD("gcdConv", 3),
	STIRLING1("s1Conv", 4),
	STIRLING2("s2Conv", 5),
	ABS_STIRLING1("as1Conv", 6),
	DOUBLE_STIRLING1("s1s1Conv", 7),
	DOUBLE_STIRLING2("s2s2Conv", 8),
	STIRLING1_STIRLING2("s1s2Conv", 9),
	STIRLING2_STIRLING1("s2s1Conv", 10),
	DOUBLE_ABS_STIRLING1("as1as1Conv", 11),
	ABS_STIRLING1_STIRLING2("as1s2Conv", 12),
	STIRLING2_ABS_STIRLING1("s2as1Conv", 13);
	
	private String name;
	private int complexityScore;
	
	private ConvolutionTransformationType(String name, int complexityScore) {
		this.name = name;
		this.complexityScore = complexityScore;
	}
	
	/**
	 * @return the name of this kind of convolution
	 */
	public String getName() {
		return this.name;
	}
	
	public int getComplexityScore() {
		return this.complexityScore;
	}
}
