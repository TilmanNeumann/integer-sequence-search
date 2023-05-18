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
import de.tilman_neumann.iss.transform.Transform;
import de.tilman_neumann.iss.transform.Transformation;
import de.tilman_neumann.iss.transform.TransformationException;

/**
 * Expansion of sequence transforms, unthreaded or threaded.

 * @author Tilman Neumann
 */
public class ExpandThread implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(ExpandThread.class);

	private final ExpandThreadController listener;

	private int maxNumberOfValues;

	private OEISSequence inputSeq;

	/**
	 * Full constructor for threaded transform computations.
	 * @param transformations
	 * @param maxNumberOfValues
	 * @param allowSimilarResults
	 * @param listener
	 */
	public ExpandThread(int maxNumberOfValues, ExpandThreadController listener) {
		this.maxNumberOfValues = maxNumberOfValues;
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
	 * Expands transforms of the input sequence in a new thread.
	 */
	public void run() {
		if (inputSeq == null) throw new NullPointerException("input sequence");
		//LOG.info("input seq = " + inputSeq.nameAndValuesString());
		
		Transform oldTransform = (Transform) inputSeq;
		try {
			Transform newTransform = oldTransform.expand(maxNumberOfValues);
			this.listener.addPartialResult(newTransform);
		} catch (TransformationException e1) {
			Transformation transformation = oldTransform.getTransformation();
			LOG.info("Could not compute " + transformation.getName() + " transform of sequence " + inputSeq.getName());
		}
		
		// expand duplicates, too, in case they differ in higher index values
		for (OEISSequence duplicate : oldTransform.getDuplicates()) {
			Transform dupTransform = (Transform) duplicate;
			try {
				Transform newDupTransform = dupTransform.expand(maxNumberOfValues);
				this.listener.addPartialResult(newDupTransform);
			} catch (TransformationException e) {
				Transformation dupTransformation = dupTransform.getTransformation();
				LOG.info("Could not compute " + dupTransformation.getName() + " transform of sequence " + dupTransform.getName());
			}
		}
		// TODO: Still too fine-grained ?
		this.listener.notifyFinish(this); // wake up control thread
	}
}
