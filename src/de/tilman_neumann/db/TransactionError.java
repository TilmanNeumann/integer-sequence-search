package de.tilman_neumann.db;

public class TransactionError extends RuntimeException {

	private static final long serialVersionUID = -4880481582760546230L;

	public TransactionError(String message, Throwable cause) {
		super(message, cause);
	}
}
