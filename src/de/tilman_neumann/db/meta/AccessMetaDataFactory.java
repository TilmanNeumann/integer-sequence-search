package de.tilman_neumann.db.meta;

import java.sql.DatabaseMetaData;

public class AccessMetaDataFactory extends MetaDataFactoryBaseImpl {

	/**
	 * @return false always; Access pretends to support then but doesn't properly
	 */
	public boolean supportsTransactions(DatabaseMetaData nativeMeta) {
		return false;
	}

}
