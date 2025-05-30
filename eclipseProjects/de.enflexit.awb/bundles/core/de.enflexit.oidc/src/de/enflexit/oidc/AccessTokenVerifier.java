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
	
	private static final String DEFAULT_ISSUER_URL = "https://login.enflex.it/realms/enflexService";
	private static final String DEFAULT_CERTIFICATES_ENDPOINT_PATH = "/protocol/openid-connect/certs";
	
	private ConfigurableJWTProcessor<SecurityContext> jwtProcessor;
	private JWKSource<SecurityContext> jwkSource;
	private JWSKeySelector<SecurityContext> keySelector;
	private DefaultJWTClaimsVerifier<SecurityContext> claimsVerifier;
	
	private String issuerURL;
	private String certificatesEndpointPath;

	private JWTClaimsSet claimsSet;
	
	private HashSet<String> requiredClaims;
	
	/**
	 * Instantiates a new access token verifier, that uses the default issuer
	 * URL and certificates endpoint path, as specified in the class constant.
	 */
	public AccessTokenVerifier() {
	}

	/**
	 * Instantiates a new access token verifier, that uses the provided issuer URL
	 * and certificates endpoint path..
	 *
	 * @param issuerURL the issuer URL
	 * @param certificatesEndpointPath the certificates endpoint path
	 */
	public AccessTokenVerifier(String issuerURL, String certificatesEndpointPath) {
		this.issuerURL = issuerURL;
		this.certificatesEndpointPath = certificatesEndpointPath;
	}

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
			
			URI certificatesUri = this.getCertificatesURI();
			if (certificatesUri!=null) {
				jwkSource = JWKSourceBuilder.create(this.getCertificatesURI().toURL()).retrying(true).build();
			}
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
			JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().issuer(this.getIssuerURL()).build();
			claimsVerifier = (new DefaultJWTClaimsVerifier<>(claimsSet, this.getRequiredClaims()));
		}
		return claimsVerifier;
	}
	
	/**
	 * Gets the required claims. If null, it is initialized with a set of default claims, namely the 
	 * subject (= unique user ID), the JWT ID, and the times when the token was issued and when it expires.
	 * @return the required claims
	 */
	public HashSet<String> getRequiredClaims() {
		if (requiredClaims==null) {
			// --- By default, require the subject (= unique user ID), the JWT ID, and the issue and expiration times.  
			requiredClaims = new HashSet<String>(Arrays.asList(
		        JWTClaimNames.SUBJECT,
		        JWTClaimNames.ISSUED_AT,
		        JWTClaimNames.EXPIRATION_TIME,
		        JWTClaimNames.JWT_ID)
			);
		}
		return requiredClaims;
	}
	
	/**
	 * Adds a claim to the list of required claims. Token verification will fail if required claims are not provided by the token.
	 * @param claim the claim
	 */
	public void addRequiredClaim(String claim) {
		this.getRequiredClaims().add(claim);
	}
	
	/**
	 * Sets the required claims.
	 * @param requiredClaims the new required claims
	 */
	public void setRequiredClaims(HashSet<String> requiredClaims) {
		this.requiredClaims = requiredClaims;
	}
	
	/**
	 * Verifies the provided access token, i.e. checks if it was issued by a
	 * trusted server, provides all required claims and is not expired.
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
			
			// --- Try to extract the subject (= keycloak user ID) from the tokens claim set.
			try {
				keycloakUserID = this.getClaimsSet().getClaimAsString(JWTClaimNames.SUBJECT);
			} catch (ParseException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error parsing user ID from the JWT ClaimsSet!");
//				e.printStackTrace();
			}
		}
		return keycloakUserID;
	}
	
	/**
	 * Gets the issuer URL.
	 * @return the issuer URL
	 */
	private String getIssuerURL() {
		if (issuerURL==null) {
			return DEFAULT_ISSUER_URL;
		} else {
			return issuerURL;
		}
	}
	
	private String getCertificatesEndpointPath() {
		if (certificatesEndpointPath==null) {
			return DEFAULT_CERTIFICATES_ENDPOINT_PATH;
		} else {
			return certificatesEndpointPath;
		}
	}
	
	/**
	 * Gets the URI to obtain the certificates from the keycloak server.
	 * @return the certificates URI
	 * @throws URISyntaxException the URI syntax exception
	 */
	private URI getCertificatesURI() throws URISyntaxException {
		
		// --- Get the URI and the relevant path strings ------------
		URI issuerURI;
		issuerURI = new URI(this.getIssuerURL());
		String uriPath = issuerURI.getPath();
		String endpointSubPath = this.getCertificatesEndpointPath();
		
		// --- Insert a slash if necessary --------------------------
		if (uriPath.endsWith("/")==false && endpointSubPath.startsWith("/")==false) {
			uriPath += "/";
		}
		
		// --- Build and return the final URI -----------------------
		uriPath += endpointSubPath;
		return issuerURI.resolve(uriPath);
	}
	
}
