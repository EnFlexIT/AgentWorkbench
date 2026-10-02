package de.enflexit.awb.ws.dynSiteApi.gen;

import de.enflexit.awb.ws.dynSiteApi.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.ws.dynSiteApi.gen.model.AssignContentToMenuRequest;
import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateMenu201Response;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuList;
import de.enflexit.awb.ws.dynSiteApi.gen.model.UpdateContentAssignmentRequest;

import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-10-01T10:08:21.991650800+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public abstract class MenuApiService {
    public abstract Response assignContentToMenu(Integer menuId,Integer elementId,AssignContentToMenuRequest assignContentToMenuRequest,SecurityContext securityContext) throws NotFoundException;
    public abstract Response createMenu(MenuItem menuItem,SecurityContext securityContext) throws NotFoundException;
    public abstract Response deleteMenu(Integer menuId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getMenus(String lang,SecurityContext securityContext) throws NotFoundException;
    public abstract Response removeAssignment(Integer menuId,Integer elementId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response updateAssignmentAndPosition(UpdateContentAssignmentRequest updateContentAssignmentRequest,SecurityContext securityContext) throws NotFoundException;
    public abstract Response updateMenu(Integer menuId,MenuItem menuItem,SecurityContext securityContext) throws NotFoundException;
    public abstract Response updateMenuPermissions(Integer menuId,String body,SecurityContext securityContext) throws NotFoundException;
}
