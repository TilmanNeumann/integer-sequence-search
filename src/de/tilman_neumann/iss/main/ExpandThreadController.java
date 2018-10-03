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
package de.tilman_neumann.iss.main;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.util.ConfigUtil;

/**
 * Controller for threaded sequence transform computations.
 * 
 * This class also implements a thread pool that stores reusable runnables
 * and a thread group.
 * 
 * @author Tilman Neumann
 */
public class ExpandThreadController extends ThreadGroup {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ExpandThreadController.class);

	private final int maxThreads;

	private volatile LinkedList<ExpandThread> runningThreads = new LinkedList<ExpandThread>();
	private volatile LinkedList<ExpandThread> idleThreads = new LinkedList<ExpandThread>();
	
	private LookupSequenceStore totalResult;

	/**
	 * Complete constructor, creates a list of reusable lookup runnables.
	 * @param minMatchCount
	 */
	public ExpandThreadController(int maxNumberOfValues, int minMatchCount, boolean allowSimilarSequences) {
		
		super("OEIS transform threads");
		this.totalResult = new LookupSequenceStore(minMatchCount, allowSimilarSequences, true);
		
		// create reusable runnables
		maxThreads = ConfigUtil.NUMBER_OF_PROCESSORS - 1; // keep 1 processor for control thread
		for (int i=0; i<maxThreads; i++) {
			ExpandThread t = new ExpandThread(maxNumberOfValues, this);
			idleThreads.add(t);
		}
	}

	/**
	 * Returns a free lookup runnable. If necessary, this method waits until
	 * a thread finishes and releases its runnable.
	 * @return free ExpandThread
	 */
	public synchronized ExpandThread get() {
		while (runningThreads.size()==maxThreads) {
			//LOG.info(runningThreads.size() + " running and " + idleThreads.size() + " idle threads");
			//LOG.info("Wait for idle thread...");
			// we have to wait. when a thread gets idle then
			// it calls this.notify() which will wake this up
			try { 
				this.wait();
			} catch (InterruptedException ie) {
				// sleep again...
			}
		}
		// now there should be an idle thread
		//LOG.info(runningThreads.size() + " running and " + idleThreads.size() + " idle threads");
		ExpandThread t = idleThreads.pop();
		runningThreads.add(t);
		//LOG.info("return " + t);
		return t;
	}
	
	/**
	 * Add a single sequence to the result sequence set.
	 * Synchronization is handled inside...
	 * @param seq
	 */
	void addPartialResult(OEISSequence seq) {
		this.totalResult.add(seq);
	}
	
	/**
	 * React on notification that a transformation process has finished.
	 * @param t thread
	 */
	synchronized void notifyFinish(ExpandThread t) {
		this.runningThreads.remove(t);
		this.idleThreads.add(t);
		this.notify(); // wake up control thread
	}

	/**
	 * @return The complete lookup result.
	 */
	public synchronized LookupSequenceStore getResult() {
		while (runningThreads.size()>0) {
			try {
				this.wait();
			} catch (InterruptedException ie) {
				// sleep again...
			}
		}
		return totalResult;
	}
}
