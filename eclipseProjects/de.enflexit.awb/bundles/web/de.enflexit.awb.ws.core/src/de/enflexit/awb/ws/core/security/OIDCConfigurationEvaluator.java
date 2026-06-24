package de.enflexit.awb.ws.core.security;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.transport.HttpClientTransportOverHTTP;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class OIDCConfigurationEvaluator uses the issuer and the '/.well-known*' path
 * to evaluate several other end points of an OpenID server, such as KeyCloak.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OIDCConfigurationEvaluator {

	private static final Logger LOG = LoggerFactory.getLogger(OIDCConfigurationEvaluator.class);
    
	private static final String CONFIG_PATH = "/.well-known/openid-configuration";
	
	private static final String ISSUER = "issuer";

	private static final String AUTHORIZATION_ENDPOINT = "authorization_endpoint";
    private static final String TOKEN_ENDPOINT = "token_endpoint";
    private static final String END_SESSION_ENDPOINT = "end_session_endpoint";

    
    private final String issuer;

    private HttpClient httpClient;
    private Map<String, Object> discoveryDocument;
    
    private String authorizationEndpoint;
    private String tokenEndpoint;
    private String endSessionEndpoint;
    
	
	/**
	 * Instantiates a new OIDC configuration.
	 *
	 * @param issuer the issuer
	 * @param clientIID the client IID
	 * @param clientSecret the client secret
	 */
	public OIDCConfigurationEvaluator(String issuer) {
		this.issuer = issuer;
        this.processMetadata();
	}
	
	/**
	 * Obtain the JSON metadata from OpenID Connect Discovery Configuration Endpoint.
	 * 
	 * @return a set of Claims about the OpenID Provider's configuration in JSON format.
	 * @throws IllegalStateException if metadata could not be fetched from the OP.
	 */
	protected Map<String, Object> getDiscoveryDocument() {
		if (discoveryDocument == null) {

			String provider = issuer;
			if (provider.endsWith("/")) {
				provider = provider.substring(0, provider.length() - 1);
			}

			try {
				Map<String, Object> result;
				String responseBody = this.getHttpClient().GET(provider + CONFIG_PATH).getContentAsString();
				Object parsedResult = new JSON().fromJSON(responseBody);

				if (parsedResult instanceof Map) {
					Map<?, ?> rawResult = (Map<?, ?>) parsedResult;
					result = rawResult.entrySet().stream().filter(entry -> entry.getValue() != null)
							.collect(Collectors.toMap(it -> it.getKey().toString(), Map.Entry::getValue));
					if (LOG.isDebugEnabled()) {
						LOG.debug("discovery document {}", result);
					}
					discoveryDocument = result;
					
				} else {
					LOG.warn("OpenID provider did not return a proper JSON object response. Result was '{}'", responseBody);
					throw new IllegalStateException("Could not parse OpenID provider's malformed response");
				}
				
			} catch (Exception e) {
				throw new IllegalStateException("invalid identity provider " + provider, e);
			}
		}
		return discoveryDocument;

	}
	/**
	 * Returns the current HttpClient.
	 * @return the HttpClient
	 */
	private HttpClient getHttpClient() {
		if (httpClient==null) {
			ClientConnector connector = new ClientConnector();
			connector.setSslContextFactory(new SslContextFactory.Client(false));
			httpClient = new HttpClient(new HttpClientTransportOverHTTP(connector));
			try {
				httpClient.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return httpClient;
	}
	
	/**
	 * Process the OpenID Connect metadata discovered by
	 * {@link #fetchOpenIdConnectMetadata()}. By default, only the
	 * {@link #AUTHORIZATION_ENDPOINT} and {@link #TOKEN_ENDPOINT} claims are
	 * extracted.
	 *
	 * @throws IllegalStateException if a required field is not present in the metadata.
	 * @see <a href= "https://openid.net/specs/openid-connect-discovery-1_0.html">OpenID Connect Discovery 1.0</a>
	 */
	protected void processMetadata() {
		
		authorizationEndpoint = (String) this.getDiscoveryDocument().get(AUTHORIZATION_ENDPOINT);
		if (authorizationEndpoint == null) {
			throw new IllegalStateException(AUTHORIZATION_ENDPOINT);
		}

		tokenEndpoint = (String) this.getDiscoveryDocument().get(TOKEN_ENDPOINT);
		if (tokenEndpoint == null) {
			throw new IllegalStateException(TOKEN_ENDPOINT);
		}

		// End session endpoint is optional.
		if (endSessionEndpoint == null) {
			endSessionEndpoint = (String) this.getDiscoveryDocument().get(END_SESSION_ENDPOINT);
		}

		// We are lenient and not throw here as some major OIDC providers do not conform to this.
		if (!Objects.equals(this.getDiscoveryDocument().get(ISSUER), issuer)) {
			LOG.warn("The issuer in the metadata is not correct.");
		}
	}
	
	
	/**
	 * Returns the authorization endpoint.
	 * @return the authorization endpoint
	 */
	public String getAuthorizationEndpoint() {
		return authorizationEndpoint;
	}
	/**
	 * Returns the token endpoint.
	 * @return the token endpoint
	 */
	public String getTokenEndpoint() {
		return tokenEndpoint;
	}
	/**
	 * Returns the end session endpoint.
	 * @return the end session endpoint
	 */
	public String getEndSessionEndpoint() {
		return endSessionEndpoint;
	}
	
}
