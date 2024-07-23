package de.enflexit.awb.ws.core.security;

import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.security.AuthenticationState;
import org.eclipse.jetty.security.Authenticator;
import org.eclipse.jetty.security.Constraint;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.security.UserIdentity;
import org.eclipse.jetty.security.authentication.LoginAuthenticator.UserAuthenticationSucceeded;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.Fields;
import org.eclipse.jetty.util.Fields.Field;

/**
 * The Class SingleApiKeySecurityHandler
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SingleApiKeySecurityHandler extends SecurityHandler.PathMapped implements Authenticator {

	private TreeMap<String, String> securityHandlerConfiguration;
	
	/**
	 * Instantiates a new SingleApiKeySecurityHandler that enables an unprotected access
	 * to the underlying resources (no API key authorization is required).
	 */
	public SingleApiKeySecurityHandler() {
		this(null);
	}
	/**
	 * Instantiates a new SingleApiKeySecurityHandler where access is granted, if the query parameter of a request 
	 * matches the specified API key name and its value.
	 *
	 */
	public SingleApiKeySecurityHandler(TreeMap<String, String> securityHandlerConfiguration) {

		this.securityHandlerConfiguration = securityHandlerConfiguration;
		
		this.setAuthenticator(this);
		
		if (this.isSecured()==true) {
			this.put("/*", Constraint.ANY_USER);
		} else {
			this.put("/*", Constraint.ALLOWED);
		}
	}

	/**
	 * Return the current user name.
	 * @return the user name
	 */
	private String getApiKeyName() {
		return securityHandlerConfiguration!=null ? securityHandlerConfiguration.get(SingleApiKeySecurityService.configParameterKeys[0]) : null;
	}
	/**
	 * Returns the current password.
	 * @return the password
	 */
	private String getApiKeyValue() {
		return securityHandlerConfiguration!=null ? securityHandlerConfiguration.get(SingleApiKeySecurityService.configParameterKeys[1]) : null;
	}
	/**
	 * Checks if is secured.
	 * @return true, if is secured
	 */
	private boolean isSecured() {
		
		String apiKeyName = this.getApiKeyName();
		String apiKeyValue = this.getApiKeyValue();
		
		boolean isSecuredLogin    = apiKeyName!=null  && apiKeyName.isBlank()==false;
		boolean isSecuredPassword = apiKeyValue!=null && apiKeyValue.isBlank()==false;
		return isSecuredLogin & isSecuredPassword;
	}
	/**
	 * Checks if the current request allows an authorized access.
	 * @return true, if is authorized access
	 */
	private boolean isAuthorizedAccess(Request request) {
		
		if (this.isSecured()==false) return true;
		
		// --- Check query parameter for API key value ------------------------ 
		String apiKeyValueCheck = null;
		try {
			Fields fields = Request.getParameters(request);
			if (fields.getSize()>0) {
				Field field   = fields.get(this.getApiKeyName());
				if (field!=null) {
					apiKeyValueCheck = field.getValue();
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		if (apiKeyValueCheck == null) {
			// --- If not found in the query parameter, check header ----------
			HttpFields httpFields = request.getHeaders();
			apiKeyValueCheck = httpFields.get(this.getApiKeyName());
		}
		return apiKeyValueCheck!=null && StringUtils.equals(apiKeyValueCheck, this.getApiKeyValue());
	}

	
	// ------------------------------------------------------------------------
	// --- From here, the methods of the Authenticator ------------------------
	// ------------------------------------------------------------------------	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.Authenticator#setConfiguration(org.eclipse.jetty.security.Authenticator.Configuration)
	 */
	@Override
	public void setConfiguration(Configuration configuration) {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.Authenticator#validateRequest(org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, org.eclipse.jetty.util.Callback)
	 */
	@Override
	public AuthenticationState validateRequest(Request request, Response response, Callback callback) throws ServerAuthException {
		
		if (this.isAuthorizedAccess(request)==true) {
			UserIdentity user = new AwbUserIdentity(this.getApiKeyName(), this.getApiKeyValue(), true);
			return new UserAuthenticationSucceeded(getAuthenticationType(), user);
		} 
		return null;
	}
}
