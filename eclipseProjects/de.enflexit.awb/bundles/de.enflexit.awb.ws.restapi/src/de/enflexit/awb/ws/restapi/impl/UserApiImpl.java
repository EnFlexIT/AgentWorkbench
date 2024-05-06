package de.enflexit.awb.ws.restapi.impl;

import java.security.Principal;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

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

/**
 * The individual implementation class for the {@link UserApi}.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-01-30T22:47:52.734099700+01:00[Europe/Berlin]")
public class UserApiImpl extends UserApiService {
    
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.restapi.gen.UserApiService#loginUser(javax.ws.rs.core.SecurityContext)
	 */
	@Override
	public Response loginUser(SecurityContext securityContext) throws NotFoundException {
		
		Principal principal = securityContext.getUserPrincipal();
		if (principal instanceof JwtPrincipal) {
			// --- Get the JWT String from principal ----------------
			JwtPrincipal jwtPrincipal = (JwtPrincipal) principal;
			String bearerString = "Bearer " + jwtPrincipal.getJwtToken();
			return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(bearerString).build();
		}
		// --- Fallback return that does make no sense  -------------
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(principal.getName()).build();
	}
	
	
	/* (non-Javadoc)
     * @see de.enflexit.awb.ws.restapi.gen.UserApiService#changePassword(de.enflexit.awb.ws.restapi.gen.model.PasswordChange, javax.ws.rs.core.SecurityContext)
     */
    @Override
    public Response changePassword(PasswordChange passwordChange, SecurityContext securityContext) throws NotFoundException {
    	
    	String errMsg = "";
    	Principal principal = securityContext.getUserPrincipal();
		if (principal instanceof JwtPrincipal) {
			// --- Get the JWT String from principal ----------------
			JwtPrincipal jwtPrincipal = (JwtPrincipal) principal;
			String userName = jwtPrincipal.getName();
			String pswdOld = passwordChange.getPasswordOld();
			String pswdNew = passwordChange.getPasswordNew();
			
			ServletSecurityConfiguration secConfig = AwbWebServerAccess.getServletSecurityConfiguration();
			if (secConfig!=null) {
				// --- Get current user name & password -------------
				String userNameSec = secConfig.getSecurityHandlerConfiguration().get(JwtParameter.UserName.getKey());
				String pswdSec = secConfig.getSecurityHandlerConfiguration().get(JwtParameter.Password.getKey()); 
				// --- Check against current credentials ------------
				if (StringHelper.isEqualString(userName, userNameSec)==true && StringHelper.isEqualString(pswdOld, pswdSec)==true) {
					// --- Check if the new password is valid -------
					boolean isValidNewPassword = pswdNew!=null && pswdNew.isBlank()==false && pswdNew.length()>=4 && StringHelper.isEqualString(pswdOld, pswdNew)==false;
					if (isValidNewPassword==true) {
						// --- Change password is allowed -----------
						secConfig.getSecurityHandlerConfiguration().put(JwtParameter.Password.getKey(), pswdNew);
						AwbWebServerAccess.saveJettyConfiguration();
						// --- Return success message ---------------
						String bearerString = "Bearer " + jwtPrincipal.getJwtToken();
						return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(bearerString).build();
						
					} else {
						errMsg = "The new password is invalid!";
					}
				
				} else {
					errMsg = "The current credentials provided are incorrect!";
				}
			} else {
				errMsg = "No security configuration could be found!";
			}
		} else {
			errMsg = "Could not find any principal information.";
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
