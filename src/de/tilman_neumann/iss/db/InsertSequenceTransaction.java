package de.tilman_neumann.iss.db;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.Db;
import de.tilman_neumann.db.StatementBaseImpl;
import de.tilman_neumann.db.TransactionBaseImpl;
import de.tilman_neumann.db.WherePart;
import de.tilman_neumann.db.sql.SqlColumn;
import de.tilman_neumann.db.sql.SqlValue;
import de.tilman_neumann.db.statement.InsertStatement;
import de.tilman_neumann.db.statement.ResultSetIterator;
import de.tilman_neumann.db.statement.SelectStatement;
import de.tilman_neumann.db.where.Equals;
import de.tilman_neumann.iss.sequence.OEISSequence;


/**
 * Transaction inserting a sequence.
 * @author Tilman Neumann
 * @since 2008-12-05
 */
public class InsertSequenceTransaction extends TransactionBaseImpl<Boolean> {

	private static final Logger LOG = Logger.getLogger(InsertSequenceTransaction.class);

	private static SqlColumn SEQ_ID = new SqlColumn("seq", "id");
	private static SqlColumn SEQ_NAME = new SqlColumn("seq", "name");
	private static SqlColumn VALUE_ID = new SqlColumn("value", "id");
	private static SqlColumn VALUE_VAL = new SqlColumn("value", "val");

	private OEISSequence entry = null;
	
	/**
	 * Full constructor
	 * @param db Database that wants to run this transaction.
	 * @param entry Sequence to insert
	 */
	public InsertSequenceTransaction(Db db, OEISSequence entry) {
		super(db);
		this.entry = entry;
	}
	
	/**
	 * @return true if the sequence has been inserted
	 */
	@Override
	protected Boolean runInternal() throws SQLException {
		boolean inserted = false;
		boolean sequenceExists = selectSequenceExists(entry);
		//LOG.debug("sequenceExists = " + sequenceExists);
		if (!sequenceExists) {
			LOG.info("sequence " + entry.getName() + " is not yet in the database");
			this.insertSeqName(entry);
			
			// Get id of just inserted sequence
            long seq_id = StatementBaseImpl.getLastInsertId(this.db, SEQ_ID);

			// now insert the values...
			int pos = 0;
			for (BigInteger value : entry.getValues()) {
				// insert value only if not existing yet
				Long valueId = selectValueId(value);
				if (valueId == null) {
					insertValue(value);
					valueId = Long.valueOf(StatementBaseImpl.getLastInsertId(this.db, VALUE_ID));
				}
				// Insert seq-value relations
				/*int modified =*/ insertSeqValue(value, seq_id, pos, valueId);
				pos++;
			}
			inserted = true;
		} else {
			LOG.info("sequence " + entry.getName() + " is already in the database");
		}
		return Boolean.valueOf(inserted);
	}
	
	// Low levels SQL functions =================================================
	
	/**
	 * @return true if a sequence is already in the database
	 */
	private boolean selectSequenceExists(OEISSequence entry) throws SQLException {
		// check if sequence already exists
		List<SqlColumn> columns = new LinkedList<SqlColumn>();
		columns.add(SEQ_NAME);
		//LOG.debug("columns = " + columns);
		WherePart where = new WherePart(new Equals(SEQ_NAME, new SqlValue(entry.getName())));
		//LOG.debug("where = " + where);
		SelectStatement select = new SelectStatement(db, "DISTINCT", columns, where);
		//LOG.debug("statement = " + select);
		
		ResultSetIterator resultItr = select.execute();
		boolean sequenceExists = resultItr.hasNext();
		//LOG.debug("JUST AFTER EXECUTION: sequenceExists = " + sequenceExists);
//		int rowCount = 0;
//		while (resultItr.hasNext()) {
//			Object[] row = resultItr.next();
//			String rowStr = ResultSetIterator.rowToString(rowCount, row);
//			LOG.debug(rowStr);
//			rowCount++;
//		}
		//LOG.debug("sequence " + entry.getName() + " in DB? " + sequenceExists);
		return sequenceExists;
	}
	
	private void insertSeqName(OEISSequence entry) throws SQLException {
		List<BigInteger> values = entry.getValues();
		// insert new sequence
		TreeMap<String, Object> columnsAndValues = new TreeMap<String, Object>();
		columnsAndValues.put("name", entry.getName());
		columnsAndValues.put("len", Integer.valueOf(values.size()));
		InsertStatement insertSeq = new InsertStatement(db, "seq", columnsAndValues);
		insertSeq.execute();
	}
	
	private Long selectValueId(BigInteger value) throws SQLException {
		// check if sequence already exists
		List<SqlColumn> columns = new LinkedList<SqlColumn>();
		columns.add(VALUE_ID);
		//LOG.debug("columns = " + columns);
		WherePart where = new WherePart(new Equals(VALUE_VAL, new SqlValue(value.abs().toString())));
		//LOG.debug("where = " + where);
		SelectStatement select = new SelectStatement(db, "DISTINCT", columns, where);
		//LOG.debug("statement = " + select);
		
		ResultSetIterator resultItr = select.execute();
		boolean hasNext = resultItr.hasNext();
		if (!hasNext) {
			// value not found
			LOG.debug("ResultSet contains no rows");
			return null;
		}
		Object[] row = resultItr.next();
		
		if (row.length==0 || row[0]==null) {
			LOG.debug("row doesn't contain the desired entry");
			return null;
		}
		
		Long valueId = (Long) row[0];
		LOG.debug("valueId = " + valueId);
		return valueId;
	}

	/**
	 * insert abs(value) into the database.
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	private int insertValue(BigInteger value) throws SQLException {
		TreeMap<String, Object> columnsAndValues = new TreeMap<String, Object>();
		columnsAndValues.put("val", value.abs().toString());
		InsertStatement insertValue = new InsertStatement(db, "value", columnsAndValues);
		int modified = insertValue.execute();
		return modified;
	}
	
	private int insertSeqValue(BigInteger value, long seq_id, int seq_pos, Long valueId) throws SQLException {
		TreeMap<String, Object> columnsAndValues = new TreeMap<String, Object>();
		columnsAndValues.put("seq_id", Long.valueOf(seq_id));
		columnsAndValues.put("seq_pos", Integer.valueOf(seq_pos));
		columnsAndValues.put("val_id", valueId);
		columnsAndValues.put("sign", Integer.valueOf(value.signum()));

		InsertStatement insertValue = new InsertStatement(db, "value", columnsAndValues);
		int modified = insertValue.execute();
		return modified;
	}
}
