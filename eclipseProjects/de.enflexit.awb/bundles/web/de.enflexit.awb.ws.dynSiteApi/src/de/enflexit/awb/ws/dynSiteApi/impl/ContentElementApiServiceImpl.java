package de.enflexit.awb.ws.dynSiteApi.impl;

import de.enflexit.awb.ws.dynSiteApi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.dynSiteApi.gen.ContentElementApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;
import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;


/**
 * The Class ContentElementApiServiceImpl.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-18T08:59:24.902319200+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class ContentElementApiServiceImpl extends ContentElementApiService {
    
    /* (non-Javadoc)
     * @see de.enflexit.awb.ws.dynSiteApi.gen.ContentElementApiService#contentElementElementIDGet(java.lang.Integer, jakarta.ws.rs.core.SecurityContext)
     */
    @Override
    public Response contentElementElementIDGet(Integer elementID, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    
    /* (non-Javadoc)
     * @see de.enflexit.awb.ws.dynSiteApi.gen.ContentElementApiService#contentElementPut(de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent, jakarta.ws.rs.core.SecurityContext)
     */
    @Override
    public Response contentElementPut(AbstractSiteContent abstractSiteContent, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
