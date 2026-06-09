package de.enflexit.awb.ws.core.security;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import de.enflexit.awb.ws.core.security.OIDCSecurityService.OIDCParameter;

/**
 * The Class OIDCTokenRefreshment.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OIDCTokenRefreshment implements AccessTokenRefreshment {

    public static final String ACCESS_TOKEN  = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    
    private HashMap<String, String> configuration;
    
    private String tokenEndpoint;
    private String refreshToken;
    private String clientId;
    private String clientSecret;

    private TokenResponse tokenResponse;
    
    
    /* (non-Javadoc)
     * @see de.enflexit.awb.ws.core.security.AccessTokenRefreshment#setConfiguration(java.util.HashMap)
     */
    @Override
    public void setConfiguration(HashMap<String, String> config) {
    	this.configuration = config;
    	this.tokenEndpoint = this.configuration.get(OIDCParameter.TokenEndpoint.getKey());
    	this.refreshToken  = this.configuration.get(OIDCTokenRefreshment.REFRESH_TOKEN);
    	this.clientId      = this.configuration.get(OIDCParameter.ClientID.getKey());
    	this.clientSecret  = this.configuration.get(OIDCParameter.ClientSecrete.getKey());
    }
   
    /* (non-Javadoc)
     * @see de.enflexit.awb.ws.core.security.AccessTokenRefreshment#refreshToken()
     */
    @Override
    public void refreshToken() {
    	
    	if (this.tokenEndpoint==null || this.refreshToken==null || this.clientId==null || this.clientSecret==null) return;
    	
    	try {
    		TokenResponse tr = this.callTokenEndpoint();
    		this.setTokenResponse(tr);
    		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
    
    /* (non-Javadoc)
     * @see de.enflexit.awb.ws.core.security.AccessTokenRefreshment#getTokenResponse()
     */
    @Override
    public TokenResponse getTokenResponse() {
		return tokenResponse;
	}
    /**
     * Sets the token response.
     * @param tokenResponse the new token response
     */
    private void setTokenResponse(TokenResponse tokenResponse) {
		this.tokenResponse = tokenResponse;
	}
    
    /**
     * Call token end point.
     *
     * @return the token response
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws URISyntaxException 
     */
    private TokenResponse callTokenEndpoint() throws IOException, URISyntaxException {
        
    	// --- Build POST-Body ----------------------------
        String body = "grant_type=refresh_token"
            + "&refresh_token=" + URLEncoder.encode(this.refreshToken, StandardCharsets.UTF_8)
            + "&client_id="     + URLEncoder.encode(this.clientId,     StandardCharsets.UTF_8)
            + "&client_secret=" + URLEncoder.encode(this.clientSecret, StandardCharsets.UTF_8);
        
        HttpURLConnection conn = (HttpURLConnection) new URI(this.tokenEndpoint).toURL().openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setConnectTimeout(5_000);
        conn.setReadTimeout(10_000);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        if (conn.getResponseCode() != 200) {
            throw new IOException("Token endpoint returned HTTP " + conn.getResponseCode());
        }

        String json = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return parseTokenResponse(json);
    }
    /**
     * Parses the token response.
     *
     * @param json the json
     * @return the token response
     */
    private TokenResponse parseTokenResponse(String json) {
        TokenResponse tr = new TokenResponse();
        tr.setAccessToken(extractJsonString(json, "access_token"));
        tr.setRefreshToken(extractJsonString(json, "refresh_token")); // nullable
        String expiresInStr = extractJsonString(json, "expires_in");
        tr.setExpiresIn(expiresInStr != null ? Long.parseLong(expiresInStr) : 300L);
        return tr;
    }

    /**
     * Extract JSON string.
     *
     * @param json the json
     * @param key the key
     * @return the string
     */
    private String extractJsonString(String json, String key) {
        
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx < 0) return null;
        
        int colon = json.indexOf(':', idx + search.length());
        if (colon < 0) return null;
        
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
        if (json.charAt(start) == '"') {
            int end = json.indexOf('"', start + 1);
            return json.substring(start + 1, end);
        } else {
            int end = start;
            while (end < json.length() && ",}]".indexOf(json.charAt(end)) < 0) end++;
            return json.substring(start, end).trim();
        }
    }

}