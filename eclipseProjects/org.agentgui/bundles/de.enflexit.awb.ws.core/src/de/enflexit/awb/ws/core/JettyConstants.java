package de.enflexit.awb.ws.core;

/**
 * Provides configuration constants for use with JettyConfigurator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public enum JettyConstants {
	
	HTTP_ENABLED("http.enabled", 1, Boolean.class, true),
	HTTP_HOST("http.host", 2, String.class, "0.0.0.0"),
	HTTP_PORT("http.port", 3, Integer.class, 0),
	HTTP_NIO("http.nio", 4, Boolean.class, true),
	
	HTTPS_ENABLED("https.enabled", 5, Boolean.class, false),
	HTTPS_HOST("https.host", 6, String.class, "0.0.0.0"),
	HTTPS_PORT("https.port", 7, Integer.class, 0),
	
	HTTP_MINTHREADS("http.minThreads", 8, Integer.class, 8),
	HTTP_MAXTHREADS("http.maxThreads", 9, Integer.class, 200),
	
	SSL_KEYSTORE("ssl.keystore", 10, String.class, null),
	SSL_PASSWORD("ssl.password", 11, String.class, null),
	SSL_KEYPASSWORD("ssl.keypassword", 12, String.class, null),
	
	SSL_NEEDCLIENTAUTH("ssl.needclientauth", 13, Boolean.class, false),
	SSL_WANTCLIENTAUTH("ssl.wantclientauth", 14, Boolean.class, false),

	SSL_PROTOCOL("ssl.protocol", 15, String.class, null),
	SSL_ALGORITHM("ssl.algorithm", 16, String.class, null),
	SSL_KEYSTORETYPE("ssl.keystoretype", 17, String.class, false),
	
	CONTEXT_PATH("context.path", 18, String.class, null),
	CONTEXT_SESSIONINACTIVEINTERVAL("context.sessioninactiveinterval", 19, Integer.class, null),
	
	HOUSEKEEPER_INTERVAL("housekeeper.interval", 20, Integer.class, null);
	
	
	private String jettyKey;
	private Integer orderPos;
	private Class<?> typeClass;
	private Object defaultValue;
	
	private JettyConstants(final String jettyKey, Integer orderPos, Class<?> type, Object defaultValue) {
		this.jettyKey = jettyKey;
		this.orderPos = orderPos;
		this.typeClass = type;
		this.defaultValue = defaultValue;
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
	public static JettyConstants valueofJettyKey(String jettyKey) {
		for (JettyConstants jc : JettyConstants.values()) {
			if (jc.getJettyKey().equals(jettyKey)==true) {
				return jc;
			}
		}
		return null;
	}

}
