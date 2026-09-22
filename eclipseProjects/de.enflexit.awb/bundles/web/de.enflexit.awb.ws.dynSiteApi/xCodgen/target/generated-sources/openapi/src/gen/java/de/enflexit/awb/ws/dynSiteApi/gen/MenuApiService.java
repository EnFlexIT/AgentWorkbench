package de.enflexit.awb.ws.dynSiteApi.gen;

import de.enflexit.awb.ws.dynSiteApi.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuList;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuPost201Response;

import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-22T09:53:42.299228200+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public abstract class MenuApiService {
    public abstract Response menuGet(String lang,SecurityContext securityContext) throws NotFoundException;
    public abstract Response menuMenuIDContentElementElementIDPut(Integer menuID,Integer elementID,SecurityContext securityContext) throws NotFoundException;
    public abstract Response menuMenuIDDelete(Integer menuID,SecurityContext securityContext) throws NotFoundException;
    public abstract Response menuMenuIDPermissionsPut(Integer menuID,String body,SecurityContext securityContext) throws NotFoundException;
    public abstract Response menuMenuIDPut(Integer menuID,MenuItem menuItem,SecurityContext securityContext) throws NotFoundException;
    public abstract Response menuPost(MenuItem menuItem,SecurityContext securityContext) throws NotFoundException;
}
