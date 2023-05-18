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

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;

import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues_UnsignedIndexListImpl;
import de.tilman_neumann.iss.sequence.ValuesStore;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatch;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchLinear;
import de.tilman_neumann.iss.sequenceMatch.SequenceMatchList;
import de.tilman_neumann.jml.base.BigIntList;
import de.tilman_neumann.util.TimeUtil;

public class OeisLookupEngineInMemory extends OeisLookupEngine {
	
	private static final Logger LOG = Logger.getLogger(OeisLookupEngineInMemory.class);

	private SequenceStore oeisSequences;
	
	/**
	 * Read OEIS data file.
	 */
	public void loadData(String oeisDataFileName) {
    	// read OEIS data:
    	LineIterator lineItr = null;
        try {
        	lineItr = FileUtils.lineIterator(new File(oeisDataFileName));
        } catch (IOException e) {
            LOG.error(e, e);
            return;
        }
        if (lineItr==null) {
        	LOG.error("OEIS data file " + oeisDataFileName + " not found or contains no data");
        	return;
        }
        
        LOG.info("load OEIS sequences from file " + oeisDataFileName + " ...");
        oeisSequences = new SequenceStore(false);
        int countIgnored = 0;
        try {
        	int lineNumber = 0;
        	while (lineItr.hasNext()) {
        		String line = lineItr.nextLine().trim();
        		lineNumber++;
        		if (line.startsWith("#")) continue; // comment
        		try {
    				String name = line.substring(0, line.indexOf(','));
    		        String valuesStr = line.substring(line.indexOf(',')+1);
    		        //LOG.debug("valuesStr = " + valuesStr);
    		        List<BigInteger> values = BigIntList.valueOf(valuesStr); // trims before parsing
    		        //LOG.debug("values = " + values);
//        			Sequence entry = new Sequence(name, new SequenceBigIntListImpl(values)); // hard memory consumption (>750MB)
        			// sequences in the complete database use a ValuesStore to save memory
//        			Sequence entry = new Sequence(name, new SequenceIndexListImpl(values)); // same results, but much slower lookup (21:21 instead 2:17)
        			OEISSequence entry = new OEISSequence(name, new SequenceValues_UnsignedIndexListImpl(values)); // fastest, higher memory consumption
        			boolean added = oeisSequences.add(entry);
    				if (!added) {
    					countIgnored++;
    				}
        		} catch (Exception ex) {
        			LOG.warn("Ignore broken line #" + lineNumber + ": " + line);
        			LOG.warn("Reason: " + ex);
                }
	        }
        } catch (OutOfMemoryError err) {
        	LOG.error("error loading OEIS data: " + err.getMessage());
        }
        LOG.info("read " + oeisSequences.size() + " OEIS sequences, ignored " + countIgnored + " exact duplicates");
        ValuesStore valuesStore = ValuesStore.get();
        valuesStore.finishStatisticCollection();
        LOG.info("number of distinct values: " + valuesStore.size());
        LOG.info("total value frequency: " + valuesStore.getTotalFrequency());
        double entropy = valuesStore.getEntropy();
        LOG.info("average value information: " + entropy + " nats");
        LOG.info("average value likelihood: " + Math.exp(-entropy));        
	}
	
	public boolean addSequence(OEISSequence seq) {
		//String name = seq.getName();
		//LOG.debug("before add of sequence " + name + ": name2seq=" + oeisSequences.name2seq.size() + ", seq2name=" + oeisSequences.seq2name.size());
		boolean ret = this.oeisSequences.add(seq);
		//LOG.debug("after add of sequence " + name + ": name2seq=" + oeisSequences.name2seq.size() + ", seq2name=" + oeisSequences.seq2name.size());
		return ret;
	}

	public OEISSequence removeSequence(String name) {
		//LOG.debug("before remove of sequence " + name + ": name2seq=" + oeisSequences.name2seq.size() + ", seq2name=" + oeisSequences.seq2name.size());
		OEISSequence ret = oeisSequences.remove(name);
		//LOG.debug("after remove of sequence " + name + ": name2seq=" + oeisSequences.name2seq.size() + ", seq2name=" + oeisSequences.seq2name.size());
		return ret;
	}
	
	/**
	 * Central lookup method. Compares a sequence and some transformations of it
	 * with the whole OEIS database.
	 * 
	 * @param lookupSeq sequence to lookup
	 * @param lookupMode how to lookup
	 */
	public void lookup(OEISSequence lookupSeq, OeisLookupMode lookupMode) {
		if (lookupSeq == null) throw new NullPointerException("lookupSeq");
		
		long totalPrepTime = 0;
		long totalLookupTime = 0;
		int maxNumberOfLookupValues = lookupSeq.size();
		String seqName = lookupSeq.getName();
		int numberOfValues = Math.min(10, maxNumberOfLookupValues);
		// number of matches is numberOfValues/3 or at least 3;
		// this matters mostly in the first round, because in later rounds we only
		// discard the hypotheses from the first round that don't work with more values
		int minNumberOfMatches = Math.max(3, numberOfValues/3);
		
		int round = 1;
		long startTime = System.currentTimeMillis();
		LookupCentral lookupCentral = new LookupCentral(minNumberOfMatches);
		LookupSequenceStore lookupSequences = lookupCentral.prepareLookupSequences(lookupSeq, lookupMode, numberOfValues);
		SequenceStore refSequences = oeisSequences;
		SequenceMatchList totalMatches = new SequenceMatchList();
		SequenceMatchList roundMatches = null;
		
		while (true) {
			
			long prepEndTime = System.currentTimeMillis();
			totalPrepTime += prepEndTime - startTime;
			String prepTimeStr = TimeUtil.timeDiffStr(startTime, prepEndTime);
			LOG.debug("Round " + round + ": Prepared " + lookupSequences.size() + " lookupSequences in " + prepTimeStr);

			// run lookup process:
			roundMatches = lookupCentral.lookup(lookupSequences, refSequences);
			// matches contain database matches and simple sequences
			long lookupEndTime = System.currentTimeMillis();
			totalLookupTime += lookupEndTime - prepEndTime;
			String lookupTimeStr = TimeUtil.timeDiffStr(prepEndTime, lookupEndTime);
			LOG.debug("Round " + round + ": Looking up sequence " + seqName + " took " + lookupTimeStr);

			// print round summary
			LOG.info("Round " + round + " match summary:\n" + roundMatches);
			LOG.info("Round " + round + ": Compared " + lookupSequences.size() + " transforms of " + seqName + " with " + refSequences.size() + " database sequences.");
			LOG.info("         Total number of matches = " + roundMatches.size());
			String roundTimeStr = TimeUtil.timeDiffStr(startTime, lookupEndTime);
			LOG.info("         Round time = " + roundTimeStr);
			LOG.info("                     (" + prepTimeStr + " for transforms,");
			LOG.info("                      " + lookupTimeStr + " for lookup)\n");
			// TODO: Print which hypotheses have been discarded
			
			// exit condition no.1: no more results to verify with more values
			if (roundMatches.size()==0) break;
			// exit condition no.2: no more values in lookup sequence
			if (numberOfValues == maxNumberOfLookupValues) break;
			
			// filter sequences for next round
			SequenceStore nextLookupSequences = new SequenceStore(false);
			refSequences = new SequenceStore(false);
			for (SequenceMatch match : roundMatches) {
				// caution: a sequence may have several matches...
				OEISSequence lookupSequence = match.getLookupSequence();
				if (match instanceof SequenceMatchLinear) {
					SequenceMatchLinear linearMatch = (SequenceMatchLinear) match;
					if (linearMatch.needsXExpansion()) {
						nextLookupSequences.add(lookupSequence);
						OEISSequence refSequence = linearMatch.getRefSequence();
						refSequences.add(refSequence);
					} else {
						// no refinement possible
						// add match to total result
						totalMatches.add(match);
					}
				} else {
					// arithmetic match needs always expansion
					nextLookupSequences.add(lookupSequence);
				}
			}
			LOG.debug("collected " + nextLookupSequences.size() + " lookup sequences and " + refSequences.size() + " database sequences for next round...");
			
			// otherwise there are more values, so we can expand transforms.
			numberOfValues = Math.min(numberOfValues+10, maxNumberOfLookupValues);
			// expand transforms
			round++;
			startTime = System.currentTimeMillis();
			lookupCentral = new LookupCentral(minNumberOfMatches);
			lookupSequences = lookupCentral.expandLookupSequences(nextLookupSequences, numberOfValues);
			// TODO: Only check the hypotheses that were found by previous round
		}
		
		// print total summary
		totalMatches.addAll(roundMatches);
		LOG.info("Total match summary:\n" + totalMatches);
		LOG.info("Total number of matches = " + totalMatches.size());
		LOG.info("Total time = " + TimeUtil.timeStr(totalPrepTime+totalLookupTime));
		LOG.info("            (" + TimeUtil.timeStr(totalPrepTime) + " for transforms,");
		LOG.info("             " + TimeUtil.timeStr(totalLookupTime) + " for lookup)\n");
	}
}
