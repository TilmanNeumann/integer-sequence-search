package de.tilman_neumann.math.app.oeis.transform;

import java.util.List;

import org.apache.log4j.Logger;
import java.math.BigInteger;

import de.tilman_neumann.math.base.bigint.Gcd;
import de.tilman_neumann.math.base.bigint.combinatorics.Binomial;
import de.tilman_neumann.math.base.bigint.combinatorics.Stirling;

/**
 * Implementation of convolution type transforms.
 * 
 * @author Tilman Neumann
 * @since 2008-11-30
 */
public class ConvolutionTransformation extends Transformation_SimpleImpl {
	private static final Logger LOG = Logger.getLogger(ConvolutionTransformation.class);

	private ConvolutionTransformationType type;
	
	public ConvolutionTransformation(ConvolutionTransformationType type) {
		this.type = type;
	}

	public String getName() {
		return this.type.getName();
	}
	
	public int getComplexityScore() {
		return super.getComplexityScore() + type.getComplexityScore();
	}

	BigInteger computeNthValue(int n, List<BigInteger> a, List<BigInteger> b) {
		BigInteger b_n = BigInteger.ZERO;
		for (int k=0; k<=n; k++) {
			BigInteger elem = null;
			switch (type) {
			case STANDARD: {
				elem = a.get(k).multiply(a.get(n-k));
				break;
			}
			case EXP: {
				BigInteger coeff = Binomial.nk(n, k);
				//LOG.debug("binomial(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case LCM: {
				elem = Gcd.lcm(a.get(k), a.get(n-k));
				break;
			}
			case GCD: {
				elem = a.get(k).gcd(a.get(n-k));
				break;
			}
			case STIRLING1: {
				BigInteger coeff = Stirling.stirling1(n, k);
				//LOG.debug("stirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case ABS_STIRLING1: {
				BigInteger coeff = Stirling.absStirling1(n, k);
				//LOG.debug("absStirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case STIRLING2: {
				BigInteger coeff = Stirling.stirling2(n, k);
				//LOG.debug("stirling2(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case DOUBLE_STIRLING1: {
				BigInteger coeff = Stirling.stirling1(n, k).multiply(Stirling.stirling1(n, n-k));
				//LOG.debug("double stirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case DOUBLE_ABS_STIRLING1: {
				BigInteger coeff = Stirling.stirling1(n, k).multiply(Stirling.stirling1(n, n-k)).abs();
				//LOG.debug("double absStirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case DOUBLE_STIRLING2: {
				BigInteger coeff = Stirling.stirling2(n, k).multiply(Stirling.stirling2(n, n-k));
				//LOG.debug("double stirling2(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case STIRLING1_STIRLING2: {
				BigInteger coeff = Stirling.stirling1(n, k).multiply(Stirling.stirling2(n, n-k));
				//LOG.debug("double stirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case ABS_STIRLING1_STIRLING2: {
				BigInteger coeff = Stirling.absStirling1(n, k).multiply(Stirling.stirling2(n, n-k));
				//LOG.debug("double stirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case STIRLING2_STIRLING1: {
				BigInteger coeff = Stirling.stirling2(n, k).multiply(Stirling.stirling1(n, n-k));
				//LOG.debug("double stirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			case STIRLING2_ABS_STIRLING1: {
				BigInteger coeff = Stirling.stirling2(n, k).multiply(Stirling.absStirling1(n, n-k));
				//LOG.debug("double stirling1(" + n + ", " + k + ") = " + coeff);
				elem = a.get(k).multiply(a.get(n-k)).multiply(coeff);
				break;
			}
			}
			b_n = b_n.add(elem);
		}
		return b_n;
	}
}
