package de.tilman_neumann.math.app.oeis.main;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import de.tilman_neumann.math.app.oeis.sequence.OEISSequence;
import de.tilman_neumann.math.app.oeis.transform.Transformation;
import de.tilman_neumann.util.ConfigUtil;

/**
 * Controller for threaded sequence transform computations.
 * 
 * This class also implements a thread pool that stores reusable runnables
 * and a thread group.
 * 
 * @author Tilman Neumann
 * @since 2011-08-15
 */
public class TransformThreadController extends ThreadGroup {
	
	private static final Logger LOG = Logger.getLogger(TransformThreadController.class);

	private final int maxThreads;

	private volatile LinkedList<TransformThread> runningThreads = new LinkedList<TransformThread>();
	private volatile LinkedList<TransformThread> idleThreads = new LinkedList<TransformThread>();
	
	private LookupSequenceStore totalResult;

	/**
	 * Complete constructor, creates a list of reusable lookup runnables.
	 * @param minMatchCount
	 */
	public TransformThreadController(Transformation[] transformations, int maxNumberOfValues, int minMatchCount, boolean allowSimilarSequences) {
		
		super("OEIS transform threads");
		this.totalResult = new LookupSequenceStore(minMatchCount, allowSimilarSequences, true);
		
		// create reusable runnables
		maxThreads = ConfigUtil.NUMBER_OF_PROCESSORS - 1; // keep 1 processor for control thread
		for (int i=0; i<maxThreads; i++) {
			TransformThread t = new TransformThread(transformations, maxNumberOfValues, allowSimilarSequences, this);
			idleThreads.add(t);
		}
	}

	/**
	 * Returns a free lookup runnable. If necessary, this method waits until
	 * a thread finishes and releases its runnable.
	 * @return free LookupThread
	 */
	public synchronized TransformThread get() {
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
		TransformThread t = idleThreads.pop();
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
	synchronized void notifyFinish(TransformThread t) {
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
