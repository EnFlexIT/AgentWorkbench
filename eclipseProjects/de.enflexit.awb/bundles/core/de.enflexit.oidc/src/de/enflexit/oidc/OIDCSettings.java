package de.enflexit.oidc;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

// TODO: Auto-generated Javadoc
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
	
	private static final String PREFERENCES_NODE = "de.enflexit.oidc.prefs";
	
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
	
	
	public static OIDCSettings loadFromPreferences() {
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(PREFERENCES_NODE);
		String issuerURL = preferences.get(PREFERENCES_KEY_ISSUER_URL, DEFAULT_ISSUER_URL);
		String realmID = preferences.get(PREFERENCES_KEY_KEYCLOAK_REALM, DEFAULT_REALM_NAME);
		String clientID = preferences.get(PREFERENCES_KEY_CLIENT_NAME, DEFAULT_CLIENT_NAME);
		String clientSecret = DEFAULT_CLIENT_SECRET;
		int localHttpPort = preferences.getInt(PREFERENCES_KEY_LOCAL_CALLBACK_PORT, DEFAULT_LOCAL_PORT);
		String localAuthenticationEndpoint = preferences.get(PREFERENCES_KEY_AUTHENTICATION_CALLBACK_ENDPOINT, DEFAULT_AUTHENTICATION_ENDPOINT);
		
		OIDCSettings settings = new OIDCSettings();
		settings.setIssuerURL(issuerURL);
		settings.setRealmID(realmID);
		settings.setClientID(clientID);
		settings.setClientSecret(clientSecret);
		settings.setLocalHTTPPort(localHttpPort);
		settings.setAuthenticationEndpoint(localAuthenticationEndpoint);
		
		return settings;
	}
	
	public void storeToPreferences() {
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(PREFERENCES_NODE);
		preferences.put(PREFERENCES_KEY_ISSUER_URL, this.issuerURL);
		preferences.put(PREFERENCES_KEY_KEYCLOAK_REALM, this.getRealmID());
		preferences.put(PREFERENCES_KEY_CLIENT_NAME, this.getClientID());
		preferences.putInt(PREFERENCES_KEY_LOCAL_CALLBACK_PORT, this.getLocalHTTPPort());
		preferences.put(PREFERENCES_KEY_AUTHENTICATION_CALLBACK_ENDPOINT, this.getAuthenticationEndpoint());
		
		try {
			preferences.flush();
		} catch (org.osgi.service.prefs.BackingStoreException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error storing settings to the preferences");
			e.printStackTrace();
		}
	}
	
}
