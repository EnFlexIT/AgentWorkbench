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
		
		// --- Server just restarted, just giving back the result -----------------------
		if (fileProcessingResult != null) {
			FileProcessingResult fpr = this.getFileProcessingResult();
			this.setFileProcessingResult(null);
			return fpr;
			
		}
		
		jettyConfig = JettyConfiguration.load(file2Process.getInputStream());

		this.validateProperties();
		// --- Set results based on validation ------------------------------------------
		if (this.getFileProcessingResult().getErrorList().size() > 0) {
			this.getFileProcessingResult().setMessage("Error while validating.");
			return this.getFileProcessingResult();
		}
		
		this.getFileProcessingResult().setSuccess(true);
		this.getFileProcessingResult().setMessage("Upload validated. Restarting server..");
		// --- Try to apply the configuration and restart the server --------------------
		new Thread(() -> applyJettyConfiguration(jettyConfig), "Server restart thread").start();

		return this.getFileProcessingResult();
	}

	/**
	 * Apply the jetty configuration and restart the server.
	 * @param newJettyConfiguration the new JettyConfiguration
	 */
	private void applyJettyConfiguration(JettyConfiguration newJettyConfiguration) {
		
		// --- Wait for response to be sent ---------------------------------------------
		try {
			Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// --- Look if there is a WebServerService for the new Server name --------------
		String serverName = newJettyConfiguration.getServerName();
		AwbWebServerServiceWrapper serviceWrapped = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(serverName);
		// --- If there is no service, set the result accordingly -----------------------
		if (serviceWrapped == null) {
			this.getFileProcessingResult().setSuccess(false);
			this.getFileProcessingResult().setMessage("No webserver service found for the servername: " + serverName);
			return;
		}

		// --- Keep the current jettyConfiguration to revert back to --------------------
		JettyConfiguration oldJettyConfiguration = serviceWrapped.getJettyConfiguration();
		// --- Server couldn't be startet with the new config ---------------------------
		if (this.restartServer(newJettyConfiguration) == false) {
			// --- Set result accordingly and revert back to the old configuration ------
			this.getFileProcessingResult().setSuccess(false);
			this.getFileProcessingResult().setMessage("Error while restarting the server. Reverting back to old configuration.");
			this.restartServer(oldJettyConfiguration);
			// --- Server restarted with the new configuration, set the result ----------
		} else {
			this.getFileProcessingResult().setSuccess(true);
			this.getFileProcessingResult().setMessage("New configuration applied.");
		}
		
	}
	
	/**
	 * Starts a Jetty server based on the specified JettyConfiguration.
	 *
	 * @param jettyConfiguration the jetty configuration
	 * @return true, if successful
	 */
	private boolean restartServer(JettyConfiguration jettyConfiguration) {
		
		// --- Get the webServerServicewrapper ------------------------------------------
		String serverName = jettyConfiguration.getServerName();
		AwbWebServerServiceWrapper serviceWrapped = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(serverName);
		// --- Set the configuration to start the Server with ---------------------------
		serviceWrapped.setJettyConfiguration(jettyConfiguration);
		
		// --- Restart the server and return the result ---------------------------------
		JettyServerManager.getInstance().stopServer(serverName);
		return JettyServerManager.getInstance().startServer(serverName);
	}
	
	
	/**
	 * Checks for valid properties. Error messages are stored within
	 * the fileProcessingresult.
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
		
		// --- Check httpPort != null ---------------------
		this.requireTrueIf(http == true, httpPort != null, JettyConstants.HTTP_PORT.getJettyKey() + "is missing");
		
		// --- Check httpPort > 1024 and < 65535 ----------
		this.requireTrueIf(http == true , httpPort != null && (httpPort > 1024 && httpPort < 65535), JettyConstants.HTTP_PORT.getJettyKey() + " invalid: " + httpPort);
		
		Boolean httpsEnabled = this.getBool(JettyConstants.HTTPS_ENABLED);
		this.requireTrue(httpsEnabled != null, JettyConstants.HTTPS_ENABLED.getJettyKey()+ " is missing");		
		
		boolean https = Boolean.TRUE.equals(httpsEnabled);
		this.requireTrue(http || https, "Either http or https must be enabled");
		
		Integer httpsPort = this.getInt(JettyConstants.HTTPS_PORT);
		
		// --- Check httpsPort != null --------------------
		this.requireTrueIf(https == true, httpsPort != null, "https enabled but" + JettyConstants.HTTPS_PORT.getJettyKey() +"  is missing");
		
		// --- Check httpPort > 1024 and < 65535 ----------
		this.requireTrueIf(https == true, httpsPort != null && (httpsPort > 1024 && httpsPort < 65535), JettyConstants.HTTPS_PORT.getJettyKey() + " invalid: " + httpsPort);

		String keyStore = this.getString(JettyConstants.SSL_KEYSTORE);
		
		// --- Check keyStore != null ---------------------
		this.requireTrueIf(https == true, keyStore != null, JettyConstants.SSL_KEYSTORE.getJettyKey() + " is missing");
		
		String sslKeyPassword = this.getString(JettyConstants.SSL_KEYPASSWORD);
		
		// --- Check sslKeyPassword != null ---------------
		this.requireTrueIf(https == true, sslKeyPassword != null, JettyConstants.SSL_KEYPASSWORD.getJettyKey() + " is missing");
		
		String sslProtocol = this.getString(JettyConstants.SSL_PROTOCOL);
		
		// --- Check sslProtocol != null ------------------
		this.requireTrueIf(https == true, sslProtocol != null, JettyConstants.SSL_PROTOCOL.getJettyKey() + " is missing");
		
		
		// ------------------------------------------------------------------------------
		// --- Thread limits ------------------------------------------------------------
		// ------------------------------------------------------------------------------
		Integer minThreads = this.getInt(JettyConstants.HTTP_MINTHREADS);
		Integer maxThreads = this.getInt(JettyConstants.HTTP_MAXTHREADS);
		
		// --- Check minThreads != null -------------------
		this.requireTrueIf(http == true, minThreads != null, JettyConstants.HTTP_MINTHREADS.getJettyKey() + " is missing");
		
		// --- Check maxThreads != null -------------------
		this.requireTrueIf(http == true, maxThreads != null, JettyConstants.HTTP_MAXTHREADS.getJettyKey() + " is missing");
		
		// --- Check maxThreads > 0 -----------------------
		this.requireTrueIf(http == true, maxThreads != null && maxThreads > 0, JettyConstants.HTTP_MINTHREADS.getJettyKey() + " invalid: " + minThreads);
		
		// --- Check minThreads > 0 -----------------------
		this.requireTrueIf(http == true, minThreads != null && minThreads > 0, JettyConstants.HTTP_MINTHREADS.getJettyKey() + " invalid: " + minThreads);
		
		// --- Check minThreads < maxThreads---------------
		this.requireTrueIf(http == true , (minThreads != null && maxThreads != null) &&  (minThreads < maxThreads), "minThreads can't be smaller than maxThreads");		
		
		// ------------------------------------------------------------------------------
		// --- CORS ---------------------------------------------------------------------
		// ------------------------------------------------------------------------------
		Boolean corsEnabled = this.getBool(JettyConstants.CORS_ENABLED);
		this.requireTrue(corsEnabled != null, JettyConstants.CORS_ENABLED.getJettyKey() + " is missing");
		
		boolean cors = Boolean.TRUE.equals(corsEnabled);
		
		
		// --- Check corsOrigins != null ------------------
		String corsOrigins = this.getString(JettyConstants.CORS_ALLOWED_ORIGINS_PARAM);
		this.requireTrueIf(cors == true, corsOrigins != null, "cors enabled but " + JettyConstants.CORS_ALLOWED_ORIGINS_PARAM.getJettyKey() + " is missing");
		
		// --- Check corsMethods != null ------------------
		String corsMethods = this.getString(JettyConstants.CORS_ALLOWED_METHODS_PARAM);
		this.requireTrueIf(cors == true, corsMethods != null, "cors enabled but " + JettyConstants.CORS_ALLOWED_METHODS_PARAM.getJettyKey() + " is missing");
		
		// --- Check corsHeaders != null ------------------
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
		// --- Attribute not found ------------------------------------------------------
		if (attribute == null || attribute.getValue() == null) {
			return null;
		}
		// --- Try to get and return the String value of the attribute ------------------
		Object value = attribute.getValue();
		if (value instanceof String) {
			return (String) value;
		}
		// --- value is not of type String ----------------------------------------------
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
		// --- Attribute not found ------------------------------------------------------
	    if (attribute == null || attribute.getValue() == null) {
	        return null;
	    }
		// --- Is the value of type Integer? --------------------------------------------
	    Object value = attribute.getValue();
	    if (value instanceof Integer) {
	        return (Integer) value;
	    }
		// --- Is the value of type Number? ---------------------------------------------
	    if (value instanceof Number) {
	        return ((Number) value).intValue();
	    }
	    // --- Fallback: try to parse it as a String ------------------------------------
	    if (value instanceof String) {
	        try {
	            return Integer.parseInt(((String) value).trim());
	        } catch (NumberFormatException e) {
	            // --- Nothing to do, error will be added below -------------------------
	        }
	    }
	    // --- Value is neither a Number nor a String -----------------------------------
	    this.getFileProcessingResult().addError("Invalid integer value for key: " + key + " (" + value + ")");
	    return null;
	}

}