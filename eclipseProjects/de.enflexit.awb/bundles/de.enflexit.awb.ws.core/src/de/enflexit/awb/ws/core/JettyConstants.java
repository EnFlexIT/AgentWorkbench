package de.enflexit.awb.ws.core;

import de.enflexit.common.crypto.KeyStoreType;

/**
 * Provides configuration constants for use with JettyConfigurator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public enum JettyConstants {
	
	HTTP_ENABLED("http.enabled", 1, Boolean.class, true, new Boolean[] {true, false}),
	HTTP_HOST("http.host", 2, String.class, "0.0.0.0", null),
	HTTP_PORT("http.port", 3, Integer.class, 8080, null),
	HTTP_TO_HTTPS("http.to.https", 4, Boolean.class, false, new Boolean[] {true, false}),
	
	HTTPS_ENABLED("https.enabled", 10, Boolean.class, false, new Boolean[] {true, false}),
	HTTPS_HOST("https.host", 11, String.class, "0.0.0.0", null),
	HTTPS_PORT("https.port", 12, Integer.class, 8443, null),
	
	SSL_KEYSTORE("ssl.keystore", 13, String.class, null, null),
	SSL_KEYSTORETYPE("ssl.keystoretype", 14, String.class, KeyStoreType.PKCS12.getType(), KeyStoreType.getAllFileExtensions()),
	SSL_PASSWORD("ssl.password", 15, String.class, null, null),
	SSL_KEYPASSWORD("ssl.keypassword", 16, String.class, null, null),
	
	SSL_PROTOCOL("ssl.protocol", 17, String.class, "TLS", null),
//	SSL_ALGORITHM("ssl.algorithm", 12, String.class, null, null),

	SSL_NEEDCLIENTAUTH("ssl.needclientauth", 18, Boolean.class, false, new Boolean[] {true, false}),
	SSL_WANTCLIENTAUTH("ssl.wantclientauth", 19, Boolean.class, false, new Boolean[] {true, false}),

	HTTP_MINTHREADS("http.minThreads", 20, Integer.class, 8  , null),
	HTTP_MAXTHREADS("http.maxThreads", 21, Integer.class, 200, null),
	
//	CONTEXT_PATH("context.path", 18, String.class, null, null),
//	CONTEXT_SESSIONINACTIVEINTERVAL("context.sessioninactiveinterval", 19, Integer.class, 300, null),
	
//	HOUSEKEEPER_INTERVAL("housekeeper.interval", 20, Integer.class, null, null);
	
	
	CORS_ENABLED("cors.filter.enabled", 30, Boolean.class, false, new Boolean[] {true, false}),
	CORS_ALLOWED_ORIGINS_PARAM("cors.allowedOrigins", 31, String.class, "*", null),
	CORS_ALLOWED_TIMING_ORIGINS_PARAM("cors.allowedTimingOrigins", 32, String.class, "", null),
	CORS_ALLOWED_METHODS_PARAM("cors.allowedMethods", 33, String.class, "GET,POST,HEAD", null),
	CORS_ALLOWED_HEADERS_PARAM("cors.allowedHeaders", 34, String.class, "X-Requested-With,Content-Type,Accept,Origin", null),
	CORS_PREFLIGHT_MAX_AGE_PARAM("cors.preflightMaxAge", 35, Integer.class, 1800, null),
	CORS_ALLOW_CREDENTIALS_PARAM("cors.allowCredentials", 36, Boolean.class, true, new Boolean[] {true, false}),
	CORS_EXPOSED_HEADERS_PARAM("cors.exposedHeaders", 37, String.class, "*", null),
	CORS_CHAIN_PREFLIGHT_PARAM("cors.chainPreflight", 38, Boolean.class, true,  new Boolean[] {true, false});
	
	
	private String jettyKey;
	private Integer orderPos;
	private Class<?> typeClass;
	private Object defaultValue;
	private Object[] possibleValues;
	
	private JettyConstants(final String jettyKey, Integer orderPos, Class<?> type, Object defaultValue, Object[] possibleValues) {
		this.jettyKey = jettyKey;
		this.orderPos = orderPos;
		this.typeClass = type;
		this.defaultValue = defaultValue;
		this.possibleValues = possibleValues;
	}
	
	public String getJettyKey() {
		return jettyKey;
	}
	public int getOrderPos() {
		return orderPos;
	}
	public Class<?> getTypeClass() {
		return typeClass;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public Object[] getPossibleValues() {
		return possibleValues;
	}
	
	public static JettyConstants valueofJettyKey(String jettyKey) {
		for (JettyConstants jc : JettyConstants.values()) {
			if (jc.getJettyKey().equals(jettyKey)==true) {
				return jc;
			}
		}
		return null;
	}

}
