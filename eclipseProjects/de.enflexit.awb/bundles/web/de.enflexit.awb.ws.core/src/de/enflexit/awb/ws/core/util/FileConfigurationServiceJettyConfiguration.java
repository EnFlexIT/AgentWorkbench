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
	JettyConfiguration jettyConfig;
	
	@Override
	public String getPerformative() {
		return "JettyConfiguration";
	}
	
	@Override
	public FileProcessingResult processFile(UploadedFile file2Process) {
		
		result = new FileProcessingResult();

		jettyConfig = JettyConfiguration.load(file2Process.getBody());

		this.validateProperties();
		if (result.getErrorList().size() > 0) {
			result.setSuccess(false);
			result.setMessage("Properties are not valid");
			return result;
		}
		result.setSuccess(true);
		result.setMessage("Upload validated. Restarting server..");
		new Thread(() -> applyJettyConfiguration(jettyConfig), "Server restart thread").start();

		return result;
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
	
	/**
	 * Checks for valid properties.
	 *
	 * @param jettyConfig the jetty config
	 * @return true, if successful
	 */
	private void validateProperties() {
		
		// --- General Checks -----------------------------------------------------------
		if (jettyConfig == null) {
			result.addError("Jetty configuration could not be loaded");
			return;
		}
		if (jettyConfig.getServerName() == null || jettyConfig.getServerName().isBlank()) {
			result.addError("Server name is empty");
			return;
		}
		if (jettyConfig.getStartOn() == null) {
			result.addError("StartOn not defined");
		}
		// ------------------------------------------------------------------------------
		// --- HTTP/ HTTPS --------------------------------------------------------------
		// ------------------------------------------------------------------------------
		Boolean httpEnabled = this.getBool(JettyConstants.HTTP_ENABLED);
		this.require(httpEnabled != null, JettyConstants.HTTP_ENABLED.getJettyKey()+ " is missing");
		
		boolean http = this.isTrue(httpEnabled);
		Integer httpPort = this.getInt(JettyConstants.HTTPS_PORT);

		this.requireIf(http == true, httpPort != null, JettyConstants.HTTP_PORT.getJettyKey() + "is missing");
		this.requireIf(http == true , httpPort != null && httpPort > 1024 && httpPort < 65535, JettyConstants.HTTP_PORT.getJettyKey() + " invalid: " + httpPort);
		
		Boolean httpsEnabled = this.getBool(JettyConstants.HTTPS_ENABLED);
		this.require(httpsEnabled != null, JettyConstants.HTTPS_ENABLED.getJettyKey()+ " is missing");		
		
		boolean https = this.isTrue(httpsEnabled);
		this.require(http || https, "Either http or https must be enabled");
		
		Integer httpsPort = this.getInt(JettyConstants.HTTPS_PORT);
		this.requireIf(https == true, httpsPort != null, "https enabled but" + JettyConstants.HTTPS_PORT.getJettyKey() +"  is missing");
		this.requireIf(https == true && httpsPort != null, httpsPort > 1024 && httpsPort < 65535, JettyConstants.HTTPS_PORT.getJettyKey() + " invalid: " + httpsPort);

		String keystore = this.getString(JettyConstants.SSL_KEYSTORE);
		this.requireIf(https == true, keystore != null, JettyConstants.SSL_KEYSTORE.getJettyKey() + " is missing");
		
		String sslKeyPassword = this.getString(JettyConstants.SSL_KEYPASSWORD);
		this.requireIf(https == true, sslKeyPassword != null, JettyConstants.SSL_KEYPASSWORD.getJettyKey() + " is missing");
		
		String sslProtocol = this.getString(JettyConstants.SSL_PROTOCOL);
		this.requireIf(https == true, sslProtocol != null, JettyConstants.SSL_PROTOCOL.getJettyKey() + " is missing");
		
		
		// ------------------------------------------------------------------------------
		// --- Thread limits ------------------------------------------------------------
		Integer minThreads = this.getInt(JettyConstants.HTTP_MINTHREADS);
		Integer maxThreads = this.getInt(JettyConstants.HTTP_MAXTHREADS);
		
		this.requireIf(http == true, minThreads != null, JettyConstants.HTTP_MINTHREADS.getJettyKey() + " is missing");
		this.requireIf(http == true, maxThreads != null, JettyConstants.HTTP_MAXTHREADS.getJettyKey() + " is missing");
		
		this.requireIf(http == true && maxThreads != null, maxThreads > 0, JettyConstants.HTTP_MINTHREADS.getJettyKey() + " invalid: " + minThreads);
		
		this.requireIf(http == true && minThreads != null, minThreads > 0, JettyConstants.HTTP_MINTHREADS.getJettyKey() + " invalid: " + minThreads);
		this.requireIf(http == true && minThreads != null && maxThreads != null, minThreads > maxThreads, "minThreads can't be smaller than maxThreads");		
		
		// ------------------------------------------------------------------------------
		// --- CORS ---------------------------------------------------------------------
		Boolean corsEnabled = this.getBool(JettyConstants.CORS_ENABLED);
		this.require(corsEnabled != null, JettyConstants.CORS_ENABLED.getJettyKey() + " is missing");
		
		boolean cors = this.isTrue(corsEnabled);
		
		String corsOrigins = this.getString(JettyConstants.CORS_ALLOWED_ORIGINS_PARAM);
		this.requireIf(cors == true, corsOrigins != null, "cors enabled but " + JettyConstants.CORS_ALLOWED_ORIGINS_PARAM.getJettyKey() + " is missing");

		String corsMethods = this.getString(JettyConstants.CORS_ALLOWED_METHODS_PARAM);
		this.requireIf(cors == true, corsMethods != null, "cors enabled but " + JettyConstants.CORS_ALLOWED_METHODS_PARAM.getJettyKey() + " is missing");
		
		String corsHeaders = this.getString(JettyConstants.CORS_ALLOWED_HEADERS_PARAM);
		this.requireIf(cors == true, corsHeaders != null, "cors enabled but " + JettyConstants.CORS_ALLOWED_HEADERS_PARAM.getJettyKey() + " is missing");
		
	}

	// ----------------------------------------------------------------------------------
	// --- Helper methods for validation ------------------------------------------------
	// ----------------------------------------------------------------------------------
	
	private void requireIf(boolean condition, boolean validIf, String errorMessage) {
	    if (condition == true && validIf == false) {
	        result.addError(errorMessage);
	    }
	}
	
	private void require(Boolean condition, String message) {
	    if (condition == false) {
	        result.addError(message);
	    }
	}

	private boolean isTrue(Boolean b) {
	    return Boolean.TRUE.equals(b);
	}
	
	//-----------------------------------------------------------------------------------
	// --- From here, helper methods for extracting values ------------------------------
	//-----------------------------------------------------------------------------------
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
		result.addError("Invalid boolean value for key: " + key + " (" + value + ")");
		return null;
	}
	
	private String getString(JettyConstants key) {
		
		JettyAttribute<?> attribute = jettyConfig.get(key);
		if (attribute == null || attribute.getValue() == null) {
			return null;
		}
		Object value = attribute.getValue();
		if (value instanceof String) {
			return (String) value;
		}
		result.addError("Invalid string value for key: " + key + " (" + value + ")");
		return null;
	}
	
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
	    result.addError("Invalid integer value for key: " + key + " (" + value + ")");
	    return null;
	}

}
