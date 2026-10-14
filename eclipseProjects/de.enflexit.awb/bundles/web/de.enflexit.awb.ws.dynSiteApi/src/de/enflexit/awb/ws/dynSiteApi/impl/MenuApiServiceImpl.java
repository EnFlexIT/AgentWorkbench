package de.enflexit.awb.ws.dynSiteApi.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateMenuRequest;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuList;
import de.enflexit.awb.ws.dynSiteApi.gen.model.UpdateContentAssignmentRequest;
import de.enflexit.awb.ws.dynSiteApi.security.UserPermissionHelper;
import de.enflexit.db.userManagement.dataModel.GroupRole;
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
    public Response createMenu(CreateMenuRequest createMenuRequest, SecurityContext securityContext) throws NotFoundException {
    
    	String username = this.getAuthenticatedUsername(securityContext);
    	if (username == null) {
    	    return Response.status(Status.UNAUTHORIZED).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Authentication required.")).build();   
    	}
		// --- Check if the user has the required role --------------------------------------------
    	if (UserPermissionHelper.isUserInRole(username, UserPermissionHelper.EDITOR_ROLE) == false) {
    		return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Access denied: Role 'Editor' required.")).build();
    	}
    	
    	// --- Validate the request ---------------------------------------------------------------
    	if (createMenuRequest == null) {
    		return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Request body is missing.")).build();
    	}
    	if (createMenuRequest.getAllowedRoles() == null || createMenuRequest.getAllowedRoles().isEmpty()) {
    	    return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Allowed roles must be specified.")).build();
    	}
    	
    	MenuItem menuItem = createMenuRequest.getContent();
    	
    	if (menuItem == null || menuItem.getCaption() == null || menuItem.getCaption().isBlank() || menuItem.getPosition() == null || menuItem.getIsHeadMenu() == null) {
            return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Missing required menu fields.")).build();
    	}
    	
    	if (menuItem.getIsHeadMenu() == false && menuItem.getParentId() == null) {
            return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "New menu is not defined as a head menu but no parent ID was provided.")).build();
    	}
    	
    	// --- Convert the rest MenuItem to a database SiteMenu -----------------------------------
    	SiteMenu newMenu = TypeConverter.toDBSiteMenu(menuItem);
    	
    	// --- Try to persist the new menu and return the resulting id or error -------------------
    	try {
    		Set<GroupRole> requiredRolesForAccess = this.getDatabaseHandler().findGroupRolesByNames(createMenuRequest.getAllowedRoles());
    		this.getDatabaseHandler().persistNewMenu(newMenu, requiredRolesForAccess);
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

    	String username = this.getAuthenticatedUsername(securityContext);
    	if (username == null) {
    	    return Response.status(Status.UNAUTHORIZED).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Authentication required.")).build();   
    	}
		// --- Check if the user has the required role --------------------------------------------
    	if (UserPermissionHelper.isUserInRole(username, UserPermissionHelper.EDITOR_ROLE) == false) {
    		return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Access denied: Role 'Editor' required.")).build();
    	}
    	
    	// --- Validate the request ---------------------------------------------------------------
    	if (menuID == null) {
            return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Menu ID is missing.")).build();
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
    
    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService#getMenus(java.lang.String, jakarta.ws.rs.core.SecurityContext)
    */
    @Override
    public Response getMenus(String lang, String forRole, SecurityContext securityContext) throws NotFoundException {
	
    	String username = this.getAuthenticatedUsername(securityContext);
    	if (username == null) {
    	    return Response.status(Status.UNAUTHORIZED).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Authentication required.")).build();   
    	}
    	
    	// --- Validate the request ---------------------------------------------------------------
    	if (lang == null || lang.isBlank() == true) {
            return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Parameter 'lang' is missing.")).build();
    	}

    	// --- Get user roles to check if authorized, also to decide which menus to deliver -------
    	Set<GroupRole> userRoles = UserPermissionHelper.getGroupRolesForUser(username);
    	GroupRole filterRole = null;

		if (forRole != null) {
			for (GroupRole role : userRoles) {
				if (role.getRole().equalsIgnoreCase(forRole)) {
					filterRole = role;
				}
			}
			
			if (filterRole == null) {
				// --- User is trying to get menus for a role he doesn't possess ---------------------
				return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Access denied: User does not possess the requested role: " + forRole)).build();
			}
		}
    	
    	Set<Integer> groupRoleIds = new HashSet<Integer>();
    	if (forRole != null) {
    		groupRoleIds.add(filterRole.getId());
    	} else {
    		
    		// --- Collect Id's of the user's group roles to request their menus with -----------------
    		userRoles.forEach(role -> groupRoleIds.add(role.getId()));
    	}
    	try {
    		List<SiteMenu> dbMenuList;
    		// --- Get menus from database --------------------------------------------------------
			dbMenuList = this.getDatabaseHandler().findMenusForUserRoles(groupRoleIds, filterRole == null);
			List<MenuItem> menuItemList = TypeConverter.toRestMenuItemList(dbMenuList, lang);
			
			// --- Prepare return type ------------------------------------------------------------
			MenuList menuList = new MenuList();
			menuList.setMenuList(menuItemList);
			return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(menuList).build();
	
		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
		}

	}
    
    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService#updateMenu(java.lang.Integer, de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem, jakarta.ws.rs.core.SecurityContext)
    */
    @Override
    public Response updateMenu(Integer menuID, MenuItem menuItem, SecurityContext securityContext) throws NotFoundException {
		
    	String username = this.getAuthenticatedUsername(securityContext);
    	if (username == null) {
    	    return Response.status(Status.UNAUTHORIZED).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Authentication required.")).build();   
    	}
		// --- Check if the user has the required role --------------------------------------------
    	if (UserPermissionHelper.isUserInRole(username, UserPermissionHelper.EDITOR_ROLE) == false) {
    		return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Access denied: Role 'Editor' required.")).build();
    	}
    	
    	// --- Validate the request ---------------------------------------------------------------
    	if (menuID == null || menuItem == null || menuItem.getCaption() == null || menuItem.getCaption().isBlank() || menuItem.getPosition() == null || menuItem.getIsHeadMenu() == null) {
    		return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Missing required menu fields.")).build();
    	}
    	
    	if (menuItem.getIsHeadMenu() == false && menuItem.getParentId() == null) {
            return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Menu is not defined as a head menu but no parent ID was provided.")).build();
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
    public Response updateMenuPermissions(Integer menuId, List<String> requestBody, SecurityContext securityContext) throws NotFoundException {
        
    	String username = this.getAuthenticatedUsername(securityContext);
    	if (username == null) {
    	    return Response.status(Status.UNAUTHORIZED).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Authentication required.")).build();   
    	}
		// --- Check if the user has the required role --------------------------------------------
    	if (UserPermissionHelper.isUserInRole(username, UserPermissionHelper.EDITOR_ROLE) == false) {
    		return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Access denied: Role 'Editor' required.")).build();
    	}
    	
    	// --- Validate the request ---------------------------------------------------------------
    	if (menuId == null || requestBody == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Menu ID and required roles must be specified.")).build(); 		
    	}
		try {
			Set<GroupRole> requiredRolesForAccess = this.getDatabaseHandler().findGroupRolesByNames(requestBody);  		
			this.getDatabaseHandler().updateMenuPermissions(menuId, requiredRolesForAccess);
			return Response.ok().build();

		} catch (IllegalArgumentException iae) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, iae.getMessage())).build();

		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
		}
    }
	
	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService#removeAssignment(java.lang.Integer, java.lang.Integer, jakarta.ws.rs.core.SecurityContext)
	*/
	@Override
	public Response removeAssignment(Integer menuID, Integer elementID, SecurityContext securityContext) throws NotFoundException {
    
    	String username = this.getAuthenticatedUsername(securityContext);
    	if (username == null) {
    	    return Response.status(Status.UNAUTHORIZED).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Authentication required.")).build();   
    	}
		// --- Check if the user has the required role --------------------------------------------
    	if (UserPermissionHelper.isUserInRole(username, UserPermissionHelper.EDITOR_ROLE) == false) {
    		return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Access denied: Role 'Editor' required.")).build();
    	}
		
    	// --- Validate the request ---------------------------------------------------------------
		if (menuID == null || elementID == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Menu ID and menu Item must be specified.")).build();
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
		
    	String username = this.getAuthenticatedUsername(securityContext);
    	if (username == null) {
    	    return Response.status(Status.UNAUTHORIZED).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Authentication required.")).build();   
    	}
		// --- Check if the user has the required role --------------------------------------------
    	if (UserPermissionHelper.isUserInRole(username, UserPermissionHelper.EDITOR_ROLE) == false) {
    		return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Access denied: Role 'Editor' required.")).build();
    	}
		
    	// --- Validate the request ---------------------------------------------------------------
		if (updateContentAssignmentRequest == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Request body is missing.")).build();
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
	
    	String username = this.getAuthenticatedUsername(securityContext);
    	if (username == null) {
    	    return Response.status(Status.UNAUTHORIZED).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Authentication required.")).build();   
    	}
		// --- Check if the user has the required role --------------------------------------------
    	if (UserPermissionHelper.isUserInRole(username, UserPermissionHelper.EDITOR_ROLE) == false) {
    		return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Access denied: Role 'Editor' required.")).build();
    	}
		
    	// --- Validate the request ---------------------------------------------------------------
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
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
		}
	}

	/**
	 * Returns the authenticated username.
	 *
	 * @param securityContext the security context
	 * @return the authenticated username
	 */
	private String getAuthenticatedUsername(SecurityContext securityContext) {
		if (securityContext == null || securityContext.getUserPrincipal() == null) {
			return null;
		}
		return securityContext.getUserPrincipal().getName();
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