package de.tilman_neumann.iss.db;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;

import de.tilman_neumann.db.Db;
import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.meta.DbMeta;
import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues_UnsignedIndexListImpl;
import de.tilman_neumann.jml.base.BigIntList;
import de.tilman_neumann.util.ConfigUtil;

public class OeisDb extends Db {
	private static final Logger LOG = Logger.getLogger(OeisDb.class);
	
	public OeisDb(Dbms dbms, String host, String port, String database, String user, String password) throws SQLException, ClassNotFoundException {
		super(dbms, host, port, database, user, password);
	}
	
	// Higher level functions =============================================================
	
	/**
	 * Creates tables of OEIS database.
	 * @throws SQLException
	 */
	// Tested 2008-12-03: Works !
	public void createTablesIfDatabaseEmpty() throws SQLException {
		DbMeta dbMeta = this.getDatabaseMetaData();
		if (dbMeta == null) {
			throw new NullPointerException("Meta data for database " + this.getDatabaseName());
		}
		int numberOfTables = dbMeta.getTableNames().size();
		if (numberOfTables == 0) {
			// Create whole database or nothing -> one transaction
			/*Boolean created =*/ new CreateOeisTablesTransaction(this).execute();
		}
	}
	
	/**
	 * Load all sequences not yet in the database;
	 * insert each sequence in a single transaction
	 * so that there will be no broken sequences in the database
	 */
	public void loadNewEntriesFromDatafile(String datafileName) throws SQLException {
    	// read OEIS data:
    	LineIterator lineItr = null;
        try {
        	lineItr = FileUtils.lineIterator(new File(datafileName));
        } catch (IOException e) {
            LOG.error(e, e);
            return;
        }
        if (lineItr==null) {
        	LOG.error("OEIS data file " + datafileName + " not found or contains no data");
        	return;
        }
    	while (lineItr.hasNext()) {
			//LOG.debug("there are more lines");
    		OEISSequence entry = null;
    		try {
				String line = lineItr.nextLine();
				//LOG.debug("current line = " + line);
				String name = line.substring(0, line.indexOf(','));
		        String valuesStr = line.substring(line.indexOf(',')+1);
		        //LOG.debug("valuesStr = " + valuesStr);
		        List<BigInteger> values = BigIntList.valueOf(valuesStr); // trims before parsing
		        //LOG.debug("values = " + values);
				entry = new OEISSequence(name, new SequenceValues_UnsignedIndexListImpl(values));
				//LOG.debug("current sequence = " + entry.nameAndValuesString());
	    	} catch (Exception e) {
	    		LOG.error("error reading sequence: " + e.getMessage(), e);
	    	}
	    	if (entry != null) {
	    		// reading sequence was successful
	    		try {
					/*Boolean inserted =*/ new InsertSequenceTransaction(this, entry).execute();
		    	} catch (Exception e) {
		    		LOG.error("error inserting sequence " + entry.getName() + ": " + e.getMessage(), e);
		    	}
	    	}
	    	
    	} // end(while)
	}
	
	// Test ====================================================================
	
	public static void main(String[] argc) throws SQLException, ClassNotFoundException {
    	ConfigUtil.initProject();
		OeisDb db = new OeisDb(Dbms.POSTGRES, "localhost", "5432", "oeis", "postgres", "postgres");
    	LOG.info("connected? " + db.isConnected());
    	LOG.info("suports transactions? " + db.supportsTransactions());
		db.createTablesIfDatabaseEmpty();
		// ...
	}
}