/*
 * integer-sequence-search (ISS) is an offline OEIS sequence search engine.
 * Copyright (C) 2018 Tilman Neumann - tilman.neumann@web.de
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, see <http://www.gnu.org/licenses/>.
 */
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
