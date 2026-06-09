package de.enflexit.common.fileConfiguration;

import java.io.InputStream;

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
	
	public boolean processFile(InputStream file2Process);
}