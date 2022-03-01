package de.enflexit.awb.ws.core;

import java.util.TreeMap;

import org.eclipse.equinox.http.jetty.JettyConstants;
import org.eclipse.jetty.server.Handler;

/**
 * A ServerConfiguration describes the configuration of a Jetty Server.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JettyConfiguration extends TreeMap<String, JettyAttribute<?>> {

	private static final long serialVersionUID = -425703333358456038L;

	private static final Boolean[] valueRangeBoolean = new Boolean[] {true, false};
	
	private String serverName;
	private Handler handler;
	private boolean mutableHandlerCollection;
	
	
	/**
	 * Instantiates a new jetty configuration.
	 * @param serverName the server name
	 */
	public JettyConfiguration(String serverName) {
		this(serverName, null, true);
	}
	/**
	 * Instantiates a new jetty configuration.
	 *
	 * @param serverName the server name
	 * @param handler the initial or the only handler to be used
	 * @param useMutableHandlerCollection the indicator to use a mutable handler collection thats allows to dynamically add {@link Handler} during server runtime
	 */
	public JettyConfiguration(String serverName, Handler handler, boolean useMutableHandlerCollection) {
		this.setServerName(serverName);
		this.setHandler(handler);
		this.setMutableHandlerCollection(useMutableHandlerCollection);
		this.setDefaultConfiguration();
	}
	
	/**
	 * Returns the server name.
	 * @return the server name
	 */
	public String getServerName() {
		return serverName;
	}
	/**
	 * Sets the server name.
	 * @param serverName the new server name
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	/**
	 * Sets the handler.
	 * @param handler the new handler
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	/**
	 * Returns the handler.
	 * @return the handler
	 */
	public Handler getHandler() {
		return handler;
	}
	
	/**
	 * Sets to use a mutable handler collection or not.
	 * @param mutableHandlerCollection the new mutable handler collection
	 */
	public void setMutableHandlerCollection(boolean mutableHandlerCollection) {
		this.mutableHandlerCollection = mutableHandlerCollection;
	}
	/**
	 * Checks if is mutable handler collection.
	 * @return true, if is mutable handler collection
	 */
	public boolean isMutableHandlerCollection() {
		return mutableHandlerCollection;
	}
	
	/**
	 * Sets the default configuration.
	 */
	private void setDefaultConfiguration() {
		
		this.setJettyAttribute(new JettyAttribute<Boolean>(JettyConstants.HTTP_ENABLED, true, valueRangeBoolean));
		this.setJettyAttribute(new JettyAttribute<Integer>(JettyConstants.HTTP_PORT, 8080, null));
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.HTTP_HOST, "0.0.0.0", null));
		
		this.setJettyAttribute(new JettyAttribute<Boolean>(JettyConstants.HTTP_NIO, true, valueRangeBoolean));
		
		this.setJettyAttribute(new JettyAttribute<Integer>(JettyConstants.HTTP_MINTHREADS, 8, null));
		this.setJettyAttribute(new JettyAttribute<Integer>(JettyConstants.HTTP_MAXTHREADS, 200, null));
		
		this.setJettyAttribute(new JettyAttribute<Boolean>(JettyConstants.HTTPS_ENABLED, false, valueRangeBoolean));
		this.setJettyAttribute(new JettyAttribute<Integer>(JettyConstants.HTTPS_PORT, 8443, null));
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.HTTPS_HOST, "0.0.0.0", null));
		
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_KEYSTORE, "", null));
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_PASSWORD, "", null));
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_KEYPASSWORD, "", null));
		
		this.setJettyAttribute(new JettyAttribute<Boolean>(JettyConstants.SSL_NEEDCLIENTAUTH, false, valueRangeBoolean));
		this.setJettyAttribute(new JettyAttribute<Boolean>(JettyConstants.SSL_WANTCLIENTAUTH, false, valueRangeBoolean));
		
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_PROTOCOL, "", null));
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_ALGORITHM, "", null));
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_KEYSTORETYPE, "", null));
		
		
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.CONTEXT_PATH, "", null));
		this.setJettyAttribute(new JettyAttribute<Integer>(JettyConstants.CONTEXT_SESSIONINACTIVEINTERVAL, 300, null));	
		
		// TODO: JettyConstants.CUSTOMIZER_CLASS
	}
	/**
	 * Sets the specified {@link JettyAttribute} to the configuration of a Jetty server.
	 * @param attribute the new jetty attribute
	 */
	public void setJettyAttribute(JettyAttribute<?> attribute) {
		this.put(attribute.getKey(), attribute);
	}
	
	
	/**
	 * Returns the HTTP port to be used by the Jetty server.
	 * @return the HTTP port
	 */
	public int getHttpPort() {

		int port = 8080; // --- The default port ---
		JettyAttribute<?> jettyAttribute = this.get(JettyConstants.HTTP_PORT);
		if (jettyAttribute!=null) {
			Object valueObject = jettyAttribute.getValue();
			if (valueObject instanceof Integer) {
				port = (int) valueObject;
			}
		}
		return port;
	}
}
