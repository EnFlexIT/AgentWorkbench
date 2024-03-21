package de.enflexit.awb.ws.core.security;

import java.io.IOException;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.DefaultIdentityService;
import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.RoleInfo;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.UserIdentity;

import de.enflexit.awb.ws.core.util.ServletHelper;

/**
 * The Class SingleApiKeySecurityHandler ... TODO
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SingleApiKeySecurityHandler extends ConstraintSecurityHandler implements LoginService {

	private final String apiKeyName;
	private final String apiKeyValue;
	
	private IdentityService identityService;
	
	/**
	 * Instantiates a new SingleApiKeySecurityHandler that enables an unprotected access
	 * to the underlying resources (no API key authorization is required).
	 */
	public SingleApiKeySecurityHandler() {
		this(null, null);
	}
	/**
	 * Instantiates a new SingleApiKeySecurityHandler where access is granted, if the query parameter of a request 
	 * matches the specified API key name and its value.
	 *
	 * @param login the login
	 * @param password the password
	 */
	public SingleApiKeySecurityHandler(String apiKeyName, String apiKeyValue) {
		this.apiKeyName = apiKeyName;
		this.apiKeyValue = apiKeyValue;
		
		this.setIdentityService(new DefaultIdentityService());
		this.setLoginService(this);
	}

	/**
	 * Checks if is secured.
	 * @return true, if is secured
	 */
	private boolean isSecured() {
		boolean isSecuredLogin    = this.apiKeyName!=null  && this.apiKeyName.isBlank()==false;
		boolean isSecuredPassword = this.apiKeyValue!=null && this.apiKeyValue.isBlank()==false;
		return isSecuredLogin & isSecuredPassword;
	}
	
	/**
	 * Checks if the current request allows an authorized access.
	 * @return true, if is authorized access
	 */
	private boolean isAuthorizedAccess(Request request) {
		
		if (this.isSecured()==false) return true;
		
		// --- Check query parameter for API key value ------------------------ 
		String apiKeyValueCheck = request.getParameter(this.apiKeyName);
		if (apiKeyValueCheck==null) {
			// --- If not found in the query parameter, check header ----------
			apiKeyValueCheck = request.getHeader(this.apiKeyName);
		}
		return apiKeyValueCheck!=null && StringUtils.equals(apiKeyValueCheck, this.apiKeyValue);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#prepareConstraintInfo(java.lang.String, org.eclipse.jetty.server.Request)
	 */
	@Override
	protected RoleInfo prepareConstraintInfo(String pathInContext, Request request) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#checkUserDataPermissions(java.lang.String, org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, org.eclipse.jetty.security.RoleInfo)
	 */
	@Override
	protected boolean checkUserDataPermissions(String pathInContext, Request request, Response response, RoleInfo constraintInfo) throws IOException {
		return this.isAuthorizedAccess(request);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#isAuthMandatory(org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, java.lang.Object)
	 */
	@Override
	protected boolean isAuthMandatory(Request baseRequest, Response base_response, Object constraintInfo) {
		if (ServletHelper.isPreflightRequest(baseRequest)==true) return false;
		return !this.isAuthorizedAccess(baseRequest);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#checkWebResourcePermissions(java.lang.String, org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, java.lang.Object, org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	protected boolean checkWebResourcePermissions(String pathInContext, Request request, Response response, Object constraintInfo, UserIdentity userIdentity) throws IOException {
		return this.isAuthorizedAccess(request);
	}
	
	// ------------------------------------------------------------------------
	// --- From here, the methods of the LoginService -------------------------
	// ------------------------------------------------------------------------	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.LoginService#getName()
	 */
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.LoginService#login(java.lang.String, java.lang.Object, javax.servlet.ServletRequest)
	 */
	@Override
	public UserIdentity login(String username, Object credentials, ServletRequest request) {
		// --- Nothing to do here ----
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.LoginService#validate(org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	public boolean validate(UserIdentity user) {
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.LoginService#logout(org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	public void logout(UserIdentity user) {
		// --- Nothing to do here ----
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.LoginService#getIdentityService()
	 */
	@Override
	public IdentityService getIdentityService() {
		return this.identityService;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.LoginService#setIdentityService(org.eclipse.jetty.security.IdentityService)
	 */
	@Override
	public void setIdentityService(IdentityService service) {
		this.identityService = service;
	}
	
}
