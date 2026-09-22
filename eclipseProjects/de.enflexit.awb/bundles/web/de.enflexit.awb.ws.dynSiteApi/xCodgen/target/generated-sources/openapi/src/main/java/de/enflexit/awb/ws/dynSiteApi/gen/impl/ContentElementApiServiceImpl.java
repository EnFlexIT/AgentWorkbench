package de.enflexit.awb.ws.dynSiteApi.gen.impl;

import de.enflexit.awb.ws.dynSiteApi.gen.*;
import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent;
import de.enflexit.awb.ws.dynSiteApi.gen.model.ContentElementPost201Response;
import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateContentRequest;

import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-21T17:30:55.433568700+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class ContentElementApiServiceImpl extends ContentElementApiService {
    @Override
    public Response contentElementElementIDDelete(Integer elementID, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response contentElementElementIDGet(Integer elementID, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response contentElementElementIDPut(Integer elementID, AbstractSiteContent abstractSiteContent, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response contentElementPost(CreateContentRequest createContentRequest, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
