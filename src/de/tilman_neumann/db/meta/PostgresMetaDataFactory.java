package de.tilman_neumann.db.meta;

public class PostgresMetaDataFactory extends MetaDataFactoryBaseImpl {

	@Override
	protected boolean isSystemTable(String tabName, String tabType, String tabSchema) {
		return (super.isSystemTable(tabName, tabType, tabSchema) || tabName.startsWith("sql_"));
	}
}
