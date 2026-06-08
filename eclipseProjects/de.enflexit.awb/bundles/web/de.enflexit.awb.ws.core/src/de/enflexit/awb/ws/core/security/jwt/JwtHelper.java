package de.enflexit.awb.ws.core.security.jwt;

import java.text.ParseException;
import java.util.Date;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * The Class JwtHelper provides some help methods for dealing with a JWT token.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtHelper {

	/**
	 * Returns the expiration time of JWT.
	 *
	 * @param accessToken the access token
	 * @return the expiration time of JWT
	 */
	public static Long getExpirationTimeOfJWT(String accessToken) {
		Date expirationDate = JwtHelper.getExpirationDateOfJWT(accessToken);
		if (expirationDate==null) return null;
		return expirationDate.getTime();
	}
	/**
	 * Returns the expiration date of JWT.
	 *
	 * @param accessToken the access token
	 * @return the expiration date of JWT
	 */
	public static Date getExpirationDateOfJWT(String accessToken) {
		JWTClaimsSet claimSet = JwtHelper.getJWTClaims(accessToken);
		if (claimSet==null) return null;
		return claimSet.getExpirationTime();
	}
	
	/**
	 * Returns the JWT claims of the specified access token.
	 *
	 * @param accessToken the access token
	 * @return the JWT claims
	 */
	public static JWTClaimsSet getJWTClaims(String accessToken) {
		
		SignedJWT jwt = JwtHelper.getSignedJWT(accessToken);
		if (jwt==null) return null;

		JWTClaimsSet claimSet = null;
		try {
			claimSet = jwt.getJWTClaimsSet();
		} catch (ParseException pEx) {
			pEx.printStackTrace();
		}
		return claimSet;
	}
	/**
	 * Returns the SignedJWT instance of the specified access token.
	 *
	 * @param accessToken the access token
	 * @return the signed JWT
	 */
	public static SignedJWT getSignedJWT(String accessToken) {
		
		if (accessToken==null) return null;
		
		SignedJWT jwt = null;
		try {
			jwt = SignedJWT.parse(accessToken);
		} catch (ParseException pEx) {
			pEx.printStackTrace();
		}
		return jwt;
	}
	
}
