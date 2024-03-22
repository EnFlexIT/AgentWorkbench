package de.enflexit.awb.ws.core.security.jwt;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

/**
 * The Class JwtHandler utilizes the JJWT library and is used for creating and parsing TWT token.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtHandler {

	private SecretKey secreteKey;
	private String issuer;
	
	private JwtParser jwtParser;

	
	private SecretKey getSecreteKey() {
		return secreteKey;
	}
	private void setSecreteKey(SecretKey secreteKey) {
		this.secreteKey = secreteKey;
	}
	
	private String getIssuer() {
		return issuer;
	}
	private void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	
	/**
	 * Inits the JwtHandler.
	 *
	 * @param secret the secret
	 * @param issuer the issuer
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public void init(String secret, String issuer) throws IllegalArgumentException, UnsupportedEncodingException {
		
		byte[] byteKey = secret.getBytes();
		this.setSecreteKey(Keys.hmacShaKeyFor(byteKey));
		
		this.setIssuer(issuer);
		// --- Test call to validate secrete and issuer parameter ---
		this.createJwsToken("Test", "TestUSer", "Maintainer");
	}
	
	/**
	 * Creates the JWT/JWS token.
	 *
	 * @param subject the subject
	 * @param userName the user name
	 * @param role the role
	 * @return the JWT string
	 */
	public String createJwsToken(String subject, String userName, String role) {
		
		// --- Define expiration --------------------------
		long expDuration = 1000l * 60 * 60 * 24 * 30 * 3; // --- 90 days ~ 3 month ---
		long nowTimeStamp = System.currentTimeMillis();
		long expTimeStamp = nowTimeStamp + expDuration;
		
		String jws = Jwts.builder()
				  .issuer(this.getIssuer())
				  .subject(subject)
				  .claim(JwtAuthenticator.JWT_CLAIM_USER, userName)
				  .claim(JwtAuthenticator.JWT_CLAIM_ROLE, role)
				  .issuedAt(new Date(nowTimeStamp))
				  .expiration(new Date(expTimeStamp))
				  .signWith(this.getSecreteKey())
				  .compact();
		return jws;
	}

	
	/**
	 * Returns a jwt parser.
	 * @return the jwt parser
	 */
	private JwtParser getJwtParser() {
		if (jwtParser==null) {
			jwtParser = Jwts.parser().verifyWith(this.getSecreteKey()).build();
		}
		return jwtParser;
	}
	/**
	 * Parses the specified jwtToken.
	 *
	 * @param jwtToken the token
	 * @return the jwt parsed
	 */
	public JwtParsed parse(String jwtToken) {

		Jws<Claims> jwsClaims = null;
		Exception exception = null;
		try {
			jwsClaims = this.getJwtParser().parseSignedClaims(jwtToken);
			
		} catch (UnsupportedJwtException ex) {
			exception = ex;
		} catch (MalformedJwtException ex) {
			exception = ex;
		} catch (SignatureException ex) {
			exception = ex;
		} catch (SecurityException ex) {
			exception = ex;
		} catch (ExpiredJwtException ex) {
			exception = ex;
		} catch (IllegalArgumentException ex) {
			exception = ex;
		}
		return new JwtParsed(jwtToken, jwsClaims, exception);
	}
	
	
	/**
	 * The sub Class JwtParsed container the verify of the host class {@link JwtHandler}.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class JwtParsed {
		
		private String token;
		private Jws<Claims> jwsClaims;
		private Exception exception;
		
		/**
		 * Instantiates a new container for a parsed JWT token.
		 *
		 * @param token the token
		 * @param jwsClaims the jws claims
		 * @param exception the jwt exception
		 */
		public JwtParsed(String token, Jws<Claims> jwsClaims, Exception exception) {
			this.setToken(token);
			this.setJwsClaims(jwsClaims);
			this.setException(exception);
		}

		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}

		public Jws<Claims> getJwsClaims() {
			return jwsClaims;
		}
		public void setJwsClaims(Jws<Claims> jwsClaims) {
			this.jwsClaims= jwsClaims;
		}

		public JwsHeader getHeader() {
			return this.getJwsClaims()!=null ? this.getJwsClaims().getHeader() : null;
		}
		public Claims getPayload() {
			return this.getJwsClaims()!=null ? this.getJwsClaims().getPayload() : null;
		}
		public byte[] getDigest() {
			return this.getJwsClaims()!=null ? this.getJwsClaims().getDigest() : null;
		}
		
		public boolean hasExceptions() {
			return this.getException()!=null;
		}
		
		public Exception getException() {
			return exception;
		}
		public void setException(Exception exception) {
			this.exception = exception;
		}
	} // end sub class
	
}
