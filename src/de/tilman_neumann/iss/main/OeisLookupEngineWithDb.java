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
package de.tilman_neumann.iss.main;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.iss.db.InsertSequenceTransaction;
import de.tilman_neumann.iss.db.OeisDb;
import de.tilman_neumann.iss.sequence.OEISSequence;

/**
 * Machine for complex searches in the OEIS database.
 * @author Tilman Neumann
 */
public class OeisLookupEngineWithDb extends OeisLookupEngine {

	private static final Logger LOG = Logger.getLogger(OeisLookupEngineWithDb.class);
	
	private OeisDb db = null;

	public OeisLookupEngineWithDb() throws SQLException, ClassNotFoundException {
    	// initialize database
		this.db = new OeisDb(Dbms.POSTGRES, "localhost", "5432", "oeis", "postgres", "postgres");
    	// TODO: Get connection data from configuration file
    	if (!db.isConnected()) {
    		throw new SQLException("Connection to database could not be established");
    	}
		LOG.info("Connection to database established.");
	}
	
	public void loadData(String oeisDataFileName) {
		try {
			// create database tables if they don't exist yet
			db.createTablesIfDatabaseEmpty();
			// load all sequences not yet in the database
			db.loadNewEntriesFromDatafile(oeisDataFileName);
		} catch (SQLException se) {
			throw new RuntimeException("data initialization failed", se);
		}
	}
	
	public boolean addSequence(OEISSequence seq) {
		try {
			return new InsertSequenceTransaction(db, seq).execute().booleanValue();
    	} catch (Exception e) {
    		LOG.error("error inserting sequence " + seq.getName() + ": " + e.getMessage(), e);
        	return false;
    	}
	}

	public OEISSequence removeSequence(String name) {
		// TODO
		return null;
	}
	
	/**
	 * looks up some transformations of the given sequence.
	 * @param lookupSeq sequence to lookup
	 * @param lookupMode how to lookup
	 */
	public void lookup(OEISSequence lookupSeq, OeisLookupMode lookupMode) {
		// TODO: Implement DB-based lookup
	}
}
