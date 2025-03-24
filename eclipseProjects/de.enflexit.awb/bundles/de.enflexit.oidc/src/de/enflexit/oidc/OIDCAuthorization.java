/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package de.enflexit.oidc;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JDialog;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.sun.net.httpserver.HttpExchange;

import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

/**
 * This class provides a simple interface to the OpenID Connect authorisation.
 * Use with getInstance(), getDialog()/connect() and getUserID().
 */
public class OIDCAuthorization implements OIDCCallbackListener {
	
	public enum AuthenticationState {
		LOGGED_IN, LOGGED_OUT, PENDING
	}

	/** The claims representing relevant infos from the ID token */
	private static final String OIDC_ID_CLAIM_USER_ID = "sub";
	private static final String OIDC_ID_CLAIM_USER_NAME = "preferred_username";
	private static final String OIDC_ID_CLAIM_FULL_NAME = "name";
	
	private static final String SECURE_PREFERENCES_TOKEN_PATH = "de/enflexit/oidc/tokens";
	private static final String PREFERENCES_KEY_ID_TOKEN = "idToken";
	private static final String PREFERENCES_KEY_ACCESS_TOKEN = "accessToken";
	private static final String PREFERENCES_KEY_REFRESH_TOKEN = "refreshToken";

	/** The single instance of this singleton class. */
	private static OIDCAuthorization instance;
	/** The OIDC client. */
	private SimpleOIDCClient oidcClient;
	
	private OIDCSettings oidcSettings;

	/** The accessed resource URI, initialized with @see OIDCPanel.DEBUG_RESOURCE_URI. */
	private String resourceURI;
	
	/** The availability handler called when the resource is available. */
	private OIDCResourceAvailabilityHandler availabilityHandler;
	/** The URLProcessor used for the network communication. */
	private URLProcessor urlProcessor;
	
	private JDialog authDialog;
	private Window owner;

	private String presetUsername;
	private boolean inited = false;

	private File truststoreFile;

	private String lastSuccessfulUser;
	
	private AuthenticationState authenticationState = AuthenticationState.LOGGED_OUT;
	
	private ArrayList<AuthenticationStateListener> authenticationStateListeners;
	
	private ISecurePreferences tokenStore;
	private boolean initialCheckDone;
	
	/**
	 * Instantiates a new OIDC authorization.
	 */
	private OIDCAuthorization() {
	}
	/**
	 * Gets the single instance of OIDCAuthorization.
	 * @return single instance of OIDCAuthorization
	 */
	public static OIDCAuthorization getInstance() {
		if (instance == null) {
			instance = new OIDCAuthorization();
//			instance.loadIDToken();
		}
		return instance;
	}

	/**
	 * Gets the OIDC client.
	 *
	 * @return the OIDC client
	 * @throws URISyntaxException the URI syntax exception
	 */
	public SimpleOIDCClient getOIDCClient() {
		if (oidcClient == null) {
			oidcClient = new SimpleOIDCClient();
//			oidcClient.setIssuerURI(this.getOIDCSettings().getIssuerURL());
		}
		return oidcClient;
	}

	/**
	 * Gets the OIDC settings. If not set yet, a new instance with default settings will be generated.
	 * @return the OIDC settings
	 */
	public OIDCSettings getOIDCSettings() {
		if (oidcSettings==null) {
			oidcSettings = new OIDCSettings();
			oidcSettings.initializeWithDefaults();
		}
		return oidcSettings;
	}
	/**
	 * Sets the OIDC settings.
	 * @param oidcSettings the new oidc settings
	 */
	public void setOIDCSettings(OIDCSettings oidcSettings) {
		this.oidcSettings = oidcSettings;
	}
	
	/**
	 * Sets the issuer URI.
	 * @param issuerURI the new issuer URI
	 * @deprecated Just kept for backwards compatibility, remove when the old authorization stuff is removed from agentgui.core
	 */
	@Deprecated 
	public void setIssuerURI(String issuerURI) {
		this.getOIDCSettings().setIssuerURL(issuerURI);
	}

	/**
	 * Gets the url processor.
	 *
	 * @return the url processor
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws KeyStoreException the key store exception
	 */
	public URLProcessor getUrlProcessor() throws URISyntaxException, IOException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
		if (urlProcessor == null) {
			init();
		}
		return urlProcessor;
	}

	/**
	 * Sets the trust store.
	 *
	 * @param trustStoreFile the new trust store
	 * @throws URISyntaxException the URI syntax exception
	 */
	public void setTrustStore(File trustStoreFile) throws URISyntaxException {
		this.truststoreFile = trustStoreFile;
		getOIDCClient().setTrustStore(trustStoreFile);;
	}

	/**
	 * Sets the availability handler.
	 *
	 * @param availabilityHandler the availability handler
	 * @return the OIDC authorization
	 */
	public OIDCAuthorization setAvailabilityHandler(OIDCResourceAvailabilityHandler availabilityHandler) {
		this.availabilityHandler = availabilityHandler;
		return this;
	}

	/**
	 * Gets the resource URI.
	 * @return the resource URI
	 */
	public String getResourceURI() {
		return resourceURI;
	}
	/**
	 * Sets the resource URI.
	 * @param resourceURI the new resource URI
	 */
	public void setResourceURI(String resourceURI) {
		this.resourceURI = resourceURI;
	}

	/**
	 * Gets the authorization dialog (with null defaults).
	 * @return the dialog
	 */
	public JDialog getDialog() {
		if (authDialog == null) {
			authDialog = this.getDialog("", null);
		}
		return authDialog;
	}
	/**
	 * Gets the authorization dialog.
	 *
	 * @param presetUsername username which should be shown preset when displaying the dialog
	 * @param owner the window to which the dialog should belong (to center etc.)
	 * @return the dialog
	 */
	public JDialog getDialog(String presetUsername, Window owner) {

		authDialog = new JDialog(owner);
		OIDCPanel oidcPanel = new OIDCPanel(this);
		if (presetUsername != null) {
			oidcPanel.getJTextFieldUsername().setText(presetUsername);
		}
		authDialog.setContentPane(oidcPanel);
		authDialog.setSize(new Dimension(500, 190));
		WindowSizeAndPostionController.setJDialogPositionOnScreen(authDialog, JDialogPosition.ParentCenter);
		return authDialog;
	}

	/**
	 * Checks if OIDC token is valid, so access has been granted.
	 *
	 * @return true, if is valid
	 * @throws URISyntaxException the URI syntax exception
	 */
	public boolean isValid() throws URISyntaxException {
		if (getOIDCClient().getAccessToken() != null) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the unique ID of the user authenticated.
	 *
	 * @return the user ID
	 * @throws OIDCProblemException if the token is not valid/the user is not authenticated yet
	 * @throws URISyntaxException the URI syntax exception
	 */
	public String getUserID() throws OIDCProblemException, URISyntaxException {
		return this.getOIDCClaim(OIDC_ID_CLAIM_USER_ID);
	}
	
	/**
	 * Gets the user name of the authenticated user.
	 * @return the user name
	 * @throws OIDCProblemException the OIDC problem exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public String getUserName() throws OIDCProblemException, URISyntaxException {
		return this.getOIDCClaim(OIDC_ID_CLAIM_USER_NAME);
	}
	
	/**
	 * Gets the full name of the authenticated user.
	 * @return the user full name
	 * @throws URISyntaxException the URI syntax exception
	 */
	public String getUserFullName() throws OIDCProblemException, URISyntaxException {
		return this.getOIDCClaim(OIDC_ID_CLAIM_FULL_NAME);
	}
	
	/**
	 * Gets the value for the specified OIDC claim.
	 * @param oidcClaim the oidc claim
	 * @return the OIDC claim
	 * @throws OIDCProblemException the OIDC problem exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	private String getOIDCClaim(String oidcClaim) throws OIDCProblemException, URISyntaxException {
		String claimValue;
//		if (!isValid()) {
//			throw new OIDCProblemException();
//		}
		Map<String, Object> allClaims = getOIDCClient().getIdClaims().getClaims();
		claimValue = (String) allClaims.get(oidcClaim);

		if (claimValue != null) {
			return claimValue;
		}
		return "";
	}

	/**
	 * Initiates the authorization process by setting the necessary URIs and data to the OIDC client and preparing the URLProcessor.
	 *
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws KeyStoreException the key store exception
	 */
	public void init() throws URISyntaxException, IOException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
		
		this.getOIDCClient();
		//oidcClient.reset();
		urlProcessor = new URLProcessor();

		oidcClient.setIssuerURI(this.getOIDCSettings().getIssuerURL());
		oidcClient.setRealmName(this.getOIDCSettings().getRealmID());
		try {
			oidcClient.retrieveProviderMetadata();
			oidcClient.setClientMetadata(getResourceURI());
		} catch (ParseException e) {
			throw new IOException("OIDC ParseException");
		}
		oidcClient.setClientID(this.getOIDCSettings().getClientID(), this.getOIDCSettings().getClientSecret());
		oidcClient.setRedirectURI(getResourceURI());
		urlProcessor.prepare(oidcClient.getRedirectURI().toURL());
		
		oidcClient.setLocalCallbackURI(this.getLocalCallbackURL());
		
		inited = true;
	}
	
	public synchronized void doInitialAuthenticationCheck() {
		if (this.initialCheckDone==false) {
			this.requestAuthorizationCode(false);
			this.initialCheckDone = true;
		}
	}
	
	/**
	 * Authorizes a user using the authorization code flow. An authorization code is requested
	 * from the keycloak server and exchanged for the required tokens. User login is handled by
	 * keycloak, i.e. if not already logged in the login page will be opened in the browser.
	 * @return true, if successful
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws KeyStoreException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public void requestAuthorizationCode(boolean showLogin) {
		
		if (!inited) {
			try {
				this.init();
			} catch (KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException | URISyntaxException | IOException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error initializing the OIDC client, please check your settings!");
				e.printStackTrace();
				return;
			}
		}
		
		URI authenticationRequestURI = null;
		try {
			authenticationRequestURI = oidcClient.buildAuthorizationCodeRequest(this.getLocalCallbackURL(), showLogin);
		} catch (SerializeException | URISyntaxException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error building the authentication request URI, please check your settings!");
			e.printStackTrace();
			return;
		}
			
		new OIDCCallbackHTTPServer(this.getOIDCSettings().getLocalHTTPPort(), this.getOIDCSettings().getAuthenticationEndpoint(), this).startInThread();
		this.setAuthenticationState(AuthenticationState.PENDING);
		try {
			Desktop.getDesktop().browse(authenticationRequestURI);
		} catch (IOException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error opening the login page, please check your OIDC settings!");
			e.printStackTrace();
			return;
		}
		
	}
	
	private String getLocalCallbackURL() {
		return "http://localhost:" + this.getOIDCSettings().getLocalHTTPPort() + this.getOIDCSettings().getAuthenticationEndpoint();
	}

	/**
	 * Connect to the authorization server and get a valid token.
	 *
	 * @param username the username
	 * @param password the password
	 * @return true, if successful
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws KeyStoreException the key store exception
	 */
	public boolean authorizeByUserAndPW(String username, String password) throws URISyntaxException, IOException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
		
		String authRedirection = "";
		if (!inited) {
			this.init();
		}

		oidcClient.setResourceOwnerCredentials(username, password);
		oidcClient.requestToken();

		urlProcessor.setAccessToken(oidcClient.getAccessToken());

		authRedirection = urlProcessor.prepare(oidcClient.getRedirectURI().toURL()).process();
		if (authRedirection == null) { 	// authenticated
			lastSuccessfulUser = username;
			if (availabilityHandler != null) {
				availabilityHandler.onResourceAvailable(urlProcessor);
			}
			return true;
		} else {
			System.err.println("OIDC authorization failed");
			System.err.println("authRedirection=" + authRedirection);
			return false;
		}
	}
	
	/**
	 * Trigger remote logout.
	 */
	public void triggerRemoteLogout() {
		try {
			boolean logoutSuccess = this.oidcClient.sendLogoutRequest();
			if (logoutSuccess==true) {
				this.setAuthenticationState(AuthenticationState.LOGGED_OUT);
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error sending logout request!");
			e.printStackTrace();
		}
	}

	/**
	 * Access some resource previously set by {@link #setResourceURI(String)}. If not accessible, show user/password dialog
	 *
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException the URI syntax exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws KeyStoreException the key store exception
	 */
	public boolean accessResource() throws IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
		
		this.getUrlProcessor().prepare(new URI(getResourceURI()).toURL());
		String result = urlProcessor.process();
		if (result == null) {
			// all good (unlikely on first call)
			if (availabilityHandler != null) {
				availabilityHandler.onResourceAvailable(urlProcessor);
			}
			return true;
			
		} else {
			
			try {
				getOIDCClient().parseAuthenticationDataFromRedirect(result, false); // don't override clientID
			} catch (ParseException e) {
				throw new IOException("OIDC ParseException");
			}
			boolean showDialog = true;
			if (availabilityHandler != null) {
				showDialog = availabilityHandler.onAuthorizationNecessary(this);
			} 
			if(showDialog){
				getDialog(presetUsername, owner).setVisible(true);
			}
			return false;
		}
		
	}
	
	/**
	 * Access some resource like {@link #accessResource()}, but also provide the presetUsername/owner for the dialog.
	 *
	 * @param presetUsername the preset username
	 * @param owner the owner frame
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException the URI syntax exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws KeyStoreException the key store exception
	 */
	public boolean accessResource(String presetUsername, Window owner) throws IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
		this.presetUsername = presetUsername;
		this.owner = owner;
		return this.accessResource();
	}

	/**
	 * Access some resource like {@link #accessResource()}, but also provide the URL and presetUsername/owner for the dialog.
	 *
	 * @param url the url
	 * @param presetUsername the preset username
	 * @param owner the owner frame
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException the URI syntax exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws KeyStoreException the key store exception
	 */
	public boolean accessResource(String url, String presetUsername, Window owner) throws IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
		this.setResourceURI(url);
		return accessResource(presetUsername, owner);
	}

	/**
	 * Returns the last successful user name.
	 * @return the last successful user
	 */
	public String getLastSuccessfulUser() {
		return lastSuccessfulUser;
	}

	/**
	 * The Class URLProcessor.
	 */
	public class URLProcessor {

		/** The Constant CHARSET_UTF_8. */
		private static final String CHARSET_UTF_8 = "UTF-8";

		/** The Constant CRLF. */
		private static final String CRLF = "\r\n"; // Line separator required by multipart/form-data.

		/** The Constant CONTENT_DISPOSITION_NAME. */
		private static final String CONTENT_DISPOSITION_NAME = "file";

		/** The Constant HTTP_METHOD_POST. */
		private static final String HTTP_METHOD_POST = "POST";

		/** The debug. */
		private boolean debug = false;

		/** The upload file. */
		private File uploadFile;

		/** The connection. */
		private HttpsURLConnection connection = null;

		/** The access token. */
		private AccessToken accessToken;

		/** The redirection URL. */
		private String redirectionURL = null;

		/** The response code. */
		private int responseCode = -1;

		/**
		 * Sets the upload file.
		 *
		 * @param uploadFile the new upload file
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public void setUploadFile(File uploadFile) throws IOException {
			this.uploadFile = uploadFile;
			if (uploadFile != null) {
				injectUpload();
			}
		}

		/**
		 * Gets the upload file.
		 *
		 * @return the upload file
		 */
		public File getUploadFile() {
			return uploadFile;
		}

		/**
		 * Gets the connection.
		 *
		 * @return the connection
		 */
		public HttpsURLConnection getConnection() {
			return connection;
		}

		/**
		 * Gets the redirection URL.
		 *
		 * @return the redirection URL
		 */
		public String getRedirectionURL() {
			return redirectionURL;
		}

		/**
		 * Gets the response code.
		 *
		 * @return the response code
		 */
		public int getResponseCode() {
			return responseCode;
		}

		/**
		 * Sets the access token.
		 *
		 * @param accessToken the access token
		 * @return the URL processor
		 */
		public URLProcessor setAccessToken(AccessToken accessToken) {
			this.accessToken = accessToken;
			return this;
		}

		/**
		 * Prepare.
		 *
		 * @param requestURL the request URL
		 * @return the URL processor
		 * @throws IOException Signals that an I/O exception has occurred.
		 * @throws KeyManagementException the key management exception
		 * @throws NoSuchAlgorithmException the no such algorithm exception
		 * @throws CertificateException the certificate exception
		 * @throws KeyStoreException the key store exception
		 */
		public URLProcessor prepare(URL requestURL) throws IOException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
//			System.out.println("requestURL=");
//			System.out.println(requestURL);

			connection = (HttpsURLConnection) requestURL.openConnection();
			connection.setInstanceFollowRedirects(false);
			Trust.trustSpecific(connection, OIDCAuthorization.this.truststoreFile);

			connection.setRequestMethod("GET");
			if (accessToken != null) {
				connection.setRequestProperty("Authorization", "Bearer " + accessToken);
			}

			if (uploadFile != null) {
				injectUpload();
			}

			return this;
		}

		/**
		 * Process a URL, that is: try to access it's resource, display error if any, return a redirection URL if indicated by the server.
		 *
		 * @return null if the access succeeded, a redirectionURL as string in case the authorization is not valid yet
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public String process() throws IOException {
			
			connection.connect();
			responseCode = connection.getResponseCode();

			if (responseCode == 200) { //
				redirectionURL = null;
			} else if (responseCode == 302) {
				redirectionURL = connection.getHeaderField("Location");
				if (debug) {
					System.out.println("redirection to:");
					System.out.println(redirectionURL);
				}
				
			} else {
				if (debug) {
					System.out.println("other response code");
				}
			}
			if (debug) {
				System.out.println("responseCode = " + responseCode);
			}
			return redirectionURL;
		}

		/**
		 * Inject a file to upload into the URLConnection and set method to POST.
		 *
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public void injectUpload() throws IOException {
			
			String charset = CHARSET_UTF_8;
			String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.

			try {
				connection.setRequestMethod(HTTP_METHOD_POST);
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
				
				OutputStream output = connection.getOutputStream();
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
				
				// Send binary file.
				writer.append("--" + boundary).append(CRLF);
				writer.append("Content-Disposition: form-data; name=\"" + CONTENT_DISPOSITION_NAME + "\"; filename=\"" + uploadFile.getName() + "\"").append(CRLF);
				
				String mediaType = URLConnection.guessContentTypeFromName(uploadFile.getName());
				if (mediaType != null) {
					writer.append("Content-Type: " + mediaType).append(CRLF);
				}
				
				writer.append("Content-Transfer-Encoding: binary").append(CRLF);
				writer.append(CRLF).flush();
				Files.copy(uploadFile.toPath(), output);
				output.flush(); // Important before continuing with writer
				writer.append(CRLF).flush(); // CRLF is important, indicates end o)f boundary
				
				// End of multipart/form-data
				writer.append("--" + boundary + "--").append(CRLF).flush();
				
			} catch (IllegalStateException | ProtocolException e) {
				// don't inject file, because http connection was already connected, will usually be overwritten later anyways
//				e.printStackTrace();
			}
		}
	}

	/**
	 * The Class OIDCProblemException, indicating a problem in the authorization process.
	 */
	private class OIDCProblemException extends RuntimeException {
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -6015464771451068526L;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.oidc.OIDCCallbackListener#callbackReceived(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void callbackReceived(HttpExchange exchange) {
		try {
			AuthenticationResponse response = AuthenticationResponseParser.parse(exchange.getRequestURI());
			
			if (response instanceof AuthenticationErrorResponse) {
				this.setAuthenticationState(AuthenticationState.LOGGED_OUT);
				System.out.println("Authentication failed!");
			} else if (response instanceof AuthenticationSuccessResponse){
				
				AuthenticationSuccessResponse successResponse = (AuthenticationSuccessResponse) response;
				AuthorizationCode authCode = successResponse.getAuthorizationCode();
				this.oidcClient.setAuthorizationCode(authCode);
				
				try {
					if (this.oidcClient.requestToken()==true) {
						this.storeIDToken();
					}
				} catch (KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException | URISyntaxException e) {
					System.err.println("[" + this.getClass().getSimpleName() + "] Error requesting tokens!");
					e.printStackTrace();
				}
				
				this.setAuthenticationState(AuthenticationState.LOGGED_IN);
			}
			
		} catch (ParseException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error parsing response!");
			e.printStackTrace();
		}
	}
	
	private ArrayList<AuthenticationStateListener> getAuthenticationListeners() {
		if (authenticationStateListeners==null) {
			authenticationStateListeners = new ArrayList<AuthenticationStateListener>();
		}
		return authenticationStateListeners;
	}
	
	/**
	 * Adds an authentication state listener.
	 * @param listener the listener
	 */
	public void addAuthenticationStateListener(AuthenticationStateListener listener) {
		if (this.getAuthenticationListeners().contains(listener)==false) {
			this.getAuthenticationListeners().add(listener);
		}
	}
	
	/**
	 * Removes an authentication state listener.
	 * @param listener the listener
	 */
	public void removeAuthenticationStateListener(AuthenticationStateListener listener) {
		if (this.getAuthenticationListeners().contains(listener)==true) {
			this.getAuthenticationListeners().remove(listener);
		}
	}
	
	private void setAuthenticationState(AuthenticationState authenticationState) {
		this.authenticationState = authenticationState;
		for (AuthenticationStateListener listener : this.getAuthenticationListeners()) {
			listener.authenticationStateChanged(authenticationState);
		}		
	}
	
	/**
	 * Gets the current authentication state.
	 * @return the authentication state
	 */
	public AuthenticationState getAuthenticationState() {
		return authenticationState;
	}

	/**
	 * Stores the current tokens in the Eclipse RCP secure preferences.
	 * @throws StorageException the storage exception
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void storeIDToken() {
		// --- Access the secure preferences store of eclipse RCP ------------- 
		
		if (this.getOIDCClient().getIdToken()!=null) {
			try {
				this.getTokenStore().put(PREFERENCES_KEY_ID_TOKEN, this.getOIDCClient().getIdToken().serialize(), true);
				this.getTokenStore().flush();
			} catch (StorageException | IOException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error storing the ID token locally!");
				e.printStackTrace();
			}
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] The ID token is not set!");
		}
		
	}
	
	/**
	 * Loads the tokens from the Eclipse RCP secure preferences, if present.
	 * @throws StorageException the storage exception
	 * @throws ParseException the parse exception
	 * @throws ParseException the parse exception
	 * @throws ParseException the parse exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	private void loadIDToken() {
		
		try {
			String idTokenSerialized = this.getTokenStore().get(PREFERENCES_KEY_ID_TOKEN, null);
			if (idTokenSerialized!=null && idTokenSerialized.isBlank()==false) {
				JWT idToken = JWTParser.parse(idTokenSerialized);
				if (idToken!=null) {
					System.out.println("Successfully loaded the ID token!");
					this.getOIDCClient().setIdToken(idToken);
					this.setAuthenticationState(AuthenticationState.LOGGED_IN);
				} else {
					System.out.println("Failed to load the ID token!");
				}
			}
		} catch (StorageException | java.text.ParseException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error loading the ID token from the local secure storage!");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Removes stored tokens from the secure preferences.
	 */
	//TODO make private when no longer needed public for the debug menu entry
	private void clearStoredTokens() {
		this.getTokenStore().remove(PREFERENCES_KEY_ID_TOKEN);
	}
	
	private ISecurePreferences getTokenStore() {
		if (tokenStore==null) {
			tokenStore = SecurePreferencesFactory.getDefault().node(SECURE_PREFERENCES_TOKEN_PATH);
		}
		return tokenStore;
	}
	
}
