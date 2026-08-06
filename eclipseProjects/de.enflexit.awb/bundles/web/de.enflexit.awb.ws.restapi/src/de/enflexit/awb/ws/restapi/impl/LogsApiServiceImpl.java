package de.enflexit.awb.ws.restapi.impl;

import de.enflexit.awb.ws.restapi.gen.*;
import de.enflexit.logging.provider.LogFileInfo;
import de.enflexit.logging.provider.LogProviderHelper;
import de.enflexit.logging.provider.LogProviderService;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;


import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.StreamingOutput;
import jakarta.validation.constraints.*;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-08-03T11:53:07.927647300+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class LogsApiServiceImpl extends LogsApiService {
  
	private LogProviderService logProvider;
	
    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.restapi.gen.LogsApiService#downloadLogArchive(java.lang.String, java.lang.String, jakarta.ws.rs.core.SecurityContext)
    */
    @Override
	public Response downloadLogArchive(@NotNull String from, @NotNull String to, SecurityContext securityContext) throws NotFoundException {

    	// --- Check who is the user ------------------------------------------
    	Principal principal = securityContext.getUserPrincipal();
    	if (principal==null) {
    		return Response.status(Status.FORBIDDEN).build();
    	}
    	
		List<LogFileInfo> logFiles;
		// --- Try to obtain the requested files ------------------------------
		try {
			logFiles = this.getLogProvider().getLogsBetween(from, to);
		} catch (IOException ioeGettingLogs) {
			ioeGettingLogs.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		StreamingOutput stream = output -> {
			try (ZipOutputStream zipOut = new ZipOutputStream(output)) {
				for (LogFileInfo logFileInfo: logFiles) {

					// --- Create a zip entry for every file ------------------
					ZipEntry entry = new ZipEntry(logFileInfo.getFileName());
					zipOut.putNextEntry(entry);
					// --- Copy the files' content to the output stream -------
					Files.copy(logFileInfo.getFilePath(), zipOut);
					zipOut.closeEntry();
				}
				zipOut.finish();

			} catch (IOException ioeWritingZip) {
				ioeWritingZip.printStackTrace();

			} finally {
				// --- Clean up temp files which might have been created ------
				List<Path> pathsToCleanUp = new ArrayList<Path>();
				logFiles.forEach(logfile -> pathsToCleanUp.add(logfile.getFilePath()));
				this.getLogProvider().cleanUp(pathsToCleanUp);
				
			}
		};
		return Response.ok(stream).type("application/zip").header("Content-Disposition", "attachment; filename=\"logs.zip\"").build();
	}
    
    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.restapi.gen.LogsApiService#getLogFiles(jakarta.ws.rs.core.SecurityContext)
    */
    @Override
    public Response getLogFiles(SecurityContext securityContext) throws NotFoundException {
    	
    	// --- Check who is the user ------------------------------------------
    	Principal principal = securityContext.getUserPrincipal();
    	if (principal==null) {
    		return Response.status(Status.FORBIDDEN).build();
    	}
    	
    	List<String> logFiles = this.getLogProvider().getAvailableLogs();
    	return Response.ok(logFiles).build();
    }
    
    /**
     * Returns the log provider.
     *
     * @return the log provider
     */
    private LogProviderService getLogProvider() {
    	if (logProvider == null) {
    		logProvider = LogProviderHelper.getLogProvider();
    	}
    	return logProvider;
    }
}