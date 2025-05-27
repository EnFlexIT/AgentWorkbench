package de.enflexit.oidc;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.DefaultJOSEObjectTypeVerifier;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

/**
 * This class implements the verification of an access token provided by the keycloak server.
 * Based on the example at https://connect2id.com/products/nimbus-jose-jwt/examples/validating-jwt-access-tokens
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class AccessTokenVerifier {
	
	//TODO Get from the settings ------------------------------------
	private static final String ISSUER_URL = "https://login.enflex.it/realms/enflexService";
	//TODO obtain from the discovery endpoint instead --------------- 
	private static final String KEYCLOAK_CERTIFICATES_URI = "https://login.enflex.it/realms/enflexService/protocol/openid-connect/certs";
	
	private ConfigurableJWTProcessor<SecurityContext> jwtProcessor;
	private JWKSource<SecurityContext> jwkSource;
	private JWSKeySelector<SecurityContext> keySelector;
	private DefaultJWTClaimsVerifier<SecurityContext> claimsVerifier; 	

	private JWTClaimsSet claimsSet;
	
	/**
	 * Gets the jwt processor.
	 * @return the jwt processor
	 * @throws MalformedURLException the malformed URL exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	private ConfigurableJWTProcessor<SecurityContext> getJwtProcessor() throws MalformedURLException, URISyntaxException {
		if (jwtProcessor==null) {
			jwtProcessor = new DefaultJWTProcessor<SecurityContext>();
			
			DefaultJOSEObjectTypeVerifier<SecurityContext> typeVerifier = new DefaultJOSEObjectTypeVerifier<SecurityContext>(JOSEObjectType.JWT);
			jwtProcessor.setJWSTypeVerifier(typeVerifier);
			jwtProcessor.setJWSKeySelector(this.getKeySelector());
			jwtProcessor.setJWTClaimsSetVerifier(this.getClaimsVerifier());
		}
		return jwtProcessor;
	}
	
	/**
	 * Gets the jwk source.
	 * @return the jwk source
	 * @throws URISyntaxException the URI syntax exception
	 * @throws MalformedURLException the malformed URL exception
	 */
	private JWKSource<SecurityContext> getJwkSource() throws URISyntaxException, MalformedURLException {
		if (jwkSource==null) {
			URI keycloakCertsURI = new URI(KEYCLOAK_CERTIFICATES_URI);
			jwkSource = JWKSourceBuilder.create(keycloakCertsURI.toURL()).retrying(true).build();
		}
		return jwkSource;
	}
	
	/**
	 * Gets the key selector.
	 * @return the key selector
	 * @throws MalformedURLException the malformed URL exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	private JWSKeySelector<SecurityContext> getKeySelector() throws MalformedURLException, URISyntaxException {
		if (keySelector==null) {
			JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256;
			keySelector = new JWSVerificationKeySelector<SecurityContext>(expectedJWSAlg, this.getJwkSource());
		}
		
		return keySelector;
	}
	
	/**
	 * Gets the claims verifier.
	 * @return the claims verifier
	 */
	private DefaultJWTClaimsVerifier<SecurityContext> getClaimsVerifier() {
		if (claimsVerifier==null) {
			HashSet<String> requiredClaims = new HashSet<>(Arrays.asList(
		        JWTClaimNames.SUBJECT,
		        JWTClaimNames.ISSUED_AT,
		        JWTClaimNames.EXPIRATION_TIME,
		        JWTClaimNames.JWT_ID)
			);
			
			JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().issuer(ISSUER_URL).build();
			        
			claimsVerifier = (new DefaultJWTClaimsVerifier<>(claimsSet, requiredClaims));
		}
		return claimsVerifier;
	}
	
	/**
	 * Verifies the provided access token.
	 * @param accessToken the access token
	 * @return true, if the token is valid
	 */
	public boolean verifyToken(String accessToken) {

		// --- Instantiate the JWTProcessor ---------------
		ConfigurableJWTProcessor<SecurityContext> jwtProcessor = null;
		try {
			jwtProcessor = this.getJwtProcessor();
		} catch (MalformedURLException | URISyntaxException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error instatiating the jwt processor, check the configured URIs!");
			e.printStackTrace();
			return false;
		}
		
		// --- Perform the actual verification ------------
		try {
			this.claimsSet = jwtProcessor.process(accessToken, null);
			return true;
		} catch (BadJWTException bje) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Access token not valid: " + bje.getMessage());
			return false;
		
		} catch (ParseException | BadJOSEException | JOSEException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error validating the provided access token");
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * Gets the set of claims that was extracted from the access token. May be null if
	 * the token was not verified yet, or the verification failed!
	 * @return the claims set
	 */
	public JWTClaimsSet getClaimsSet() {
		return claimsSet;
	}
	
	/**
	 * Gets the unique user ID of the keycloak user the verified access token belongs to.
	 * @return the keycloak user ID, null if no token was verified or the verification failed. 
	 */
	public String getKeycloakUserID() {
		String keycloakUserID = null;
		if (this.getClaimsSet()!=null) {
			try {
				keycloakUserID = this.getClaimsSet().getClaimAsString(JWTClaimNames.SUBJECT);
			} catch (ParseException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error parsing user ID from the JWT ClaimsSet!");
//				e.printStackTrace();
			}
		}
		return keycloakUserID;
	}
}
