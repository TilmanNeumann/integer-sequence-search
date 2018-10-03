package de.tilman_neumann.iss.main;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequenceMatch.SequenceMatchList;
import de.tilman_neumann.util.ConfigUtil;

/**
 * Controller for OEIS lookup threads.
 * 
 * This class also implements a thread pool that stores reusable runnables
 * and a thread group.
 * 
 * @author Tilman Neumann
 * @since 2011-08-15
 */
public class LookupThreadController extends ThreadGroup {
	
	private static final Logger LOG = Logger.getLogger(LookupThreadController.class);

	private final int maxThreads;

	private volatile LinkedList<LookupThread> runningThreads = new LinkedList<LookupThread>();
	private volatile LinkedList<LookupThread> idleThreads = new LinkedList<LookupThread>();

	private SequenceMatchList totalResult = new SequenceMatchList();

	/**
	 * Complete constructor, creates a list of reusable lookup runnables.
	 * @param oeisSequences
	 * @param minMatchCount
	 */
	public LookupThreadController(SequenceStore oeisSequences, int minMatchCount) {
		
		super("OEIS lookup threads");
		// create reusable runnables
		maxThreads = ConfigUtil.NUMBER_OF_PROCESSORS - 1; // keep 1 processor for control thread
		for (int i=0; i<maxThreads; i++) {
			LookupThread t = new LookupThread(oeisSequences, minMatchCount, this);
			idleThreads.add(t);
		}
	}

	/**
	 * Returns a free lookup runnable. If necessary, this method waits until
	 * a thread finishes and releases its runnable.
	 * @return free LookupThread
	 */
	public synchronized LookupThread get() {
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
		LookupThread t = idleThreads.pop();
		runningThreads.add(t);
		//LOG.info("return " + t);
		return t;
	}
	
	/**
	 * Adds the partial result of the given thread to the total result.
	 * @param t thread
	 * @param partialResult partial result
	 */
	synchronized void addPartialResult(LookupThread t, SequenceMatchList partialResult) {
		this.totalResult.addAll(partialResult);
		this.runningThreads.remove(t);
		this.idleThreads.add(t);
		this.notify(); // wake up control thread
	}

	/**
	 * @return The complete lookup result.
	 */
	public synchronized SequenceMatchList getResult() {
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
 