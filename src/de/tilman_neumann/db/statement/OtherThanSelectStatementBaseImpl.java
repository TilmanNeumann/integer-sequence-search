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
package de.tilman_neumann.db.statement;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.Db;
import de.tilman_neumann.db.StatementBaseImpl;
import de.tilman_neumann.util.TimeUtil;

/**
 * Base class for SQL statements that do only return a number of modified rows.
 * @author Tilman Neumann
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
