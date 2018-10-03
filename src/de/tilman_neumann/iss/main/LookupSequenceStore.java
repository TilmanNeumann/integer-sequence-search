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

import java.util.Set;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequenceComparator.SequenceDuplicateFinder4;
import de.tilman_neumann.iss.sequenceComparator.SequenceMatchFinderBaseImpl;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchCondition;
import de.tilman_neumann.iss.sequenceComparison.SequenceMatchCondition1To1;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchLinear;
import de.tilman_neumann.iss.transform.Transform;

/**
 * A map of distinct sequences to their names with fast lookup by exact values.
 * @author Tilman Neumann
 */
public class LookupSequenceStore extends SequenceStore {

	private static final long serialVersionUID = -9201914563761527886L;
	
	private static final Logger LOG = Logger.getLogger(LookupSequenceStore.class);
	
	private final SequenceMatchFinderBaseImpl duplicateFinder;
	private boolean allowSimilarResults;
	
	/**
	 * Constructor for an empty sequence set with default initial load capacity 16.
	 */
	public LookupSequenceStore(int minNumberOfMatches, boolean allowSimilarResults, boolean verbose) {
		this(16, minNumberOfMatches, allowSimilarResults, verbose);
	}
	
	/**
	 * Constructor for an empty sequence set with specific initial load capacity.
	 */
	public LookupSequenceStore(int capacity, int minNumberOfMatches, boolean allowSimilarResults, boolean verbose) {
		super(capacity, verbose);
//		final SequenceMatchCondition duplicateCondition = new SequenceMatchConditionAsymmetric(minNumberOfMatches); // 5:09.9 with dup4
//		final SequenceMatchCondition duplicateCondition = new SequenceMatchConditionSymmetric(minNumberOfMatches);  // 5:09.7 with dup4
		final SequenceMatchCondition duplicateCondition = new SequenceMatchCondition1To1(minNumberOfMatches);       // 5:09.2 with dup4
//		duplicateFinder = new SequenceMatchFinderLinear(duplicateCondition);// 5:10.4 with 1:1cond
//		duplicateFinder = new SequenceDuplicateFinder(duplicateCondition);  // 5:10.8 with 1:1cond
//		duplicateFinder = new SequenceDuplicateFinder2(duplicateCondition); // 5:13.7 with 1:1cond
//		duplicateFinder = new SequenceDuplicateFinder3(duplicateCondition); // 5:11.6 with 1:1cond
		duplicateFinder = new SequenceDuplicateFinder4(duplicateCondition); // 5:09.2 with 1:1cond
		this.allowSimilarResults = allowSimilarResults;
	}

	/**
	 * Adds a new sequence to this sequence set.
	 * Simple sequences and near duplicates of already contained sequences are
	 * filtered if <code>allowSimilarResults = false</code>.
	 * 
	 * @param seq sequence to add
	 * @return true if added, false if not new
	 */
	public boolean add(OEISSequence seq) {
		if (!allowSimilarResults) {
			// check for almost exact matches (except sign and start values).
			// this is synchronized because other threads may add new sequences
			// while we are running here over the list of currently available
			// lookup sequences; we could avoid synchronization if the search for
			// duplicates would be done in a separate step...
			synchronized (syncObject) {
				for (final OEISSequence storedSequence : this) {
					try {
						this.duplicateFinder.compare(storedSequence, seq);
					} catch (final SequenceMatchLinear match) {
						// keep duplicate for comparisons with more values
						if (storedSequence instanceof Transform && seq instanceof Transform) {
							// keep the one with the lower complexity as representative
							String storedName = storedSequence.getName();
							String seqName = seq.getName();
							Transform storedTransform = (Transform) storedSequence;
							Transform newTransform = (Transform) seq;
							int storedComplexity = storedTransform.getComplexityScore();
							int newComplexity = newTransform.getComplexityScore();
							//LOG.debug("Stored seq. " + storedName + " has complexity " + storedComplexity);
							//LOG.debug("New seq. " + seqName + " has complexity " + newComplexity);
							if (storedComplexity > newComplexity) {
								// replace current representative with new sequence
								// (already synchronized)
								super.remove(storedName);
								Set<OEISSequence> storedDuplicates = storedSequence.removeDuplicates();
								seq.addDuplicates(storedDuplicates);
								seq.addDuplicate(storedSequence);
								super.add(seq);
								LOG.info("replaced sequence " + storedName + " with sequence " + seqName);
								return true; //added
							}
						}
						
						// keep current representative
						storedSequence.addDuplicate(seq);
						LOG.info("ignored duplicate of " + storedSequence.getName() + " (match count = " + match.getMatchCount() + "): " + seq.nameAndValuesString());
						Set<OEISSequence> duplicateDuplicates = seq.removeDuplicates();
						int numberOfDuplicateDuplicates = duplicateDuplicates.size();
						if (numberOfDuplicateDuplicates > 0) {
							//LOG.debug("duplicate sequence " + seq.getName() + " had " + numberOfDuplicateDuplicates + " duplicates, too!");
							storedSequence.addDuplicates(duplicateDuplicates);
						}
						return false;
					}
				}
			}
		}
		boolean added = super.add(seq);
		if (added) {
			if (verbose) LOG.info("added " + seq.nameAndValuesString());
			return true;
		}
		return false;
	}
}
