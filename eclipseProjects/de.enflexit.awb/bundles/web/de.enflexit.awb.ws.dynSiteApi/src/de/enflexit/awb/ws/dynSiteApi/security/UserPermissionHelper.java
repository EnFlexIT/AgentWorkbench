package de.enflexit.awb.ws.dynSiteApi.security;

import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.awb.ws.core.db.WebAppDatabaseHandler;
import de.enflexit.db.userManagement.dataModel.GroupRole;

/**
 * The Class UserPermissionHelper.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class UserPermissionHelper {

	private static Logger LOGGER = LoggerFactory.getLogger(UserPermissionHelper.class);
	
	public static final String EDITOR_ROLE = "editor";
	public static final String USER_ROLE = "user";
	
	private static WebAppDatabaseHandler webAppDatabaseHandler;
	
	/**
	 * Returns the group roles for user.
	 *
	 * @param userName the user name
	 * @return the group roles for user
	 */
	public static Set<GroupRole> getGroupRolesForUser(String userName) {
		
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
	public static boolean isUserInRole(String userName, String requiredRole) {
		
		if (requiredRole == null || requiredRole.isBlank() == true || userName == null || userName.isBlank() == true) return false;
		Set<GroupRole> roles = getGroupRolesForUser(userName);
        
        return isUserInRole(roles, requiredRole);
	}
	/**
	 * Checks if is user in role.
	 *
	 * @param userRoles the user roles
	 * @param requiredRole the required role
	 * @return true, if is user in role
	 */
	public static boolean isUserInRole(Set<GroupRole> userRoles, String requiredRole) {

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