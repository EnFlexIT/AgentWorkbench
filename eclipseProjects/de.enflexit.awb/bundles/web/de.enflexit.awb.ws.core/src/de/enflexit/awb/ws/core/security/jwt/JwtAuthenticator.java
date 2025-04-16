package de.enflexit.awb.ws.core.security.jwt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.security.AuthenticationState;
import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.security.UserIdentity;
import org.eclipse.jetty.security.authentication.LoginAuthenticator;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.awb.ws.core.security.jwt.JwtHandler.JwtParsed;
import de.enflexit.awb.ws.core.util.ServletHelper;

/**
 * The Class JwtAuthenticator
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtAuthenticator extends LoginAuthenticator {
	
	public static final String JWT_SECRET = "JWT_SECRET";
	public static final String JWT_ISSUER = "JWT_ISSUER";
	public static final String JWT_VALIDITY_PERIOD = "JWT_VALIDITY_PERIOD";
	public static final String JWT_RENEW_EACH_CALL = "JWT_RENEW_EACH_CALL";
	
	public static final String JWT_VERBOSE = "JWT_VERBOSE";

	protected static final String JWT_CLAIM_USER = "user";
	protected static final String JWT_CLAIM_ROLE = "role";
	private static final String DEFAULT_JWT_CLAIM_ROLE = "LocalUser";
	private static final String DEFAULT_JWT_CLAIM_SUBJECT = "AWB-WebServer";
	
	/** Name of authentication method provided by this authenticator. */
	private static final String AUTH_METHOD = "Bearer";
	private static final String AUTH_HEADER_VALUE_PREFIX = AUTH_METHOD + " ";

	/** Logger instance. */
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticator.class);
    /** hack to allow env variable based logging changes **/
    public Boolean verbose = true;

	/** who issued token **/
	private String issuer;
	/** issuer secret **/
	private String secret;
	private int validityPeriod;
	private boolean renewWithEachCall;
	
	private Charset charset;

	/** JWT token parser component. */
	private JwtHandler jwtHandler;

	
	/**
	 * Instantiates a new jwt authenticator.
	 * @param configMap the map with configuration options
	 */
	public JwtAuthenticator(Map<String, String> configMap) {
		this.applyConfiguration(configMap);
		this.getJwtHandler();
		this.getJwtSessionStore();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.Authenticator#getAuthenticationType()
	 */
	@Override
	public String getAuthenticationType() {
		return AUTH_METHOD;
	}
	
	/**
	 * Applies the configuration based on the specified map.
	 * @param configMap the configuration map to apply
	 */
	private void applyConfiguration(Map<String, String> configMap) {
		
		if (configMap==null || configMap.size()==0) return;
		
		if (configMap.containsKey(JWT_SECRET) && configMap.get(JWT_SECRET)!=null && configMap.get(JWT_SECRET).equals("")==false) {
			this.setSecret(configMap.get(JWT_SECRET));
		}
		if (configMap.containsKey(JWT_ISSUER) && configMap.get(JWT_ISSUER)!=null && configMap.get(JWT_ISSUER).equals("")==false) {
			this.setIssuer(configMap.get(JWT_ISSUER));
		}
		
		if (configMap.containsKey(JWT_VALIDITY_PERIOD) && configMap.get(JWT_VALIDITY_PERIOD)!=null && configMap.get(JWT_VALIDITY_PERIOD).equals("")==false) {
			Integer validityPeriod = Integer.valueOf(configMap.get(JWT_VALIDITY_PERIOD));
			this.setValidityPeriod(validityPeriod);
		}
		if (configMap.containsKey(JWT_RENEW_EACH_CALL) && configMap.get(JWT_RENEW_EACH_CALL)!=null && configMap.get(JWT_RENEW_EACH_CALL).equals("")==false) {
			Boolean isRenew = Boolean.valueOf(configMap.get(JWT_RENEW_EACH_CALL));
			this.setRenewWithEachCall(isRenew);
		}
		

		if (configMap.containsKey(JWT_VERBOSE) && configMap.get(JWT_VERBOSE)!=null && configMap.get(JWT_VERBOSE).equals("")==false) {
			verbose = Boolean.valueOf(configMap.get(JWT_VERBOSE));
			logger.info("verbose jwt logging enabled");
		}
	}

	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}

	public int getValidityPeriod() {
		return validityPeriod;
	}
	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	
	public boolean isRenewWithEachCall() {
		return renewWithEachCall;
	}
	public void setRenewWithEachCall(boolean renewWithEachCall) {
		this.renewWithEachCall = renewWithEachCall;
	}
	
	
	public Charset getCharset() {
		if (charset==null) {
			charset = StandardCharsets.ISO_8859_1;
		}
		return charset;
	}
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Returns the JWT token handler (for creation and parsing).
	 * @return the JwtHandler
	 */
	private JwtHandler getJwtHandler() {
		if (jwtHandler==null) {
			jwtHandler = new JwtHandler();
			try {
				jwtHandler.init(this.getSecret(), this.getIssuer(), this.getValidityPeriod());
			} catch (IllegalArgumentException | UnsupportedEncodingException ex) {
				jwtHandler = null;
				ex.printStackTrace();
			}
		}
		return jwtHandler;
	}
	/**
	 * Return the JWT session store.
	 * @return the JwtSessionStore
	 */
	private JwtSessionStore getJwtSessionStore() {
		return JwtSessionStore.getInstance();
	}

	
	/**
	 * Validates a login request.
	 *
	 * @param req the servlet request
	 * @param resp the servlet response
	 * @param callback the callback
	 * @return the authentication
	 */
	public AuthenticationState validateLoginRequest(Request req, Response resp, Callback callback) {
		
		String credentials = req.getHeaders().get(HttpHeader.AUTHORIZATION);
		if (credentials != null && credentials.isBlank()==false) {
            int space = credentials.indexOf(' ');
            if (space > 0) {
                String method = credentials.substring(0, space);
                if ("basic".equalsIgnoreCase(method)) {
                    credentials = credentials.substring(space + 1);
                    credentials = new String(Base64.getDecoder().decode(credentials), this.getCharset());
                    int i = credentials.indexOf(':');
                    if (i > 0) {
                        String username = credentials.substring(0, i);
                        String password = credentials.substring(i + 1);
                        UserIdentity user = this.login(username, password, req, resp);
                        if (user!=null) {
                        	// --- Create JWT for the user ----------
                        	String jwtToken = this.getJwtHandler().createJwsToken(DEFAULT_JWT_CLAIM_SUBJECT, username, DEFAULT_JWT_CLAIM_ROLE);
                        	JwtPrincipal jwtPrincipal = (JwtPrincipal) user.getUserPrincipal();
                        	jwtPrincipal.addJwtToken(jwtToken);
                        	// --- Create JwtUserIdentity -----------
                        	JwtUserIdentity jwtIdentity = new JwtUserIdentity(jwtPrincipal);
                        	
                            //response.setHeader(HttpHeader.WWW_AUTHENTICATE.asString(), AUTH_HEADER_VALUE_PREFIX + jwtToken);
                            resp.getHeaders().put(HttpHeader.WWW_AUTHENTICATE, AUTH_HEADER_VALUE_PREFIX + jwtToken);
                        	return new JwtAuthentication(this, jwtIdentity);
                        }
                    }
                }
            }
        }
		// ------------------------------------------------------
		// --- No JWT, no credentials ---------------------------
		// ------------------------------------------------------
        if (AuthenticationState.Deferred.isDeferred(resp)) {
        	return AuthenticationState.SEND_FAILURE;
        }

        String value = "basic realm=\"" + _loginService.getName() + "\"";
        value += ", charset=\"" + this.getCharset().name() + "\"";
        
        resp.getHeaders().put(HttpHeader.WWW_AUTHENTICATE, value);
        Response.writeError(req, resp, callback, HttpStatus.UNAUTHORIZED_401);
        return AuthenticationState.CHALLENGE;
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.Authenticator#validateRequest(org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, org.eclipse.jetty.util.Callback)
	 */
	@Override
	public AuthenticationState validateRequest(Request req, Response resp, Callback callback) throws ServerAuthException {

		// ----------------------------------------------------------
		// --- Check if a Bearer token is available -----------------
		// ----------------------------------------------------------
		String jwtInput = this.getBearerToken(req);
		if (jwtInput==null && ServletHelper.isLoginPathRequest((Request) req)==true) {
			// ------------------------------------------------------
			// --- No JWT token available, check credentials --------
			// ------------------------------------------------------
			AuthenticationState authentication = this.validateLoginRequest(req, resp, callback); 
			if (authentication!=null && authentication instanceof JwtAuthentication) {
				// --- Cache the JwtAuthentication created ---------- 
				this.getJwtSessionStore().cacheAuthentication((JwtAuthentication) authentication);
			}
            return authentication;
		}
		
		// ----------------------------------------------------------
		// --- From here handling of JWT token ----------------------
		// ----------------------------------------------------------
		JwtAuthentication authentication = null;
		if (verbose) logger.info("jwt={}", jwtInput);
		if (jwtInput!=null) {
			try {
				// --- Try parsing JWT string -----------------------
				JwtParsed jwtParsed = this.getJwtHandler().parse(jwtInput);
				if (jwtParsed.hasExceptions()==true || jwtParsed.getJwsClaims()==null) {
					this.getJwtSessionStore().removeAuthentication(jwtInput);
					if (verbose) logger.info("unable to decode jwt, returning unauthenticated");
			        Response.writeError(req, resp, callback, HttpStatus.UNAUTHORIZED_401);
					return AuthenticationState.SEND_FAILURE;
				}
				
				// --- Test user name and role ----------------------
				String username = jwtParsed.getPayload().get(JWT_CLAIM_USER, String.class);
				String role     = jwtParsed.getPayload().get(JWT_CLAIM_ROLE, String.class);
				if (username==null || username.isBlank()==true) {
					this.getJwtSessionStore().removeAuthentication(jwtInput);
					if (verbose) logger.info("no username provided in jwt, returning unauthenticated");
			        Response.writeError(req, resp, callback, HttpStatus.UNAUTHORIZED_401);
					return AuthenticationState.SEND_FAILURE;
				}
				if (verbose) logger.info("jwt username={} role={}", username, role);
				
				// --- Get JwtAuthentication from local cache -------
				authentication = this.getJwtSessionStore().getAuthentication(jwtInput);
				if (authentication==null) {
					this.getJwtSessionStore().removeAuthentication(jwtInput);
					if (verbose) logger.info("jwt token not found in local cache, returning unauthenticated");
			        Response.writeError(req, resp, callback, HttpStatus.UNAUTHORIZED_401);
					return AuthenticationState.SEND_FAILURE;
				}
				
				// --- Check the user name again -------------------- 
				String prevUsername = authentication.getUserIdentity().getUserPrincipal().getName();
				if (username.equals(prevUsername)==false) {
					this.getJwtSessionStore().removeAuthentication(jwtInput);
					if (verbose) logger.info("user name differs, returning unauthenticated");
			        Response.writeError(req, resp, callback, HttpStatus.UNAUTHORIZED_401);
					return AuthenticationState.SEND_FAILURE;
				}
				
				// --- Logout the current user? ---------------------
				if (ServletHelper.isLogoutPathRequest((Request) req)==true) {
					this.getJwtSessionStore().removeAuthentication(jwtInput);
			        Response.writeError(req, resp, callback, HttpStatus.UNAUTHORIZED_401);
					return AuthenticationState.SEND_FAILURE;
				}
				
				// --- Renew JWT token with each call? --------------
				if (this.isRenewWithEachCall()==true) {
					// --- Add token to current JwtAuthentication ---
                	String jwtToken = this.getJwtHandler().createJwsToken(DEFAULT_JWT_CLAIM_SUBJECT, username, DEFAULT_JWT_CLAIM_ROLE);
                	JwtPrincipal jwtPrincipal = (JwtPrincipal) authentication.getUserIdentity().getUserPrincipal();
                	jwtPrincipal.addJwtToken(jwtToken);
                	
                	// --- Adjust response header -------------------
                	resp.getHeaders().put(HttpHeader.WWW_AUTHENTICATE, AUTH_HEADER_VALUE_PREFIX + jwtToken);

                	// --- Save to session store --------------------
                	this.getJwtSessionStore().cacheAuthentication(authentication);
				}
				
			} catch (Exception e) {
				if (verbose) {
					logger.info("JWT ticket validation failed: {}", e.toString());
					logger.info("JWT Exception info", e);
					logger.info("returning unauthenticated");
					e.printStackTrace();
				}
				// don't want to do this, sends 500, even if token was just expired
				// throw new ServerAuthException("JWT ticket validation failed", e);
			}
		}

		// --- Return an authentication ----------------------------- 
		if (authentication != null) {
			return authentication;
		}
        Response.writeError(req, resp, callback, HttpStatus.UNAUTHORIZED_401);
		return AuthenticationState.SEND_FAILURE;
	}

	/**
	 * Get the bearer token from the HTTP request. The token is in the HTTP request
	 * "Authorization" header in the form of: "Bearer [token]"
	 */
	private String getBearerToken(Request request) {
		String authHeader = request.getHeaders().get(HttpHeader.AUTHORIZATION);
		if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
			return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
		}
		return null;
	}

}
