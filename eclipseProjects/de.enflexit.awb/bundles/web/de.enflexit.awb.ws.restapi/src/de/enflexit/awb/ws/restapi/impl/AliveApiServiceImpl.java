package de.enflexit.awb.ws.restapi.impl;

import de.enflexit.awb.ws.restapi.gen.AliveApiService;
import de.enflexit.awb.ws.restapi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * The Class AliveApiServiceImpl.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-02-20T16:32:18.722136900+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class AliveApiServiceImpl extends AliveApiService {
    
    /* (non-Javadoc)
     * @see de.enflexit.awb.ws.restapi.gen.AliveApiService#aliveGet(jakarta.ws.rs.core.SecurityContext)
     */
    @Override
    public Response aliveGet(SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "I'm alive!")).build();
    }
}
