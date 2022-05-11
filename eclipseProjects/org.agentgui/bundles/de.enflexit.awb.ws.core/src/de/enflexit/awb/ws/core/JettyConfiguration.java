package de.enflexit.awb.ws.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

import agentgui.core.common.AbstractUserObject;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.common.SerialClone;

/**
 * A JettyConfiguration describes the configuration of a Jetty Server
 * to be started with Agent.Workbench.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JettyConfiguration", propOrder = {
    "serverName",
    "startOn",
    "mutableHandlerCollection",
    "jettySettings"
})
public class JettyConfiguration implements Serializable {

	private static final long serialVersionUID = -425703333358456038L;
	private static final String FILE_ENCODING = "UTF-8";
	
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
	
	private String serverName;
	private StartOn startOn;
	private boolean mutableHandlerCollection;

	private TreeMap<String, JettyAttribute<?>> jettySettings;
	
	private transient Handler handler;
	private transient JettyCustomizer jettyCustomizer;
	
	
	/**
	 * <b>Do not use ! - Only for the file loading of JAXB.</b><br>
	 * Instantiates a new jetty configuration.
	 */
	@Deprecated
	public JettyConfiguration() { }
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
		this.setMutableHandlerCollection(useMutableHandlerCollection);
		this.setHandler(handler);
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
	 * Returns the jetty settings as sorted list.
	 * The preferred order is adjusted within the enumeration {@link JettyConstants}.  
	 * @return the jetty settings sorted
	 */
	public List<JettyAttribute<?>> getJettySettingsSorted() {
		ArrayList<JettyAttribute<?>> settings = new ArrayList<>(this.getJettySettings().values());
		Collections.sort(settings);
		return settings;
	}
	
	/**
	 * Returns the jetty settings.
	 * @return the jetty settings
	 */
	public TreeMap<String, JettyAttribute<?>> getJettySettings() {
		if (jettySettings==null) {
			jettySettings = new TreeMap<String, JettyAttribute<?>>();
		}
		return jettySettings;
	}
	/**
	 * Puts the specified JettyAttribute to the local settings.
	 *
	 * @param key the key
	 * @param attribute the attribute
	 */
	public void put(String key, JettyAttribute<?> attribute) {
		this.getJettySettings().put(key, attribute);
		
	}
	/**
	 * Returns the stored value for the specified key.
	 *
	 * @param jettyConstant the jetty constant
	 * @return the jetty attribute
	 */
	public JettyAttribute<?> get(JettyConstants jettyConstant) {
		return this.getJettySettings().get(jettyConstant.getJettyKey());
	}
	/**
	 * Returns the stored value for the specified key.
	 *
	 * @param key the key
	 * @return the jetty attribute
	 */
	public JettyAttribute<?> get(String key) {
		return this.getJettySettings().get(key);
	}
	/**
	 * Returns the key set.
	 *
	 * @return the list
	 */
	public Set<String> keySet() {
		return this.getJettySettings().keySet();
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
		
		this.setJettyAttribute(new JettyAttribute<Boolean>(JettyConstants.HTTP_ENABLED));
		this.setJettyAttribute(new JettyAttribute<Integer>(JettyConstants.HTTP_PORT));
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.HTTP_HOST));
		
		this.setJettyAttribute(new JettyAttribute<Boolean>(JettyConstants.HTTP_NIO));
		
		this.setJettyAttribute(new JettyAttribute<Boolean>(JettyConstants.HTTPS_ENABLED));
		this.setJettyAttribute(new JettyAttribute<Integer>(JettyConstants.HTTPS_PORT));
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.HTTPS_HOST));
		
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_KEYSTORE));
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_KEYSTORETYPE));

		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_PASSWORD));
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_KEYPASSWORD));
		
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_PROTOCOL));
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.SSL_ALGORITHM));
		
		this.setJettyAttribute(new JettyAttribute<Boolean>(JettyConstants.SSL_NEEDCLIENTAUTH));
		this.setJettyAttribute(new JettyAttribute<Boolean>(JettyConstants.SSL_WANTCLIENTAUTH));
		
		this.setJettyAttribute(new JettyAttribute<Integer>(JettyConstants.HTTP_MINTHREADS));
		this.setJettyAttribute(new JettyAttribute<Integer>(JettyConstants.HTTP_MAXTHREADS));
		
		this.setJettyAttribute(new JettyAttribute<String>(JettyConstants.CONTEXT_PATH));
		this.setJettyAttribute(new JettyAttribute<Integer>(JettyConstants.CONTEXT_SESSIONINACTIVEINTERVAL));	
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
	
	
	/**
	 * Returns a copy of the current instance, while the Handler (see {@link #getHandler()}) and 
	 * the JettyCustomizer (see {@link #getJettyCustomizer()}) will be the same instances.
	 * 
	 * @return the copy of the current JettyConfiguration
	 */
	public JettyConfiguration getCopy(){
		JettyConfiguration jc = SerialClone.clone(this);
		jc.setHandler(this.getHandler());
		jc.setJettyCustomizer(this.getJettyCustomizer());
		return jc;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {

		if (compObj==null || (! (compObj instanceof JettyConfiguration))) return false;
		if (compObj==this) return true;

		
		JettyConfiguration jcComp = (JettyConfiguration) compObj;
		
		if (jcComp.getServerName().equals(this.getServerName())==false) return false;
		if (jcComp.getStartOn()!=this.getStartOn()) return false;
		if (jcComp.isMutableHandlerCollection()!=this.isMutableHandlerCollection()) return false;

		if (jcComp.getJettySettings().equals(this.getJettySettings())==false) return false;

		if (jcComp.getHandler()!=this.getHandler()) return false;
		if (jcComp.getJettyCustomizer()!=this.getJettyCustomizer()) return false;
		
		return true;
	}
	
	
	// ----------------------------------------------------------------------------------
	// --- From here methods to save or load a JettyConfiguration -----------------------
	// ----------------------------------------------------------------------------------
	/**
	 * Returns the file instance for the specified {@link JettyConfiguration}.
	 *
	 * @param jConfig the JettyConfiguration
	 * @return the file for the JettyConfiguration
	 */
	public static File getFile(JettyConfiguration jConfig) {
		
		if (jConfig==null || jConfig.getServerName()==null || jConfig.getServerName().isBlank()) {
			throw new NullPointerException("Either, JettyConfiguration is null or the server name was not set!");
		}
		return new File(BundleHelper.getPathProperties() + jConfig.getServerName() + ".xml");
	}
	
	/**
	 * Saves the current configuration.
	 * @return true, if successful
	 */
	public boolean save() {
		return save(this);
	}
	/**
	 * Saves the specified JettyConfiguration to the specified file.
	 *
	 * @param config the config
	 * @param file the file
	 * @return true, if successful
	 */
	public static boolean save(JettyConfiguration config) {
		
		boolean success = true;
		
		// --- Check the JettyConfiguration instance ------
		if (config==null) {
			System.err.println("[" + JettyConfiguration.class.getSimpleName() + "] No JettyConfiguration instance was specified to be saved!");
			return false;
		} 

		// --- Where to save? -----------------------------
		File file = getFile(config);
		if (file==null) {
			System.err.println("[" + JettyConfiguration.class.getSimpleName() + "] The path for saving a configuration as XML file is not allowed to be null!");
			return false;
		}
		
			
		FileWriter fileWriter = null;
		try {
			// --- Define the JAXB context ------------
			JAXBContext pc = JAXBContext.newInstance(JettyConfiguration.class);
			Marshaller pm = pc.createMarshaller();
			pm.setProperty(Marshaller.JAXB_ENCODING, FILE_ENCODING);
			pm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// --- Write instance to xml-File ---------
			fileWriter = new FileWriter(file);
			pm.marshal(config, fileWriter);
			success = true;
			
		} catch (Exception ex) {
			System.err.println("[" + JettyConfiguration.class.getSimpleName() + "] Error while saving the user object as XML file:");
			ex.printStackTrace();
		} finally {
			try {
				if (fileWriter!=null) fileWriter.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}				
		}
		return success;
	}
	/**
	 * Loads a JettyConfiguration from the specified file.
	 *
	 * @param file the file
	 * @return the jetty configuration
	 */
	public static JettyConfiguration load(File file) {
		
		if (file==null || file.exists()==false) return null;
		
		JettyConfiguration jConfig = null;
		InputStream inputStream = null;
		InputStreamReader isReader = null;
		try {
			
			JAXBContext context = JAXBContext.newInstance(JettyConfiguration.class);
			Unmarshaller unMarsh = context.createUnmarshaller();
			
			inputStream = new FileInputStream(file);
			isReader  = new InputStreamReader(inputStream, FILE_ENCODING);
			
			Object jaxbObject = unMarsh.unmarshal(isReader);
			if (jaxbObject!=null && jaxbObject instanceof JettyConfiguration) {
				jConfig = (JettyConfiguration)jaxbObject;
			}
			
		} catch (Exception ex) {
			System.out.println("[" + AbstractUserObject.class.getSimpleName() + "] Error while loading the user object from XML file:");
			ex.printStackTrace();
		} finally {
			try {
				if (isReader!=null) isReader.close();
				if (inputStream!=null) inputStream.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}	
		}
		return jConfig;
	}

}
