package de.tilman_neumann.db.where;

import java.util.SortedSet;

import de.tilman_neumann.db.Dbms;
import de.tilman_neumann.db.sql.SqlStringWithParams;

/**
 * An arbitrary complex constraint that may be used as part of
 * an SQL WHERE part.
 * @author Tilman Neumann
 * @since 2008-12-03
 */
public abstract class Constraint {
	
	/**
	 * @param dbms the DBMS for which this shall be prepared as a string
	 * @return this constraint as an SQL snippet
	 */
	abstract public SqlStringWithParams toSqlStringWithParams(Dbms dbms);
	
	/**
	 * @return the table names in this constraint
	 */
	abstract public SortedSet<String> getTableNames();

//	@Override
//	abstract public String toString();
}
