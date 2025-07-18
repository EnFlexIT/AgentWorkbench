package de.enflexit.awb.ws.dynSiteApi.gen.impl;

import de.enflexit.awb.ws.dynSiteApi.gen.*;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentList;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentListUpdate;

import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-18T08:22:31.214228+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class ContentApiServiceImpl extends ContentApiService {
    @Override
    public Response contentMenuIDGet(Integer menuID, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response contentPut(SiteContentListUpdate siteContentListUpdate, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
