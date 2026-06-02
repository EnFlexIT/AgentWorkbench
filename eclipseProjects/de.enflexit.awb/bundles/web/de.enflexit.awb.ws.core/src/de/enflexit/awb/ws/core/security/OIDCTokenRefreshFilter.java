package de.enflexit.awb.ws.core.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import com.nimbusds.jwt.SignedJWT;

/**
 * Jetty-Filter, der den OIDC Access Token transparent
 * per refresh_token erneuert, bevor er abläuft.
 */
public class OIDCTokenRefreshFilter implements Filter {

    private static final String SESSION_RESPONSE = "org.eclipse.jetty.security.openid.response";
    private static final String SESSION_EXPIRES_AT = "oidc.token.expires_at";

    private static final long THRESHOLD_SEC = 60;

    private String tokenEndpoint;
    private String clientId;
    private String clientSecret;

    
    /* (non-Javadoc)
     * @see jakarta.servlet.Filter#init(jakarta.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig cfg) {
        this.tokenEndpoint = cfg.getInitParameter("tokenEndpoint");
        this.clientId      = cfg.getInitParameter("clientId");
        this.clientSecret  = cfg.getInitParameter("clientSecret");
    }

    /* (non-Javadoc)
     * @see jakarta.servlet.Filter#doFilter(jakarta.servlet.ServletRequest, jakarta.servlet.ServletResponse, jakarta.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpSession session = httpReq.getSession(false);

        if (session != null) {
            this.tryRefresh(session);
        }
        chain.doFilter(req, res);
    }

    /**
     * Try refresh.
     * @param session the session
     */
	private void tryRefresh(HttpSession session) {

		try {
			// --- 1. Get Jetty's OIDC-Response from Session  ---------------------------
			@SuppressWarnings("unchecked")
			Map<String, Object> oidcResponse = (Map<String, Object>) session.getAttribute(SESSION_RESPONSE);
			if (oidcResponse == null) return;

			
			// --- Get expiration time from JWT token -----------------------------------
//			String accessToken = (String)  oidcResponse.get("Access_token");
//			SignedJWT jwtCheck = SignedJWT.parse(accessToken);
//			Date expirationDateTime = jwtCheck.getJWTClaimsSet().getExpirationTime();
//			long expiration = expirationDateTime.getTime();
			
			
			String refreshToken = (String) oidcResponse.get("refresh_token");
			if (refreshToken == null) return;

			// --- 2. Check expiration time (at first calculate and remind) -------------
			Long expiresAt = (Long) session.getAttribute(SESSION_EXPIRES_AT);
			if (expiresAt == null) {
				// --- Read 'expires'from OIDC-Response ---------------------------------
				Number expiresIn = (Number) oidcResponse.get("expires_in");
				if (expiresIn == null) return;
				expiresAt = (System.currentTimeMillis() / 1000) + expiresIn.longValue();
				session.setAttribute(SESSION_EXPIRES_AT, expiresAt);
			}

			long now = System.currentTimeMillis() / 1000;
			if (now <= expiresAt - THRESHOLD_SEC) return; // --- enough time ------------ 

			// --- 3. Call token end point ----------------------------------------------
			TokenResponse tr = this.callTokenEndpoint(refreshToken);

			// --- 4. Refresh Session ---------------------------------------------------
			oidcResponse.put("access_token", tr.accessToken);
			if (tr.refreshToken != null) {
				oidcResponse.put("refresh_token", tr.refreshToken); // rotating tokens
			}
			session.setAttribute(SESSION_RESPONSE, oidcResponse);
			session.setAttribute(SESSION_EXPIRES_AT, (System.currentTimeMillis() / 1000) + tr.expiresIn);
			
		} catch (Exception e) {
			System.err.println("[OidcRefresh] Token refresh failed: " + e.getMessage());
		}
	}

    /**
     * Call token end point.
     *
     * @param refreshToken the refresh token
     * @return the token response
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws URISyntaxException 
     */
    private TokenResponse callTokenEndpoint(String refreshToken) throws IOException, URISyntaxException {
        
    	// --- Build POST-Body ----------------------------
        String body = "grant_type=refresh_token"
            + "&refresh_token=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8)
            + "&client_id="     + URLEncoder.encode(clientId,     StandardCharsets.UTF_8)
            + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8);
        
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
        tr.accessToken  = extractJsonString(json, "access_token");
        tr.refreshToken = extractJsonString(json, "refresh_token"); // nullable
        String expiresInStr = extractJsonString(json, "expires_in");
        tr.expiresIn = expiresInStr != null ? Long.parseLong(expiresInStr) : 300L;
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

    /**
     * The Class TokenResponse.
     * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
     */
    private static class TokenResponse {
        String accessToken;
        String refreshToken;
        long expiresIn;
    }
}