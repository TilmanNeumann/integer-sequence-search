package de.tilman_neumann.db.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Base class for database meta data of any kind (global, table, column, index, ...)
 * The philosophy behind this interface is a complete decoupling from the 
 * database connection. The MetaDataFactories get raw java.sql.* meta data
 * and prepare it according to their knowledge of the systems particuliarities.
 * However, they never get to see the Connection.
 * @author Tilman Neumann
 * @since 2008-12-03
 */
abstract public class MetaDataFactoryBaseImpl implements MetaDataFactory {

	/**
	 * @return true if the database we connected to supports transaction
	 * @throws SQLException 
	 */
	public boolean supportsTransactions(DatabaseMetaData nativeMeta) throws SQLException {
		return nativeMeta.supportsTransactions();
	}

	public DbMeta getDatabaseMetaData(DatabaseMetaData nativeMeta) throws SQLException {
		SortedSet<String> tableNames = this.readTableNamesFromDB(nativeMeta, false);
		return new DbMeta(tableNames);
	}

	public TableMeta getTableMetaData(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ColumnMeta getColumnMetaData(String tableName,
			String columnName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Default implementation to get native table meta data objects.
	 * @param dbMeta native database meta data
	 * @param tableTypes array with wanted table types ("TABLE" and/or "VIEW")
	 * @return ResultSet with meta information about all found tables.
	 * @throws SQLException
	 */
	protected ResultSet getTableMetas(DatabaseMetaData dbMeta, String[] tableTypes) throws SQLException {
		return dbMeta.getTables(null, null, null, tableTypes);
	}

	/**
	 * Default implementation to check if the given table name is a system table.
	 * @param tabName
	 * @param tabType
	 * @param tabSchema
	 * @return true if system table (or view)
	 */
	protected boolean isSystemTable(String tabName, String tabType, String tabSchema) {
		return (tabType.contains("system") || tabSchema.equals("information_schema"));
	}
	
	/**
	 * Fetches the names of the non-system tables in the database.
	 * @param dbMeta native database meta data
	 * @param includeViews if true then views are returned, too
	 * @return names of non-system tables (and views)
	 * @throws SQLException
	 */
    private SortedSet<String> readTableNamesFromDB(DatabaseMetaData dbMeta, boolean includeViews) throws SQLException {
		TreeSet<String> tableNames = new TreeSet<String>();
        if (dbMeta != null) {
        	// get table meta data.
        	// for Access, the following call already filters all system tables
        	// for Postgres, tables starting with "pg_" are filtered, but those with "sql_" not
            String[] tableArray = null;
            if (includeViews) {
                tableArray = new String[2];
                tableArray[0] = "TABLE";
                tableArray[1] = "VIEW";
            } else {
                tableArray = new String[1];
                tableArray[0] = "TABLE";
            }
            ResultSet tableMetas = getTableMetas(dbMeta, tableArray);
    		if (tableMetas != null) {
                while (tableMetas.next()) {
                	// Copy name or filter
                    String tabName = tableMetas.getString("TABLE_NAME");
                    tabName = (tabName!=null) ? tabName.toLowerCase() : "";
                    String tabSchema = tableMetas.getString("TABLE_SCHEM");
                    tabSchema = (tabSchema!=null) ? tabSchema.toLowerCase() : "";
                    String tabType = tableMetas.getString("TABLE_TYPE");
                    tabType = (tabType!=null) ? tabType.toLowerCase() : "";
                    //LOG.debug("Table " + tabName + ": tabSchema=" + tabSchema + ", tabType=" + tabType);

                    // DBMS-specific filters
                    if (this.isSystemTable(tabName, tabType, tabSchema)) {
                        continue;
                    }

                    // not a system table or view
                    tableNames.add(tabName);
                }
                // close ResultSet (important to avoid memory leaks in database server)
                tableMetas.close();
    		}
        }
		return tableNames;
    }
}
