/*
 * integer-sequence-search (ISS) is an offline OEIS sequence search engine.
 * Copyright (C) 2018 Tilman Neumann (www.tilman-neumann.de)
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

// TODO: Get more metadata
// TODO: ColumnMeta should be DBMS independent; implement necessary conversions
public class ColumnMeta {
	private String sqlTypeName = null;
	private Integer sqlType = null;
	private String javaType = null;
	
	public ColumnMeta(String sqlTypeName, int sqlType, String javaType) {
		this.sqlTypeName = sqlTypeName;
		this.sqlType = Integer.valueOf(sqlType);
		this.javaType = javaType;
	}
}
