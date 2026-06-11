package de.enflexit.awb.ws.core.util;

import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
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
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class FileConfigurationServiceJettyConfiguration implements FileConfigurationService {

	private FileProcessingResult fileProcessingResult;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.fileConfiguration.FileConfigurationService#getPerformative()
	 */
	@Override
	public String getPerformative() {
		return "JettyConfiguration";
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.fileConfiguration.FileConfigurationService#processFile(de.enflexit.common.fileConfiguration.UploadedFile)
	 */
	@Override
	public FileProcessingResult processFile(UploadedFile file2Process) {
		
		JettyConfiguration jettyConfig = JettyConfiguration.load(file2Process.getInputStream());

		if (this.hasValidProperties(jettyConfig) == false) {
			this.getFileProcessingResult().setSuccess(false);
			this.getFileProcessingResult().setMessage("Properties are not valid");
		}
		this.getFileProcessingResult().setSuccess(true);
		this.getFileProcessingResult().setMessage("Upload validated. Restarting server..");
		new Thread(() -> applyJettyConfiguration(jettyConfig), "Server restart thread").start();

		// --- Get return value ---------------------------
		FileProcessingResult fpr = this.getFileProcessingResult();
		this.setFileProcessingResult(null);
		return fpr;
	}
	
	
	/**
	 * Returns the file processing result.
	 * @return the file processing result
	 */
	private FileProcessingResult getFileProcessingResult() {
		if (fileProcessingResult==null) {
			fileProcessingResult = new FileProcessingResult();
		}
		return fileProcessingResult;
	}
	/**
	 * Sets the file processing result.
	 * @param fileProcessingResult the new file processing result
	 */
	private void setFileProcessingResult(FileProcessingResult fileProcessingResult) {
		this.fileProcessingResult = fileProcessingResult;
	}
	
	/**
	 * Checks for valid properties.
	 *
	 * @param jettyConfig the jetty config
	 * @return true, if successful
	 */
	private boolean hasValidProperties(JettyConfiguration jettyConfig) {
		
		if (jettyConfig == null) this.getFileProcessingResult().addError("Jetty configuration could not be loaded");
		if (jettyConfig.getServerName() == null || jettyConfig.getServerName().isBlank()) {
			this.getFileProcessingResult().addError("Server name is empty");
		}
		
		Boolean httpEnabled = this.getBool(jettyConfig, JettyConstants.HTTP_ENABLED);
		Boolean httpsEnabled = this.getBool(jettyConfig, JettyConstants.HTTPS_ENABLED);
		if (httpEnabled == null || httpsEnabled == null) {
			return false;
		}
		if (httpEnabled == false && httpsEnabled == false) {
			this.getFileProcessingResult().addError("http and https disabled");
		}
		
		if (httpEnabled == true) {
			Integer httpPort = this.getInt(jettyConfig, JettyConstants.HTTP_PORT);
			if (httpPort == null) {
				return false;
			}
			if (httpPort < 1024 || httpPort > 65535) {
				this.getFileProcessingResult().addError("Invalid http port: " + httpPort);
				return false;
			}
		}
		if (httpsEnabled == true) {
			Integer httpsPort = this.getInt(jettyConfig, JettyConstants.HTTPS_PORT);
			if (httpsPort == null) {
				return false;
			}
			if (httpsPort < 1024 || httpsPort > 65535) {
				this.getFileProcessingResult().addError("Invalid http port: " + httpsPort);
				return false;
			}
			String keystore = this.getString(jettyConfig, JettyConstants.SSL_KEYSTORE);
			
		}		

		return true;
	}

	/**
	 * Apply the jetty configuration and restart the server.
	 * @param newJettyConfiguration the new JettyConfiguration
	 */
	private void applyJettyConfiguration(JettyConfiguration newJettyConfiguration) {
		try {
			
			// --- WEhy the fuck sleep now ? ----
			Thread.sleep(1000);
			
			String serverName = newJettyConfiguration.getServerName();
			AwbWebServerServiceWrapper serviceWrapped = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(serverName);
			if (serviceWrapped==null) return;
			
			JettyConfiguration oldJettyConfiguration = serviceWrapped.getJettyConfiguration(); 
			if (this.startServer(newJettyConfiguration)==false) {
				this.startServer(oldJettyConfiguration);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Starts a Jetty server based on the specified JettyConfiguration.
	 *
	 * @param jettyConfiguration the jetty configuration
	 * @return true, if successful
	 */
	private boolean startServer(JettyConfiguration jettyConfiguration) {
		
		String serverName = jettyConfiguration.getServerName();
		
		AwbWebServerServiceWrapper serviceWrapped = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(serverName);
		serviceWrapped.setJettyConfiguration(jettyConfiguration);
		
		JettyServerManager.getInstance().stopServer(serverName);
		return JettyServerManager.getInstance().startServer(serverName);
	}
	
	
	private Boolean getBool(JettyConfiguration config, JettyConstants key) {

		JettyAttribute<?> attribute = config.get(key);

		if (attribute == null || attribute.getValue() == null) {
			this.getFileProcessingResult().addError("No value found for key: " + key);
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
		this.getFileProcessingResult().addError("Invalid boolean value for key: " + key + " (" + value + ")");
		return null;
	}
	
	private String getString(JettyConfiguration jettyConfig, JettyConstants key) {
		
		JettyAttribute<?> attribute = jettyConfig.get(key);
		if (attribute == null || attribute.getValue() == null) {
			this.getFileProcessingResult().addError("No attribute found for the key: "+ key);
			return null;
		}
		Object value = attribute.getValue();
		if (value instanceof String) {
			return (String) value;
		}
		this.getFileProcessingResult().addError("Invalid string value for key: " + key + " (" + value + ")");
		return null;
	}
	
	private Integer getInt(JettyConfiguration config, JettyConstants key) {

	    JettyAttribute<?> attribute = config.get(key);

	    if (attribute == null || attribute.getValue() == null) {
	        this.getFileProcessingResult().addError("No value found for key: " + key);
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
	    this.getFileProcessingResult().addError("Invalid integer value for key: " + key + " (" + value + ")");
	    return null;
	}

}
