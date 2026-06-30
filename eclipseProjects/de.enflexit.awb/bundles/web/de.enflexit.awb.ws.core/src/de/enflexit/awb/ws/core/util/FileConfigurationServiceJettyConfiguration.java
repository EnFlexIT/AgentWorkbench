package de.enflexit.awb.ws.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.TreeMap;

import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
import de.enflexit.awb.ws.core.JettyAttribute;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyConstants;
import de.enflexit.awb.ws.core.JettySecuritySettings;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.core.ServletSecurityConfiguration;
import de.enflexit.awb.ws.core.security.OIDCSecurityService.OIDCParameter;
import de.enflexit.awb.ws.core.security.jwt.JwtSingleUserSecurityService.JwtParameter;
import de.enflexit.awb.ws.server.AwbServer;
import de.enflexit.common.fileConfiguration.FileConfigurationService;
import de.enflexit.common.fileConfiguration.FileDownload;
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
	* @see de.enflexit.common.fileConfiguration.FileConfigurationService#getCurrentConfigurationFile()
	*/
	@Override
	public FileDownload getCurrentConfigurationFile() {
		
		//TODO Always AWBServer.NAME? Where should the name come from?
		JettyConfiguration jettyConfig = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME).getJettyConfiguration();
		if (jettyConfig == null) {
			return null;
		}
		
		File currentConfigurationFile = JettyConfiguration.getFile(jettyConfig);
		byte[] content;
		try {
			content = Files.readAllBytes(currentConfigurationFile.toPath());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
		FileDownload fileDownload = new FileDownload();
		fileDownload.setFileName(currentConfigurationFile.getName());
		fileDownload.setContentType(FileDownload.XML_CONTENT_TYPE);
		fileDownload.setContent(content);
		return fileDownload;
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
			this.getFileProcessingResult().setMessage("Validation error.");
			FileProcessingResult fpr = this.getFileProcessingResult();
			this.setFileProcessingResult(null);
			return fpr;
		}
		
		this.getFileProcessingResult().setSuccess(true);
		this.getFileProcessingResult().setMessage("Upload validated. Restarting server..");
		// --- Try to apply the configuration and restart the server --------------------
		new Thread(() -> applyJettyConfiguration(jettyConfig), "Server restart thread").start();

		return this.getFileProcessingResult();
	}

	/**
	 * Apply the jetty configuration and restart the server.
	 * 
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
			this.getFileProcessingResult().addError("Server name is missing");
		}
		if (jettyConfig.getStartOn() == null) {
			this.getFileProcessingResult().addError("StartOn is missing");
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
		this.requireTrueIf(https == true, keyStore != null, "https enabled but" + JettyConstants.SSL_KEYSTORE.getJettyKey() + " is missing");
		
		String sslKeyPassword = this.getString(JettyConstants.SSL_KEYPASSWORD);
		
		// --- Check sslKeyPassword != null ---------------
		this.requireTrueIf(https == true, sslKeyPassword != null, "https enabled but" + JettyConstants.SSL_KEYPASSWORD.getJettyKey() + " is missing");
		
		String sslProtocol = this.getString(JettyConstants.SSL_PROTOCOL);
		
		// --- Check sslProtocol != null ------------------
		this.requireTrueIf(https == true, sslProtocol != null,  "https enabled but" +JettyConstants.SSL_PROTOCOL.getJettyKey() + " is missing");
		
		
		// ------------------------------------------------------------------------------
		// --- Thread limits ------------------------------------------------------------
		// ------------------------------------------------------------------------------
		Integer minThreads = this.getInt(JettyConstants.HTTP_MINTHREADS);
		Integer maxThreads = this.getInt(JettyConstants.HTTP_MAXTHREADS);
		
		// --- Check minThreads != null -------------------
		this.requireTrueIf(http == true, minThreads != null, "http enabled but" + JettyConstants.HTTP_MINTHREADS.getJettyKey() + " is missing");
		
		// --- Check maxThreads != null -------------------
		this.requireTrueIf(http == true, maxThreads != null, "http enabled but" + JettyConstants.HTTP_MAXTHREADS.getJettyKey() + " is missing");
		
		// --- Check maxThreads > 0 -----------------------
		this.requireTrueIf(http == true, maxThreads != null && maxThreads > 0, "http enabled but" + JettyConstants.HTTP_MINTHREADS.getJettyKey() + " invalid: " + minThreads);
		
		// --- Check minThreads > 0 -----------------------
		this.requireTrueIf(http == true, minThreads != null && minThreads > 0, "http enabled but" + JettyConstants.HTTP_MINTHREADS.getJettyKey() + " invalid: " + minThreads);
		
		// --- Check minThreads < maxThreads---------------
		this.requireTrueIf(http == true , (minThreads != null && maxThreads != null) &&  (minThreads < maxThreads), "minThreads can't be smaller than maxThreads");		
		
		// ------------------------------------------------------------------------------
		// --- CORS ---------------------------------------------------------------------
		// ------------------------------------------------------------------------------
		Boolean corsEnabled = this.getBool(JettyConstants.CORS_ENABLED);
		this.requireTrue(corsEnabled != null, JettyConstants.CORS_ENABLED.getJettyKey() + " is missing");
		// --- Get the boolean value avoiding null --------
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
		
		// ------------------------------------------------------------------------------
		// --- Security Settings --------------------------------------------------------
		// ------------------------------------------------------------------------------
		JettySecuritySettings securitySettings = jettyConfig.getSecuritySettings();
		ServletSecurityConfiguration servletConfig = securitySettings.getActivedServletSecurityConfiguration(JettySecuritySettings.ID_SERVER_SECURITY);
		
		// --- General settings ---------------------------------------------------------
		Boolean securityHandlerEnabled = servletConfig.isSecurityHandlerActivated();
		// --- Check securityHandlerEnabled != null -------
		this.requireTrue(securityHandlerEnabled != null, "securityHandlerActivated is missing");
		// --- Get the boolean value avoiding null --------
		boolean securityEnabled = Boolean.TRUE.equals(securityHandlerEnabled);
		String handlerName = servletConfig.getSecurityHandlerName();
		// --- Check handler name != null -----------------
		this.requireTrueIf(securityEnabled == true, handlerName != null, "security handler activated but handler name is missing");
		
		// --- Security handler configuration -------------------------------------------
		if (securityEnabled == true) {
			TreeMap<String, String> securityHandlerConfiguration = servletConfig.getSecurityHandlerConfiguration();
			this.validateSecurityConfiguration(securityHandlerConfiguration, handlerName);
		}
		
	}

	
	/**
	 * Checks if there are values missing in the specified security configuration.
	 *
	 * @param securityHandlerConfiguration the security handler configuration
	 * @param handlerName the handler name
	 */
	private void validateSecurityConfiguration(TreeMap<String, String> securityHandlerConfiguration, String handlerName) {
		switch (handlerName) {
		case "JwtSingleUserSecurityHandler":
			// --- Check whether essential values are null ------------------------------
			boolean userName = securityHandlerConfiguration.get(JwtParameter.UserName.getKey()) != null;
			boolean password = securityHandlerConfiguration.get(JwtParameter.Password.getKey()) != null;
			boolean jwtIssuer = securityHandlerConfiguration.get(JwtParameter.JwtIssuer.getKey()) != null;
			boolean jwtSecrete = securityHandlerConfiguration.get(JwtParameter.JwtSecrete.getKey()) != null;
			boolean jwtValidityPeriod = securityHandlerConfiguration.get(JwtParameter.JwtValidityPeriod.getKey()) != null;
			// --- If any value is false (null) add the error message -------------------
			this.requireTrue((userName && password && jwtIssuer  && jwtSecrete  && jwtValidityPeriod), "Missing jwt configuration");
			break;
			
		case "OIDCSecurityHandler":
			// --- Check whether essential values are null ------------------------------
			boolean issuer = securityHandlerConfiguration.get(OIDCParameter.Issuer.getKey()) != null;
			boolean tokenEndpoint = securityHandlerConfiguration.get(OIDCParameter.TokenEndpoint.getKey()) != null;
			boolean clientId = securityHandlerConfiguration.get(OIDCParameter.ClientID.getKey()) != null;
			boolean clientSecrete = securityHandlerConfiguration.get(OIDCParameter.ClientSecrete.getKey()) != null;
			// --- If any value is false (null) add the error message -------------------
			this.requireTrue(issuer && tokenEndpoint && clientId && clientSecrete, "Missing OIDC configuration");
			break;
		}
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