package de.enflexit.awb.ws.restapi.gen.impl;

import de.enflexit.awb.ws.restapi.gen.*;
import de.enflexit.awb.ws.restapi.gen.model.*;

import de.enflexit.awb.ws.restapi.gen.model.SystemInformation;

import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T22:50:45.497388700+02:00[Europe/Berlin]")
public class InfoApiServiceImpl extends InfoApiService {
    @Override
    public Response infoGet(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
