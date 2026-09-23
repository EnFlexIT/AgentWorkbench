package de.enflexit.awb.ws.dynSiteApi.gen;

import de.enflexit.awb.ws.dynSiteApi.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateMenu201Response;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuList;

import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-23T10:17:15.337896100+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public abstract class MenuApiService {
    public abstract Response assignContentToMenu(Integer menuID,Integer elementID,SecurityContext securityContext) throws NotFoundException;
    public abstract Response createMenu(MenuItem menuItem,SecurityContext securityContext) throws NotFoundException;
    public abstract Response deleteMenu(Integer menuID,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getMenus(String lang,SecurityContext securityContext) throws NotFoundException;
    public abstract Response updateMenu(Integer menuID,MenuItem menuItem,SecurityContext securityContext) throws NotFoundException;
    public abstract Response updateMenuPermissions(Integer menuID,String body,SecurityContext securityContext) throws NotFoundException;
}
