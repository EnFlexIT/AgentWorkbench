package de.enflexit.awb.ws.dynSiteApi.impl;

import java.util.List;

import de.enflexit.awb.ws.core.db.WebAppDatabaseHandler;
import de.enflexit.awb.ws.core.db.dataModel.SiteMenu;
import de.enflexit.awb.ws.dynSiteApi.RestApiConfiguration;
import de.enflexit.awb.ws.dynSiteApi.content.TypeConverter;
import de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuList;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * The Class MenuApiServiceImpl.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-04-02T11:34:41.198040400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class MenuApiServiceImpl extends MenuApiService {

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService#menuGet(jakarta.ws.rs.core.SecurityContext)
	 */
	@Override
	public Response menuGet(SecurityContext securityContext) throws NotFoundException {
		
		// --- Get menus from database --------------------------------------------------
		WebAppDatabaseHandler dbHandler = new WebAppDatabaseHandler();
		List<SiteMenu> dbMenuList = dbHandler.dbLoadEntityInstanceList(SiteMenu.class);
		List<MenuItem> menuItemList = TypeConverter.getMenuItemList(dbMenuList);
		
		// --- Prepare return type ------------------------------------------------------
		MenuList menuList = new MenuList();
		menuList.setMenuList(menuItemList);
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(menuItemList).build();
		//return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Unknown menueID!")).build();
	}
		
}
