package de.tilman_neumann.db.meta;

import java.util.SortedSet;

public class DbMeta {

	private SortedSet<String> tableNames = null;
	
	public DbMeta(SortedSet<String> tableNames) {
		this.tableNames = tableNames;
	}

	public SortedSet<String> getTableNames() {
		return this.tableNames;
	}
}
