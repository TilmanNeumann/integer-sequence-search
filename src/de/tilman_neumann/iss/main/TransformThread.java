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
import de.tilman_neumann.iss.transform.Transformation;
import de.tilman_neumann.iss.transform.TransformationException;

/**
 * Computation of sequence transforms, unthreaded or threaded.
 *
 * @author Tilman Neumann
 */
public class TransformThread implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(TransformThread.class);

	private final TransformThreadController listener;

	private Transformation[] transformations;
	private int maxNumberOfValues;
	private boolean allowSimilarResults;

	private OEISSequence inputSeq;

	/**
	 * Full constructor for threaded transform computations.
	 * @param transformations
	 * @param maxNumberOfValues
	 * @param allowSimilarResults
	 * @param listener
	 */
	public TransformThread(Transformation[] transformations, int maxNumberOfValues, boolean allowSimilarResults, TransformThreadController listener) {
		this.transformations = transformations;
		this.maxNumberOfValues = maxNumberOfValues;
		this.allowSimilarResults = allowSimilarResults;
		this.listener = listener;
	}

	/**
	 * Set the sequence to transform.
	 * Must be called before invoking the run() method.
	 * @param inputSeq
	 */
	public void setInputSeq(OEISSequence inputSeq) {
		this.inputSeq = inputSeq;
	}
	
	/**
	 * Computes transforms of the input sequence in a new thread.
	 */
	public void run() {
		if (inputSeq == null) throw new NullPointerException("input sequence");
		//LOG.info("input seq = " + inputSeq.nameAndValuesString());
		
		// compute transforms:
		for (Transformation transformation : this.transformations) {
			try {
				OEISSequence seq = transformation.compute(inputSeq, maxNumberOfValues, !allowSimilarResults);
				this.listener.addPartialResult(seq);
			} catch (TransformationException e) {
				LOG.info("Could not compute " + transformation.getName() + " transform of sequence " + inputSeq.getName());
			}
		}
		this.listener.notifyFinish(this); // wake up control thread
	}

	/**
	 * Computes transforms of the input sequence without threads.
	 * 
	 * @return computed transforms
	 */
	public static LookupSequenceStore computeTransforms(OEISSequence inputSeq, Transformation[] transformations, int maxNumberOfValues, int minNumberOfMatches, boolean allowSimilarResults) {
		if (inputSeq == null) throw new NullPointerException("input sequence");
		//LOG.info("input seq = " + inputSeq.nameAndValuesString());
		
		// do not log intermediate results
		LookupSequenceStore sequences = new LookupSequenceStore(transformations.length, minNumberOfMatches, allowSimilarResults, false);
		// compute simple transforms:
		for (Transformation transformation : transformations) {
			try {
				sequences.add(transformation.compute(inputSeq, maxNumberOfValues, !allowSimilarResults));
			} catch (TransformationException te) {
				LOG.info("Discard " + transformation.getName() + " transform of sequence " + inputSeq.getName() + ". Reason: " + te.getMessage());
			}
		}
		return sequences;
	}
}
