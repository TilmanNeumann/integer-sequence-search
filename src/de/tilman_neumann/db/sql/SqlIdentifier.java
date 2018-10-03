package de.tilman_neumann.db.sql;

import de.tilman_neumann.db.Dbms;

/**
 * Base interface for SQL identifiers
 * @author Tilman Neumann
 * @since 2008-12-03
 */
public interface SqlIdentifier {
	/**
	 * @return the table name included by this SQLIdentifier, if there is one.
	 * Remember to quote this according to the DBMS conventions before putting
	 * the result into an SQL string.
	 */
	// TODO: Check references + quoting
	public String getTableName();
	
	/**
	 * Must be overridden by implementing classes such that (fully qualified)
	 * SQL descriptions are returned.
	 * @param dbms The DBMS for which this SQL identifier shall be formatted
	 * @return fully qualified SQL description
	 */
	public String toString(Dbms dbms);
	
	/**
	 * @param dbms The DBMS for which this SQL identifier shall be formatted
	 * @return this as an SQL string with place holders and the values to insert.
	 */
	public SqlStringWithParams toSqlStringWithParams(Dbms dbms);
}
