package de.enflexit.common.fileConfiguration;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

/**
 * The Interface FileConfigurationService.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public interface FileConfigurationService {

	public boolean processFile(FormDataBodyPart file2Process);
	public String getPerformative();
	
	public default String getPerformativeNotNull() {
		String perf = this.getPerformative();
		if (perf==null || perf.isBlank()==true) {
			perf = this.getClass().getSimpleName();
		}
		return perf.toLowerCase();
	}
}