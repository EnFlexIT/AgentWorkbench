package de.enflexit.awb.ws.core.util;

import de.enflexit.awb.ws.core.JettyAttribute;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyConstants;
import de.enflexit.awb.ws.core.JettyCustomizer;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.server.AwbServer;
import de.enflexit.common.fileConfiguration.FileConfigurationService;
import de.enflexit.common.fileConfiguration.FileProcessingResult;
import de.enflexit.common.fileConfiguration.UploadedFile;


/**
 * The Class FileConfigurationServiceJettyConfiguration.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class FileConfigurationServiceJettyConfiguration implements FileConfigurationService {

	FileProcessingResult result;
	
	@Override
	public String getPerformative() {
		return "JettyConfiguration";
	}
	
	@Override
	public FileProcessingResult processFile(UploadedFile file2Process) {
		
		result = new FileProcessingResult();

		JettyConfiguration jettyConfig = JettyConfiguration.load(file2Process.getBody());

		if (this.hasValidProperties(jettyConfig) == false) {
			result.setSuccess(false);
			result.setMessage("Properties are not valid");
		}
		result.setSuccess(true);
		result.setMessage("Upload validated. Restarting server..");
		new Thread(() -> applyJettyConfiguration(jettyConfig), "Server restart thread").start();

		return result;
	}
	
	/**
	 * Checks for valid properties.
	 *
	 * @param jettyConfig the jetty config
	 * @return true, if successful
	 */
	private boolean hasValidProperties(JettyConfiguration jettyConfig) {
		
		if (jettyConfig == null) result.addError("Jetty configuration could not be loaded");
		if (jettyConfig.getServerName() == null || jettyConfig.getServerName().isBlank()) {
			result.addError("Server name is empty");
		}
		
		Boolean httpEnabled = this.getBool(jettyConfig, JettyConstants.HTTP_ENABLED);
		Boolean httpsEnabled = this.getBool(jettyConfig, JettyConstants.HTTPS_ENABLED);
		if (httpEnabled == null || httpsEnabled == null) {
			return false;
		}
		if (httpEnabled == false && httpsEnabled == false) {
			result.addError("http and https disabled");
		}
		
		if (httpEnabled == true) {
			Integer httpPort = this.getInt(jettyConfig, JettyConstants.HTTP_PORT);
			if (httpPort == null) {
				return false;
			}
			if (httpPort < 1024 || httpPort > 65535) {
				result.addError("Invalid http port: " + httpPort);
				return false;
			}
		}
		if (httpsEnabled == true) {
			Integer httpsPort = this.getInt(jettyConfig, JettyConstants.HTTPS_PORT);
			if (httpsPort == null) {
				return false;
			}
			if (httpsPort < 1024 || httpsPort > 65535) {
				result.addError("Invalid http port: " + httpsPort);
				return false;
			}
			String keystore = this.getString(jettyConfig, JettyConstants.SSL_KEYSTORE);
			
		}		
		

		return true;
	}

	/**
	 * Apply the jetty configuration and restart the server.
	 *
	 * @param jettyConfig the jetty config
	 */
	private void applyJettyConfiguration(JettyConfiguration jettyConfig) {
		try {
			Thread.sleep(1000);
			JettyServerManager.getInstance().stopServer(jettyConfig.getServerName());
			JettyCustomizer customizer = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME).getJettyConfigurationFromPropertiesFile().getJettyCustomizer();
			jettyConfig.setJettyCustomizer(customizer);
			JettyConfiguration.save(jettyConfig);
			JettyServerManager.getInstance().startServer(jettyConfig);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private Boolean getBool(JettyConfiguration config, JettyConstants key) {

		JettyAttribute<?> attribute = config.get(key);

		if (attribute == null || attribute.getValue() == null) {
			result.addError("No value found for key: " + key);
			return null;
		}

		Object value = attribute.getValue();
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof String) {
			String valueString = ((String) value).trim().toLowerCase();

			if (valueString.equals("true") || valueString.equals("false")) {
				return Boolean.parseBoolean(valueString);
			}
		}
		result.addError("Invalid boolean value for key: " + key + " (" + value + ")");
		return null;
	}
	
	private String getString(JettyConfiguration jettyConfig, JettyConstants key) {
		
		JettyAttribute<?> attribute = jettyConfig.get(key);
		if (attribute == null || attribute.getValue() == null) {
			result.addError("No attribute found for the key: "+ key);
			return null;
		}
		Object value = attribute.getValue();
		if (value instanceof String) {
			return (String) value;
		}
		result.addError("Invalid string value for key: " + key + " (" + value + ")");
		return null;
	}
	
	private Integer getInt(JettyConfiguration config, JettyConstants key) {

	    JettyAttribute<?> attribute = config.get(key);

	    if (attribute == null || attribute.getValue() == null) {
	        result.addError("No value found for key: " + key);
	        return null;
	    }

	    Object value = attribute.getValue();
	    if (value instanceof Integer) {
	        return (Integer) value;
	    }
	    if (value instanceof Number) {
	        return ((Number) value).intValue();
	    }
	    if (value instanceof String) {
	        try {
	            return Integer.parseInt(((String) value).trim());
	        } catch (NumberFormatException e) {
	            // --- Nothing to do, error will be added below ---------------
	        }
	    }
	    result.addError("Invalid integer value for key: " + key + " (" + value + ")");
	    return null;
	}

}
