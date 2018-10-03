package de.tilman_neumann.db.statement;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.Db;
import de.tilman_neumann.db.StatementBaseImpl;
import de.tilman_neumann.util.TimeUtil;

/**
 * Base class for SQL statements that do only return a number of modified rows.
 * @author Tilman Neumann
 * @since 2008-12-10
 */
abstract public class OtherThanSelectStatementBaseImpl extends StatementBaseImpl {
	
	private static final Logger LOG = Logger.getLogger(OtherThanSelectStatementBaseImpl.class);

	/**
	 * Standard constructor.
	 * @param db The database that wants to execute this statement
	 */
	public OtherThanSelectStatementBaseImpl(Db db) {
		super(db);
	}
	
	/**
	 * Executes an CREATE TABLE, INSERT, UPDATE or DELETE statement.
	 * @return number of modified database entries.
	 */
	public int execute() throws SQLException {
		this.prepareExecution();
		LOG.debug("executeSQL(" + preparedStatement + ")");
		long start = System.currentTimeMillis();
		int modified = this.preparedStatement.executeUpdate();
		long end = System.currentTimeMillis();
		LOG.debug("executeSQL() required " + TimeUtil.timeDiffStr(start, end));
		return modified;
	}
}
