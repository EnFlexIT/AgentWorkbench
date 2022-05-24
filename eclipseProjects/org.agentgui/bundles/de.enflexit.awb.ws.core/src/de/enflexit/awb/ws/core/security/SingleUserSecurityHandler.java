package de.enflexit.awb.ws.core.security;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.DefaultIdentityService;
import org.eclipse.jetty.security.RoleInfo;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.UserIdentity;

/**
 * The Class SingleUserSecurityHandler ... TODO
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SingleUserSecurityHandler extends ConstraintSecurityHandler {

	private final String login;
	private final String password;
	
	
	/**
	 * Instantiates a new SingleUserSecurityHandler that enables an unprotected access
	 * to the underlying resources (no login and no password is required).
	 */
	public SingleUserSecurityHandler() {
		this(null, null);
	}
	/**
	 * Instantiates a new SingleUserSecurityHandler where access is granted, if the login
	 * information matching the specified login and password.
	 *
	 * @param login the login
	 * @param password the password
	 */
	public SingleUserSecurityHandler(String login, String password) {
		this.login = login;
		this.password = password;
		
		this.setAuthenticator(new BasicAuthenticator());
		this.setIdentityService(new DefaultIdentityService());
		this.setLoginService(new SingleUserLoginService(login, password));
	}

	/**
	 * Checks if is secured.
	 * @return true, if is secured
	 */
	private boolean isSecured() {
		boolean isSecuredLogin    = this.login!=null && this.login.isBlank()==false;
		boolean isSecuredPassword = this.password!=null && this.password.isBlank()==false;
		return isSecuredLogin & isSecuredPassword;
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
		return this.isSecured();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#isAuthMandatory(org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, java.lang.Object)
	 */
	@Override
	protected boolean isAuthMandatory(Request baseRequest, Response base_response, Object constraintInfo) {
		return this.isSecured();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#checkWebResourcePermissions(java.lang.String, org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, java.lang.Object, org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	protected boolean checkWebResourcePermissions(String pathInContext, Request request, Response response, Object constraintInfo, UserIdentity userIdentity) throws IOException {
		return userIdentity!=null && StringUtils.equals(login, userIdentity.getUserPrincipal().getName());
	}

}
