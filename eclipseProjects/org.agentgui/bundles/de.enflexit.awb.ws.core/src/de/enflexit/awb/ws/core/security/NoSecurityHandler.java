package de.enflexit.awb.ws.core.security;

import java.io.IOException;

import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.RoleInfo;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.UserIdentity;

/**
 * The Class NoSecurityHandler does no security checks and is internally only used 
 * to replace a real (working) SecurtiyHandler by a non securing SecurityHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class NoSecurityHandler extends ConstraintSecurityHandler {

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
		return true;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#isAuthMandatory(org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, java.lang.Object)
	 */
	@Override
	protected boolean isAuthMandatory(Request baseRequest, Response base_response, Object constraintInfo) {
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#checkWebResourcePermissions(java.lang.String, org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, java.lang.Object, org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	protected boolean checkWebResourcePermissions(String pathInContext, Request request, Response response, Object constraintInfo, UserIdentity userIdentity) throws IOException {
		return true;
	}
}
