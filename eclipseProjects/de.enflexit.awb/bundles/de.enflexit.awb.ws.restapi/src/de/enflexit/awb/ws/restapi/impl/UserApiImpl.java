package de.enflexit.awb.ws.restapi.impl;

import java.security.Principal;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.enflexit.awb.ws.core.security.jwt.JwtPrincipal;
import de.enflexit.awb.ws.restapi.RestApiConfiguration;
import de.enflexit.awb.ws.restapi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.UserApi;
import de.enflexit.awb.ws.restapi.gen.UserApiService;
import de.enflexit.awb.ws.restapi.gen.model.PasswordChange;
import de.enflexit.awb.ws.restapi.gen.model.UserLogin;

/**
 * The individual implementation class for the {@link UserApi}.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-01-30T22:47:52.734099700+01:00[Europe/Berlin]")
public class UserApiImpl extends UserApiService {
    
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.restapi.gen.UserApiService#loginUser(de.enflexit.awb.ws.restapi.gen.model.UserLogin, javax.ws.rs.core.SecurityContext)
	 */
	@Override
	public Response loginUser(UserLogin userLogin, SecurityContext securityContext) throws NotFoundException {
		
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
    	
    	System.out.println("=> " + passwordChange.getPasswordOld() + " " + passwordChange.getPasswordNew());
    	
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    
}
