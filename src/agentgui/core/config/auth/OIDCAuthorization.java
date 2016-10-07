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
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * The Class OIDCAuthorization.
 */
public class OIDCAuthorization {

	private static final String OIDC_ID_CLAIM_USERID = "sub";

	/** The instance. */
	private static OIDCAuthorization instance;

	private SimpleOIDCClient oidcClient;

	private OIDCPanel oidcPanel;

	/**
	 * Instantiates a new OIDC authorization.
	 */
	private OIDCAuthorization() {
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

	/**
	 * Gets the dialog.
	 *
	 * @param presetUsername the preset username
	 * @return the dialog
	 */
	public JDialog getDialog(String presetUsername, Frame ownerFrame) {
		JDialog authDialog = new JDialog(ownerFrame);
		authDialog.setContentPane(getPanel());
		if (presetUsername != null) {
			getPanel().getTfUsername().setText(presetUsername);
		}
		authDialog.setSize(new Dimension(500, 150));
		authDialog.setLocationRelativeTo(null);
		return authDialog;
	}

	public OIDCPanel getPanel(ActionListener parentGUI) {
		if (oidcPanel == null) {
			oidcPanel = getPanel().setParent(parentGUI);
		}
		return oidcPanel;
	}

	public OIDCPanel getPanel() {
		if (oidcPanel == null) {
			oidcPanel = new OIDCPanel(this);
		}
		return oidcPanel;
	}

	/**
	 * Checks if is valid.
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
	 * Gets the user ID.
	 *
	 * @return the user ID
	 */
	public String getUserID() {
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

	public SimpleOIDCClient getOIDCClient() {
		if (oidcClient == null) {
			oidcClient = new SimpleOIDCClient();
		}
		return oidcClient;
	}

	private String getIssuerURI() {
//		return getTfIdProvider().getText();
		return OIDCPanel.DEBUG_ISSUER_URI;
	}

	private String getResourceURI() {
//		return getTfLicenseServer().getText();
		return OIDCPanel.DEBUG_RESOURCE_URI;
	}

	private String getClientId() {
//		return getTfClientId().getText();
		return OIDCPanel.DEBUG_CLIENT_ID;
	}

	private String getClientSecret() {
//		return getTfClientSecret().getText();
		return OIDCPanel.DEBUG_CLIENT_SECRET;
	}

	public boolean connect(String username, String password) {

		String authRedirection = "";
		AccessToken accessToken = null;

		try {
			getOIDCClient();
			oidcClient.reset();
			oidcClient.setTrustStore(new File(Application.getGlobalInfo().getPathProperty(false) + Trust.OIDC_TRUST_STORE));

			oidcClient.setIssuerURI(getIssuerURI());
			oidcClient.retrieveProviderMetadata();
			oidcClient.setClientMetadata(getResourceURI());
			oidcClient.setClientID(getClientId(), getClientSecret());
			oidcClient.setRedirectURI(getResourceURI());

//			System.out.println("try a direct access to the resource (EOM licenseer)");
			authRedirection = doResourceAccess(accessToken);

//			if (authRedirection == null) { 	// no authentication required (or already authenticated?)
//				System.out.println("resource available");
//				return true;
//			}

//			System.out.println("authentication redirection necessary");

//		System.out.println("parse authentication parameters from redirection");
			oidcClient.parseAuthenticationDataFromRedirect(authRedirection, false); // don't override clientID
//		System.out.println("set USER credentials");
			oidcClient.setResourceOwnerCredentials(getPanel().getTfUsername().getText(), getPanel().getTfPassword().getText());

			oidcClient.requestToken();
			accessToken = oidcClient.getAccessToken();

//			System.out.println("access the resource (licenseer) again, this time sending an access token");
			authRedirection = doResourceAccess(accessToken);
			if (authRedirection == null) { 	// no authentication required (or already authenticated?)
//				System.out.println("resource available");
//				System.out.println("the logged in resource should be shown");
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

	// Optional: UserInfo request
	// Alternatively: pass Access Token on to another client, use it to access a resource there
	public String doResourceAccess(AccessToken accessToken) throws ParseException, IOException {
		if (accessToken != null) {

			System.out.println(OIDCAuthorization.getInstance().getUserID());

//			getOIDCClient().dumpTokenInfo();
			// only for debugging
//			getOIDCClient().requestUserInfo();
//			System.out.println("UserInfoJSON:");
//			System.out.println(getOIDCClient().getUserInfoJSON() + "");
		}
		return processURL(getOIDCClient().getRedirectURI().toURL(), accessToken);
	}

	public String processURL(URL requestURL, AccessToken accessToken) throws IOException {
		String redirectionURL = "";

//		System.out.println("requestURL=");
//		System.out.println(requestURL);

		HttpURLConnection.setFollowRedirects(false);

		HttpsURLConnection conn = (HttpsURLConnection) requestURL.openConnection();
		Trust.trustSpecific(conn, new File(Application.getGlobalInfo().getPathProperty(false) + Trust.OIDC_TRUST_STORE));

		conn.setRequestMethod("GET");
		if (accessToken != null) {
			conn.setRequestProperty("Authorization", "bearer " + accessToken);
		}

		conn.connect();

		int responseCode = conn.getResponseCode();

		if (responseCode == 302) {
			redirectionURL = conn.getHeaderField("Location");
//			System.out.println("redirection to:");
//			System.out.println(redirectionURL);
			return redirectionURL;
		} else if (responseCode == 400) {
			System.err.println("400: General Error");
			return null;
		} else if (responseCode == 500) {
			System.err.println("500");
			return null;
		} else if (responseCode == 200) { //
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
/*
			String inputLine;
 			while ((inputLine = in.readLine()) != null) {
//				System.out.println(inputLine);
			}
*/
			in.close();
			return null;
		} else {
			System.out.println("responseCode =" + responseCode);
			return null;
		}
	}

	class OIDCProblemException extends RuntimeException {

		private static final long serialVersionUID = -6015464771451068526L;

	}
}
