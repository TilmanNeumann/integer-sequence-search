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
