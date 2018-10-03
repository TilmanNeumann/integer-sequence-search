/*
 * integer-sequence-search (ISS) is an offline OEIS sequence search engine.
 * Copyright (C) 2018 Tilman Neumann (www.tilman-neumann.de)
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
package de.tilman_neumann.db;

import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * Base class for procedures that should run as an atomic database access and that do something
 * with the data and possibly return some result (or error).
 * 
 * @author Tilman Neumann
 * 
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
