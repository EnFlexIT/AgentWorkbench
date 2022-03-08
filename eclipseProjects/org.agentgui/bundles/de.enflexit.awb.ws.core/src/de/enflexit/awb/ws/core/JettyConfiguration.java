package de.enflexit.awb.ws.core;

import java.util.TreeMap;

import org.eclipse.equinox.http.jetty.JettyConstants;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

/**
 * A ServerConfiguration describes the configuration of a Jetty Server
 * to be started with Agent.Workbench.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JettyConfiguration extends TreeMap<String, JettyAttribute<?>> {

	private static final long serialVersionUID = -425703333358456038L;

	public enum StartOn {
		AwbStart,
		ProjectLoaded,
		JadeStartup,
		ManualStart;

		/**
		 * Increases the specified StartOn value by one.
		 *
		 * @param startOn the start on value to start from
		 * @return the increased start on value or the 
		 */
		public static StartOn increase(StartOn startOn) {
			int ordinal = startOn.ordinal() + 1;
			StartOn[] startOnArray = StartOn.values();
			if (ordinal>startOnArray.length) {
				return startOnArray[startOnArray.length-1];
			}
			return startOnArray[ordinal];
		}
		/**
		 * Decreases the specified StartOn value by one.
		 *
		 * @param startOn the start on value to start from
		 * @return the decreased StartOn value or <code>null</code>
		 */
		public static StartOn decrease(StartOn startOn) {
			if (startOn==null) return null;
			int ordinal = startOn.ordinal() - 1;
			if (ordinal<0) {
				return null;
			}
			StartOn[] startOnArray = StartOn.values();
			return startOnArray[ordinal];
		}
	}
	
	private static final Boolean[] valueRangeBoolean = new Boolean[] {true, false};
	
	
	private StartOn startOn;
	private String serverName;
	private Handler handler;
	private boolean mutableHandlerCollection;
	
	private JettyCustomizer jettyCustomizer;
	
	/**
	 * Instantiates a new jetty configuration.
	 *
	 * @param serverName the server name
	 * @param startOn the start time when the server has to be started
	 */
	public JettyConfiguration(String serverName, StartOn startOn) {
		this(serverName, startOn, null, true);
	}
	/**
	 * Instantiates a new jetty configuration.
	 *
	 * @param serverName the server name
	 * @param startOn the start time when the server has to be started
	 * @param handler the initial or the only handler to be used
	 * @param useMutableHandlerCollection the indicator to use a mutable handler collection thats allows to dynamically add {@link Handler} during server runtime
	 */
	public JettyConfiguration(String serverName, StartOn startOn, Handler handler, boolean useMutableHandlerCollection) {
		if (handler==null && useMutableHandlerCollection==false) {
			throw new IllegalArgumentException("No Handler was specified for the server, but the option for a mutable handler collection was set to 'false'!");
		}
		this.setServerName(serverName);
		this.setStartOn(startOn);
		this.setHandler(handler);
		this.setMutableHandlerCollection(useMutableHandlerCollection);
		this.setDefaultConfiguration();
	}
	
	/**
	 * Returns when the server is to be started.
	 * @return the start on
	 */
	public StartOn getStartOn() {
		if (startOn==null) {
			startOn = StartOn.AwbStart;
		}
		return startOn;
	}
	/**
	 * Sets when the server start on.
	 * @param startOn the new start on
	 */
	public void setStartOn(StartOn startOn) {
		this.startOn = startOn;
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
	 * Sets the {@link JettyCustomizer} that can be used to programmatically 
	 * customize the {@link Server} instance before it is started.
	 * 
	 * @param jettyCustomizer the new jetty customizer
	 */
	public void setJettyCustomizer(JettyCustomizer jettyCustomizer) {
		this.jettyCustomizer = jettyCustomizer;
	}
	/**
	 * Returns the {@link JettyCustomizer} that can be used to programmatically 
	 * customize the {@link Server} instance before it is started..
	 * @return the jetty customizer or <code>null</code>, if not specified
	 */
	public JettyCustomizer getJettyCustomizer() {
		return jettyCustomizer;
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
