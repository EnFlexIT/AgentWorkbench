package de.enflexit.logging.provider;

import java.nio.file.Path;

/**
 * The Class LogFileInfo.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class LogFileInfo {

	private String fileName;
	private Path filePath;
	
	public LogFileInfo(String filename, Path filePath) {
		this.fileName = filename;
		this.filePath = filePath;
	}
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the filePath
	 */
	public Path getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

	
}