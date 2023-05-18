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
package de.tilman_neumann.db;

import de.tilman_neumann.db.meta.AccessMetaDataFactory;
import de.tilman_neumann.db.meta.InformixMetaDataFactory;
import de.tilman_neumann.db.meta.MetaDataFactory;
import de.tilman_neumann.db.meta.PostgresMetaDataFactory;
import de.tilman_neumann.db.meta.SqlServerMetaDataFactory;

/**
 * Supported DBMS and some DBMS-specific (but DB-independent) functions.
 * @author Tilman Neumann
 */
public enum Dbms {
    /** PostgreSQL */
	POSTGRES () {
		public String toString() {
			return "PostgreSQL";
		}
		String getJdbcDriver() {
			return "org.postgresql.Driver";
		}
		String getProtocol() {
			return "jdbc:postgresql:";
		}
		String getInstance(String host, String port, String database, String instance) {
			String ret = "//" + host;
            if (port.length() > 0) {
            	ret = ret + ":" + port;
            }
            ret = ret + "/" + database + ":";
			return ret;
		}
		public String getSqlIdentifierQuote() {
			return "\"";
		}
		public MetaDataFactory getMetaDataFactory() {
			return new PostgresMetaDataFactory();
		}
	},
	
    /** Informix */
	INFORMIX () {
		public String toString() {
			return "Informix";
		}
		String getJdbcDriver() {
			return "com.informix.jdbc.IfxDriver";
		}
		String getProtocol() {
			return "jdbc:informix-sqli:";
		}
		String getInstance(String host, String port, String database, String instance) {
			String ret = "//" + host;
            if (port.length() > 0) {
            	ret += ":" + port;
            }
            ret += "/" + database + ":";
            if (instance!=null && instance.trim().length()>0) {
            	ret += "informixserver=" + instance + ";";
            }
			return ret;
		}
		public String getSqlIdentifierQuote() {
			return ""; // not supported
		}
		public MetaDataFactory getMetaDataFactory() {
			return new InformixMetaDataFactory();
		}
	},
	
    /** Access */
	ACCESS () {
		public String toString() {
			return "Access";
		}
		String getJdbcDriver() {
	        if (System.getProperty("java.vendor").startsWith("Microsoft")) {
	            return "com.ms.jdbc.odbc.JdbcOdbcDriver";
	        }
			return "sun.jdbc.odbc.JdbcOdbcDriver";
		}
		String getProtocol() {
			return "jdbc:odbc:";
		}
		String getInstance(String host, String port, String database, String instance) {
			return this.getProtocol() + database + ";";
		}
		public String getSqlIdentifierQuote() {
			return "\"";
		}
		public MetaDataFactory getMetaDataFactory() {
			return new AccessMetaDataFactory();
		}
	},
	
    /** Microsoft SQL-Server */
	SQLSERVER () {
		public String toString() {
			return "Microsoft SQL-Server";
		}
		String getJdbcDriver() {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
		String getProtocol() {
			return "jdbc:sqlserver:";
		}
		String getInstance(String host, String port, String database, String instance) {
			String ret = "//" + host;
            if (instance.length() > 0) {
            	ret = ret + "\\" + instance;
            }
            if (port.length() > 0) {
            	ret = ret + ":" + port;
            }
            ret = ret + ";databaseName=" + database + ";:";
			return ret;
		}
		public String getSqlIdentifierQuote() {
			return "\"";
		}
		public MetaDataFactory getMetaDataFactory() {
			return new SqlServerMetaDataFactory();
		}
	};

    /**
     * @return description of this DBMS
     */
    @Override
    abstract public String toString();

    /**
     * @return class name of JDBC driver implementation
     */
    abstract String getJdbcDriver();
    
    /**
     * @return JDBC protocol name
     */
    abstract String getProtocol();

    /**
     * @return specification of a database instance suited for the connection URL
     * @optional not supported by Access and PostgreSQL
     */
    abstract String getInstance(String host, String port, String database, String instance);

    /**
     * @return connection URL
     */
    String getConnectionURL(String host, String port, String database, String instance, String userName, String password, String charSet) {
    	String url = this.getProtocol() + this.getInstance(host, port, database, instance);
    	if (userName!=null && userName.trim().length()>0) {
    		url += "user=" + userName.trim() + ";";
    	}
    	if (password!=null && password.trim().length()>0) {
    		url += "password=" + password.trim() + ";";
    	}
    	if (charSet!=null && charSet.trim().length()>0) {
    		url += "charSet=" + charSet.trim() + ";";
    	}
    	return url;
    }
    
    /**
     * @return the String (usually a single character) a DBMS uses to quote SQL
     * identifiers like table or column names
     */
    abstract public String getSqlIdentifierQuote();
	
    /**
     * @return Factory for database-independent meta data
     */
    // TODO: Extend and improve concept of wrapping native database code!!
    // TODO: Call this "Service"?
    abstract public MetaDataFactory getMetaDataFactory();
}
