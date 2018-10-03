package de.tilman_neumann.db.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * An SQL values list.
 * @author Tilman Neumann
 * @since 2008-12-03
 */
abstract public class SqlValueList extends ArrayList<SqlValue> implements SqlIdentifier {

	private static final long serialVersionUID = -1380954408462369438L;

	/**
	 * Constructor for an empty list.
	 */
	public SqlValueList() {
		super();
	}

	/**
	 * Copy constructor.
	 * @param values original list of values
	 */
	public SqlValueList(List<SqlValue> values) {
		super(values);
	}
	
	/**
	 * @return null (value lists don't contain a table name)
	 */
	public String getTableName() {
		return null;
	}

	/**
	 * @return this value list as an SQL string.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('(');
		if (this.size()>0) {
			for (SqlValue value : this) {
				b.append(value.toString() + ",");
			}
			// remove last comma
			int len = b.length();
			b.delete(len-1, len); 
		}
		// terminate
		b.append(')');
		return b.toString();
	}
}
