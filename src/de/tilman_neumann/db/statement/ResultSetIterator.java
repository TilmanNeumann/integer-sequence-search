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
package de.tilman_neumann.db.statement;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.meta.ColumnMeta;

/**
 * Iterator simplifying access to the results of select statements.
 * By the way it avoids passing through and the same result set more than once.
 * @author Tilman Neumann
 */
public class ResultSetIterator implements Iterator<Object[]> {
	private static final Logger LOG = Logger.getLogger(ResultSetIterator.class);

	private static Map<Integer, String> CODES2DESCRIPTIONS;
	
	private ResultSet rs = null;
	private int columnCount = 0;
	private ColumnMeta[] columnMetas = null;
    
	private boolean hasNext = false;
	
	static {
//		LOG.debug("ResultSet.CLOSE_CURSORS_AT_COMMIT = " + ResultSet.CLOSE_CURSORS_AT_COMMIT);
//		LOG.debug("ResultSet.CONCUR_READ_ONLY = " + ResultSet.CONCUR_READ_ONLY);
//		LOG.debug("ResultSet.CONCUR_UPDATABLE = " + ResultSet.CONCUR_UPDATABLE);
//		LOG.debug("ResultSet.FETCH_FORWARD = " + ResultSet.FETCH_FORWARD);
//		LOG.debug("ResultSet.FETCH_REVERSE = " + ResultSet.FETCH_REVERSE);
//		LOG.debug("ResultSet.FETCH_UNKNOWN = " + ResultSet.FETCH_UNKNOWN);
//		LOG.debug("ResultSet.HOLD_CURSORS_OVER_COMMIT = " + ResultSet.HOLD_CURSORS_OVER_COMMIT);
//		LOG.debug("ResultSet.TYPE_FORWARD_ONLY = " + ResultSet.TYPE_FORWARD_ONLY);
//		LOG.debug("ResultSet.TYPE_SCROLL_INSENSITIVE = " + ResultSet.TYPE_SCROLL_INSENSITIVE);
//		LOG.debug("ResultSet.TYPE_SCROLL_SENSITIVE = " + ResultSet.TYPE_SCROLL_SENSITIVE);
		CODES2DESCRIPTIONS = new HashMap<Integer, String>();
		CODES2DESCRIPTIONS.put(Integer.valueOf(ResultSet.CLOSE_CURSORS_AT_COMMIT), "ResultSet.CLOSE_CURSORS_AT_COMMIT");
		CODES2DESCRIPTIONS.put(Integer.valueOf(ResultSet.CONCUR_READ_ONLY), "ResultSet.CONCUR_READ_ONLY");
		CODES2DESCRIPTIONS.put(Integer.valueOf(ResultSet.CONCUR_UPDATABLE), "ResultSet.CONCUR_UPDATABLE");
		CODES2DESCRIPTIONS.put(Integer.valueOf(ResultSet.FETCH_FORWARD), "ResultSet.FETCH_FORWARD");
		CODES2DESCRIPTIONS.put(Integer.valueOf(ResultSet.FETCH_REVERSE), "ResultSet.FETCH_REVERSE");
		CODES2DESCRIPTIONS.put(Integer.valueOf(ResultSet.FETCH_UNKNOWN), "ResultSet.FETCH_UNKNOWN");
		CODES2DESCRIPTIONS.put(Integer.valueOf(ResultSet.HOLD_CURSORS_OVER_COMMIT), "ResultSet.HOLD_CURSORS_OVER_COMMIT");
		CODES2DESCRIPTIONS.put(Integer.valueOf(ResultSet.TYPE_FORWARD_ONLY), "ResultSet.TYPE_FORWARD_ONLY");
		CODES2DESCRIPTIONS.put(Integer.valueOf(ResultSet.TYPE_SCROLL_INSENSITIVE), "ResultSet.TYPE_SCROLL_INSENSITIVE");
		CODES2DESCRIPTIONS.put(Integer.valueOf(ResultSet.TYPE_SCROLL_SENSITIVE), "ResultSet.TYPE_SCROLL_SENSITIVE");
	}
	
	/**
	 * Full constructor.
	 * @param rs ResultSet
	 * @throws SQLException 
	 */
	public ResultSetIterator(ResultSet rs) throws SQLException {
		this.rs = rs;
		if (rs == null) {
			LOG.info("ResultSet is null!");
			return;
		}
		
//		try {
//			rs.last(); //move to the last row // last() is only supported by scrollable result sets
//			int numRows = rs.getRow(); // equal to row count
//			LOG.debug("record count = " + numRows);
//			rs.beforeFirst();	
//		} catch (SQLException se) {
//			LOG.error("record count failed" , se);
//		}
	
		ResultSetMetaData rsMeta = rs.getMetaData();
		if (rsMeta == null) {
			LOG.info("ResultSetMetaData is null!");
			return;
		}
		
		this.columnCount = rsMeta.getColumnCount();
		//LOG.debug("result set has " + columnCount + " columns");
		Map<String, String> rsProps = getResultSetProps(rs);
		rsProps.put("class", rs.getClass().getName());
		rsProps.put("numberOfColumns", String.valueOf(columnCount));
		//LOG.debug("result set properties: " + rsProps);
		
		if (columnCount > 0) {
			// Get meta data for all columns
			columnMetas = readColumnMetas(columnCount, rsMeta);
			// preload first column to be able to answer hasNext()
			try {
				//LOG.debug("row properties: " + getRowProps(rs));
				hasNext = rs.next(); // first() is only supported by scrollable result sets
			} catch (SQLException se) {
				LOG.warn("rs.first() threw " + se.getMessage(), se);
			}
			//LOG.debug("has rows = " + hasNext);
		}
	}
	
	private static Map<String, String> getResultSetProps(ResultSet rs) throws SQLException {
		Map<String, String> rsProps = new HashMap<String, String>();
		rsProps.put("type", CODES2DESCRIPTIONS.get(Integer.valueOf(rs.getType())));
		rsProps.put("concurrency", CODES2DESCRIPTIONS.get(Integer.valueOf(rs.getConcurrency())));
		rsProps.put("fetchDirection", CODES2DESCRIPTIONS.get(Integer.valueOf(rs.getFetchDirection())));
		rsProps.put("fetchSize", String.valueOf(rs.getFetchSize()));
		return rsProps;
	}
	
	private static ColumnMeta[] readColumnMetas(int cc, ResultSetMetaData rsMeta) throws SQLException {
		ColumnMeta[] columnMetas = null;
		if (cc > 0) {
			columnMetas = new ColumnMeta[cc];
			for (int i = 1; i <= cc; i++) {
            	String sqlTypeName = rsMeta.getColumnTypeName(i);
            	int sqlType = rsMeta.getColumnType(i); // from java.sql.Types
            	String javaType = rsMeta.getColumnClassName(i);
				columnMetas[i-1] = new ColumnMeta(sqlTypeName, sqlType, javaType);
			}
		}
		return columnMetas;
	}

	private static Map<String, String> getRowProps(ResultSet rs) throws SQLException {
		Map<String, String> rowProps = new HashMap<String, String>();
		rowProps.put("rowIndex", String.valueOf(rs.getRow()));
		rowProps.put("isBeforeFirst", String.valueOf(rs.isBeforeFirst()));
		rowProps.put("isFirst", String.valueOf(rs.isFirst()));
		rowProps.put("isLast", String.valueOf(rs.isLast()));
		rowProps.put("isAfterLast", String.valueOf(rs.isAfterLast()));
		return rowProps;
	}

	/**
	 * @return The number of columns of the result
	 */
	public int getNumberOfColumns() {
		return this.columnCount;
	}

	/**
	 * @return array with the ColumnMeta objects of all columns
	 */
	public ColumnMeta[] getColumnMetas() {
		return columnMetas;
	}
	
	/**
	 * @return true if the ResultSet has more data rows
	 */
	public boolean hasNext() {
		return hasNext; //(nextRow != null);
	}

	/**
	 * @return the next row in the result set
	 */
	public Object[] next() {
		Object[] row = readNextRow(columnCount, rs);
		try {
			hasNext = rs.next();
		} catch (SQLException se) {
			LOG.warn("rs.next() gave " + se.getMessage(), se);
		}
		return row;
	}

	private static Object[] readNextRow(int cc, ResultSet rs) {
		Object[] row = null;
		//LOG.debug("try to read next row of result set " + rs);

		if (rs != null) {
			try {
				//LOG.debug("row properties: " + getRowProps(rs));
				row = new Object[cc];
                for (int i = 1; i <= cc; i++) {
                	row[i-1] = rs.getObject(i);
                }
			} catch (SQLException se) {
				LOG.warn("reading row: " + se.getMessage(), se);
			}
		}
		return row;
	}

	public static String rowToString(int rowNumber, Object[] row) {
		//LOG.debug("row = " + row);
		String rowStr = "";
		int entriesCount = 0;
		if (row != null) {
			for (Object o : row) {
				rowStr += o.toString() + ", ";
				entriesCount++;
			}
		}
		if (rowStr.length()>0) {
			rowStr = rowStr.substring(0, rowStr.length()-2);
		}
		//LOG.debug("row " + rowNumber  + " = " + rowStr + " (" + entriesCount + " entries)");
		return rowStr;
	}
	
	/**
	 * @throws IllegalStateException always, operation is not supported
	 */
	public void remove() throws IllegalStateException {
		throw new IllegalStateException("remove() operation is not supported");
	}

	/**
	 * Close result set.
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		if (this.rs!=null) {
			rs.close();
			rs = null;
		}
	}
	
	/**
	 * Close result set on extinction of this.
	 */
	public void finalize() {
		try {
			this.close();
		} catch (SQLException se) {
			LOG.error("closing result set failed: " + se.getMessage(), se);
		}
	}
}
