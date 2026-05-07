package de.enflexit.awb.ws.restapi.impl;

import java.security.Principal;
import de.enflexit.awb.ws.restapi.gen.*;
import de.enflexit.awb.core.update.AWBUpdater;
import de.enflexit.awb.ws.restapi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.restapi.gen.DoUpdateApiService;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-03-25T12:33:25.416793100+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class DoUpdateApiServiceImpl extends DoUpdateApiService {
    @Override
    public Response doUpdatePost(SecurityContext securityContext) throws NotFoundException {
    	
    	Principal principal = securityContext.getUserPrincipal();
    	if (principal==null) {
    		return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Permission denied!!")).build();
    	}
    	
    	AWBUpdater awbUpdater = new AWBUpdater(true, true);
    	awbUpdater.start();
    	
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "Update scheduled")).build();
    }
}