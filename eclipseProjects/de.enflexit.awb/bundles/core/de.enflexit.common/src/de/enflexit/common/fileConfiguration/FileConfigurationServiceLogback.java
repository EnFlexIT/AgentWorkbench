package de.enflexit.common.fileConfiguration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.enflexit.common.PathHandling;
import de.enflexit.logging.AwbLogbackConfigurator;
import de.enflexit.logging.LoggingActivator;
import de.enflexit.logging.PropertyContentProvider.FileToProvide;

/**
 * The Class FileConfigurationServiceLogback is used to apply a 
 * new logback configuration through the {@link FileConfigurationServiceManager}. 
 * It is also able to provide the current logback.xml configuration file.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class FileConfigurationServiceLogback implements FileConfigurationService {

	FileProcessingResult fileProcessingResult;
	
	/* (non-Javadoc)
	* @see de.enflexit.common.fileConfiguration.FileConfigurationService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "LogbackConfiguration";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.fileConfiguration.FileConfigurationService#processFile(de.enflexit.common.fileConfiguration.UploadedFile)
	*/
	@Override
	public FileProcessingResult processFile(UploadedFile file2Process) {
		
		// --- Check if there is an input stream ----------------------------------------
		if (file2Process.getInputStream() == null) {
			this.getFileProcessingResult().setMessage("Uploaded file is empty or invalid.");
			return this.getFileProcessingResult();
		}
		
		byte[] content;
		try {
			// --- Read the InputStream -------------------------------------------------
			content = file2Process.getInputStream().readAllBytes();

		} catch (IOException ioeRead) {
			ioeRead.printStackTrace();
			this.getFileProcessingResult().setMessage("Error while loading the file.");
			FileProcessingResult fpr = this.getFileProcessingResult();
			this.setFileProcessingResult(null);
			return fpr;
		}	
		
		// --- Get path to the logback.xml ----------------------------------------------
		Path logbackXmlFile = PathHandling.getPropertiesPath(LoggingActivator.class, true).resolve(FileToProvide.LOGBACK_CONFIGURATION.toString());
		
		// --- Validate new config ------------------------------------------------------
		if (AwbLogbackConfigurator.isValidLogbackConfiguration(new ByteArrayInputStream(content)) == false) {
			this.getFileProcessingResult().setMessage("Invalid configuration.");
			FileProcessingResult fpr = this.getFileProcessingResult();
			this.setFileProcessingResult(null);
			return fpr;
		}
		
		// --- Replace existing file ----------------------------------------------------
		try {
			Files.write(logbackXmlFile, content);
			
		} catch (IOException ioeWrite) {
			ioeWrite.printStackTrace();
			this.getFileProcessingResult().setMessage("Error while writing new configuration file");
			FileProcessingResult fpr = this.getFileProcessingResult();
			this.setFileProcessingResult(null);
			return fpr;
		}
		// --- Load the new configuration -----------------------------------------------
		try {
			AwbLogbackConfigurator.loadConfiguration(logbackXmlFile);

		} catch (Exception e) {
			e.printStackTrace();
			this.getFileProcessingResult().setMessage("Error while applying new configuration");
			FileProcessingResult fpr = this.getFileProcessingResult();
			this.setFileProcessingResult(null);
			return fpr;
		}
		// --- No errors occurred, return success ---------------------------------------
		this.getFileProcessingResult().setSuccess(true);
		this.getFileProcessingResult().setMessage("Upload Successful. Configuration applied");
		FileProcessingResult fpr = this.getFileProcessingResult();
		this.setFileProcessingResult(null);
		return fpr;
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.fileConfiguration.FileConfigurationService#getCurrentConfigurationFile()
	*/
	@Override
	public FileDownload getCurrentConfigurationFile() {
		
		// --- Check if the file exists ---------------------------------------
		Path logbackXmlFile = PathHandling.getPropertiesPath(LoggingActivator.class, true).resolve(FileToProvide.LOGBACK_CONFIGURATION.toString());
		if (logbackXmlFile.toFile().exists() == false) {
			return null;
		}
		
		byte[] content;
		try {
			// --- Read the file ----------------------------------------------
			content = Files.readAllBytes(logbackXmlFile);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
		// --- Set name, type and content --------------------------------------
		FileDownload fileDownload = new FileDownload();
		fileDownload.setFileName("logback");
		fileDownload.setContentType(FileDownload.XML_CONTENT_TYPE);
		fileDownload.setContent(content);
		
		return fileDownload;
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
	 * @param fileProcessingresult the new file processing result
	 */
	private void setFileProcessingResult(FileProcessingResult fileProcessingresult) {
		this.fileProcessingResult = fileProcessingresult;
	}

}