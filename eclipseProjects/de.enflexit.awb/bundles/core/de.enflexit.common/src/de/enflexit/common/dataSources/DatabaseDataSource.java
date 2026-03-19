package de.enflexit.common.dataSources;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * The Class DatabaseDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-EssenS
 */
@Entity
@DiscriminatorValue("dbms")
public class DatabaseDataSource extends AbstractDataSource {

	private static final long serialVersionUID = 6704254616526361690L;

	@Column(name="dbms_name", nullable=false)
	private String dbmsName; 
	
	@Column(name="host_or_ip", nullable=false)
	private String hostOrIP;
	
	@Column(nullable=false)
	private int port;
	
	@Column(name="db_name", nullable=false)
	private String dbName;
	
	@Column(name="user_name", nullable=false)
	private String userName;
	
	@Column(name="password", nullable=false)
	private String password;
	
	
	/**
	 * Returns the DBMS name (e.g. MariaDB or PostGres).
	 * @return the DBMS name
	 */
	public String getDbmsName() {
		return dbmsName;
	}
	/**
	 * Sets the DBMS name.
	 * @param dbmsName the new DBMS name
	 */
	public void setDbmsName(String dbmsName) {
		this.dbmsName = dbmsName;
	}

	/**
	 * Returns the host or IP.
	 * @return the host or IP
	 */
	public String getHostOrIP() {
		return hostOrIP;
	}
	/**
	 * Sets the host or IP.
	 * @param hostOrIP the new host or IP
	 */
	public void setHostOrIP(String hostOrIP) {
		this.hostOrIP = hostOrIP;
	}

	/**
	 * Returns the port.
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * Sets the port.
	 * @param port the new port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Returns the database name.
	 * @return the database name
	 */
	public String getDbName() {
		return dbName;
	}
	/**
	 * Sets the database name.
	 * @param dbName the new database name
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * Returns the database username.
	 * @return the database username
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * Sets the database username.
	 * @param userName the new database username
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Returns the database password.
	 * @return the database password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Sets the database password.
	 * @param databasePassword the new database password
	 */
	public void setPassword(String databasePassword) {
		this.password = databasePassword;
	}
	
}
