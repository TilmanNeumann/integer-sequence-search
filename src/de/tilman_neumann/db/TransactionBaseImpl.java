package de.tilman_neumann.db;

import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * Base class for procedures that should run as an atomic database access and that do something
 * with the data and possibly return some result (or error).
 * 
 * @author Tilman Neumann
 * @since 2008-12-08
 * @param <T> result type
 */
abstract public class TransactionBaseImpl<T> {
	
	private static final Logger LOG = Logger.getLogger(TransactionBaseImpl.class);

	protected Db db = null;
	private boolean supportsTransactions = false;

	/**
	 * Full constructor
	 * @param db The database that wants to run this transaction.
	 */
	public TransactionBaseImpl(Db db) {
		this.db = db;
		supportsTransactions = db.supportsTransactions();
	}
	/**
	 * Main method of the transaction manager.
	 * Cleans up the remains of an eventual last transaction, executes the new one,
	 * treats errors on returns the result.
	 * @param t transaction to execute
	 * @return result of the transaction
	 */
	public T execute() throws TransactionError {
		// start real transaction if supported
		try {
			db.connection.setAutoCommit(this.supportsTransactions);
		} catch (SQLException se) {
			throw new TransactionError("transaction initialization failed", se);
		}
		LOG.info("transaction initialized...");
		// run transaction
		T result = null;
		try {
			result = this.runInternal();
			// no error yet, try commit
			if (supportsTransactions) {
				db.connection.commit();
			}
		} catch (Exception e) {
			if (supportsTransactions) {
				try {
					db.connection.rollback();
				} catch (SQLException se) {
					throw new TransactionError("transaction & rollback failed", se);
				}
			}
			throw new TransactionError("transaction failed, rollback successful", e);
		}
		LOG.info("transaction successful, result = " + result);
		// last statement is kept open for reuse
		// last result set is kept open because the result might be a data row iterator
		return result;
	}

	/**
	 * Executes this transaction and produces some result. The result type should be overridden
	 * by subclasses to express explicitly what they produced!
	 * @return T an object of the result type this transaction is designed for
	 * @throws SQLException 
	 */
	protected abstract T runInternal() throws SQLException;
}
