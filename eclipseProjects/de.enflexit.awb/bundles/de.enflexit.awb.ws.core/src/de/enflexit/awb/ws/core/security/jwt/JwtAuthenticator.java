package de.enflexit.awb.ws.core.security.jwt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.security.authentication.DeferredAuthentication;
import org.eclipse.jetty.security.authentication.LoginAuthenticator;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.UserIdentity;
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
	public static final String JWT_COOKIE_NAME = "JWT_COOKIE_NAME";
	public static final String JWT_VERBOSE = "JWT_VERBOSE";

	public static final String JWT_CLAIM_USER = "user";
	public static final String JWT_CLAIM_ROLE = "role";

	private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";
	private static final String AUTH_COOKIE_KEY = "awb-jwt";
	
	/** Name of authentication method provided by this authenticator. */
	private static final String AUTH_METHOD = "Bearer";
	/** Session attribute used to cache JWT authentication data. */
	private static final String CACHED_AUTHN_ATTRIBUTE = JwtAuthenticator.class.getName(); 

	/** Logger instance. */
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticator.class);
    /** hack to allow env variable based logging changes **/
    public Boolean verbose = true;

	/** who issued token **/
	private String issuer;
	/** issuer secret **/
	private String secret;

	
	/** JWT ticket parser component. */
	private JwtHandler jwtHandler;

	private Charset charset;
	private String cookieKey = null;

	/** Map of tickets to sessions. */
	private ConcurrentMap<String, WeakReference<HttpSession>> sessionMap;

	
	/**
	 * Instantiates a new jwt authenticator.
	 * @param configMap the map with configuration options
	 */
	public JwtAuthenticator(Map<String, String> configMap) {
		this.applyConfiguration(configMap);
		this.getSessionMap();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.Authenticator#getAuthMethod()
	 */
	@Override
	public String getAuthMethod() {
		return AUTH_METHOD;
	}

	/**
	 * Applies the configuration based on the specified map.
	 * @param configMap the configuration map to apply
	 */
	private void applyConfiguration(Map<String, String> configMap) {
		
		if (configMap==null || configMap.size()==0) return;
		
		if (configMap.containsKey(JWT_SECRET) && !configMap.get(JWT_SECRET).equals("")) {
			this.setSecret(configMap.get(JWT_SECRET));
		}
		if (configMap.containsKey(JWT_ISSUER) && !configMap.get(JWT_ISSUER).equals("")) {
			this.setIssuer(configMap.get(JWT_ISSUER));
		}
		if (configMap.containsKey(JWT_COOKIE_NAME) && !configMap.get(JWT_COOKIE_NAME).equals("")) {
			this.setCookieKey(configMap.get(JWT_COOKIE_NAME));
		}

		if (configMap.containsKey(JWT_VERBOSE) && !configMap.get(JWT_VERBOSE).equals("")) {
			verbose = true;
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

	public Charset getCharset() {
		if (charset==null) {
			charset = StandardCharsets.ISO_8859_1;
		}
		return charset;
	}
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public String getCookieKey() {
		if (cookieKey==null) {
			cookieKey = AUTH_COOKIE_KEY;
		}
		return cookieKey;
	}
	public void setCookieKey(String cookieKey) {
		this.cookieKey = cookieKey;
	}
	
	private JwtHandler getJwtHandler() {
		if (jwtHandler==null) {
			jwtHandler = new JwtHandler();
			try {
				jwtHandler.init(this.getSecret(), this.getIssuer());
			} catch (IllegalArgumentException | UnsupportedEncodingException ex) {
				jwtHandler = null;
				ex.printStackTrace();
			}
		}
		return jwtHandler;
	}
	

	/**
	 * Validates a login request.
	 *
	 * @param servletRequest the servlet request
	 * @param servletResponse the servlet response
	 * @param mandatory the mandatory
	 * @return the authentication
	 * @throws ServerAuthException the server auth exception
	 */
	public Authentication validateLoginRequest(final ServletRequest servletRequest, final ServletResponse servletResponse, final boolean mandatory) {
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
    	
		if (!mandatory) {
			return new DeferredAuthentication(this);
		}
		
		String credentials = request.getHeader(HttpHeader.AUTHORIZATION.asString());
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
                        UserIdentity user = login(username, password, request);
                        if (user != null) {
                        	// --- Create JWT for the user ------
                        	String jwtToken = this.getJwtHandler().createJwsToken("AWB-WebServer", username, "LocalUser");
                        	JwtPrincipal jwtPrincipal = (JwtPrincipal) user.getUserPrincipal();
                        	jwtPrincipal.setJwtToken(jwtToken);
                        	
                        	String value = AUTH_HEADER_VALUE_PREFIX + jwtToken;
                            response.setHeader(HttpHeader.WWW_AUTHENTICATE.asString(), value);
                        	return new JwtAuthentication(this, jwtPrincipal);
                        }
                    }
                }
            }
        }
		// ------------------------------------------------------
		// --- No JWT, no credentials ---------------------------
		// ------------------------------------------------------
        if (DeferredAuthentication.isDeferred(response)) {
        	return Authentication.UNAUTHENTICATED;
        }

        String value = "basic realm=\"" + _loginService.getName() + "\"";
        value += ", charset=\"" + this.getCharset().name() + "\"";
        
        response.setHeader(HttpHeader.WWW_AUTHENTICATE.asString(), value);
        try {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
        return Authentication.SEND_CONTINUE;
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.Authenticator#validateRequest(javax.servlet.ServletRequest, javax.servlet.ServletResponse, boolean)
	 */
	@Override
	public Authentication validateRequest(final ServletRequest servletRequest, final ServletResponse servletResponse, final boolean mandatory) throws ServerAuthException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		
		// ----------------------------------------------------------
		// --- Check if a Bearer token is available -----------------
		// ----------------------------------------------------------
		String jwt = this.getBearerToken(request);
		if (jwt==null && ServletHelper.isLoginPathRequest((Request) servletRequest)==true) {
			// ------------------------------------------------------
			// --- No JWT token available, check credentials --------
			// ------------------------------------------------------
            return this.validateLoginRequest(servletRequest, servletResponse, mandatory);
		}
		
		// ----------------------------------------------------------
		// --- From here handling of JWT token ----------------------
		// ----------------------------------------------------------
		JwtAuthentication authentication = null;
		if (verbose) logger.info("jwt={}", jwt);
		if (jwt != null) {
			try {
				// --- Try parsing JWT string -----------------------
				JwtParsed jwtParsed = this.getJwtHandler().parse(jwt);
				if (jwtParsed.hasExceptions()==true || jwtParsed.getJwsClaims()==null) {
					if (verbose) logger.info("unable to decode jwt, returning unauthenticated");
					return Authentication.UNAUTHENTICATED;
				}
				
				// --- Test user name and password ------------------
				String username = jwtParsed.getPayload().get(JWT_CLAIM_USER, String.class);
				String role     = jwtParsed.getPayload().get(JWT_CLAIM_ROLE, String.class);
				if (username == null) {
					if (verbose) logger.info("no username provided in jwt, returning unauthenticated");
					return Authentication.UNAUTHENTICATED;
				}
				if (verbose) logger.info("jwt username={} role={}", username, role);
				

				JwtPrincipal principle = new JwtPrincipal(username);
				principle.setJwtToken(jwt);
				principle.setRole(role);

				authentication = new JwtAuthentication(this, principle);
				this.cacheAuthentication(request, authentication);
				
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

		if (authentication != null) return authentication;
		return Authentication.UNAUTHENTICATED;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.Authenticator#secureResponse(javax.servlet.ServletRequest, javax.servlet.ServletResponse, boolean, org.eclipse.jetty.server.Authentication.User)
	 */
	@Override
	public boolean secureResponse(final ServletRequest request, final ServletResponse response, final boolean mandatory, final Authentication.User user) throws ServerAuthException {
		return true;
	}

	/**
	 * Get the bearer token from the HTTP request. The token is in the HTTP request
	 * "Authorization" header in the form of: "Bearer [token]"
	 */
	private String getBearerToken(HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeader.AUTHORIZATION.asString());
		if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
			return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
		}

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (getCookieKey().equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	
	
	// --------------------------------------------------------------
	// --- From here local session cache for open sessions ----------
	// --------------------------------------------------------------
	/**
	 * Returns the current session map.
	 * @return the session map
	 */
	private ConcurrentMap<String, WeakReference<HttpSession>> getSessionMap() {
		if (sessionMap==null) {
			sessionMap = new ConcurrentHashMap<String, WeakReference<HttpSession>>();
		}
		return sessionMap;
	}
	
	protected void clearCachedAuthentication(final String ticket) {
		final WeakReference<HttpSession> sessionRef = this.getSessionMap().remove(ticket);
		if (sessionRef != null && sessionRef.get() != null) {
			sessionRef.get().removeAttribute(CACHED_AUTHN_ATTRIBUTE);
		}
	}

	private void cacheAuthentication(final HttpServletRequest request, final JwtAuthentication authentication) {
		final HttpSession session = request.getSession(true);
		if (session!=null) {
			session.setAttribute(CACHED_AUTHN_ATTRIBUTE, authentication);
			this.getSessionMap().put(authentication.getJwtToken(), new WeakReference<HttpSession>(session));
		}
	}

	private JwtAuthentication fetchCachedAuthentication(final HttpServletRequest request) {
		final HttpSession session = request.getSession(false);
		if (session != null) {
			return (JwtAuthentication) session.getAttribute(CACHED_AUTHN_ATTRIBUTE);
		}
		return null;
	}

	

}
