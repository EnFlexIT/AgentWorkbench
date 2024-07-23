package de.enflexit.awb.ws.restapi.impl;

import java.security.Principal;

import org.eclipse.jetty.security.UserPrincipal;

import de.enflexit.awb.ws.core.ServletSecurityConfiguration;
import de.enflexit.awb.ws.core.security.jwt.JwtPrincipal;
import de.enflexit.awb.ws.core.security.jwt.JwtSingleUserSecurityService.JwtParameter;
import de.enflexit.awb.ws.restapi.AwbWebServerAccess;
import de.enflexit.awb.ws.restapi.RestApiConfiguration;
import de.enflexit.awb.ws.restapi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.UserApi;
import de.enflexit.awb.ws.restapi.gen.UserApiService;
import de.enflexit.awb.ws.restapi.gen.model.PasswordChange;
import de.enflexit.common.StringHelper;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;

/**
 * The individual implementation class for the {@link UserApi}.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-01-30T22:47:52.734099700+01:00[Europe/Berlin]")
public class UserApiImpl extends UserApiService {
    
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.restapi.gen.UserApiService#loginUser(javax.ws.rs.core.SecurityContext)
	 */
	@Override
	public Response loginUser(SecurityContext securityContext) throws NotFoundException {
		
		Principal principal = securityContext.getUserPrincipal();
		if (principal==null) {
			String errMsg = "Could not find any principal information.";
			return Response.status(Status.UNAUTHORIZED).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, errMsg)).build();
		}
		
		if (principal instanceof JwtPrincipal jwtPrincipal) {
			// --- Get the JWT String from principal ----------------
			String bearerString = "Bearer " + jwtPrincipal.getJwtToken();
			return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(bearerString).build();
		}
		// --- Fallback return that does make no sense  -------------
		return Response.status(Status.ACCEPTED).entity(new ApiResponseMessage(ApiResponseMessage.OK, principal.getName())).build();
	}
	
	
	/* (non-Javadoc)
     * @see de.enflexit.awb.ws.restapi.gen.UserApiService#changePassword(de.enflexit.awb.ws.restapi.gen.model.PasswordChange, javax.ws.rs.core.SecurityContext)
     */
    @Override
    public Response changePassword(PasswordChange passwordChange, SecurityContext securityContext) throws NotFoundException {
    	
    	String errMsg = "";
    	Principal principal = securityContext.getUserPrincipal();
    	if (principal==null) {
    		errMsg = "Could not find any principal information.";
    		
    	} else {
    		// --- Get new settings ---------------------------------
    		String userName = principal.getName();
        	String pswdOld = passwordChange.getPasswordOld();
        	String pswdNew = passwordChange.getPasswordNew();
    		
        	ServletSecurityConfiguration secConfig = AwbWebServerAccess.getServletSecurityConfiguration();
			if (secConfig!=null) {
				// --- Get current user name & password -------------
				String userNameSec = secConfig.getSecurityHandlerConfiguration().get(JwtParameter.UserName.getKey());
				String pswdSec = secConfig.getSecurityHandlerConfiguration().get(JwtParameter.Password.getKey()); 
				// --- Check against current credentials ------------
				if (StringHelper.isEqualString(userName, userNameSec)==true && StringHelper.isEqualString(pswdOld, pswdSec)==true) {
					boolean isValidNewPassword = pswdNew!=null && pswdNew.isBlank()==false && pswdNew.length()>=4 && StringHelper.isEqualString(pswdOld, pswdNew)==false;
					if (isValidNewPassword==true) {
						// --- Change password is allowed -----------
						secConfig.getSecurityHandlerConfiguration().put(JwtParameter.Password.getKey(), pswdNew);
						AwbWebServerAccess.saveJettyConfiguration();
						// --- Return success message -----------
						String okMessage = null;
						if (principal instanceof UserPrincipal uPrincipal) {
							okMessage = "BASIC Password Changed for " + uPrincipal.getName();
							
						} else if (principal instanceof JwtPrincipal jwtPrincipal) {
							okMessage = "Bearer " + jwtPrincipal.getJwtToken();
							
						} else {
							System.err.println("[" + this.getClass().getSimpleName() + "] Unknown principal type '" + principal.getClass().getName() + "'");
						}
						return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(okMessage).build();
						
					} else {
						errMsg = "The new password is invalid!";
					}
					
				} else {
					errMsg = "The credentials currently provided are incorrect!";	
				}
				
			} else {
				errMsg = "No security configuration could be found!";
			}
    	}

		// --- Return error message -----------------------------
        return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, errMsg)).build();
    }


    /* (non-Javadoc)
	 * @see de.enflexit.awb.ws.restapi.gen.UserApiService#logout(javax.ws.rs.core.SecurityContext)
	 */
	@Override
	public Response logout(SecurityContext securityContext) throws NotFoundException {
		// --- Nothing to do here ! ---
		
		
		
		return null;
	}
    
}
