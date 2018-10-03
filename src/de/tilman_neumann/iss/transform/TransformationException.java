package de.tilman_neumann.iss.transform;

/**
 * Exception thrown when a transform transform does not exist or when it
 * shall be discarded (e.g. if it is the id-transform, and that exists already).
 * @author Tilman Neumann
 * @since 2011-09-19
 */
public class TransformationException extends Exception {

	private static final long serialVersionUID = 1508281021255732072L;

	public TransformationException(String msg) {
		super(msg);
	}
}
