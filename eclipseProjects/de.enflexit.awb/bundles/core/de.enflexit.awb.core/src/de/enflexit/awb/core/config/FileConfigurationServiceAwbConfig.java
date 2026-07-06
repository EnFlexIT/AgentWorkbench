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
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.common.fileConfiguration.FileConfigurationService;
import de.enflexit.common.fileConfiguration.FileDownload;
import de.enflexit.common.fileConfiguration.FileProcessingResult;
import de.enflexit.common.fileConfiguration.UploadedFile;

/**
 * The Class FileConfigurationServiceAwbConfig.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class FileConfigurationServiceAwbConfig implements FileConfigurationService {

	FileProcessingResult fileProcessingresult;
	
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
		
		Properties properties = new Properties();
		byte[] content;
		
		// --- Load the file as properties ------------------------------------
		try {
			content = file2Process.getInputStream().readAllBytes();
			properties.load(new ByteArrayInputStream(content));

		} catch (IOException ioe) {
			ioe.printStackTrace();
			this.getFileProcessingResult().setMessage("Error while loading the file");
			return this.getFileProcessingResult();
		}
		
		properties = this.checkAndReplaceWithDefault(properties);
		if (this.getFileProcessingResult().getErrorList().size() > 0) {
			this.getFileProcessingResult().setMessage("Validation error.");
			return this.getFileProcessingResult();
		}
//		try {
//			Files.write(this.getPreferencesFile().toPath(), content);
//		} catch (IOException e) {
//			e.printStackTrace();
//			this.getFileProcessingResult().setMessage("Error while overwriting AwbConfiguration.");
//			return this.getFileProcessingResult();
//		}
		this.getFileProcessingResult().setSuccess(true);
		this.getFileProcessingResult().setMessage("File upload successful. Restarting Agent.Workbench...");
		return this.getFileProcessingResult();
	}

	/**
	 * Validate properties.
	 *
	 * @param properties the properties 2 validate
	 * @return true, if successful
	 */
	private Properties checkAndReplaceWithDefault(Properties properties) {

		String awbCoreBundleVersion = properties.getProperty(BundleProperties.DEF_AWG_CORE_BUNLDE_VERSION, Application.getGlobalInfo().getVersionInfo().getVersionInfo());
		
		String projectDir = properties.getProperty(BundleProperties.DEF_PROJECTS_DIRECTORY, Application.getGlobalInfo().getPathProjects());
		if (projectDir != null) properties.put(BundleProperties.DEF_PROJECTS_DIRECTORY, projectDir);
		
		String runAs = properties.getProperty(BundleProperties.DEF_RUNAS, Application.getGlobalInfo().getExecutionMode().toString());
		if (runAs != null) properties.put(BundleProperties.DEF_RUNAS, runAs);
		
		String benchValue = properties.getProperty(BundleProperties.DEF_BENCH_VALUE, ""+ Application.getGlobalInfo().getBenchValue());
		if (benchValue != null) properties.put(BundleProperties.DEF_BENCH_VALUE, benchValue);
		String benchExecOn = properties.getProperty(BundleProperties.DEF_BENCH_EXEC_ON, Application.getGlobalInfo().getBenchExecOn());
		if (benchExecOn != null) properties.put(BundleProperties.DEF_BENCH_EXEC_ON, benchExecOn);
		String benchSkipAllways = properties.getProperty(BundleProperties.DEF_BENCH_SKIP_ALLWAYS, ""+ Application.getGlobalInfo().isBenchAlwaysSkip());
		if (benchSkipAllways != null) properties.put(BundleProperties.DEF_BENCH_SKIP_ALLWAYS, benchSkipAllways);
		
		String maximizeMainWindow = properties.getProperty(BundleProperties.DEF_MAXIMiZE_MAIN_WINDOW, ""+ Application.getGlobalInfo().isMaximzeMainWindow());
		if (maximizeMainWindow != null) properties.put(BundleProperties.DEF_MAXIMiZE_MAIN_WINDOW, maximizeMainWindow);
		String lookAndFeel = properties.getProperty(BundleProperties.DEF_LOOK_AND_FEEL, Application.getGlobalInfo().getAppLookAndFeelClassName());
		if (lookAndFeel != null) properties.put(BundleProperties.DEF_LOOK_AND_FEEL, lookAndFeel);
		
		String loggingEnabled = properties.getProperty(BundleProperties.DEF_LOGGING_ENABLED, ""+ Application.getGlobalInfo().isLoggingEnabled());
		if (loggingEnabled != null) properties.put(BundleProperties.DEF_LOGGING_ENABLED, loggingEnabled);
		String loggingBasePath = properties.getProperty(BundleProperties.DEF_LOGGING_BASE_PATH, Application.getGlobalInfo().getLoggingBasePath());
		if (loggingBasePath != null) properties.put(BundleProperties.DEF_LOGGING_BASE_PATH, loggingBasePath);
		
		// --- Check required values for all server modes ---------------------
		if (runAs.equalsIgnoreCase(ExecutionMode.SERVER.toString()) || runAs.equalsIgnoreCase(ExecutionMode.SERVER_MASTER.toString()) || runAs.equalsIgnoreCase(ExecutionMode.SERVER_SLAVE.toString())) {
			
			String autoStart = properties.getProperty(BundleProperties.DEF_AUTOSTART, ""+ Application.getGlobalInfo().isServerAutoRun());
			if (autoStart != null) {
				properties.put(BundleProperties.DEF_AUTOSTART, autoStart);
			} else {
				this.getFileProcessingResult().addError(BundleProperties.DEF_AUTOSTART + " is missing");
			}
			
			String masterUrl = properties.getProperty(BundleProperties.DEF_MASTER_URL, Application.getGlobalInfo().getServerMasterURL());
			if (masterUrl != null) {
				properties.put(BundleProperties.DEF_MASTER_URL, masterUrl);
			} else {
				this.getFileProcessingResult().addError(BundleProperties.DEF_MASTER_URL + " is missing");
			}
			
			String masterPort = properties.getProperty(BundleProperties.DEF_MASTER_PORT, ""+ Application.getGlobalInfo().getServerMasterPort());
			if (masterPort != null) {
				properties.put(BundleProperties.DEF_MASTER_PORT, masterPort);
			} else {
				this.getFileProcessingResult().addError(BundleProperties.DEF_MASTER_PORT + " is missing");
			}
			
			String masterPortMtp = properties.getProperty(BundleProperties.DEF_MASTER_PORT4MTP, ""+ Application.getGlobalInfo().getServerMasterPort4MTP());
			if (masterPortMtp != null) {
				properties.put(BundleProperties.DEF_MASTER_PORT4MTP, masterPortMtp);
			} else {
				this.getFileProcessingResult().addError(BundleProperties.DEF_MASTER_PORT4MTP + " is missing");
			}
			
			String masterProtocol = properties.getProperty(BundleProperties.DEF_MASTER_PROTOCOL, ""+ Application.getGlobalInfo().getServerMasterProtocol());
			if (masterProtocol != null) {
				properties.put(BundleProperties.DEF_MASTER_PROTOCOL, masterProtocol);
			} else {
				this.getFileProcessingResult().addError(BundleProperties.DEF_MASTER_PROTOCOL + " is missing");
			}
			
			String ownMtpCreation = properties.getProperty(BundleProperties.DEF_OWN_MTP_CREATION, Application.getGlobalInfo().getOwnMtpCreation().toString());
			if (ownMtpCreation != null) {
				properties.put(BundleProperties.DEF_OWN_MTP_CREATION, ownMtpCreation);
			} else {
				this.getFileProcessingResult().addError(BundleProperties.DEF_OWN_MTP_CREATION + " is missing");
			}
			
			String ownMtpIp = properties.getProperty(BundleProperties.DEF_OWN_MTP_IP, Application.getGlobalInfo().getOwnMtpIP());
			if (ownMtpIp != null) {
				properties.put(BundleProperties.DEF_OWN_MTP_IP, ownMtpIp);
			} else {
				this.getFileProcessingResult().addError(BundleProperties.DEF_OWN_MTP_IP + " is missing");
			}
			
			String ownMtpPort = properties.getProperty(BundleProperties.DEF_OWN_MTP_PORT, ""+ Application.getGlobalInfo().getOwnMtpPort());
			if (ownMtpPort != null) {
				properties.put(BundleProperties.DEF_OWN_MTP_CREATION, ownMtpCreation);
			} else {
				this.getFileProcessingResult().addError(BundleProperties.DEF_OWN_MTP_CREATION + " is missing");
			}
			String ownMtpProtocol = properties.getProperty(BundleProperties.DEF_OWN_MTP_PROTOCOL, Application.getGlobalInfo().getMtpProtocol().toString());
			if (ownMtpProtocol != null) {
				properties.put(BundleProperties.DEF_OWN_MTP_PROTOCOL, ownMtpProtocol);
			} else {
				this.getFileProcessingResult().addError(BundleProperties.DEF_OWN_MTP_PROTOCOL + " is missing");
			}
		}
		
		String updateAutoConfig = properties.getProperty(BundleProperties.DEF_UPDATE_AUTOCONFIG, ""+ Application.getGlobalInfo().getUpdateAutoConfiguration());
		if (updateAutoConfig != null) properties.put(BundleProperties.DEF_UPDATE_AUTOCONFIG, updateAutoConfig);
		String updateKeepDictionary = properties.getProperty(BundleProperties.DEF_UPDATE_KEEP_DICTIONARY, ""+ Application.getGlobalInfo().getUpdateKeepDictionary());
		if (updateKeepDictionary != null) properties.put(BundleProperties.DEF_UPDATE_KEEP_DICTIONARY, updateKeepDictionary);
		String updateLastChecked = properties.getProperty(BundleProperties.DEF_UPDATE_DATE_LAST_CHECKED, ""+ Application.getGlobalInfo().getUpdateDateLastChecked());
		if (updateLastChecked != null) properties.put(BundleProperties.DEF_UPDATE_DATE_LAST_CHECKED, updateLastChecked);
		
		if (runAs.equalsIgnoreCase(ExecutionMode.DEVICE_SYSTEM.toString())) {
			String deviceServiceProject = properties.getProperty(BundleProperties.DEF_DEVICE_SERVICE_PROJECT_FOLDER);
		}
		
		
		return properties;
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
		if (this.fileProcessingresult == null) {
			fileProcessingresult = new FileProcessingResult();
		}
		return fileProcessingresult;
	}

	
}