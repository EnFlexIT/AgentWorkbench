package de.enflexit.awb.core.config;

/**
 * The Class SecurityPolicies.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class SecurityPolicies {
	
	public static final int POLICY_MINIMAL_USER_NAME_LENGHT = 5;
	public static final int POLICY_MINIMAL_PASSWORD_LENGHT = 5;
	
	/**
	 * Checks if the user name matches the security policies.
	 *
	 * @param userName the user name
	 * @return the user name error
	 */
	public static String getUserNameError(String userName) {
		
		if (userName == null || userName.isBlank() == true) {
			return "The user name is empty.";
			
		} else if (userName.length() < POLICY_MINIMAL_USER_NAME_LENGHT) {
			return "The user name is to short.";
			
		}
		return null;
	}
	
	/**
	 * Checks if the password matches the security policies.
	 *
	 * @param password to check
	 * @return In case of error returns the specific error
	 */
	public static String getPasswordError(String password) {
		
		if (password == null || password.isBlank() == true) {
			return "The password is empty.";
			
		} else if (password.length() < POLICY_MINIMAL_PASSWORD_LENGHT) {
			return "The password is to short.";
			
		}
		return null;
	}
}
