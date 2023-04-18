package de.enflexit.awb.samples.ws.restapi.server.gen.impl;

import de.enflexit.awb.samples.ws.restapi.server.gen.*;
import de.enflexit.awb.samples.ws.restapi.server.gen.model.SystemInformation;

import java.util.List;
import de.enflexit.awb.samples.ws.restapi.server.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-04-17T17:09:44.461949400+02:00[Europe/Berlin]")
public class InfoApiServiceImpl extends InfoApiService {
    @Override
    public Response infoGet(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
