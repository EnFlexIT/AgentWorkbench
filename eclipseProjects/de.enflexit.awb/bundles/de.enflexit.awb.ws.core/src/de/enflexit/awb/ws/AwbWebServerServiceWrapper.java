package de.enflexit.awb.ws;

import de.enflexit.awb.ws.core.JettyConfiguration;

/**
 * The Class AwbWebServerServiceWrapper is used to wrap the registered {@link AwbWebServerService}
 * and to save or load the persisted settings.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbWebServerServiceWrapper {

	private AwbWebServerService webServerService;
	private JettyConfiguration jettyConfigurationFromFile;
	private JettyConfiguration jettyConfigurationForEditing;

	
	/**
	 * Instantiates a new wrapper for an {@link AwbWebServerService}.
	 * @param webServerService the actual AwbWebServerService to wrap
	 */
	public AwbWebServerServiceWrapper(AwbWebServerService webServerService) {
		this.setWebServerService(webServerService);
	}
	
	/**
	 * Sets the web server service.
	 * @param webServerService the new web server service
	 */
	private void setWebServerService(AwbWebServerService webServerService) {
		this.webServerService = webServerService;
	}
	/**
	 * Returns the web server service.
	 * @return the web server service
	 */
	public AwbWebServerService getWebServerService() {
		return webServerService;
	}

	
	/**
	 * Returns the {@link JettyConfiguration} of the registered {@link AwbWebServerService}.
	 * @return the JettyConfiguration defined in the corresponding service
	 */
	public JettyConfiguration getJettyConfigurationFromServiceDefinition() {
		return this.getWebServerService().getJettyConfiguration();
	}
	
	/**
	 * Returns the {@link JettyConfiguration}, stored in the file within the AWB-properties directory.
	 * If not available yet, the service configures {@link JettyConfiguration} will be taken and initially 
	 * stored to a file (named as [server name].xml).
	 * .
	 * @return the JettyConfiguration as stored in the properties file
	 */
	public JettyConfiguration getJettyConfigurationFromPropertiesFile() {
		if (jettyConfigurationFromFile==null) {
			jettyConfigurationFromFile = this.getJettyConfigurationFromServiceDefinition();
			// --- Additionally, try to load the configuration from file ------
			JettyConfiguration jcFileRead = JettyConfiguration.load(JettyConfiguration.getFile(this.jettyConfigurationFromFile));
			if (jcFileRead==null) {
				// --- Save current settings ----------------------------------
				JettyConfiguration.save(this.jettyConfigurationFromFile);
			} else {
				// --- Set handler and customizer to file settings ------------
				jcFileRead.setHandler(this.jettyConfigurationFromFile.getHandler());
				jcFileRead.setJettyCustomizer(this.jettyConfigurationFromFile.getJettyCustomizer());
				jettyConfigurationFromFile = jcFileRead;
			}
		}
		return jettyConfigurationFromFile;
	}
	
	/**
	 * Returns the {@link JettyConfiguration} that can be used to edit the JettySettings 
	 * before they are stored in the properties file within the AWB-properties directory.
	 * .
	 * @return a copy of the JettyConfiguration as stored in the corresponding properties file
	 */
	public JettyConfiguration getJettyConfiguration() {
		if (jettyConfigurationForEditing==null) {
			jettyConfigurationForEditing = this.getJettyConfigurationFromPropertiesFile().getCopy();
		}
		return jettyConfigurationForEditing;
	}
	
	
	/**
	 * Reverts the JettyConfiguration to the last stored file version.<br>
	 * Use {@link #getJettyConfiguration()} to get the revised version of the configuration
	 */
	public void revertJettyConfigurationToPropertiesFile() {
		this.jettyConfigurationForEditing = null;
	}
	/**
	 * Reverts the JettyConfiguration to the initial service defined configuration.
	 * Use {@link #getJettyConfiguration()} to get the revised version of the configuration
	 */
	public void revertJettyConfigurationToServiceDefinition() {
		this.jettyConfigurationForEditing = null;
		this.jettyConfigurationFromFile = null;
		this.getJettyConfigurationFromServiceDefinition().save();
	}
	
	
	/**
	 * Saves the currently edited JettySettings.
	 * @return true, if successful
	 */
	public boolean save() {
		if (this.hasChangedJettySettings()==true) {
			this.jettyConfigurationFromFile = this.jettyConfigurationForEditing.getCopy();
			return this.getJettyConfigurationFromPropertiesFile().save();
		}
		return true;
	}
	/**
	 * Checks if the current JettySettings have changed.
	 * @return true, if the settings have changed
	 */
	public boolean hasChangedJettySettings() {
		return ! this.getJettyConfiguration().equals(this.getJettyConfigurationFromPropertiesFile());
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof AwbWebServerService) {
			// --- Compare to registered AwbWebServerService ------------------
			AwbWebServerService comp = (AwbWebServerService) obj;
			return comp.equals(this.getWebServerService());
		} else if (obj instanceof AwbWebServerServiceWrapper) {
			// --- Compare wrapper --------------------------------------------
			AwbWebServerServiceWrapper compWrapper = (AwbWebServerServiceWrapper) obj;
			return compWrapper.getWebServerService().equals(this.getWebServerService());
		}
		return false;
	}

}
