package de.tilman_neumann.db.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
 
public class InformixMetaDataFactory extends MetaDataFactoryBaseImpl {

	@Override
	protected ResultSet getTableMetas(DatabaseMetaData dbMeta, String[] tableTypes) throws SQLException {
		return dbMeta.getTables("", "", "", tableTypes);
	}

	@Override
	protected boolean isSystemTable(String tabName, String tabType, String tabSchema) {
		return (super.isSystemTable(tabName, tabType, tabSchema) || (tabName.startsWith("sys") && tabName.length() > 3));
	}

}
