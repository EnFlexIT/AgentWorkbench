package de.enflexit.awb.ws.dynSiteApi.impl;

import java.util.List;

import de.enflexit.awb.ws.dynSiteApi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;
import de.enflexit.awb.ws.dynSiteApi.gen.SetContentApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentListUpdate;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * The Class SetContentApiServiceImpl.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-04-02T11:34:41.198040400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class SetContentApiServiceImpl extends SetContentApiService {
    
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.dynSiteApi.gen.SetContentApiService#setContentPut(de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentListUpdate, jakarta.ws.rs.core.SecurityContext)
	 */
	@Override
    public Response setContentPut(SiteContentListUpdate siteContentListUpdate, SecurityContext securityContext) throws NotFoundException {

		Integer menueID = siteContentListUpdate.getMenuID();
		List<AbstractSiteContent> siteContentList = siteContentListUpdate.getContentList();
		
		
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
