package de.enflexit.awb.ws.core.util;

import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyCustomizer;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.server.AwbServer;
import de.enflexit.common.fileConfiguration.FileConfigurationService;

import java.io.InputStream;


public class FileConfigurationServiceJettyConfiguration implements FileConfigurationService {

	@Override
	public String getPerformative() {
		return "JettyConfiguration";
	}
	
	@Override
	public boolean processFile(InputStream file2Process) {

//    	try {
//    		InputStream inputStream = file2Process.getEntityAs(InputStream.class);
//    		JettyConfiguration jettyConfig = JettyConfiguration.load(inputStream);
//    		
//    		if (this.isWellFormedXml(jettyConfig)== false) return false;
//    		if (this.hasValidProperties(jettyConfig) == false) return false;
//    			new Thread(new Runnable() {
//    				
//    				@Override
//    				public void run() {
//    					try {
//    						Thread.sleep(1000);
//    						JettyServerManager.getInstance().stopServer(jettyConfig.getServerName());
//    						JettyCustomizer customizer = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME).getJettyConfigurationFromPropertiesFile().getJettyCustomizer();
//    						JettyConfiguration.save(jettyConfig);
//    						jettyConfig.setJettyCustomizer(customizer);
//    						JettyServerManager.getInstance().startServer(jettyConfig);
//    					} catch (Exception ex) {
//    						ex.printStackTrace();
//    					}
//    				}
//    			}, "Server-restart-Thread").start();
//    	} catch (Exception ex) {
//    		ex.printStackTrace();
//    	}
		return true;
	}
	

	/**
	 * Checks if is well formed xml.
	 *
	 * @param config2Validate the config 2 validate
	 * @return true, if is well formed xml
	 */
	private boolean isWellFormedXml(JettyConfiguration config2Validate) {
		return true;
	}
	
	/**
	 * Checks for valid properties.
	 *
	 * @param jettyConfig the jetty config
	 * @return true, if successful
	 */
	private boolean hasValidProperties(JettyConfiguration jettyConfig) {
		return true;
	}


}
