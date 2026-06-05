package de.enflexit.common.dataSources;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class DatabaseDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-EssenS
 */
@Entity
@DiscriminatorValue("dbms")

@XmlRootElement(name = "DatabaseDataSource")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "dbmsName",
    "connectionURL",
    "dbName",
    "userName",
    "password"
})

public class DatabaseDataSource extends AbstractDataSource {

	private static final long serialVersionUID = 6704254616526361690L;

	@Column(name="dbms_name", nullable=false)
	private String dbmsName; 
	
	@Column(name="connection_url", nullable=false)
	private String connectionURL;
	
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
	public String getDBMSName() {
		return dbmsName;
	}
	/**
	 * Sets the DBMS name.
	 * @param dbmsName the new DBMS name
	 */
	public void setDBMSName(String dbmsName) {
		this.dbmsName = dbmsName;
	}

	/**
	 * Returns the host or IP.
	 * @return the host or IP
	 */
	public String getConnectionURL() {
		return connectionURL;
	}
	/**
	 * Sets the connection URL.
	 * @param connectionURL the new connection URL
	 */
	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
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
	 * Returns the database user name.
	 * @return the database user name
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * Sets the database user name.
	 * @param userName the new database user name
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
