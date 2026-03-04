package de.enflexit.awb.ws.restapi.gen.impl;

import de.enflexit.awb.ws.restapi.gen.*;

import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-03-04T17:07:13.922796800+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class DoRestartApiServiceImpl extends DoRestartApiService {
    @Override
    public Response doRestartPost(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
