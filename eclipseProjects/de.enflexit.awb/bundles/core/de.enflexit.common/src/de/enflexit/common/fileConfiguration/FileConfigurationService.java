package de.enflexit.common.fileConfiguration;

/**
 * The Interface FileConfigurationService defines the methods needed
 * to enable the reconfiguration of some Agent.Workbench component through
 * file upload. An implementing class should also be able to return the 
 * current configuration file for user convenience.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public interface FileConfigurationService {

	/**
	 * Should return a unique identifier for the service.
	 *
	 * @return the performative
	 */
	public String getPerformative();
	
	/**
	 * Returns the performative that is for sure lower case and not null.
	 * 
	 * @return the performative not null
	 */
	public default String getPerformativeNotNull() {
		String perf = this.getPerformative();
		if (perf==null || perf.isBlank()==true) {
			perf = this.getClass().getSimpleName();
		}
		return perf.toLowerCase();
	}
	
	/**
	 * Should validate and apply the specified configuration file.
	 *
	 * @param file2Process the configuration file to process and apply
	 * @return the result of the operation
	 */
	public FileProcessingResult processFile(UploadedFile file2Process);
	
	/**
	 * Should return the current configuration file.
	 *
	 * @return the current configuration file
	 */
	public FileDownload getCurrentConfigurationFile();
}