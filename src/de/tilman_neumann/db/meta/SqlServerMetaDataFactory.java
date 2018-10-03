package de.tilman_neumann.db.meta;

public class SqlServerMetaDataFactory extends MetaDataFactoryBaseImpl {

	@Override
	protected boolean isSystemTable(String tabName, String tabType, String tabSchema) {
		return (super.isSystemTable(tabName, tabType, tabSchema) || tabSchema.equals("sys"));
	}
}
