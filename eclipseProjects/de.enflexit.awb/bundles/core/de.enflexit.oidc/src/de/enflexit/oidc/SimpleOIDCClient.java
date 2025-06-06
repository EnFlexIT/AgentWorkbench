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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.RefreshTokenGrant;
import com.nimbusds.oauth2.sdk.ResourceOwnerPasswordCredentialsGrant;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.client.ClientRegistrationErrorResponse;
import com.nimbusds.oauth2.sdk.client.ClientRegistrationResponse;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest.Builder;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.LogoutRequest;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser;
import com.nimbusds.openid.connect.sdk.Prompt;
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse;
import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.nimbusds.openid.connect.sdk.UserInfoSuccessResponse;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformationResponse;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientRegistrationRequest;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientRegistrationResponseParser;

import net.minidev.json.JSONObject;

/**
 * This very simple OpenID Connect (OIDC) Public Client implementation is built after the official cookbook at
 * https://bitbucket.org/connect2id/oauth-2.0-sdk-with-openid-connect-extensions/src/23e6a6c5b751fadd79938859def37ff7a0d05c24/docs/cookbook_client.md
 * The original code was used and altered to create an easy to use native application client for some simple and common use cases.
 * It implements only a small subset of OIDC primitives and is therefore not a feature complete wrapper around the connect2id/nimbusds library.
 * It has been proven to work as a client to keycloak.
 */
public class SimpleOIDCClient {

	private static final String URLPATH_WELL_KNOWN_OPENID = ".well-known/openid-configuration";

	private URI issuerURI;
	private URI authorizationEndpointURI;
	private URI userInfoEndpointURI;
	private URI redirectURI;
	private URI localCallbackURI;

	private OIDCProviderMetadata providerMetadata;
	private OIDCClientInformation clientInformation;
	private OIDCClientMetadata clientMetadata;
	private String clientMetadataJSON;

	private String realmName;
	private ClientID clientID;
	private Secret clientSecret;
	private State state;
	private Nonce nonce;

	private AuthorizationCode authorizationCode;
	private ResourceOwnerPasswordCredentialsGrant resourceOwnerCredentialsGrant;
	
	private AccessToken accessToken;
	private JWT idToken;
	private RefreshToken refreshToken;
	
	private JSONObject userInfoClaims;
	private JWTClaimsSet idClaims;

	private File trustStoreFile;
	
	private boolean debug = false;
	
	/**
	 * Reset.
	 */
	public void reset() {
		this.authorizationCode = null;
		this.accessToken = null;
		this.idToken = null;
		this.refreshToken = null;
		this.userInfoClaims = null;
		this.idClaims = null;
	}

	/**
	 * Issuer discovery
	 * The WebFinger protocol is used to find the OpenID Provider (OP). The library does not have any out-of-the box support for WebFinger, so in the following example we assume you already have acquired the issuer URL of the OP (possibly from developer documentation).
	 */
	public void lookupOpenIDProvider() {
		throw new RuntimeException("WebFinger OpenID Provider discovery is not yet implemented. Set issuer manually by calling setIssuerURI().");
	}

	/**
	 * Sets the issuer URI.
	 *
	 * @param issuerURIString the new issuer URI
	 * @throws URISyntaxException the URI syntax exception
	 */
	public void setIssuerURI(String issuerURIString) throws URISyntaxException {
		issuerURI = new URI(issuerURIString);
	}

	/**
	 * Sets the authorization endpoint URI.
	 *
	 * @param authorizationEndpointURIString the new authorization endpoint URI
	 * @throws URISyntaxException the URI syntax exception
	 */
	/*
	 * If the provider does not support the discovery protocol, set authorizationEndpointURI manually with the authorization endpoint URL received out-of-band.
	 */
	void setAuthorizationEndpointURI(String authorizationEndpointURIString) throws URISyntaxException {
		setAuthorizationEndpointURI(new URI(authorizationEndpointURIString));
	}

	/**
	 * Sets the authorization endpoint URI.
	 *
	 * @param endpointURI the new authorization endpoint URI
	 */
	public void setAuthorizationEndpointURI(URI endpointURI) {
		authorizationEndpointURI = endpointURI;
	}

	/**
	 * Sets the user info endpoint URI.
	 * If the provider does not support the discovery protocol, set userInfoEndpointURI manually with the user info endpoint URL received out-of-band.
	 *
	 * @param userInfoEndpointURI the new user info endpoint URI
	 * @throws URISyntaxException the URI syntax exception
	 */
	void setUserInfoEndpointURI(String userInfoEndpointURI) throws URISyntaxException {
		this.userInfoEndpointURI = new URI(userInfoEndpointURI);
	}

	/**
	 * Sets the provider metadata by String.
	 *
	 * @param providerMetadataString the new provider metadata
	 * @throws ParseException the parse exception
	 */
	void setProviderMetadata(String providerMetadataString) throws ParseException {
		setProviderMetadata(OIDCProviderMetadata.parse(providerMetadataString));
	}

	/**
	 * Sets the provider metadata by object.
	 *
	 * @param providerMetadata the new provider metadata
	 */
	void setProviderMetadata(OIDCProviderMetadata providerMetadata) {
		this.providerMetadata = providerMetadata;
		authorizationEndpointURI = providerMetadata.getAuthorizationEndpointURI();
		userInfoEndpointURI = providerMetadata.getUserInfoEndpointURI();
	}

	/**
	 * Retrieve provider metadata.
	 * Provider configuration information
	 * Obtaining the provider configuration information can be done either out-of-band or using the optional discovery process:
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws KeyStoreException the key store exception
	 */
	public void retrieveProviderMetadata() throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
		
		String urlPartRealm = "";
		
		// --- If a realm was specified, set that part of the URL accordingly
		if (this.getRealmName()!=null) {
			urlPartRealm = "/realms/" + this.getRealmName() + "/";
		}
		
		URL providerConfigurationURL = issuerURI.resolve(urlPartRealm + URLPATH_WELL_KNOWN_OPENID).toURL();
//		System.out.println(providerConfigurationURL);
		URLConnection conn = providerConfigurationURL.openConnection();

		if (trustStoreFile != null) {
			Trust.trustSpecific((HttpsURLConnection) conn, trustStoreFile);
		}
		InputStream stream = conn.getInputStream();
		// Read all data from URL
		String providerInfo = null;
		try (java.util.Scanner s = new java.util.Scanner(stream)) {
			providerInfo = s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
		setProviderMetadata(OIDCProviderMetadata.parse(providerInfo));
	}

	/**
	 * Sets the client ID.
	 * If the provider does not support dynamic client registration, set client ID with the client id received out-of-band.
	 * 
	 * @param clientIDString the new client ID
	 */
	void setClientID(String clientIDString) {
		setClientID(clientIDString, null);
	}

	/**
	 * Sets the client ID.
	 * If the provider does not support dynamic client registration, set client ID and client secret with the client credentials received out-of-band.
	 *
	 * @param clientIDString the client ID string
	 * @param clientSecret the client secret
	 */
	public void setClientID(String clientIDString, String clientSecret) {
		clientID = new ClientID(clientIDString);
		if (this.clientSecret != null && (clientSecret == null || clientSecret.isEmpty())) {
			this.clientSecret = new Secret();
		} else {
			this.clientSecret = new Secret(clientSecret);
		}
		clientInformation = new OIDCClientInformation(clientID, null, clientMetadata, this.clientSecret);
	}
	
	

	/**
	 * Gets the realm name.
	 * @return the realm name
	 */
	public String getRealmName() {
		return realmName;
	}
	/**
	 * Sets the realm name.
	 * @param realmName the new realm name
	 */
	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	/**
	 * Sets the client registration metadata.
	 *
	 * @param allowedClientRedirectURI the new client registration metadata
	 * @throws ParseException the parse exception
	 */
	public void setClientRegistrationMetadata(String allowedClientRedirectURI) throws ParseException {
		clientMetadataJSON = "{\"redirect_uris\": [\"" + allowedClientRedirectURI + "\"],\"response_types\": [\"code\"]}";
		clientMetadata = OIDCClientMetadata.parse(JSONObjectUtils.parse(clientMetadataJSON));
	}

	/**
	 * Sets the client metadata.
	 *
	 * @param allowedClientRedirectURI the new client metadata
	 * @throws ParseException the parse exception
	 */
	public void setClientMetadata(String allowedClientRedirectURI) throws ParseException {
		clientMetadataJSON = "{\"application_type\": \"native\",\"redirect_uris\": [\"" + allowedClientRedirectURI + "\"],\"response_types\": [\"code\"]}";
		clientMetadata = OIDCClientMetadata.parse(JSONObjectUtils.parse(clientMetadataJSON));
	}

	/**
	 * Client registration
	 * If the provider supports dynamic registration, a new client can be registered using the client registration process:.
	 *
	 * @param initialAccessToken the initial access token
	 * @throws SerializeException the serialize exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws KeyStoreException the key store exception
	 */
	public void registerClient(BearerAccessToken initialAccessToken) throws SerializeException, IOException, ParseException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {

		// Make registration request
		OIDCClientRegistrationRequest registrationRequest = new OIDCClientRegistrationRequest(providerMetadata.getRegistrationEndpointURI(), clientMetadata, initialAccessToken);

		// --- Old style ----------
//		HTTPResponse regHTTPResponse = registrationRequest.toHTTPRequest().send(null, Trust.getSocketFactory(trustStoreFile));
		// --- New style ----------
		HTTPRequest request = registrationRequest.toHTTPRequest();
		request.setHostnameVerifier(null);
		request.setSSLSocketFactory(Trust.getSocketFactory(trustStoreFile));
		HTTPResponse regHTTPResponse = request.send();
		
		// Parse and check response
		ClientRegistrationResponse registrationResponse = OIDCClientRegistrationResponseParser.parse(regHTTPResponse);

		if (registrationResponse instanceof ClientRegistrationErrorResponse) {
			ClientRegistrationErrorResponse errorResponse = ((ClientRegistrationErrorResponse) registrationResponse);
			ErrorObject error = errorResponse.getErrorObject();
			System.err.println(this.getClass().getSimpleName() + " - " + errorResponse.indicatesSuccess());
			System.err.println("Dynamic Client Registration failed, Error:");
			System.err.println(errorResponse);
			System.err.println(error);
			System.err.println(regHTTPResponse.getStatusCode());
			System.err.println(regHTTPResponse.getWWWAuthenticate());
			System.err.println(regHTTPResponse.getLocation());
		} else {
			clientInformation = ((OIDCClientInformationResponse) registrationResponse).getOIDCClientInformation();
			clientID = clientInformation.getID();
		}
	}

	/**
	 * Sets the redirect URI.
	 * Make sure redirectURI matches a URI known by the provider.
	 *
	 * @param redirectURIString the new redirect URI
	 * @throws URISyntaxException the URI syntax exception
	 */
	public void setRedirectURI(String redirectURIString) throws URISyntaxException {
		this.redirectURI = new URI(redirectURIString);
	}
	/**
	 * Gets the redirect URI.
	 * 
	 * @return the redirect URI
	 */
	public URI getRedirectURI() {
		return this.redirectURI;
	}

	/**
	 * Sets the local callback URI.
	 * @param localCallbackURI the new local callback URI
	 * @throws URISyntaxException the URI syntax exception
	 */
	public void setLocalCallbackURI(String localCallbackURI) throws URISyntaxException {
		this.localCallbackURI = new URI(localCallbackURI);
	}
	/**
	 * Gets the local callback URI.
	 * @return the local callback URI
	 */
	public URI getLocalCallbackURI() {
		return localCallbackURI;
	}

	/**
	 * Gets the state.
	 * 
	 * @return the state
	 */
	public State getState() {
		return this.state;
	}

	/**
	 * Parses the authentication data from redirect.
	 *
	 * @param redirectionURL the redirection URL
	 * @param overrideClient the override client
	 * @throws ParseException the parse exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public void parseAuthenticationDataFromRedirect(String redirectionURL, boolean overrideClient) throws ParseException, URISyntaxException {
		AuthenticationRequest authenticationRequest = AuthenticationRequest.parse(new URI(redirectionURL));
		setAuthorizationEndpointURI(authenticationRequest.getEndpointURI());
		if (overrideClient) {
			clientID = authenticationRequest.getClientID();
		}
		state = authenticationRequest.getState();
		redirectURI = authenticationRequest.getRedirectionURI();
	}

	/**
	 * Authentication request
	 * The authentication request is done by redirecting the end user to the provider, for more details see the OIDC specification. The response is
	 * handled by the specified local callback URL, where a local HTTP server must be listening to process it. If showLoginPage==true, the login page
	 * will be shown if the user is not logged in already. Otherwise the redirect will occur immediately, leading to a {@link AuthenticationErrorResponse}
	 * if not logged in. 
	 *
	 * @param localCallbackURL the local callback URL
	 * @param showLoginForm whether to show the login page if not logged in - set to false if you only want to check for an existing login
	 * @return the authorization code request URI
	 * @throws SerializeException the serialize exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public URI buildAuthorizationCodeRequest(String localCallbackURL, boolean showLoginForm) throws SerializeException, URISyntaxException {
		
		if (state==null) state = new State();
		if (nonce==null) nonce = new Nonce();
		
		AuthenticationRequest authenticationRequest;
		// Specify scope
		Scope scope = Scope.parse("openid");

		
		Builder builder = new AuthenticationRequest.Builder(new ResponseType(ResponseType.Value.CODE), scope, clientID, new URI(localCallbackURL));
		builder.endpointURI(authorizationEndpointURI);
		builder.state(state);
		builder.nonce(nonce);
		
		if (showLoginForm==false) {
			builder.prompt(new Prompt(Prompt.Type.NONE));
		}
		
		authenticationRequest = builder.build();

		URI authReqURI = authenticationRequest.toURI();

		return authReqURI;
	}

	/**
	 * Receive the Authentication Response
	 * The authentication response is sent from the provider by redirecting the end user to the redirect URI specified in the initial authentication request from the client.
	 * Code flow
	 * The Authorization Code can be extracted from the query part of the redirect URL:
	 *
	 * @param responseURL the response URL
	 */
	public void processAuthenticationResponse(String responseURL) {
		
		AuthenticationResponse authResp = null;
		try {
			authResp = AuthenticationResponseParser.parse(new URI(responseURL));
		} catch (ParseException | URISyntaxException e) {
			e.printStackTrace();

		}

		if (authResp instanceof AuthenticationErrorResponse) {
			AuthenticationErrorResponse errorResponse = (AuthenticationErrorResponse) authResp;
			ErrorObject error = errorResponse.getErrorObject();
			System.err.println(this.getClass().getSimpleName() + " - Authentication failed! Error:");
			System.err.println(errorResponse.indicatesSuccess());
			System.err.println(errorResponse);
			System.err.println(error);
		} else {
			/* Don't forget to check the state!
			 * The state in the received authentication response must match the state
			 * specified in the previous outgoing authentication request.
			 */
			AuthenticationSuccessResponse successResponse = (AuthenticationSuccessResponse) authResp;
			if (!this.verifyState(successResponse.getState())) {
				System.err.println(this.getClass().getSimpleName() + " - " + "State not valid");
				return;
			}
			authorizationCode = successResponse.getAuthorizationCode();
			System.out.println("Authorization Code: " + authorizationCode);
			
		}
	}

	/**
	 * Verify the state.
	 *
	 * @param state the state
	 * @return true, if successful
	 */
	private boolean verifyState(State state) {
		if (state != null && this.state != null && this.state.equals(state)) {
			return true;
		}
		System.err.println(this.getClass().getSimpleName() + " - this.state=" + this.state);
		System.err.println("     state=" + state);
		return false;
	}

	/**
	 * Sets the resource owner credentials.
	 *
	 * @param user the user
	 * @param password the password
	 */
	public void setResourceOwnerCredentials(String user, String password) {
		resourceOwnerCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(user, new Secret(password));
	}
	
	/**
	 * Sets the authorization code.
	 * @param authorizationCode the new authorization code
	 */
	public void setAuthorizationCode(AuthorizationCode authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	/**
	 * Token Request
	 * When an authorization code (using code or hybrid flow) has been obtained, a token request can made to get the access token and the id token:.
	 *
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws KeyStoreException the key store exception
	 * @throws URISyntaxException 
	 */
	public boolean requestToken() {

		AuthorizationGrant grant;
		if (authorizationCode == null) {
			if (resourceOwnerCredentialsGrant == null) {
				System.err.println(this.getClass().getSimpleName() + " - Authentication Code is null and no user/password set, stopping token retrieval");
				return false;
			} else {
				grant = resourceOwnerCredentialsGrant;
			}
		} else {
			grant = new AuthorizationCodeGrant(authorizationCode, getLocalCallbackURI());
		}
		
		return this.requestToken(grant);
	}
	
	public boolean refreshTokens() {
		if (this.refreshToken!=null) {
			RefreshTokenGrant grant = new RefreshTokenGrant(this.refreshToken);
			return this.requestToken(grant);
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Refresh token not set!");
			return false;
		}
	}
		
		
	private boolean requestToken(AuthorizationGrant grant)  {
		
		TokenRequest tokenReq = new TokenRequest(providerMetadata.getTokenEndpointURI(), new ClientSecretBasic(clientID, clientInformation.getSecret()), grant, Scope.parse("openid"));
		HTTPResponse tokenHTTPResp = null;
		try {
			// --- Old style ----------
//			tokenHTTPResp = tokenReq.toHTTPRequest().send(null, Trust.getSocketFactory(trustStoreFile));
			// --- New style ----------
			HTTPRequest request = tokenReq.toHTTPRequest();
			request.setHostnameVerifier(null);
			try {
				request.setSSLSocketFactory(Trust.getSocketFactory(trustStoreFile));
			} catch (KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error establishing a trusted connection!");
				e.printStackTrace();
			}
			tokenHTTPResp = request.send();
			
		} catch (SerializeException | IOException e) {
			e.printStackTrace();
		}

		// Parse and check response
		TokenResponse tokenResponse = null;
		try {
			tokenResponse = OIDCTokenResponseParser.parse(tokenHTTPResp);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (tokenResponse instanceof TokenErrorResponse) {
			ErrorObject error = ((TokenErrorResponse) tokenResponse).getErrorObject();
			if (this.debug==true) {
				System.err.println(this.getClass().getSimpleName() + " - Token retrieval failed!");
				System.err.println("Error: " + error.getCode());
				System.err.println("Description: " + error.getDescription());
			}
			return false;
		}

		OIDCTokenResponse accessTokenResponse = (OIDCTokenResponse) tokenResponse;
		accessToken = accessTokenResponse.getOIDCTokens().getAccessToken();
		idToken = accessTokenResponse.getOIDCTokens().getIDToken();
		refreshToken = accessTokenResponse.getTokens().getRefreshToken();
		
		return true;
	}

	/**
	 * Gets the access token.
	 * @return the access token
	 */
	public AccessToken getAccessToken() {
		return accessToken;
	}
	/**
	 * Sets the access token.
	 * @param accessToken the new access token
	 */
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * Gets the ID token.
	 * @return the ID token
	 */
	public JWT getIdToken() {
		return idToken;
	}
	/**
	 * Sets the id token.
	 * @param idToken the new id token
	 */
	public void setIdToken(JWT idToken) {
		this.idToken = idToken;
	}
	/**
	 * Gets the refresh token.
	 * @return the refresh token
	 */
	public RefreshToken getRefreshToken() {
		return refreshToken;
	}
	/**
	 * Sets the refresh token.
	 * @param refreshToken the new refresh token
	 */
	public void setRefreshToken(RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * Dump token info.
	 *
	 * @throws com.nimbusds.oauth2.sdk.ParseException the nimbusds parse exception
	 * @throws java.text.ParseException the java text parse exception
	 */
	public void dumpTokenInfo() throws ParseException, java.text.ParseException {
		System.out.println("Access Token:");
		System.out.println(accessToken);

		System.out.println("ID Token:");
		System.out.println(idToken);

		if (idToken != null) {
			idClaims = verifyIdToken();
			Map<String, Object> allClaims = idClaims.getClaims();
			for (Iterator<String> iterator = allClaims.keySet().iterator(); iterator.hasNext();) {
				String claimKey = (String) iterator.next();
				System.out.println(claimKey + ":" + allClaims.get(claimKey));
			}
		}
	}

	/**
	 * UserInfo Request
	 * Using the access token, information about the end user can be obtained by making a user info request.
	 *
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws KeyStoreException the key store exception
	 * @throws ParseException the parse exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void requestUserInfo() throws KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException, ParseException, FileNotFoundException, IOException {
		if (accessToken == null) {
			System.err.println(this.getClass().getSimpleName() + " - Access Token null, stopping UserInfo retrieval");
			return;
		}

		UserInfoRequest userInfoReq = new UserInfoRequest(
				userInfoEndpointURI,
				(BearerAccessToken) accessToken);

		// --- Old style ----------
//		HTTPResponse userInfoHTTPResp = userInfoReq.toHTTPRequest().send(null, Trust.getSocketFactory(trustStoreFile));
		// --- New style ----------
		HTTPRequest request = userInfoReq.toHTTPRequest();
		request.setHostnameVerifier(null);
		request.setSSLSocketFactory(Trust.getSocketFactory(trustStoreFile));
		HTTPResponse userInfoHTTPResp = request.send();
		
		
		UserInfoResponse userInfoResponse = null;
		userInfoResponse = UserInfoResponse.parse(userInfoHTTPResp);

		if (userInfoResponse instanceof UserInfoErrorResponse) {
			UserInfoErrorResponse errorResponse = ((UserInfoErrorResponse) userInfoResponse);
			ErrorObject error = errorResponse.getErrorObject();

			System.err.println(this.getClass().getSimpleName() + " - " + errorResponse.indicatesSuccess());
			System.err.println("Userinfo retrieval failed:");
			System.err.println(errorResponse);
			System.err.println(error);
			System.err.println(error.getHTTPStatusCode());
			System.err.println(userInfoHTTPResp.getStatusCode());
			System.err.println(userInfoHTTPResp.getWWWAuthenticate());
			System.err.println(userInfoHTTPResp.getLocation());
		}

		UserInfoSuccessResponse successResponse = (UserInfoSuccessResponse) userInfoResponse;
		userInfoClaims = successResponse.getUserInfo().toJSONObject();
	}

	/**
	 * Validate the ID token
	 * The id token obtained from the token request must be validated, see ID token validation
	 * *
	 * 
	 * @return the JWT claims set
	 * @throws ParseException the parse exception
	 * @throws java.text.ParseException
	 */
	private JWTClaimsSet verifyIdToken() throws ParseException, java.text.ParseException {
		JWTClaimsSet claims = null;
		claims = idToken.getJWTClaimsSet();

		return claims;
	}

	/**
	 * Gets the id claims.
	 * @return the id claims
	 */
	public JWTClaimsSet getIdClaims() {
		try {
			return verifyIdToken();
		} catch (ParseException | java.text.ParseException e) {
			return new JWTClaimsSet.Builder().build(); // return empty claims
		}
	}

	/**
	 * Gets the user info JSON.
	 * @return the user info JSON
	 */
	public JSONObject getUserInfoJSON() {
		return userInfoClaims;
	}
	
	public boolean sendLogoutRequest() throws KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
		LogoutRequest logoutRequest = new LogoutRequest(this.providerMetadata.getEndSessionEndpointURI(), this.idToken);
		HTTPResponse httpResponse = null;
		
		try {
			HTTPRequest httpRequest = logoutRequest.toHTTPRequest();
			httpRequest.setHostnameVerifier(null);
			httpRequest.setSSLSocketFactory(Trust.getSocketFactory(trustStoreFile));
			httpResponse = httpRequest.send();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (httpResponse!=null) {
			// --- Logout successful - clear tokens etc. -----------------------
			this.reset();
			return true;
		} else {
			System.err.println("[" + this.getClass().getSimpleName() +"] No logout response received!");
			return false;
		}
	}

	/**
	 * Sets the trust store.
	 * @param trustStoreFile the new trust store
	 */
	public void setTrustStore(File trustStoreFile) {
		this.trustStoreFile = trustStoreFile;
		//Trust.trustSpecific(null, trustStoreFile);
	}

}