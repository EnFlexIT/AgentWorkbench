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
	private JettyConfiguration jettyConfiguration;
	
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
	 * @return the jetty configuration
	 */
	public JettyConfiguration getJettyConfiguration() {
		if (jettyConfiguration==null) {
			jettyConfiguration = this.getWebServerService().getJettyConfiguration();
			// --- Additionally, try to load the configuration from file ------
			JettyConfiguration jettyConfigurationFromFile = JettyConfiguration.load(JettyConfiguration.getFile(this.jettyConfiguration));
			if (jettyConfigurationFromFile==null) {
				// --- Save current settings ----------------------------------
				JettyConfiguration.save(this.jettyConfiguration);
			} else {
				// --- Set handler and customizer to file settings ------------
				jettyConfigurationFromFile.setHandler(this.jettyConfiguration.getHandler());
				jettyConfigurationFromFile.setJettyCustomizer(this.jettyConfiguration.getJettyCustomizer());
				jettyConfiguration = jettyConfigurationFromFile;
			}
		}
		return jettyConfiguration;
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
