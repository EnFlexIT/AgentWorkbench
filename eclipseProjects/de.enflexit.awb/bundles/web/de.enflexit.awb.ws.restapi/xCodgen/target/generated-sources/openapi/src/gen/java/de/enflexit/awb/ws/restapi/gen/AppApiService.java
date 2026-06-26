package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.io.File;
import de.enflexit.awb.ws.restapi.gen.model.Message;
import de.enflexit.awb.ws.restapi.gen.model.Properties;

import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-25T11:45:17.027739100+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public abstract class AppApiService {
    public abstract Response downloadAppSettingsFile(String xPerformative,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getAppSettings(String xPerformative,HttpServletRequest request, SecurityContext securityContext) throws NotFoundException;
    public abstract Response setAppSettings(Properties properties,SecurityContext securityContext) throws NotFoundException;
    public abstract Response uploadAppSettingsFile(FormDataBodyPart _fileBodypart,String xPerformative,SecurityContext securityContext) throws NotFoundException;
}
