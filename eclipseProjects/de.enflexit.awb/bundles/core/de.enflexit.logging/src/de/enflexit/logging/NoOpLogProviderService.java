package de.enflexit.logging;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import de.enflexit.logging.provider.LogFileInfo;
import de.enflexit.logging.provider.LogProviderService;
import de.enflexit.logging.provider.LogProviderType;

/**
 * The Class NoOpLogProviderService is used in case neither file nor database logging is enabled.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class NoOpLogProviderService implements LogProviderService {

	@Override
	public LogProviderType getType() {
		return LogProviderType.NO_OP;
	}

	@Override
	public List<String> getAvailableLogs() {
		return Collections.emptyList();
	}

	@Override
	public List<LogFileInfo> getLogsBetween(String from, String to) throws IOException {
		return Collections.emptyList();
	}

	@Override
	public void cleanUp(List<Path> filesToCleanUp) {
		// --- Nothing to do --------------------
	}

}
