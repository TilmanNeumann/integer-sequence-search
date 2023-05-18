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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.tilman_neumann.db.meta.DbMeta;
import de.tilman_neumann.db.meta.MetaDataFactory;

/**
 * Abstraction of the database connection.
 * @author Tilman Neumann
 */
public class Db {
	
	private static final Logger LOG = Logger.getLogger(Db.class);
	
	private String host = null;
	private String port = null;
	private String database = null;
	private String instance = null;
	private String user = null;
	private String password = null;
	private String charSet = null;
	
	private Dbms dbms;
	
	/** the thing all database accesses will go through */
	protected Connection connection;
	
	protected StatementBaseImpl lastStatement;
	
	private boolean supportsTransactions;
	
	private MetaDataFactory metadataFactory = null;
	
	/** Simplified constructor. */
	public Db(Dbms dbms, String host, String port, String database, String user, String password) throws SQLException, ClassNotFoundException {
		this(dbms, host, port, database, null, user, password, null);
	}
	
	/**
	 * Full constructor, creates a connection to the given database.
	 * @param dbms type of the database system
	 * @param host database server
	 * @param port the port the database server is listening on
	 * @param database the name of the database
	 * @param user user name
	 * @param password password
	 * @param charSet a particular character set
	 * @throws SQLException if the connection could not be established
	 * @throws ClassNotFoundException if the JDBC driver could not be found (required on class path)
	 */
	public Db(Dbms dbms, String host, String port, String database, String instance, String user, String password, String charSet) throws SQLException, ClassNotFoundException {
		// store all connection data
		this.dbms = dbms;
		this.host = host;
		this.port = port;
		this.database = database;
		this.instance = instance;
		this.user = user;
		this.password = password;
		this.charSet = charSet;
		
		// get JDBC driver
	   	String jdbcDriver = dbms.getJdbcDriver();
    	try {
    		Class.forName(jdbcDriver); // registers JDBC driver
    	} catch (ClassNotFoundException cfe) {
    		throw new ClassNotFoundException("JDBC driver class " + jdbcDriver + ": ", cfe);
    	}
    	
    	// connection URL
		String url = dbms.getConnectionURL(host, port, database, instance, user, password, charSet);
    	LOG.debug("connection URL = " + url);

    	try {
        	// Connection informations usually include at least "user" and "password"...
        	Properties connectionInfo = new Properties();
        	if (user != null) {
        		connectionInfo.setProperty("user", user);
        	}
        	if (password != null) {
        		connectionInfo.setProperty("password", password);
        	}
        	if (charSet != null) {
        		connectionInfo.setProperty("charSet", charSet);
        	}
        	this.connection = DriverManager.getConnection(url, connectionInfo);
    	} catch (SQLException se) {
    		throw new RuntimeException("Could not connect: " + se.getMessage(), se);
    	}
    	
    	// meta data that is unlikely to change during a session
		this.metadataFactory = dbms.getMetaDataFactory();
		// though DatabaseMetaData should not be instantiated because it may change
		// during a connection, the transaction support will usually not change...
		this.supportsTransactions = metadataFactory.supportsTransactions(connection.getMetaData());
		LOG.info("supportsTransactions = " + supportsTransactions);
	}
	
	/**
	 * @return A meta data factory that produces system independent meta data for a particular DBMS
	 */
	public MetaDataFactory getMetaDataFactory() {
		return this.metadataFactory;
	}
	
	/**
	 * @return The name of this database
	 */
	protected String getDatabaseName() {
		return this.database;
	}
	
	/**
	 * @return true if connection is established
	 */
	public boolean isConnected() {
		return (this.connection!=null);
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	/**
	 * @return true if transactions are supported
	 */
	public boolean supportsTransactions() {
		return this.supportsTransactions;
	}

    /**
     * @return the String (usually a single character) a DBMS uses to quote SQL identifiers like table names
     */
	public String getSqlIdentifierQuote() {
		return this.dbms.getSqlIdentifierQuote();
	}
    
    /**
     * @return The DBMS type of this database
     */
    public Dbms getDBMS() {
    	return this.dbms;
    }
    
    /**
     * @return DBMetaData
     * @throws SQLException
     */
    public DbMeta getDatabaseMetaData() throws SQLException {
    	return this.metadataFactory.getDatabaseMetaData(connection.getMetaData());
    }
	
    /**
     * Closes database connection. This may be called explicitly or implicitly via the finalize() method.
     */
    public void close() throws SQLException {
    	// Close last statement and result set
    	lastStatement.close();
    	
    	// now close the connection itself
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException se) {
                throw new SQLException("Connection " + connection + ".close(): " + se.getMessage());
            }
            connection = null;
        }
    }
    
	/**
	 * Close connection on exit.
	 */
	public void finalize() {
		try {
			this.close();
		} catch (SQLException e) {
			LOG.error(this + ".finalize(): " + e.getMessage(), e);
		}
	}
}
