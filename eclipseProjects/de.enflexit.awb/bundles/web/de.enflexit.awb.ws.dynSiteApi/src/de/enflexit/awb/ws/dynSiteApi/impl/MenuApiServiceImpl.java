package de.enflexit.awb.ws.dynSiteApi.impl;

import de.enflexit.awb.ws.core.db.WebAppDatabaseHandler;
import de.enflexit.awb.ws.core.db.dataModel.SiteMenu;
import de.enflexit.awb.ws.dynSiteApi.RestApiConfiguration;
import de.enflexit.awb.ws.dynSiteApi.content.TypeConverter;
import de.enflexit.awb.ws.dynSiteApi.gen.*;
import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateMenu201Response;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuList;

import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-23T10:17:15.337896100+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public class MenuApiServiceImpl extends MenuApiService {
    @Override
    public Response assignContentToMenu(Integer menuID, Integer elementID, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response createMenu(MenuItem menuItem, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response deleteMenu(Integer menuID, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getMenus(String lang, SecurityContext securityContext) throws NotFoundException {
		// --- Get menus from database --------------------------------------------------
		WebAppDatabaseHandler dbHandler = new WebAppDatabaseHandler();
		List<SiteMenu> dbMenuList = dbHandler.dbLoadEntityInstanceList(SiteMenu.class);
		List<MenuItem> menuItemList = TypeConverter.getMenuItemList(dbMenuList, lang);
		
		// --- Prepare return type ------------------------------------------------------
		MenuList menuList = new MenuList();
		menuList.setMenuList(menuItemList);
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(menuItemList).build();

	}
    
    @Override
    public Response updateMenu(Integer menuID, MenuItem menuItem, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response updateMenuPermissions(Integer menuID, String body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
