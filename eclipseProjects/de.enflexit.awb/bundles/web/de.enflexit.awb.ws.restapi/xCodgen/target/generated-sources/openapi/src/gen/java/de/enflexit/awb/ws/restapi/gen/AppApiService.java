package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.Properties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-12-19T14:09:40.842261300+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public abstract class AppApiService {
    public abstract Response getAppSettings(String xPerformative,HttpServletRequest request, SecurityContext securityContext) throws NotFoundException;
    public abstract Response setAppSettings(Properties properties,SecurityContext securityContext) throws NotFoundException;
}
