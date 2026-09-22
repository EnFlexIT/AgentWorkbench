package de.enflexit.awb.ws.dynSiteApi.gen.impl;

import de.enflexit.awb.ws.dynSiteApi.gen.*;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuList;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuPost201Response;

import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-22T09:53:42.299228200+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public class MenuApiServiceImpl extends MenuApiService {
    @Override
    public Response menuGet(String lang, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response menuMenuIDContentElementElementIDPut(Integer menuID, Integer elementID, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response menuMenuIDDelete(Integer menuID, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response menuMenuIDPermissionsPut(Integer menuID, String body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response menuMenuIDPut(Integer menuID, MenuItem menuItem, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response menuPost(MenuItem menuItem, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
