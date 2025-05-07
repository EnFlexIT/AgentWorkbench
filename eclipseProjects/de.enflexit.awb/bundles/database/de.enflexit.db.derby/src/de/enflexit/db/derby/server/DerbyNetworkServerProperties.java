package de.enflexit.db.derby.server;

import de.enflexit.common.StringHelper;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;

/**
 * The Class BundleProperties organizes the storage of application configuration data.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DerbyNetworkServerProperties {

	public static String DERBY_SERVER_PREFERENCES = "de.enflexit.db.derby.server";
	
	public static String DERBY_SERVER_PREFERENCES_START_SERVER = "derby.server.execute";
	
	public static String DERBY_SERVER_PREFERENCES_HOST = "derby.server.host";
	public static String DERBY_SERVER_PREFERENCES_PORT = "derby.server.port";
	public static String DERBY_SERVER_PREFERENCES_USERNAME = "derby.server.username";
	public static String DERBY_SERVER_PREFERENCES_PASSWORD = "derby.server.password";
	
	private IEclipsePreferences eclipsePreferences;
	
	private Boolean isStartDerbyNetworkServer;
	private String host;
	private Integer portNumber;
	private String userName;
	private String password;
	
	
	/**
	 * Instantiates a new derby network server properties.
	 */
	public DerbyNetworkServerProperties() {
		this.load();
	}
	
	/**
	 * Returns the eclipse preferences.
	 * @return the eclipse preferences
	 */
	private IEclipsePreferences getEclipsePreferences() {
		if (eclipsePreferences==null) {
			IScopeContext iScopeContext = ConfigurationScope.INSTANCE;
			eclipsePreferences = iScopeContext.getNode(DERBY_SERVER_PREFERENCES);
		}
		return eclipsePreferences;
	}
	/**
	 * Loads all properties to the current instance.
	 */
	private void load() {
		this.isStartDerbyNetworkServer();
		this.getHost();
		this.getPort();
		this.getUserName();
		this.getPassword();
	}
	
	/**
	 * Saves the current DerbyNetworkServerProperties.
	 * @return true, if successful
	 */
	public boolean save() {
		try {
			this.getEclipsePreferences().putBoolean(DERBY_SERVER_PREFERENCES_START_SERVER, this.isStartDerbyNetworkServer);
			this.getEclipsePreferences().put(DERBY_SERVER_PREFERENCES_HOST, this.host);
			this.getEclipsePreferences().putInt(DERBY_SERVER_PREFERENCES_PORT, this.portNumber);
			this.getEclipsePreferences().put(DERBY_SERVER_PREFERENCES_USERNAME, this.userName);
			this.getEclipsePreferences().put(DERBY_SERVER_PREFERENCES_PASSWORD, this.password);
			this.getEclipsePreferences().flush();
			return true;
			
		} catch (BackingStoreException bsEx) {
			bsEx.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Sets (and saves) all server properties at one.
	 *
	 * @param isStartServer the is execute server
	 * @param host the host
	 * @param portNumber the port number
	 * @param userName the user name
	 * @param password the password
	 */
	public void setProperties(boolean isStartServer, String host, int portNumber, String userName, String password) {
		this.isStartDerbyNetworkServer = isStartServer;
		this.host = host;
		this.portNumber = portNumber;
		this.userName = userName;
		this.password = password;
		this.save();
	}
	
	
	/**
	 * Returns if the derby network server should be started.
	 * @return true, if is start derby network server
	 */
	public boolean isStartDerbyNetworkServer() {
		if (isStartDerbyNetworkServer==null) {
			isStartDerbyNetworkServer = this.getEclipsePreferences().getBoolean(DERBY_SERVER_PREFERENCES_START_SERVER, false);
		}
		return isStartDerbyNetworkServer;
	}
	/**
	 * Sets whether to the start derby network server.
	 * @param isStartServer the indicator to start derby network server
	 */
	public void setStartDerbyNetworkServer(boolean isStartServer) {
		this.isStartDerbyNetworkServer = isStartServer;
	}
	
	
	/**
	 * Returns the host address as InetAddress.
	 *
	 * @return the host InetAddress
	 * @throws UnknownHostException the unknown host exception
	 */
	public InetAddress getHostInetAddress() throws UnknownHostException {
		return InetAddress.getByName(this.getHost());
	}
	
	/**
	 * Return the host to be used by the derby network server.
	 * @return the host
	 */
	public String getHost() {
		if (host==null) {
			host = this.getEclipsePreferences().get(DERBY_SERVER_PREFERENCES_HOST, "localhost"); 
		}
		return host; 
	}
	/**
	 * Sets the host to be used by the derby network server.
	 * @param host the new host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	
	/**
	 * Return the port number to be used by the derby network server.
	 * @return the port number
	 */
	public int getPort() {
		if (portNumber==null) {
			portNumber = this.getEclipsePreferences().getInt(DERBY_SERVER_PREFERENCES_PORT, 1527);
		}
		return portNumber;
	}
	/**
	 * Sets the port number to be used by the derby network server.
	 * @param portNumber the new port
	 */
	public void setPort(int portNumber) {
		this.portNumber = portNumber;
	}

	
	/**
	 * Return the user name to be used for the derby network server.
	 * @return the host
	 */
	public String getUserName() {
		if (userName==null) {
			userName = this.getEclipsePreferences().get(DERBY_SERVER_PREFERENCES_USERNAME, "awb");
		}
		return userName;
	}
	/**
	 * Sets the user name to be used for the derby network server.
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	/**
	 * Return the user name to be used for the derby network server.
	 * @return the host
	 */
	public String getPassword() {
		if (password==null) {
			password = this.getEclipsePreferences().get(DERBY_SERVER_PREFERENCES_PASSWORD, "awb");
		}
		return password;
	}
	/**
	 * Sets the password to be used for the derby network server.
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObject) {
		
		if (compObject==null) return false;
		if (compObject instanceof DerbyNetworkServerProperties==false) return false;
		if (compObject==this) return true;
		
		DerbyNetworkServerProperties compProperties = (DerbyNetworkServerProperties) compObject;
		
		if (compProperties.isStartDerbyNetworkServer()!=this.isStartDerbyNetworkServer()) return false;
		
		if (StringHelper.isEqualString(compProperties.getHost(), this.getHost())==false) return false;
		if (compProperties.getPort()!=this.getPort()) return false;
		
		if (StringHelper.isEqualString(compProperties.getUserName(), this.getUserName())==false) return false;
		if (StringHelper.isEqualString(compProperties.getPassword(), this.getPassword())==false) return false;
		
		return true;
	}
	
}
