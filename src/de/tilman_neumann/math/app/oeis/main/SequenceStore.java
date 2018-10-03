package de.tilman_neumann.math.app.oeis.main;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.tilman_neumann.math.app.oeis.sequence.OEISSequence;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues;
import de.tilman_neumann.math.app.oeis.transform.Transform;

/**
 * A collection of sequences with fast lookup by (exact) values and by name.
 * 
 * @author Tilman Neumann
 * @since 2008-12-19
 */
public class SequenceStore implements Iterable<OEISSequence>, Serializable {

	private static final long serialVersionUID = -545594281693791230L;
	
	private static final Logger LOG = Logger.getLogger(SequenceStore.class);
	
	// basic data structure ----------------------------------------------
	// Note that if we wanted both fast lookup from names and fast iteration,
	// then we would need indices; but then add and remove were expensive
	// because we had to adjust all indices on that operations...

	// this must be a map for fast lookup from values
	private HashMap<SequenceValues, String> values2name;
	// map for fast lookup from names; with sub-optimal iteration speed.
	private Map<String, OEISSequence> name2seq;
	// used to synchronize add()
	protected final Boolean syncObject = new Boolean(true);
	// -------------------------------------------------------------------

	protected boolean verbose = false;

	/**
	 * Constructor for an empty sequence set with default initial load capacity.
	 */
	public SequenceStore(boolean verbose) {
		this(16, verbose);
	}
	
	/**
	 * Constructor for an empty sequence set with specified initial load capacity.
	 */
	public SequenceStore(int capacity, boolean verbose) {
		values2name = new HashMap<SequenceValues, String>(capacity);
		name2seq = new HashMap<String, OEISSequence>(capacity);
		this.verbose = verbose;
	}
	
	/**
	 * Adds a sequence to this sequence set;
	 * sequences with exactly the same values are added as "duplicates".
	 * 
	 * @param seq The sequence to add
	 * @return true if added, false if not new
	 */
	public boolean add(OEISSequence seq) {
		//LOG.debug("test sequence " + seq.nameAndValuesString());
		String seqName = seq.getName();
		 // a sequence with the same name must not be added twice
		if (name2seq.containsKey(seqName)) return false;
		
		SequenceValues values = seq.getAbstractValues();
		String storedName = values2name.get(values);
		if (storedName == null) {
			// values are not known so far
			synchronized (syncObject) {
				// check again to exclude that another thread has added the sequence
				// while the current thread was waiting
				storedName = values2name.get(values);
				if (storedName == null) {
					name2seq.put(seqName, seq);
					values2name.put(values, seqName);
					//LOG.debug("added sequence " + seq.nameAndValuesString());
					return true; //added
				}
			}
		}
		
		// There is already a sequence with exactly the same values.
		// Keep duplicate for new comparisons when more values are available
		OEISSequence storedSequence = get(storedName);
		if (storedSequence instanceof Transform && seq instanceof Transform) {
			// keep the one with the lower complexity as representative
			Transform storedTransform = (Transform) storedSequence;
			Transform newTransform = (Transform) seq;
			int storedComplexity = storedTransform.getComplexityScore();
			int newComplexity = newTransform.getComplexityScore();
			//LOG.debug("Stored seq. " + storedName + " has complexity " + storedComplexity);
			//LOG.debug("New seq. " + seq.getName() + " has complexity " + newComplexity);
			if (storedComplexity > newComplexity) {
				// replace current representative with new sequence
				synchronized(syncObject) {
					name2seq.remove(storedName);
					values2name.remove(storedSequence.getAbstractValues());
					Set<OEISSequence> storedDuplicates = storedSequence.removeDuplicates();
					seq.addDuplicates(storedDuplicates);
					seq.addDuplicate(storedSequence);
					name2seq.put(seqName, seq);
					values2name.put(values, seqName);
					LOG.info("replaced sequence " + storedName + " with sequence " + seqName);
					return true; //added
				}
			}
		}

		// keep current representative
		Set<OEISSequence> duplicateDuplicates = seq.removeDuplicates();
		storedSequence.addDuplicates(duplicateDuplicates);
		storedSequence.addDuplicate(seq);
		if (verbose) LOG.info("ignored sequence " + seq.getName() + " which is an exact duplicate of sequence " + storedName);
		//LOG.debug("\t sequence " + seq.nameAndValuesString());
		//LOG.debug("\t sequence " + name2seq.get(storedName).nameAndValuesString());
		return false;
	}
	
	public void add(SequenceStore sequences) {
		if (sequences!=null) {
			for (OEISSequence seq : sequences) {
				this.add(seq);
			}
		}
	}
	
	public OEISSequence get(String seqName) {
		return name2seq.get(seqName);
	}
	
	/**
	 * Removes the sequence with the given name from this.
	 * @param seqName
	 * @return the sequence that has been removed, or null if it was not contained.
	 */
	public OEISSequence remove(String seqName) {
		OEISSequence seq = name2seq.get(seqName);
		if (seq!=null) {
			// there is a sequence to remove.
			// also multiple attempts to remove do not matter, we synchronize
			// because it could be confusing if several threads receive
			// the sequence as a return value although only one of the 
			// threads actually removed it...
			synchronized (syncObject) {
				// check again if sequence is still contained
				seq = name2seq.get(seqName);
				if (seq!=null) {
					values2name.remove(seq.getAbstractValues());
					name2seq.remove(seqName);
					return seq;
				}
			}
		}
		//LOG.debug("sequence " + seqName + " has not been found...");
		return seq;
	}
	
	public int size() {
		return name2seq.size();
	}

	/**
	 * @return An iterator over all sequences in this.
	 */
	public Iterator<OEISSequence> iterator() {
		return name2seq.values().iterator();
	}
}
