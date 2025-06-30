package de.enflexit.awb.ws.restapi.impl;

import java.security.Principal;
import java.util.Map;

import org.eclipse.jetty.security.openid.OpenIdCredentials;
import org.eclipse.jetty.security.openid.OpenIdUserPrincipal;

import de.enflexit.awb.ws.restapi.RestApiConfiguration;
import de.enflexit.awb.ws.restapi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.restapi.gen.AppApiService;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.model.Properties;
import de.enflexit.awb.ws.restapi.tools.PropertyConverter;
import de.enflexit.awb.ws.webApp.AwbWebApplication;
import de.enflexit.awb.ws.webApp.AwbWebApplicationManager;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;

/**
 * The individual implementation class for the {@link AppApiService}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-04-29T11:33:26.991574300+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class AppApiServiceImpl extends AppApiService {
    
    /* (non-Javadoc)
     * @see de.enflexit.awb.ws.restapi.gen.AppApiService#getAppSettings(jakarta.ws.rs.core.SecurityContext)
     */
    @Override
    public Response getAppSettings(SecurityContext securityContext) throws NotFoundException {
        
    	// --- Check who is the user --------------------------------
    	Principal principal = securityContext.getUserPrincipal();
    	
    	// --- Create the properties to return ----------------------
    	de.enflexit.common.properties.Properties awbProps = null;
    	if (principal==null) {
    		awbProps = AwbWebApplicationManager.getProperties(AwbWebApplication.PropertyType.PublicProperties);
    	} else {
    		awbProps = AwbWebApplicationManager.getProperties(AwbWebApplication.PropertyType.AllProperties);
    		this.addOIDCPrincipalInformation(principal, awbProps);
    	}

    	// --- Convert response -------------------------------------
    	Properties endpointProps = PropertyConverter.toWebRestProperties(awbProps);
    	return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(endpointProps).build();
    }
    
    /**
     * Adds the principal information.
     *
     * @param principal the principal
     * @param props the Properties of the common bundle
     */
    private void addOIDCPrincipalInformation(Principal principal, de.enflexit.common.properties.Properties awbProps) {
    	
    	if (principal==null || awbProps==null) return;
    	if (principal instanceof OpenIdUserPrincipal == false) return;
    	
    	try {

    		OpenIdUserPrincipal oidUP = (OpenIdUserPrincipal) principal;
    		OpenIdCredentials credentials = oidUP.getCredentials();
			
    		Map<String,Object> claims = credentials.getClaims();
    		//claims.keySet().forEach(key -> System.out.println(key + ": " + claims.get(key)));

    		String id = (String) claims.get("sub");
    		awbProps.setStringValue("_oidc.id", id);
    		
    		String name = (String) claims.get("name");
    		awbProps.setStringValue("_oidc.name", name);
    		
    		String preferredUsername = (String) claims.get("preferred_username");
    		awbProps.setStringValue("_oidc.preferred_username", preferredUsername);
    		
    		String givenName = (String) claims.get("given_name");
    		awbProps.setStringValue("_oidc.given_name", givenName);
    		
    		String familyName = (String) claims.get("family_name");
    		awbProps.setStringValue("_oidc.family_name", familyName);
    		
    		String email = (String) claims.get("email");
    		awbProps.setStringValue("_oidc.email", email);
    		
    		boolean email_verified = (Boolean)claims.get("email_verified");
    		awbProps.setBooleanValue("_oidc.email_verified", email_verified);
    		
    		String locale = (String) claims.get("locale");
    		awbProps.setStringValue("_oidc.locale", locale);
    		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
    
    
    /* (non-Javadoc)
     * @see de.enflexit.awb.ws.restapi.gen.AppApiService#setAppSettings(de.enflexit.awb.ws.restapi.gen.model.Properties, jakarta.ws.rs.core.SecurityContext)
     */
    @Override
    public Response setAppSettings(Properties properties, SecurityContext securityContext) throws NotFoundException {
        
    	// --- Check who is the user --------------------------------
    	Principal principal = securityContext.getUserPrincipal();
    	if (principal==null) {
    		return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Permission denied!!")).build();
    	}
    	
    	// TODO
    	
    	
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
