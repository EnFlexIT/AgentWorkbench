package de.enflexit.awb.ws.restapi.gen.impl;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.ws.restapi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.restapi.gen.AppApiService;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.model.Properties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-08T12:08:47.460531100+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class AppApiServiceImpl extends AppApiService {
    @Override
    public Response getAppSettings(String xPerformative, HttpServletRequest request, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response setAppSettings(Properties properties, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response uploadAppSettingsFile(FormDataBodyPart _fileBodypart, String xPerformative, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
