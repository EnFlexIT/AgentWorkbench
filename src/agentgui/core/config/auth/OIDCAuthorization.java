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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
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
	private String issuerURI = OIDCPanel.DEBUG_ISSUER_URI;
	private OIDCResourceAvailabilityHandler availabilityHandler;
	private URLProcessor urlProcessor;
	private JDialog authDialog;

	/**
	 * Instantiates a new OIDC authorization.
	 */
	private OIDCAuthorization() {
		urlProcessor = new URLProcessor();
	}
	
	public URLProcessor getUrlProcessor(){
		return urlProcessor;
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
	public String getIssuerURI() {
		return issuerURI;
	}

	/**
	 * Sets the issuer URI.
	 */
	public void setIssuerURI(String issuerURI) {
		this.issuerURI = issuerURI;
	}
	
	/**
	 * Sets the resource URI.
	 */
	public void setResourceURI(String resourceURI) {
		this.resourceURI = resourceURI;
	}

	/**
	 * Gets the resource URI.
	 * 
	 * @return the resource URI
	 */
	public String getResourceURI() {

		return resourceURI;
	}

	/**
	 * Gets the client id.
	 * 
	 * @return the client id
	 */
	public String getClientId() {
		return OIDCPanel.DEBUG_CLIENT_ID;
	}

	/**
	 * Gets the client secret.
	 * 
	 * @return the client secret
	 */
	public String getClientSecret() {
		return OIDCPanel.DEBUG_CLIENT_SECRET;
	}
	
	public void setTrustStore(File truststoreFile){
		getOIDCClient();
		oidcClient.setTrustStore(truststoreFile);
	}
	
	public void init(){
		getOIDCClient();
		oidcClient.reset();
		urlProcessor = new URLProcessor();

		try {
			oidcClient.setIssuerURI(getIssuerURI());
			oidcClient.retrieveProviderMetadata();
			oidcClient.setClientMetadata(getResourceURI());
			oidcClient.setClientID(getClientId(), getClientSecret());
			oidcClient.setRedirectURI(getResourceURI());
			urlProcessor.prepare(oidcClient.getRedirectURI().toURL());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
//			System.out.println("try a direct access to the resource");
			authRedirection = urlProcessor.process();

			if (authRedirection == null) { 	// no authentication required
				System.out.println("resource available");
				if (availabilityHandler != null) {
					availabilityHandler.onResourceAvailable(urlProcessor);
				}
				return true;
			}

//			System.out.println("authentication redirection necessary");

//			System.out.println("parse authentication parameters from redirection");
			oidcClient.parseAuthenticationDataFromRedirect(authRedirection, false); // don't override clientID
//			System.out.println("set USER credentials");
			oidcClient.setResourceOwnerCredentials(username, password);

			oidcClient.requestToken();
			accessToken = oidcClient.getAccessToken();

//			System.out.println("This is the access token");
//			System.out.println(accessToken);
			urlProcessor.setAccessToken(accessToken);

//			System.out.println("access the resource again, this time sending an access token");
			authRedirection = urlProcessor.prepare(oidcClient.getRedirectURI().toURL()).process();
			if (authRedirection == null) { 	// authenticated
//				System.out.println("resource available now");

				if (availabilityHandler != null) {
					availabilityHandler.onResourceAvailable(urlProcessor);
				}
				return true;
			} else {
				System.err.println("OIDC authorization failed");
				System.err.println("authRedirection="+authRedirection);
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
			String result = urlProcessor.prepare(new URL(getResourceURI())).process();
			if (result == null) {
				// all good (unlikely on first call)
			} else {
				getDialog(presetUsername, ownerFrame).setVisible(true);
			}
		} catch (IOException e) {
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
		return urlProcessor.prepare(getOIDCClient().getRedirectURI().toURL()).process();
	}

	public class URLProcessor {
		
		private static final String CHARSET_UTF_8 = "UTF-8";
		private static final String CRLF = "\r\n"; // Line separator required by multipart/form-data.
		private static final String CONTENT_DISPOSITION_NAME = "file";
		private static final String HTTP_METHOD_POST = "POST";

		private boolean debug = false;

		private File uploadFile;
		
		private HttpsURLConnection connection = null;
		private AccessToken accessToken;
		
		private String redirectionURL = null;
		private int responseCode = -1;
		
		public void setUploadFile(File uploadFile){
			this.uploadFile = uploadFile;
			injectUpload();
		}

		public HttpsURLConnection getConnection() {
			return connection;
		}

		public String getRedirectionURL() {
			return redirectionURL;
		}

		public int getResponseCode() {
			return responseCode;
		}

		public URLProcessor setAccessToken(AccessToken accessToken) {
			this.accessToken = accessToken;
			return this;
		}
		
		public URLProcessor prepare(URL requestURL) throws IOException{
//			System.out.println("requestURL=");
//			System.out.println(requestURL);
			
			connection = (HttpsURLConnection) requestURL.openConnection();
			connection.setInstanceFollowRedirects(false);
			Trust.trustSpecific(connection, new File(Application.getGlobalInfo().getPathProperty(true) + Trust.OIDC_TRUST_STORE));

			connection.setRequestMethod("GET");
			if (accessToken != null) {
				connection.setRequestProperty("Authorization", "Bearer " + accessToken);
			}
			
			if(uploadFile != null){
				injectUpload();
			}
			
			return this;
		}

		/**
		 * Process a URL, that is: try to access it's resource, display error if any, return a redirection URL if indicated by the server.
		 *
		 * @param requestURL the requested URL
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
		
		public void injectUpload() {
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
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
