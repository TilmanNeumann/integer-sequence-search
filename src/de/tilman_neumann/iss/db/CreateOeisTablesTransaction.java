package de.tilman_neumann.iss.db;

import java.sql.SQLException;
import java.util.TreeMap;

import de.tilman_neumann.db.Db;
import de.tilman_neumann.db.TransactionBaseImpl;
import de.tilman_neumann.db.statement.CreateTableStatement;

/**
 * Transaction that creates all tables in the OEIS DB if none exists yet.
 * @author Tilman Neumann
 * @since 2008-12-07
 */
public class CreateOeisTablesTransaction extends TransactionBaseImpl<Boolean> {

	/**
	 * Full constructor.
	 * @param db The database that wants to run this transaction.
	 */
	public CreateOeisTablesTransaction(Db db) {
		super(db);
	}
	
	/**
	 * @return true if the tables have been created
	 */
	@Override
	protected Boolean runInternal() throws SQLException {
		this.createTableSeq();
		this.createTableValue();
		this.createTableSeqValue();
		//this.createTransformTable();
		//this.createSequenceTransformTable();
		// if all statements have been carried out without error, then all tables have been inserted
		return Boolean.valueOf(true);
	}
	
	/**
	 * Creates the table with basic sequence information (not the values themselves):
	 * CREATE TABLE seq {<br>
	 *    id	bigserial,<br>
	 *    name	varchar UNIQUE NOT NULL,<br>
	 *    len	integer NOT NULL<br>
	 * };
	 * Note that "sequence" is avoided because it is an SQL key word
	 * (though most databases might allowing quoting keywords at most places)
	 * @throws SQLException
	 */
	private int createTableSeq() throws SQLException {
		TreeMap<String, String> columnsAndTypes = new TreeMap<String, String>();
		columnsAndTypes.put("id", "bigserial");
		columnsAndTypes.put("name", "varchar UNIQUE NOT NULL");
		columnsAndTypes.put("len", "integer NOT NULL");
		CreateTableStatement stat = new CreateTableStatement(db, "seq", columnsAndTypes);
		return stat.execute();
	}
	
	/**
	 * Creates table of values unique in DB:
	 * CREATE TABLE value {<br>
	 *    id		bigserial,<br>
	 *    val		varchar UNIQUE NOT NULL<br>
	 * };
	 * @throws SQLException
	 */
	private int createTableValue() throws SQLException {
		TreeMap<String, String> columnsAndTypes = new TreeMap<String, String>();
		columnsAndTypes.put("id", "bigserial");
		columnsAndTypes.put("val", "varchar UNIQUE NOT NULL");
		CreateTableStatement stat = new CreateTableStatement(db, "value", columnsAndTypes);
		int ret = stat.execute();
		return ret;
	}
	
	/**
	 * Creates table of sequence values:
	 * CREATE TABLE seq_value {<br>
	 *    id		bigserial,<br>
	 *    seq_id	bigint NOT NULL,<br>
	 *    seq_pos	integer NOT NULL,<br>
	 *    val_id	bigint NOT NULL,<br>
	 *    sign		integer NOT NULL<br>
	 * };
	 * @throws SQLException
	 */
	private int createTableSeqValue() throws SQLException {
		TreeMap<String, String> columnsAndTypes = new TreeMap<String, String>();
		columnsAndTypes.put("id", "bigserial");
		columnsAndTypes.put("seq_id", "bigint NOT NULL"); // TODO: FOREIGN KEY?
		columnsAndTypes.put("seq_pos", "integer NOT NULL");
		columnsAndTypes.put("val_id", "bigint NOT NULL"); // TODO: FOREIGN KEY?
		columnsAndTypes.put("sign", "integer NOT NULL");
		CreateTableStatement stat = new CreateTableStatement(db, "seq_value", columnsAndTypes);
		int ret = stat.execute();
		return ret;
	}
	
	// TODO: Not sure yet if this is good...
	private int createTransformTable() throws SQLException {
		TreeMap<String, String> columnsAndTypes = new TreeMap<String, String>();
		columnsAndTypes.put("id", "serial");
		columnsAndTypes.put("name", "varchar");
		CreateTableStatement stat = new CreateTableStatement(db, "transform", columnsAndTypes);
		int ret = stat.execute();
		return ret;
	}

	/**
	 * Create table for a sequence of transforms that have been applied to
	 * an original OEIS entry in order to to get the new sequence with sequence_id.
	 * Each entry means that the "order"-th transform on the computation of
	 * sequence "sequence_id" was "k_transform_id".
	 * The 0.th transform for a "sequence_id" is the sequence from which the new sequence was derived.
	 * @throws SQLException
	 */
	// TODO: Not sure yet if this is good...
	private int createSequenceTransformTable() throws SQLException {
		TreeMap<String, String> columnsAndTypes = new TreeMap<String, String>();
		columnsAndTypes.put("id", "serial");
		columnsAndTypes.put("k_transform_id", "integer");
		columnsAndTypes.put("sequence_id", "integer");
		columnsAndTypes.put("order", "integer");
		CreateTableStatement stat = new CreateTableStatement(db, "sequence_transform", columnsAndTypes);
		int ret = stat.execute();
		return ret;
	}
}
