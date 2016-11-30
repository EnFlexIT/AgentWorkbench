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
package agentgui.core.config.auth;

import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JDialog;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.token.AccessToken;

import agentgui.core.application.Application;

/**
 * This class provides a simple interface to the OpenID Connect authorisation.
 * Use with getInstance(), getDialog()/connect() and getUserID().
 */
public class OIDCAuthorization {

	private static final String OIDC_ID_CLAIM_USERID = "sub"; // subject
	private static OIDCAuthorization instance;
	private SimpleOIDCClient oidcClient;
	private String resourceURI = OIDCPanel.DEBUG_RESOURCE_URI;
	private OIDCResourceAvailabilityHandler availabilityHandler;
	private URLProcessor urlProcessor;
	private JDialog authDialog;

	/**
	 * Instantiates a new OIDC authorization.
	 */
	private OIDCAuthorization() {
		urlProcessor = new URLProcessor();
	}

	/**
	 * Gets the single instance of OIDCAuthorization.
	 *
	 * @return single instance of OIDCAuthorization
	 */
	public static OIDCAuthorization getInstance() {
		if (instance == null) {
			instance = new OIDCAuthorization();
		}
		return instance;
	}

	public JDialog getDialog() {
		if (authDialog == null) {
			authDialog = getDialog("", null);
		}
		return authDialog;
	}

	/**
	 * Gets the authorization dialog.
	 *
	 * @param presetUsername username which should be shown preset when displaying the dialog
	 * @param ownerFrame the frame to which the dialog should belong (to center etc.)
	 * @return the dialog
	 */
	public JDialog getDialog(String presetUsername, Frame ownerFrame) {

		authDialog = new JDialog(ownerFrame);
		OIDCPanel oidcPanel = new OIDCPanel(this);
		if (presetUsername != null) {
			oidcPanel.getJTextFieldUsername().setText(presetUsername);
		}
		authDialog.setContentPane(oidcPanel);
		authDialog.setSize(new Dimension(500, 190));
		authDialog.setLocationRelativeTo(null);
		return authDialog;
	}

	/**
	 * Checks if OIDC token is valid, so access has been granted.
	 *
	 * @return true, if is valid
	 */
	public boolean isValid() {
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
	 */
	public String getUserID() throws OIDCProblemException {
		String userID;
		if (!isValid()) {
			throw new OIDCProblemException();
		}
		Map<String, Object> allClaims = getOIDCClient().getIdClaims().getClaims();
		userID = (String) allClaims.get(OIDC_ID_CLAIM_USERID);

		if (userID != null) {
			return userID;
		}
		return "";
	}

	/**
	 * Gets the OIDC client.
	 * 
	 * @return the OIDC client
	 */
	public SimpleOIDCClient getOIDCClient() {
		if (oidcClient == null) {
			oidcClient = new SimpleOIDCClient();
		}
		return oidcClient;
	}

	/**
	 * Gets the issuer URI.
	 * 
	 * @return the issuer URI
	 */
	private String getIssuerURI() {
		return OIDCPanel.DEBUG_ISSUER_URI;
	}

	/**
	 * Sets the resource URI.
	 */
	private void setResourceURI(String resourceURI) {
		this.resourceURI = resourceURI;
	}

	/**
	 * Gets the resource URI.
	 * 
	 * @return the resource URI
	 */
	private String getResourceURI() {

		return resourceURI;
	}

	/**
	 * Gets the client id.
	 * 
	 * @return the client id
	 */
	private String getClientId() {
		return OIDCPanel.DEBUG_CLIENT_ID;
	}

	/**
	 * Gets the client secret.
	 * 
	 * @return the client secret
	 */
	private String getClientSecret() {
		return OIDCPanel.DEBUG_CLIENT_SECRET;
	}

	/**
	 * Connect to the authorization server and get a valid token
	 *
	 * @param username the username
	 * @param password the password
	 * @return true, if successful
	 */
	public boolean connect(String username, String password) {
		String authRedirection = "";
		AccessToken accessToken = null;

		try {
			getOIDCClient();
			oidcClient.reset();
			oidcClient.setTrustStore(new File(Application.getGlobalInfo().getPathProperty(true) + Trust.OIDC_TRUST_STORE));

			oidcClient.setIssuerURI(getIssuerURI());
			oidcClient.retrieveProviderMetadata();
			oidcClient.setClientMetadata(getResourceURI());
			oidcClient.setClientID(getClientId(), getClientSecret());
			oidcClient.setRedirectURI(getResourceURI());

//			System.out.println("try a direct access to the resource (EOM licenseer)");
			authRedirection = accessUserID(accessToken);

//			if (authRedirection == null) { 	// no authentication required (or already authenticated?)
//				System.out.println("resource available");
//				return true;
//			}

//			System.out.println("authentication redirection necessary");

//			System.out.println("parse authentication parameters from redirection");
			oidcClient.parseAuthenticationDataFromRedirect(authRedirection, false); // don't override clientID
//			System.out.println("set USER credentials");
			oidcClient.setResourceOwnerCredentials(username, password);

			oidcClient.requestToken();
			accessToken = oidcClient.getAccessToken();

//			System.out.println("This is the access token");
//			System.out.println(accessToken);

//			System.out.println("access the resource (licenseer) again, this time sending an access token");
			authRedirection = accessUserID(accessToken);
			if (authRedirection == null) { 	// no authentication required (or already authenticated?)
//				System.out.println("resource available");
//				System.out.println("the logged in resource should be shown");

				if (availabilityHandler != null) {
					availabilityHandler.onResourceAvailable(urlProcessor);
				}

				return true;
			} else {
				System.err.println("OIDC authorization failed");
				return false;
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public OIDCAuthorization setAvailabilityHandler(OIDCResourceAvailabilityHandler availabilityHandler) {
		this.availabilityHandler = availabilityHandler;
		return this;
	}

	public void accessResource(String url, String presetUsername, Frame ownerFrame) {
		try {
			setResourceURI(url);
			String result = urlProcessor.processURL(new URL(getResourceURI()));
			if (result == null) {
				// all good (unlikely)
			} else {
				getDialog(presetUsername, ownerFrame).setVisible(true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Optional: UserInfo request
	/**
	 * Try to access a resource secured by OIDC (currently that means: print user ID ).
	 *
	 * @param accessToken the access token
	 * @return the string
	 * @throws ParseException the parse exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// Alternatively: pass Access Token on to another client, use it to access a resource there
	public String accessUserID(AccessToken accessToken) throws ParseException, IOException {

		if (accessToken != null) {
//			System.out.println(OIDCAuthorization.getInstance().getUserID());

//			getOIDCClient().dumpTokenInfo();
			// only for debugging
//			getOIDCClient().requestUserInfo();
//			System.out.println("UserInfoJSON:");
//			System.out.println(getOIDCClient().getUserInfoJSON() + "");
		}
		return urlProcessor.setAccessToken(accessToken).processURL(getOIDCClient().getRedirectURI().toURL());
	}

	public class URLProcessor {
		private String redirectionURL = null;
		private int responseCode = -1;
		private String content = "";
		private boolean debug = false;
		AccessToken accessToken;
		private HttpsURLConnection connection = null;

		public HttpsURLConnection getConnection() {
			return connection;
		}

		public String getRedirectionURL() {
			return redirectionURL;
		}

		public int getResponseCode() {
			return responseCode;
		}

		public String getContent() {
			return content;
		}

		public URLProcessor setAccessToken(AccessToken accessToken) {
			this.accessToken = accessToken;
			return this;
		}

		/**
		 * Process a URL, that is: try to access it's resource, display error if any, return a redirection URL if indicated by the server.
		 *
		 * @param requestURL the requested URL
		 * @return null if the access succeeded, a redirectionURL as string in case the authorization is not valid yet
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public String processURL(URL requestURL) throws IOException {

//		System.out.println("requestURL=");
//		System.out.println(requestURL);

			content = "";

			HttpURLConnection.setFollowRedirects(false);

			connection = (HttpsURLConnection) requestURL.openConnection();
			Trust.trustSpecific(connection, new File(Application.getGlobalInfo().getPathProperty(true) + Trust.OIDC_TRUST_STORE));

			connection.setRequestMethod("GET");
			if (accessToken != null) {
				connection.setRequestProperty("Authorization", "Bearer " + accessToken);
			}

			connection.connect();

			responseCode = connection.getResponseCode();

			if (responseCode == 200) { //
				/*
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream().re));
				
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					content += inputLine;
				}
				in.close();
				*/
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
				System.out.println(content);
			}
			return redirectionURL;
		}
	}

	/**
	 * The Class OIDCProblemException, indicating a problem in the authorization process.
	 */
	class OIDCProblemException extends RuntimeException {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -6015464771451068526L;

	}
}
