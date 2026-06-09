package de.enflexit.common.fileConfiguration;

/**
 * The Interface FileConfigurationService.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public interface FileConfigurationService {

	public String getPerformative();
	public default String getPerformativeNotNull() {
		String perf = this.getPerformative();
		if (perf==null || perf.isBlank()==true) {
			perf = this.getClass().getSimpleName();
		}
		return perf.toLowerCase();
	}
	
	public FileProcessingResult processFile(UploadedFile file2Process);
}