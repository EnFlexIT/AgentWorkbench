package de.enflexit.awb.core.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo.DeviceSystemExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.EmbeddedSystemAgentVisualisation;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.MtpProtocol;
import de.enflexit.awb.core.project.PlatformJadeConfig.MTP_Creation;
import de.enflexit.common.fileConfiguration.FileConfigurationService;
import de.enflexit.common.fileConfiguration.FileDownload;
import de.enflexit.common.fileConfiguration.FileProcessingResult;
import de.enflexit.common.fileConfiguration.UploadedFile;

/**
 * The Class FileConfigurationServiceAwbConfig provides one method to download the current
 * de.enflexit.awb.core.prefs file and one to overwrite the existing one through a
 * file upload. 
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class FileConfigurationServiceAwbConfig implements FileConfigurationService {

	private FileProcessingResult fileProcessingResult;
	private Properties properties;
	
	/* (non-Javadoc)
	* @see de.enflexit.common.fileConfiguration.FileConfigurationService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "AwbConfiguration";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.fileConfiguration.FileConfigurationService#processFile(de.enflexit.common.fileConfiguration.UploadedFile)
	*/
	@Override
	public FileProcessingResult processFile(UploadedFile file2Process) {

		// --- Check if there is an input stream ----------------------------------------
		if (file2Process.getInputStream() == null) {
			this.getFileProcessingResult().setMessage("No input stream found.");
			return this.getFileProcessingResult();
		}
		byte[] content;
		properties = new Properties();
		// --- Load the file as properties ----------------------------------------------
		try {
			content = file2Process.getInputStream().readAllBytes();
			properties.load(new ByteArrayInputStream(content));

		} catch (IOException ioe) {
			ioe.printStackTrace();
			this.getFileProcessingResult().setMessage("Error while loading the file.");
			FileProcessingResult fpr = this.getFileProcessingResult();
			this.reset();
			return fpr;
		}
		
		// --- Validate -----------------------------------------------------------------
		this.validateProperties();
		if (this.getFileProcessingResult().getErrorList().size() > 0) {
			// --- Errors occured while validating, don't apply and return --------------
			this.getFileProcessingResult().setMessage("Validation error.");
			FileProcessingResult fpr = this.getFileProcessingResult();
			this.reset();
			return fpr;
		}
		
		try  {
			// --- Write file ---------------------------------------------------------------
			Files.write(this.getPreferencesFile().toPath(), content);
		} catch (IOException e) {
			e.printStackTrace();
			this.getFileProcessingResult().setMessage("Error while writing new file.");
			FileProcessingResult fpr = this.getFileProcessingResult();
			this.reset();
			return fpr;
		}
		
		this.getFileProcessingResult().setSuccess(true);
		this.getFileProcessingResult().setMessage("File upload successful. Restarting Agent.Workbench...");
		
		// --- Return result while asynchronously restarting ----------------------------
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					// --- Wait for response to be sent ---------------------------------
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Application.restart();
			}
		}).start();
		
		FileProcessingResult fpr = this.getFileProcessingResult();
		this.reset();
		return fpr;
	}

	/**
	 * Checks the properties for crucial missing values. 
	 */
	private void validateProperties() {

		ExecutionMode runAs = AwbEnumeration.getExecutionMode(this.getProperties().getProperty(BundleProperties.DEF_RUNAS));
		if (runAs == null) {
			// --- Validating without execution mode is pointless -----------------------
			this.getFileProcessingResult().addError(BundleProperties.DEF_RUNAS + " is missing or invalid");
			return;
		}
		MtpProtocol masterProtocol = AwbEnumeration.getMtpProtocol(properties.getProperty(BundleProperties.DEF_MASTER_PROTOCOL));
		if (masterProtocol == null) {
			this.getFileProcessingResult().addError(BundleProperties.DEF_MASTER_PROTOCOL + " is missing");
		}
		MtpProtocol ownMtpProtocol = AwbEnumeration.getMtpProtocol(properties.getProperty(BundleProperties.DEF_OWN_MTP_PROTOCOL));
		if (ownMtpProtocol == null) {
			this.getFileProcessingResult().addError(BundleProperties.DEF_OWN_MTP_PROTOCOL + " is missing");
		}
		MTP_Creation ownMtpCreation = AwbEnumeration.getMtpCreation(properties.getProperty(BundleProperties.DEF_OWN_MTP_CREATION));
		if (ownMtpCreation == null) {
			this.getFileProcessingResult().addError(BundleProperties.DEF_OWN_MTP_CREATION + " is missing");
		}
		
		if (runAs == ExecutionMode.SERVER || runAs == ExecutionMode.SERVER_MASTER || runAs == ExecutionMode.SERVER_SLAVE) {
			String autoStart = properties.getProperty(BundleProperties.DEF_AUTOSTART);
			if (autoStart == null || autoStart.isBlank()) {
				this.getFileProcessingResult().addError(BundleProperties.DEF_AUTOSTART + " is missing");
			}
			
		} else if (runAs == ExecutionMode.DEVICE_SYSTEM) {
			
			// --- Checks for Device_System execution mode ------------------------------
			String deviceServiceProject = properties.getProperty(BundleProperties.DEF_DEVICE_SERVICE_PROJECT_FOLDER);
			if (deviceServiceProject == null || deviceServiceProject.isBlank()) {
				this.getFileProcessingResult().addError(BundleProperties.DEF_DEVICE_SERVICE_PROJECT_FOLDER + " is missing");
			}
			// --- Check for valid device system execution mode -------------------------
			DeviceSystemExecutionMode deviceServiceExecAs = AwbEnumeration.getDeviceSystemExecutionMode(properties.getProperty(BundleProperties.DEF_DEVICE_SERVICE_EXEC_AS));
			if (deviceServiceExecAs == null) {
				this.getFileProcessingResult().addError(BundleProperties.DEF_DEVICE_SERVICE_EXEC_AS + " is missing or invalid");
				
			} else if (deviceServiceExecAs == DeviceSystemExecutionMode.SETUP) {
				// --- In setup mode service setup has to be specified ------------------
				String serviceSetup = properties.getProperty(BundleProperties.DEF_DEVICE_SERVICE_SETUP);
				if (serviceSetup == null || serviceSetup.isBlank()) {
					this.getFileProcessingResult().addError(BundleProperties.DEF_DEVICE_SERVICE_SETUP + " is missing");
				}
				
			} else if (deviceServiceExecAs == DeviceSystemExecutionMode.AGENT) {
				// --- In agent mode at least one agent name and class are needed -------
				String agentName = properties.getProperty(BundleProperties.DEF_DEVICE_SERVICE_AGENT_NAME);
				if (agentName == null || agentName.isBlank()) this.getFileProcessingResult().addError(BundleProperties.DEF_DEVICE_SERVICE_AGENT_NAME+ " is missing");

				EmbeddedSystemAgentVisualisation agentVisualization = AwbEnumeration.getEmbeddedSystemAgentVisualization(properties.getProperty(BundleProperties.DEF_DEVICE_SERVICE_VISUALIZATION));
				if (agentVisualization == null) {
					this.getFileProcessingResult().addError(BundleProperties.DEF_DEVICE_SERVICE_VISUALIZATION + " is missing or invalid");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	* @see de.enflexit.common.fileConfiguration.FileConfigurationService#getCurrentConfigurationFile()
	*/
	@Override
	public FileDownload getCurrentConfigurationFile() {
		
		File currentConfigurationFile = this.getPreferencesFile();
		if (currentConfigurationFile.exists() == false) return null;
		
		byte[] content;
		try {
			content = Files.readAllBytes(currentConfigurationFile.toPath());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
		FileDownload fileDownload = new FileDownload();
		fileDownload.setFileName(currentConfigurationFile.getName());
		fileDownload.setContentType(FileDownload.DEFAULT_CONTENT_TYPE);
		fileDownload.setContent(content);
		
		return fileDownload;
	}
	
	/**
	 * Returns the preferences file.
	 *
	 * @return the preferences file
	 */
	private File getPreferencesFile() {

		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		URL configUrl = Platform.getConfigurationLocation().getURL();
		Path configDirectory = new File(configUrl.getFile()).toPath();

		return configDirectory.resolve(".settings").resolve(bundle.getSymbolicName() + ".prefs").toFile();
	}
	
	/**
	 * Returns the file processing result.
	 *
	 * @return the file processing result
	 */
	private FileProcessingResult getFileProcessingResult() {
		if (this.fileProcessingResult == null) {
			fileProcessingResult = new FileProcessingResult();
		}
		return fileProcessingResult;
	}

	/**
	 * Sets the file processing result.
	 *
	 * @param fileProcessingResult the new file processing result
	 */
	private void setFileProcessingResult(FileProcessingResult fileProcessingresult) {
		this.fileProcessingResult = fileProcessingresult;
	}
	
	/**
	 * Returns the properties.
	 *
	 * @return the properties
	 */
	private Properties getProperties() {
		return properties;
	}
	/**
	 * Sets the properties.
	 *
	 * @param properties the new properties
	 */
	private void setProperties(Properties properties) {
		this.properties = properties;
	}
	/**
	 * Sets fileProcessingResult and properties to null.
	 */
	private void reset() {
		this.setFileProcessingResult(null);
		this.setProperties(null);
	}
}