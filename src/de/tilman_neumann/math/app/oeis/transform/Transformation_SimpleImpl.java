package de.tilman_neumann.math.app.oeis.transform;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.tilman_neumann.math.app.oeis.sequence.OEISSequence;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues_BigIntListImpl;
import de.tilman_neumann.math.app.oeis.sequence.SequenceValues_UnsignedIndexListImpl;

/**
 * Default transformation implementation for transformations that
 * compute their values from the input sequence values alone.
 * 
 * @author Tilman Neumann
 * @since 2011-09-17
 */
public abstract class Transformation_SimpleImpl extends Transformation {

	abstract public String getName();

	@Override
	public int getNumberOfAdditionalOutputValues() {
		return 0;
	}

	@Override
	public Transform compute(OEISSequence inputSeq, int maxNumberOfInputValues, boolean forLookup) {
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);
		
		ArrayList<BigInteger> b = new ArrayList<BigInteger>(numberOfInputValues);
		for (int n=0; n<numberOfInputValues; n++) {
			b.add(computeNthValue(n, a, b));
		}
		
		String name = this.getName(inputSeq.getName());
		SequenceValues outputValues = forLookup ? new SequenceValues_UnsignedIndexListImpl(b) : new SequenceValues_BigIntListImpl(b);
		return new Transform(name, outputValues, this, inputSeq, numberOfInputValues);
	}

	@Override
	Transform expand(Transform oldTransform, int maxNumberOfInputValues) {
		OEISSequence inputSeq = oldTransform.getInputSequence();
		int oldNumberOfInputValues = oldTransform.getNumberOfConsideredInputValues();
		int numberOfInputValues = Math.min(inputSeq.size(), maxNumberOfInputValues);
		List<BigInteger> a = inputSeq.getValues().subList(0, numberOfInputValues);
		
		List<BigInteger> b = new ArrayList<BigInteger>(numberOfInputValues);
		// copy old values
		b.addAll(oldTransform.getValues());
		// compute new values
		for (int n=oldNumberOfInputValues; n<numberOfInputValues; n++) {
			b.add(computeNthValue(n, a, b));
		}
		
		SequenceValues oldTransformValues = oldTransform.getAbstractValues();
		SequenceValues outputValues =
			oldTransformValues instanceof SequenceValues_UnsignedIndexListImpl ?
				new SequenceValues_UnsignedIndexListImpl(b) :
			    new SequenceValues_BigIntListImpl(b);
		return new Transform(oldTransform.getName(), outputValues, this, inputSeq, numberOfInputValues);
	}

	abstract BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b);
}
