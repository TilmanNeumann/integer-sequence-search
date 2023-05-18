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
package de.tilman_neumann.iss.main;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.util.ConfigUtil;
import de.tilman_neumann.util.TimeUtil;

/**
 * Abstraction of a OEIS search engine.
 * @author Tilman Neumann
 */
abstract public class OeisLookupEngine {
	private static final Logger LOG = Logger.getLogger(OeisLookupEngine.class);
	
	/**
	 * Initialization of search engine.
	 * Logs required time and memory.
	 * @param engine The search engine to be initialized
	 */
	public void init() {
		// check initial memory status
        Runtime.getRuntime().gc();
    	long heapSize = Runtime.getRuntime().totalMemory();
    	//long heapMaxSize = Runtime.getRuntime().maxMemory();
    	long heapFreeSize = Runtime.getRuntime().freeMemory();
    	//LOG.debug("heapSize=" + heapSize + ",heapMaxSize=" + heapMaxSize + ",heapFreeSize=" + heapFreeSize);

    	String oeisDataFileName = ConfigUtil.PROJECT_ROOT + ConfigUtil.FILE_SEPARATOR + "stripped.txt";
    	long initStart = System.currentTimeMillis();
		this.loadData(oeisDataFileName);
		long initEnd = System.currentTimeMillis();
		
    	// check memory requirements:
        Runtime.getRuntime().gc();
    	long newHeapSize = Runtime.getRuntime().totalMemory();
    	//long newHeapMaxSize = Runtime.getRuntime().maxMemory();
    	long newHeapFreeSize = Runtime.getRuntime().freeMemory();
    	//LOG.debug("heapSize=" + newHeapSize + ",heapMaxSize=" + newHeapMaxSize + ",heapFreeSize=" + newHeapFreeSize);
    	long requiredMemory = (newHeapSize-newHeapFreeSize) - (heapSize-heapFreeSize);

		LOG.info("search engine init: " + TimeUtil.timeDiffStr(initStart, initEnd) + ", " + requiredMemory + " bytes");
	}
	
	/**
	 * Read OEIS data file.
	 */
	abstract public void loadData(String oeisDataFileName);

	/**
	 * Adds the given sequence to the store of known sequences.
	 * @param seq
	 * @return true if really added
	 */
	abstract public boolean addSequence(OEISSequence seq);

	/**
	 * Removes the sequence with the given name.
	 * @param name
	 * @return Sequence or null
	 */
	abstract public OEISSequence removeSequence(String name);
	
	/**
	 * Looks up some transformations of the given sequence.
	 * @param lookupSeq the sequence for which we want to find some relations.
	 * @param lookupMode The kind of relations we are looking for
	 */
	abstract public void lookup(OEISSequence lookupSeq, OeisLookupMode lookupMode);
}
