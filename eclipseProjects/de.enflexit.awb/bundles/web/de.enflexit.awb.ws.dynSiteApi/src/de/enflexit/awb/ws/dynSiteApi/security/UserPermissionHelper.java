package de.enflexit.awb.ws.dynSiteApi.security;

import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.awb.ws.core.db.WebAppDatabaseHandler;
import de.enflexit.db.userManagement.dataModel.GroupRole;
import de.enflexit.db.userManagement.dataModel.UserRole;

/**
 * The Class UserPermissionHelper.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class UserPermissionHelper {

	private static Logger LOGGER = LoggerFactory.getLogger(UserPermissionHelper.class);
	
	public static final String EDITOR_ROLE = "editor";
	
	private static WebAppDatabaseHandler webAppDatabaseHandler;

	
	
	/**
	 * Returns the backend roles for the specified user
	 *
	 * @param userName the user name
	 * @return the backend roles for
	 */
	public static Set<UserRole> getBackendRolesFor(String userName) {
		
		if (userName == null) return Collections.emptySet();
		
			Set<UserRole> userRoles = UserPermissionHelper.getDatabaseHandler().getUserManagementDatabaseHandler().getBackendUserRoles(userName);
			return userRoles;
	}	
	
	/**
	 * Checks if is user in backend role.
	 *
	 * @param userName the user name
	 * @param requiredRole the required role
	 * @return true, if is user in backend role
	 */
	public static boolean isUserInBackendRole(String userName, String requiredRole) {
		
		if (requiredRole == null || requiredRole.isBlank() == true || userName == null || userName.isBlank() == true) return false;
		Set<UserRole> roles = getBackendRolesFor(userName);
        
        return isUserInBackendRole(roles, requiredRole);
	}	
	/**
	 * Checks if is user in backend role.
	 *
	 * @param userRoles the user roles
	 * @param requiredRole the required role
	 * @return true, if is user in backend role
	 */
	public static boolean isUserInBackendRole(Set<UserRole> userRoles, String requiredRole) {

		if (requiredRole == null || requiredRole.isBlank() == true) return false;
		
		return userRoles.stream().map(UserRole::getRole).anyMatch(roleName -> roleName.equalsIgnoreCase(requiredRole.trim()));		
		
	}
	
	
	/**
	 * Returns the group roles for user.
	 *
	 * @param userName the user name
	 * @return the group roles for user
	 */
	public static Set<GroupRole> getFrontendRolesFor(String userName) {
		
		if (userName == null) return Collections.emptySet();
		
		try {
			Set<GroupRole> userRoles = UserPermissionHelper.getDatabaseHandler().getUserManagementDatabaseHandler().getFrontendUserRoles(userName);
			return userRoles;
			
		} catch (Exception ex) {
			LOGGER.error("Error while checking roles for User '{}'", userName, ex);
			return Collections.emptySet();
		}
	}

	/**
	 * Checks if is user in role.
	 *
	 * @param userName the user name
	 * @param requiredRole the required role
	 * @return true, if is user in role
	 */
	public static boolean isUserInFrontendRole(String userName, String requiredRole) {
		
		if (requiredRole == null || requiredRole.isBlank() == true || userName == null || userName.isBlank() == true) return false;
		Set<GroupRole> roles = getFrontendRolesFor(userName);
        
        return isUserInFrontendRole(roles, requiredRole);
	}
	/**
	 * Checks if is user in role.
	 *
	 * @param userRoles the user roles
	 * @param requiredRole the required role
	 * @return true, if is user in role
	 */
	public static boolean isUserInFrontendRole(Set<GroupRole> userRoles, String requiredRole) {

		if (requiredRole == null || requiredRole.isBlank() == true) return false;
		
		return userRoles.stream().map(GroupRole::getRole).anyMatch(roleName -> roleName.equalsIgnoreCase(requiredRole.trim()));		
		
	}
	
	/**
	 * Returns the database handler.
	 *
	 * @return the database handler
	 */
	private static WebAppDatabaseHandler getDatabaseHandler() {
		if (webAppDatabaseHandler == null) {
			webAppDatabaseHandler = new WebAppDatabaseHandler();
		}
		return webAppDatabaseHandler;
	}
}