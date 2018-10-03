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
package de.tilman_neumann.db.where;

import de.tilman_neumann.db.sql.SqlIdentifier;

public class SimilarTo extends BinaryComparisonConstraint {

	// TODO: rhs must be regular expression / string pattern ?
	public SimilarTo(SqlIdentifier lhs, String op, SqlIdentifier rhs) {
		super(lhs, op, rhs); // TODO: Operator name depends on DBMS
	}
}
