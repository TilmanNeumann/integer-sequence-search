package de.tilman_neumann.iss.main;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequenceComparator.SequenceMatchFinderLinear;
import de.tilman_neumann.iss.sequenceComparator.SimpleSequenceFinder;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchCondition;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchConditionArithmeticStepFunction;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchArithmetic;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchLinear;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchList;

/**
 * OEIS lookup for a single number sequence and some transforms of it.
 * 
 * Parallelization is implemented such that one thread compares
 * one lookup sequence with the whole database. This requires quite a lot
 * of threads. To avoid re-instantiating all required member variables
 * in each thread, this class is not really a Thread but a Runnable 
 * that does the instantiations; these objects are reused by always new threads.

 * @author Tilman Neumann
 * @since 2011-08-15
 */
public class LookupThread implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(LookupThread.class);

	private final SequenceStore oeisSequences;
	private final int minNumberOfMatches;
	private final SimpleSequenceFinder simpleSequenceFinder;
	private final SequenceMatchFinderLinear matchFinder;
	private final LookupThreadController listener;

	private OEISSequence lookupSeq;

	public LookupThread(SequenceStore oeisSequences, int minNumberOfMatches, LookupThreadController listener) {
		this.oeisSequences = oeisSequences;
		this.minNumberOfMatches = minNumberOfMatches;
		this.simpleSequenceFinder = new SimpleSequenceFinder(minNumberOfMatches);
		//SequenceMatchCondition matchCondition = new SequenceMatchConditionAsymmetric(minNumberOfMatches);           // 5:21.6 with dup4, 1:1cond (61 matches)
		//SequenceMatchCondition matchCondition = new SequenceMatchConditionSymmetric(minNumberOfMatches);            // 5:20.5 with dup4, 1:1cond (61 matches)
		SequenceMatchCondition matchCondition = new SequenceMatchConditionArithmeticStepFunction(minNumberOfMatches); // 5:09.2 with dup4, 1:1cond (51 matches)
		this.matchFinder = new SequenceMatchFinderLinear(matchCondition);
		this.listener = listener;
	}

	/**
	 * Set sequence to lookup before starting a new thread.
	 * @param lookupSeq
	 */
	public void setLookupSeq(OEISSequence lookupSeq) {
		this.lookupSeq = lookupSeq;
	}
	
	/**
	 * OEIS lookup of the sequence set via {@link setLookupSeq}.
	 */
	public void run() {
		SequenceMatchList summary = new SequenceMatchList();
		// check first if the lookup sequence is a simple sequence.
		// simple sequences can be considered a generic extension of the database,
		// thus this is the right place to look for them.
		try {
			this.simpleSequenceFinder.testForSimpleSequence(lookupSeq);
		} catch (SequenceMatchArithmetic sse) {
			LOG.info("found potential simple sequence " + lookupSeq.getName() + ": " + sse.toString());
			summary.add(sse);
		}
		
		for (final OEISSequence oeisSequence : oeisSequences) {
			//LOG.info(lookupSeq.nameAndValuesString());
			if (oeisSequence.size() >= minNumberOfMatches) {
				try {
					matchFinder.compare(lookupSeq, oeisSequence);
				} catch (final SequenceMatchLinear match) {
					// print match matrix etc.
					LOG.info(match.toString() + "\n");
					// remember for summary printing
					summary.add(match);
				}
			}
		}
		this.listener.addPartialResult(this, summary); // wake up control thread
	}
}
