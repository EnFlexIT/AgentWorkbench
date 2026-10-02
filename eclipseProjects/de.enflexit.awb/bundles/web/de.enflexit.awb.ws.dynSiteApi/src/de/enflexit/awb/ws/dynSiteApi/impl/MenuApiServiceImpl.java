package de.enflexit.awb.ws.dynSiteApi.impl;

import java.io.IOException;
import java.util.List;

import de.enflexit.awb.ws.core.db.WebAppDatabaseHandler;
import de.enflexit.awb.ws.core.db.dataModel.SiteMenu;
import de.enflexit.awb.ws.core.exceptions.DatabaseException;
import de.enflexit.awb.ws.dynSiteApi.RestApiConfiguration;
import de.enflexit.awb.ws.dynSiteApi.content.TypeConverter;
import de.enflexit.awb.ws.dynSiteApi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;
import de.enflexit.awb.ws.dynSiteApi.gen.model.AssignContentToMenuRequest;
import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateMenu201Response;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuList;
import de.enflexit.awb.ws.dynSiteApi.gen.model.UpdateContentAssignmentRequest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-23T10:17:15.337896100+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public class MenuApiServiceImpl extends MenuApiService {
 
	private WebAppDatabaseHandler webAppDatabaseHandler;

    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService#createMenu(de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem, jakarta.ws.rs.core.SecurityContext)
    */
    @Override
    public Response createMenu(MenuItem menuItem, SecurityContext securityContext) throws NotFoundException {
        
    	if (menuItem == null) {
            return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Menu item is missing!")).build();
    	}
		
    	if (menuItem.getCaption() == null || menuItem.getPosition() == null || menuItem.getIsHeadMenu() == null) {
    		return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Caption, Position and isHeadMenu must be specified")).build();
    	}
    	
    	if (menuItem.getIsHeadMenu() == false && menuItem.getParentId() == null) {
            return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "New menu is not defined as a head menu but no parent ID was provided")).build();
    	}
    	
    	SiteMenu newMenu = TypeConverter.toDBSiteMenu(menuItem);
    	try {
    		this.getDatabaseHandler().persistNewMenu(newMenu);
    		return Response.status(Status.CREATED).entity(new CreateMenu201Response().menuId(newMenu.getId())).build();
			
		} catch (IllegalArgumentException iae) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, iae.getMessage())).build();

		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
		}
    	
    }
    
    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService#deleteMenu(java.lang.Integer, jakarta.ws.rs.core.SecurityContext)
    */
    @Override
    public Response deleteMenu(Integer menuID, SecurityContext securityContext) throws NotFoundException {

    	if (menuID == null) {
            return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Menu ID is missing!")).build();
    	}
		
    	try {
    		this.getDatabaseHandler().deleteMenuIncludingSubMenusById(menuID);
    		return Response.ok().build();
			
		} catch (IllegalArgumentException iae) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, iae.getMessage())).build();

		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
		}
    }
    
    @Override
    public Response getMenus(String lang, SecurityContext securityContext) throws NotFoundException {
		// --- Get menus from database --------------------------------------------------
		List<SiteMenu> dbMenuList = this.getDatabaseHandler().dbLoadEntityInstanceList(SiteMenu.class);
		List<MenuItem> menuItemList = TypeConverter.toRestMenuItemList(dbMenuList, lang);
		
		// --- Prepare return type ------------------------------------------------------
		MenuList menuList = new MenuList();
		menuList.setMenuList(menuItemList);
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(menuList).build();

	}
    
    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService#updateMenu(java.lang.Integer, de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem, jakarta.ws.rs.core.SecurityContext)
    */
    @Override
    public Response updateMenu(Integer menuID, MenuItem menuItem, SecurityContext securityContext) throws NotFoundException {
		
    	if (menuID == null || menuItem == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "menu ID and menu Item must be specified")).build();
		}
		
		
    	SiteMenu updatedMenu = TypeConverter.toDBSiteMenu(menuItem);
		try {
			this.getDatabaseHandler().updateMenu(updatedMenu, menuID);
			return Response.ok().build();

		} catch (IllegalArgumentException iae) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, iae.getMessage())).build();

		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
		}
    }
    
    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService#updateMenuPermissions(java.lang.Integer, java.lang.String, jakarta.ws.rs.core.SecurityContext)
    */
    @Override
    public Response updateMenuPermissions(Integer menuID, String body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
	
	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService#removeAssignment(java.lang.Integer, java.lang.Integer, jakarta.ws.rs.core.SecurityContext)
	*/
	@Override
	public Response removeAssignment(Integer menuID, Integer elementID, SecurityContext securityContext) throws NotFoundException {
    	if (menuID == null || elementID == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "menu ID and menu Item must be specified")).build();
		}
    	
		try {
			this.getDatabaseHandler().removeAssignmentOf(menuID, elementID);
			return Response.ok().build();

		} catch (IllegalArgumentException iae) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, iae.getMessage())).build();

		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
		}
	}
	
	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService#updateAssignmentAndPosition(de.enflexit.awb.ws.dynSiteApi.gen.model.UpdateContentAssignmentRequest, jakarta.ws.rs.core.SecurityContext)
	*/
	@Override
	public Response updateAssignmentAndPosition(UpdateContentAssignmentRequest updateContentAssignmentRequest, SecurityContext securityContext) throws NotFoundException {
		
		if (updateContentAssignmentRequest == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Request body is missing")).build();
		}
		Integer elementId = updateContentAssignmentRequest.getElementId();
		Integer sourceMenuId = updateContentAssignmentRequest.getSourceMenuId();
		Integer targetMenuId = updateContentAssignmentRequest.getTargetMenuId();
		Integer targetPosition = updateContentAssignmentRequest.getPosition();
		
		if (elementId == null || sourceMenuId == null || targetMenuId == null || targetPosition == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Element ID, Source menu ID, target menu ID and target position must be specified.")).build();
		}
		
		try {
			this.getDatabaseHandler().updateMenuAssignmentAndPosition(elementId, sourceMenuId, targetMenuId, targetPosition);
			return Response.ok().build();

		} catch (IllegalArgumentException iae) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, iae.getMessage())).build();

		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
		}
	}
   

	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService#assignContentToMenu(java.lang.Integer, java.lang.Integer, de.enflexit.awb.ws.dynSiteApi.gen.model.AssignContentToMenuRequest, jakarta.ws.rs.core.SecurityContext)
	*/
	@Override
	public Response assignContentToMenu(Integer menuID, Integer elementID, AssignContentToMenuRequest assignContentToMenuRequest, SecurityContext securityContext) throws NotFoundException {
		
		if (assignContentToMenuRequest == null || assignContentToMenuRequest.getPosition() == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Request body is missing")).build();
		}

		if (elementID == null || menuID == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Element ID and menu ID must be specified.")).build();
		}

		try {
			this.getDatabaseHandler().assignContentToMenu(menuID, elementID, assignContentToMenuRequest.getPosition());
			return Response.ok().build();

		} catch (IllegalArgumentException iae) {
			return Response.status(Status.BAD_REQUEST)
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, iae.getMessage())).build();

		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(
					new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
		}
	}

		/**
		 * Returns the database handler.
		 *
		 * @return the database handler
		 */
		private WebAppDatabaseHandler getDatabaseHandler() {
			if (webAppDatabaseHandler == null) {
				webAppDatabaseHandler = new WebAppDatabaseHandler();
			}
			return webAppDatabaseHandler;
		}
}