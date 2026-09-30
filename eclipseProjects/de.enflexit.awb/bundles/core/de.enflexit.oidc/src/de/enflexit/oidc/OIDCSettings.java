package de.enflexit.oidc;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This class manages the configuration settings for OIDC authorization.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class OIDCSettings {
	
	public static final String PREFERENCES_KEY_ISSUER_URL = "issuerURL";
	public static final String PREFERENCES_KEY_KEYCLOAK_REALM = "realmName";
	public static final String PREFERENCES_KEY_CLIENT_NAME = "clientName";
	public static final String PREFERENCES_KEY_LOCAL_CALLBACK_PORT = "callback.localPort";
	public static final String PREFERENCES_KEY_AUTHENTICATION_CALLBACK_ENDPOINT = "callback.localAuthEndpoint";
	
	private static final String DEFAULT_ISSUER_URL = "https://login.enflex.it";
	private static final String DEFAULT_REALM_NAME = "enflexService";
	private static final String DEFAULT_CLIENT_NAME = "Agent.Workbench";
	private static final int DEFAULT_LOCAL_PORT = 8888;
	private static final String DEFAULT_AUTHENTICATION_ENDPOINT = "/oauth/callback/";
	
	private static final String DEFAULT_CLIENT_SECRET = "PeQ5NZeH4aGpr58knm2MviLA5IJ5uvY3";
	
	private IEclipsePreferences eclipsePreferences;
	
	private String issuerURL;
	private String realmID;
	
	private String clientID;
	private String clientSecret;
	
	private int localHTTPPort;
	private String authenticationEndpoint;
	
	/**
	 * Gets the issuer URL.
	 * @return the issuer URL
	 */
	public String getIssuerURL() {
		return issuerURL;
	}
	/**
	 * Sets the issuer URL.
	 * @param issuerURL the new issuer URL
	 */
	public void setIssuerURL(String issuerURL) {
		this.issuerURL = issuerURL;
	}
	
	/**
	 * Gets the realm name.
	 * @return the realm name
	 */
	public String getRealmID() {
		return realmID;
	}
	/**
	 * Sets the realm name.
	 * @param realmID the new realm name
	 */
	public void setRealmID(String realmID) {
		this.realmID = realmID;
	}
	
	/**
	 * Gets the client name.
	 * @return the client name
	 */
	public String getClientID() {
		return clientID;
	}
	/**
	 * Sets the client name.
	 * @param clientID the new client name
	 */
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	
	/**
	 * Gets the client secret.
	 * @return the client secret
	 */
	public String getClientSecret() {
		return clientSecret;
	}
	/**
	 * Sets the client secret.
	 * @param clientSecret the new client secret
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	/**
	 * Gets the local server port.
	 * @return the local server port
	 */
	public int getLocalHTTPPort() {
		return localHTTPPort;
	}
	/**
	 * Sets the local server port.
	 * @param localHTTPPort the new local server port
	 */
	public void setLocalHTTPPort(int localHTTPPort) {
		this.localHTTPPort = localHTTPPort;
	}
	
	/**
	 * Gets the authentication callback endpoint.
	 * @return the authentication callback endpoint
	 */
	public String getAuthenticationEndpoint() {
		return authenticationEndpoint;
	}
	/**
	 * Sets the authentication callback endpoint.
	 * @param authenticationEndpoint the new authentication callback endpoint
	 */
	public void setAuthenticationEndpoint(String authenticationEndpoint) {
		this.authenticationEndpoint = authenticationEndpoint;
	}
	
	/**
	 * Returns the eclipse preferences.
	 * @return the eclipse preferences
	 */
	public IEclipsePreferences getEclipsePreferences() {
		if (eclipsePreferences==null) {
			Bundle bundle = FrameworkUtil.getBundle(this.getClass());
			IScopeContext iScopeContext = ConfigurationScope.INSTANCE;
			eclipsePreferences = iScopeContext.getNode(bundle.getSymbolicName());
//			eclipsePreferences.addPreferenceChangeListener(this.getChangeListener());
		}
		return eclipsePreferences;
	}
	
	/**
	 * Loads the OIDC settings from the preferences.
	 * @return the OIDC settings
	 */
	public OIDCSettings loadFromPreferences() {
		String issuerURL = this.getEclipsePreferences().get(PREFERENCES_KEY_ISSUER_URL, DEFAULT_ISSUER_URL);
		String realmID = this.getEclipsePreferences().get(PREFERENCES_KEY_KEYCLOAK_REALM, DEFAULT_REALM_NAME);
		String clientID = this.getEclipsePreferences().get(PREFERENCES_KEY_CLIENT_NAME, DEFAULT_CLIENT_NAME);
		String clientSecret = DEFAULT_CLIENT_SECRET;
		int localHttpPort = this.getEclipsePreferences().getInt(PREFERENCES_KEY_LOCAL_CALLBACK_PORT, DEFAULT_LOCAL_PORT);
		String localAuthenticationEndpoint = this.getEclipsePreferences().get(PREFERENCES_KEY_AUTHENTICATION_CALLBACK_ENDPOINT, DEFAULT_AUTHENTICATION_ENDPOINT);
		
		this.setIssuerURL(issuerURL);
		this.setRealmID(realmID);
		this.setClientID(clientID);
		this.setClientSecret(clientSecret);
		this.setLocalHTTPPort(localHttpPort);
		this.setAuthenticationEndpoint(localAuthenticationEndpoint);
		
		return this;
	}
	
	/**
	 * Stores the OIDC settings to the preferences.
	 */
	public void storeToPreferences() {
		this.getEclipsePreferences().put(PREFERENCES_KEY_ISSUER_URL, this.issuerURL);
		this.getEclipsePreferences().put(PREFERENCES_KEY_KEYCLOAK_REALM, this.getRealmID());
		this.getEclipsePreferences().put(PREFERENCES_KEY_CLIENT_NAME, this.getClientID());
		this.getEclipsePreferences().putInt(PREFERENCES_KEY_LOCAL_CALLBACK_PORT, this.getLocalHTTPPort());
		this.getEclipsePreferences().put(PREFERENCES_KEY_AUTHENTICATION_CALLBACK_ENDPOINT, this.getAuthenticationEndpoint());
		
		try {
			this.getEclipsePreferences().flush();
		} catch (org.osgi.service.prefs.BackingStoreException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error storing settings to the preferences");
			e.printStackTrace();
		}
	}
	
}
