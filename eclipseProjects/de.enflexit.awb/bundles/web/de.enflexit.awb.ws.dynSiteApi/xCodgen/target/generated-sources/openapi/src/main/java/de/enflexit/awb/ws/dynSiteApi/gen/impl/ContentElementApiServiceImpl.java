package de.enflexit.awb.ws.dynSiteApi.gen.impl;

import de.enflexit.awb.ws.dynSiteApi.gen.*;
import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent;

import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-23T11:55:38.634832400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class ContentElementApiServiceImpl extends ContentElementApiService {
    @Override
    public Response contentElementElementIDGet(Integer elementID, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response contentElementPut(AbstractSiteContent abstractSiteContent, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
