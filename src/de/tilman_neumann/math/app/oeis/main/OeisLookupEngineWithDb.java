package de.tilman_neumann.math.app.oeis.main;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.math.app.oeis.db.InsertSequenceTransaction;
import de.tilman_neumann.math.app.oeis.db.OeisDb;
import de.tilman_neumann.math.app.oeis.sequence.OEISSequence;

/**
 * Machine for complex searches in the OEIS database.
 * @author Tilman Neumann
 * @since 2008-12-16
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
