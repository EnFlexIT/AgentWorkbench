package de.enflexit.awb.ws.core.util;

import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
import de.enflexit.awb.ws.core.JettyAttribute;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyConstants;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.common.fileConfiguration.FileConfigurationService;
import de.enflexit.common.fileConfiguration.FileProcessingResult;
import de.enflexit.common.fileConfiguration.UploadedFile;

/**
 * The Class FileConfigurationServiceJettyConfiguration.
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class FileConfigurationServiceJettyConfiguration implements FileConfigurationService {

	private FileProcessingResult fileProcessingResult;
	private JettyConfiguration jettyConfig;
	
	
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
		
		jettyConfig = JettyConfiguration.load(file2Process.getInputStream());

		this.validateProperties();
		if (this.getFileProcessingResult().getErrorList().size() > 0) {
			this.getFileProcessingResult().setMessage("Properties are not valid");
			return this.getFileProcessingResult();
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
	 * Apply the jetty configuration and restart the server.
	 * @param newJettyConfiguration the new JettyConfiguration
	 */
	private void applyJettyConfiguration(JettyConfiguration newJettyConfiguration) {
		
		try {
			// --- Wait for response to be sent -----------------------------------------
			Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String serverName = newJettyConfiguration.getServerName();
		AwbWebServerServiceWrapper serviceWrapped = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(serverName);
		if (serviceWrapped == null) return;

		JettyConfiguration oldJettyConfiguration = serviceWrapped.getJettyConfiguration();
		if (this.startServer(newJettyConfiguration) == false) {
			this.startServer(oldJettyConfiguration);
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
	
	
	/**
	 * Checks for valid properties. Error messages are stored within
	 * the fileProcessingresult.
	 *
	 * @param jettyConfig the jetty config
	 */
	private void validateProperties() {
		
		// --- General Checks -----------------------------------------------------------
		if (jettyConfig == null) {
			this.getFileProcessingResult().addError("Jetty configuration could not be loaded");
			return;
		}
		if (jettyConfig.getServerName() == null || jettyConfig.getServerName().isBlank()) {
			this.getFileProcessingResult().addError("Server name is empty");
			return;
		}
		if (jettyConfig.getStartOn() == null) {
			this.getFileProcessingResult().addError("StartOn not defined");
		}
		
		// ------------------------------------------------------------------------------
		// --- HTTP/ HTTPS --------------------------------------------------------------
		// ------------------------------------------------------------------------------
		
		Boolean httpEnabled = this.getBool(JettyConstants.HTTP_ENABLED);
		this.requireTrue(httpEnabled != null, JettyConstants.HTTP_ENABLED.getJettyKey()+ " is missing");
		
		boolean http = Boolean.TRUE.equals(httpEnabled);
		Integer httpPort = this.getInt(JettyConstants.HTTP_PORT);

		this.requireTrueIf(http == true, httpPort != null, JettyConstants.HTTP_PORT.getJettyKey() + "is missing");
		this.requireTrueIf(http == true , httpPort != null && (httpPort > 1024 && httpPort < 65535), JettyConstants.HTTP_PORT.getJettyKey() + " invalid: " + httpPort);
		
		Boolean httpsEnabled = this.getBool(JettyConstants.HTTPS_ENABLED);
		this.requireTrue(httpsEnabled != null, JettyConstants.HTTPS_ENABLED.getJettyKey()+ " is missing");		
		
		boolean https = Boolean.TRUE.equals(httpsEnabled);
		this.requireTrue(http || https, "Either http or https must be enabled");
		
		Integer httpsPort = this.getInt(JettyConstants.HTTPS_PORT);
		this.requireTrueIf(https == true, httpsPort != null, "https enabled but" + JettyConstants.HTTPS_PORT.getJettyKey() +"  is missing");
		this.requireTrueIf(https == true, httpsPort != null && (httpsPort > 1024 && httpsPort < 65535), JettyConstants.HTTPS_PORT.getJettyKey() + " invalid: " + httpsPort);

		String keystore = this.getString(JettyConstants.SSL_KEYSTORE);
		this.requireTrueIf(https == true, keystore != null, JettyConstants.SSL_KEYSTORE.getJettyKey() + " is missing");
		
		String sslKeyPassword = this.getString(JettyConstants.SSL_KEYPASSWORD);
		this.requireTrueIf(https == true, sslKeyPassword != null, JettyConstants.SSL_KEYPASSWORD.getJettyKey() + " is missing");
		
		String sslProtocol = this.getString(JettyConstants.SSL_PROTOCOL);
		this.requireTrueIf(https == true, sslProtocol != null, JettyConstants.SSL_PROTOCOL.getJettyKey() + " is missing");
		
		
		// ------------------------------------------------------------------------------
		// --- Thread limits ------------------------------------------------------------
		// ------------------------------------------------------------------------------
		
		Integer minThreads = this.getInt(JettyConstants.HTTP_MINTHREADS);
		Integer maxThreads = this.getInt(JettyConstants.HTTP_MAXTHREADS);
		
		this.requireTrueIf(http == true, minThreads != null, JettyConstants.HTTP_MINTHREADS.getJettyKey() + " is missing");
		this.requireTrueIf(http == true, maxThreads != null, JettyConstants.HTTP_MAXTHREADS.getJettyKey() + " is missing");
		
		this.requireTrueIf(http == true, maxThreads != null && maxThreads > 0, JettyConstants.HTTP_MINTHREADS.getJettyKey() + " invalid: " + minThreads);
		
		this.requireTrueIf(http == true, minThreads != null && minThreads > 0, JettyConstants.HTTP_MINTHREADS.getJettyKey() + " invalid: " + minThreads);
		this.requireTrueIf(http == true , (minThreads != null && maxThreads != null) &&  (minThreads < maxThreads), "minThreads can't be smaller than maxThreads");		
		
		// ------------------------------------------------------------------------------
		// --- CORS ---------------------------------------------------------------------
		// ------------------------------------------------------------------------------
		
		Boolean corsEnabled = this.getBool(JettyConstants.CORS_ENABLED);
		this.requireTrue(corsEnabled != null, JettyConstants.CORS_ENABLED.getJettyKey() + " is missing");
		
		boolean cors = Boolean.TRUE.equals(corsEnabled);
		
		String corsOrigins = this.getString(JettyConstants.CORS_ALLOWED_ORIGINS_PARAM);
		this.requireTrueIf(cors == true, corsOrigins != null, "cors enabled but " + JettyConstants.CORS_ALLOWED_ORIGINS_PARAM.getJettyKey() + " is missing");

		String corsMethods = this.getString(JettyConstants.CORS_ALLOWED_METHODS_PARAM);
		this.requireTrueIf(cors == true, corsMethods != null, "cors enabled but " + JettyConstants.CORS_ALLOWED_METHODS_PARAM.getJettyKey() + " is missing");
		
		String corsHeaders = this.getString(JettyConstants.CORS_ALLOWED_HEADERS_PARAM);
		this.requireTrueIf(cors == true, corsHeaders != null, "cors enabled but " + JettyConstants.CORS_ALLOWED_HEADERS_PARAM.getJettyKey() + " is missing");
		
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
	
	// ----------------------------------------------------------------------------------
	// --- Helper methods for validation ------------------------------------------------
	// ----------------------------------------------------------------------------------
	
	/**
	 * Adds the errorMessage if the expression is false while the condition is true.
	 *
	 * @param condition the condition determining if the expression must be true
	 * @param expression2validate the expression which must be true if the condition is true
	 * @param errorMessage the error message to add if the expression is false
	 */
	private void requireTrueIf(boolean condition, boolean expression2validate, String errorMessage) {
	    if (condition == true && expression2validate == false) {
	        this.getFileProcessingResult().addError(errorMessage);
	    }
	}
	
	/**
	 * Adds the errorMessage if the condition is false
	 *
	 * @param condition the condition
	 * @param errorMessage the message
	 */
	private void requireTrue(Boolean condition, String errorMessage) {
	    if (condition == false) {
	        this.getFileProcessingResult().addError(errorMessage);
	    }
	}

	
	//-----------------------------------------------------------------------------------
	// --- From here, helper methods for extracting values ------------------------------
	//-----------------------------------------------------------------------------------
	
	/**
	 * Returns the Boolean value of the JettyAttribute corresponding 
	 * to the specified key, or null, if the attribute can't be found 
	 * or the value not parsed.
	 *
	 * @param key the key
	 * @return the Boolean value of the jettyAttribute corresponding to the specified key
	 */
	private Boolean getBool(JettyConstants key) {

		JettyAttribute<?> attribute = jettyConfig.get(key);

		if (attribute == null || attribute.getValue() == null) {
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

	/**
	 * Returns the String value of the JettyAttribute corresponding 
	 * to the specified key, or null, if the attribute can't be found 
	 * or is not of type String.
	 *
	 * @param key the key
	 * @return the String value of the jettyAttribute corresponding to the specified key
	 */
	private String getString(JettyConstants key) {
		
		JettyAttribute<?> attribute = jettyConfig.get(key);
		if (attribute == null || attribute.getValue() == null) {
			return null;
		}
		Object value = attribute.getValue();
		if (value instanceof String) {
			return (String) value;
		}
		this.getFileProcessingResult().addError("Invalid string value for key: " + key + " (" + value + ")");
		return null;
	}
	
	/**
	 * Returns the Integer value of the JettyAttribute corresponding 
	 * to the specified key, or null, if the attribute can't be found 
	 * or is not a number/ parseable String.
	 *
	 * @param key the key
	 * @return the Boolean value of the jettyAttribute corresponding to the specified key
	 */	
	private Integer getInt(JettyConstants key) {

	    JettyAttribute<?> attribute = jettyConfig.get(key);

	    if (attribute == null || attribute.getValue() == null) {
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
