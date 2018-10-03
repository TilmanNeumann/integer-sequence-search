package de.tilman_neumann.math.app.oeis.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import java.math.BigInteger;

import de.tilman_neumann.math.app.oeis.sequence.OEISSequence;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues_BigIntListImpl;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues_UnsignedIndexListImpl;
import de.tilman_neumann.math.base.bigint.BigIntConstants;
import de.tilman_neumann.math.base.bigint.BigIntTriangle;
import de.tilman_neumann.math.base.bigint.combinatorics.Factorial;
import de.tilman_neumann.math.partitions.IntegerPartition;
import de.tilman_neumann.math.partitions.IntegerPartitionGenerator;

/**
 * Implementation of inverse partition transforms.
 * 
 * This is a sample application of the partitions generator, but
 * interesting of its own account.
 * 
 * Method partitionsTransform(..) creates number triangles from integer sequences.
 * Many of these triangles and the sequences of their row sums play important
 * roles in OEIS.
 * 
 * Note that it would also make sense to apply the partitions transform to
 * arbitrary numbers, not just integers.
 * 
 * @author Tilman Neumann
 */
public class PartitionTransformation_Inverse extends Transformation {
	
	private static final Logger LOG = Logger.getLogger(PartitionTransformation_Inverse.class);

	private boolean weighted;
	private boolean central;
	private String name;
	
	public PartitionTransformation_Inverse(boolean weighted, boolean central) {
		this.weighted = weighted;
		this.central = central;
		String weightStr = weighted ? "Weighted" : "Unweighted";
		String momentStr = central ? "Central" : "Raw";
		this.name = "inv" + weightStr + momentStr + "Part";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getNumberOfAdditionalOutputValues() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Transform compute(OEISSequence inputSeq, int maxNumberOfInputValues, boolean forLookup) {
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);
		
		BigIntTriangle triangle = new BigIntTriangle();
		for (int n=0; n<numberOfInputValues; n++) {
			// compute transform for n.th row
			triangle.addRow(computeNthRow(n+1, a));
			//LOG.debug("Triangle after " + n + " rows:\n" + triangle);
		}
		List<BigInteger> b = triangle.getRowSums();
		
		String name = this.getName(inputSeq.getName());
		SequenceValues outputValues = forLookup ? new SequenceValues_UnsignedIndexListImpl(b) : new SequenceValues_BigIntListImpl(b);
		return new TriangleTransform(name, outputValues, triangle, this, inputSeq, numberOfInputValues);
	}

	Transform expand(Transform oldTransform, int maxNumberOfInputValues) {
		OEISSequence inputSeq = oldTransform.getInputSequence();
		int oldNumberOfInputValues = oldTransform.getNumberOfConsideredInputValues();
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);
		
		List<BigInteger> b = new ArrayList<BigInteger>(numberOfInputValues);
		// copy old values
		b.addAll(oldTransform.getValues());
		// compute new values
		TriangleTransform oldTriangleTransform;
		try {
			oldTriangleTransform = (TriangleTransform) oldTransform;
		} catch (ClassCastException cce) {
			LOG.error("oldTransform " + oldTransform.getName() + " of " + oldTransform.getClass() + " is not a TriangleTransform!?");
			throw cce;
		}
		BigIntTriangle triangle = oldTriangleTransform.getTriangle();
		for (int n=oldNumberOfInputValues; n<numberOfInputValues; n++) {
			 // triangle indices start with 1
			triangle.addRow(computeNthRow(n+1, a));
			b.add(triangle.getRowSum(n+1));
		}
		
		SequenceValues oldTransformValues = oldTransform.getAbstractValues();
		SequenceValues outputValues =
			oldTransformValues instanceof SequenceValues_UnsignedIndexListImpl ?
				new SequenceValues_UnsignedIndexListImpl(b) :
			    new SequenceValues_BigIntListImpl(b);
		return new TriangleTransform(oldTransform.getName(), outputValues, triangle, this, inputSeq, numberOfInputValues);
	}

	/**
	 * Compute inverse partitions triangle.
	 * Kept for backward compatibility with old tests.
	 * 
	 * @param a
	 * @param n
	 * @param weighted If <code>true</code>, then values for each partition are weighted by the number of ways to realize the partition
	 * @param central If <code>true</code>, then partitions are skipped that contain a "1"-part.
	 * @return
	 */
	// weighted raw: 
	// A000040 -> A007447 (Logarithm of e.g.f. for primes)
	// -> correct inverse
	//
	// unweighted raw: is not the inverse Euler transform !
	public BigIntTriangle computeTriangle(List<BigInteger> a) {
		if (a==null) return null;
		int numberOfInputValues = a.size();
		BigIntTriangle triangle = new BigIntTriangle();
		for (int n=0; n<numberOfInputValues; n++) {
			// compute transform for n.th row
			triangle.addRow(computeNthRow(n+1, a));
			//LOG.debug("Triangle after " + n + " rows:\n" + triangle);
		}
		return triangle;
	}
	
	/**
	 * Compute n.th row of the triangle, with the first row having index 1.
	 * @param n
	 * @param a
	 * @param t triangle
	 */
	private ArrayList<BigInteger> computeNthRow(int n, List<BigInteger> a) {
		// create zero-initialized row
		ArrayList<BigInteger> nthRow = new ArrayList<BigInteger>(n);
		for (int j=0; j<n; j++) {
			nthRow.add(BigIntConstants.ZERO);
		}
		
		IntegerPartitionGenerator pg = new IntegerPartitionGenerator(n);
		while (pg.hasNext()) {
			IntegerPartition p = new IntegerPartition(pg.next());
			if (central) {
				Set<Integer> parts = p.keySet();
				if (parts.contains(Integer.valueOf(1))) {
					// partition contains primary part, thus it is not added to T[n,k]
					continue; // next partition
				}
			}
			BigInteger summand = weighted ? p.getNumberOfRealizations() : BigInteger.ONE;
			//LOG.debug("partition of " + n + ": " + p + ", realizations=" + summand);
			for (Map.Entry<Integer, Integer> partAndPower : p.entrySet()) {
				int part = partAndPower.getKey().intValue();
				int power = partAndPower.getValue().intValue();
				BigInteger xval = a.get(part-1);
				BigInteger elem = xval.pow(power);
				//LOG.debug("a["+part+"]^"+power + " = " + elem);
				summand = summand.multiply(elem);
			}

			int partIndex = p.totalCount()-1;
			if (partIndex%2 != 0) {
				// sign is (-1)^(k-1)
				summand = summand.negate();
			}
			summand = summand.multiply(Factorial.withMemory(partIndex));
			
			BigInteger entry = nthRow.get(partIndex);
			//LOG.debug("entry before: " + entry);
			nthRow.set(partIndex, entry.add(summand));
			//LOG.debug("entry afterwards: " + entry + "+" + summand + " = " + nthRow.get(partIndex));
			//LOG.debug("nthRow after adding partition " + p +" :\n" + nthRow);
		}
		return nthRow;
	}
}
