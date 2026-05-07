package de.enflexit.awb.ws.restapi.impl;

import de.enflexit.awb.ws.restapi.gen.*;

import java.util.List;

import de.enflexit.common.p2.P2OperationsHandler;

import java.io.InputStream;
import java.security.Principal;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response.Status;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-03-25T12:33:25.416793100+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class IsUpdateAvailableApiServiceImpl extends IsUpdateAvailableApiService {
    @Override
    public Response isUpdateAvailableGet(SecurityContext securityContext) throws NotFoundException {
    	
    	Principal principal = securityContext.getUserPrincipal();
    	if (principal==null) {
    		return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Permission denied!!")).build();
    	}
    	
    	boolean isUpdateAvailable = P2OperationsHandler.getInstance().checkForNewerBundles();
    	
        return Response.ok(isUpdateAvailable).build();
    }
}