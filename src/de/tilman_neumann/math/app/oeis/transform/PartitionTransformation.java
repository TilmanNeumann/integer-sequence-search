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
import de.tilman_neumann.math.partitions.IntegerPartition;
import de.tilman_neumann.math.partitions.IntegerPartitionGenerator;

/**
 * Implementation of forward partition transforms.
 * 
 * This is a sample application of the partitions generator, but
 * interesting of its own account.
 * 
 * The main method creates a triangle by a kind of "partition transform" from sequence.
 * The entry T[n,k] is the sum over all partitions with k parts of integer n,
 * evaluated at x[part] instead of part. (This "joining" of partitions that have the same
 * number of parts is exactly what is done when Stirling numbers of the
 * second kind are computed from partition counts.)<br>
 * 
 * Example: n=6 has partitions<br>
 * [6]<br>
 * [5, 1]<br>
 * [4, 2]<br>
 * [4, 1, 1]<br>
 * [3, 3]<br>
 * [3, 2, 1]<br>
 * [3, 1, 1, 1]<br>
 * [2, 2, 2]<br>
 * [2, 2, 1, 1]<br>
 * [2, 1, 1, 1, 1]<br>
 * [1, 1, 1, 1, 1, 1]<br>
 * 
 * There are three partitions of 6 with exactly 3 parts: [4, 1, 1], [3, 2, 1] and [2, 2, 2].<br>
 * Therefore T[6, 3] = x[4]*x[1]^2 + x[3]*x[2]*x[1] + x[2]^3<br>
 * 
 * If <code>weighted</code> is <code>true</code>, then the additive terms are multiplied with the
 * number of ways in which these partitions can be realized.
 * For n = 6, these numbers are (in the same order as the partitions above)
 * 1, 6, 15, 15, 10, 60, 20, 15, 45, 15, 1.
 * Thus, with these coefficients, we get
 * T[6,3] = 15*x[4]*x[1]^2 + 60*x[3]*x[2]*x[1] + 15*x[2]^3<br>
 * 
 * Both the triangles and their row sums can reproduce a great number of
 * important OEIS entries.
 * 
 * Note that it would also make sense to apply the partitions transform to
 * arbitrary numbers, not just integers.
 * 
 * @author Tilman Neumann
 */
public class PartitionTransformation extends Transformation {
	
	private static final Logger LOG = Logger.getLogger(PartitionTransformation.class);

	private boolean weighted;
	private boolean central;
	private String name;

	public PartitionTransformation(boolean weighted, boolean central) {
		this.weighted = weighted;
		this.central = central;
		String weightStr = weighted ? "weighted" : "unweighted";
		String momentStr = central ? "Central" : "Raw";
		this.name = weightStr + momentStr + "Part";
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
		//LOG.debug("expand triangle " + triangle.getUid() + " from " + oldNumberOfInputValues + " to " + numberOfInputValues + " rows", new Throwable());
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
	 * Compute partitions triangle.
	 * Kept for backward compatibility with old tests.
	 * 
	 * @param a sequence of integers at which the partitions are evaluated
	 * @param n the integer whose partitions are computed
	 * @param weighted If <code>true</code>, then values for each partition are weighted by the number of ways to realize the partition
	 * @param central If <code>true</code>, then partitions are skipped that contain a "1"-part.
	 * (tested with A000009 (see comment from Michael Somos)) <br>
	 * @return triangle T[n,k]
	 */
	// weighted raw transform:
	// correct: A000040 -> A007446 (Exponentiation of e.g.f. for primes)
	// wrong: A132841 -> A132841 (Exponentiation of ...)
	// correct: A000129 -> A006669 (exponentiation of g.f. for Pell numbers)
	// correct: A000045 -> A006701 (exponentiation of g.f. for Fibonacci numbers)
	// correct: A000055 -> A006790 (exponentiation of e.g.f. for trees)
	// correct: A000081 -> A006871 (Exponentiation of g.f. for rooted trees)
	// wrong: A000045 -> A007552 (Exponentiation of Fibonacci numbers)
	// wrong: A000292 -> A145453 (Exponential transform of tetrahedral numbers)
	// -> seems to be equal to exponentiation of generating functions ?
	//
	// unweighted raw:
	// is not the Euler transform !
	BigIntTriangle computeTriangle(List<BigInteger> a) {
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
	 * @return n.th row of the triangle
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
			BigInteger entry = nthRow.get(partIndex);
			//LOG.debug("entry before: " + entry);
			nthRow.set(partIndex, entry.add(summand));
			//LOG.debug("entry afterwards: " + entry + "+" + summand + " = " + nthRow.get(partIndex));
			//LOG.debug("nthRow after adding partition " + p +" :\n" + nthRow);
		}
		return nthRow;
	}
}
