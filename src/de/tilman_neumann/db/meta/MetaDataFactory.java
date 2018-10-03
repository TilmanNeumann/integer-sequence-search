package de.tilman_neumann.db.meta;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;


public interface MetaDataFactory {

	/**
	 * @return true if the database we connected to supports transaction
	 * @throws SQLException 
	 */
	public boolean supportsTransactions(DatabaseMetaData nativeMeta) throws SQLException;
	
	public DbMeta getDatabaseMetaData(DatabaseMetaData nativeMeta) throws SQLException;
	
	public TableMeta getTableMetaData(String tableName) throws SQLException;
	
	public ColumnMeta getColumnMetaData(String tableName, String columnName) throws SQLException;

}
