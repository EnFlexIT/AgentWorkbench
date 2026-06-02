package de.enflexit.awb.ws.restapi.gen;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.ws.restapi.gen.model.Properties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-02T10:38:19.398049500+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public abstract class AppApiService {
    public abstract Response getAppSettings(String xPerformative, HttpServletRequest request,SecurityContext securityContext) throws NotFoundException;
    public abstract Response setAppSettings(Properties properties,SecurityContext securityContext) throws NotFoundException;
    public abstract Response uploadAppSettingsFile(FormDataBodyPart _fileBodypart,String xPerformative,SecurityContext securityContext) throws NotFoundException;
}
