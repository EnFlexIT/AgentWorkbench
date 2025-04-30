package de.enflexit.awb.ws.restapi.impl;

import java.security.Principal;

import de.enflexit.awb.ws.restapi.RestApiConfiguration;
import de.enflexit.awb.ws.restapi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.restapi.gen.AppApiService;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.model.Properties;
import de.enflexit.awb.ws.restapi.tools.PropertyConverter;
import de.enflexit.awb.ws.webApp.AwbWebApplication;
import de.enflexit.awb.ws.webApp.AwbWebApplicationManager;
import de.enflexit.awb.ws.webApp.AwbWebApplication.PropertyType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;


/**
 * The individual implementation class for the {@link AppApiService}.
 *
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
    	Properties props = null;
    	if (principal==null) {
    		props = PropertyConverter.toWebAppProperties(AwbWebApplicationManager.getProperties(AwbWebApplication.PropertyType.PublicProperties));
    	} else {
    		props = PropertyConverter.toWebAppProperties(AwbWebApplicationManager.getProperties(AwbWebApplication.PropertyType.AllProperties));
    	}
    	return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(props).build();
    }
    
    
    
    /* (non-Javadoc)
     * @see de.enflexit.awb.ws.restapi.gen.AppApiService#setAppSettings(de.enflexit.awb.ws.restapi.gen.model.Properties, jakarta.ws.rs.core.SecurityContext)
     */
    @Override
    public Response setAppSettings(Properties properties, SecurityContext securityContext) throws NotFoundException {
        
    	// --- Check who is the user --------------------------------
    	Principal principal = securityContext.getUserPrincipal();
    	    	
    	// TODO
    	
    	
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
