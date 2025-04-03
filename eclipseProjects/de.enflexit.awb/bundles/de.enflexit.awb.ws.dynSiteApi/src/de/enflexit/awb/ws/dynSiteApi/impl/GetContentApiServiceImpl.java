package de.enflexit.awb.ws.dynSiteApi.impl;

import de.enflexit.awb.ws.dynSiteApi.RestApiConfiguration;
import de.enflexit.awb.ws.dynSiteApi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.dynSiteApi.gen.GetContentApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentList;
import de.enflexit.awb.ws.dynSiteApi.samples.DynamicContentExampleFactory;
import de.enflexit.awb.ws.dynSiteApi.samples.ImageHelper;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * The Class GetContentApiServiceImpl.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-04-02T11:34:41.198040400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class GetContentApiServiceImpl extends GetContentApiService {

	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.dynSiteApi.gen.GetContentApiService#getContentMenueIDGet(java.lang.Integer, jakarta.ws.rs.core.SecurityContext)
	 */
	@Override
    public Response getContentMenueIDGet(Integer menueID, SecurityContext securityContext) throws NotFoundException {
        
    	if (menueID==null) {
            return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Missing menueID!")).build();
    	}
		
    	// --- Create the content according to the menueID -------------------
		SiteContentList scList = this.getSiteContentList(menueID);

		
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(scList).build();
    }
	
	
	/**
	 * Returns the site content list.
	 *
	 * @param menuID the menu ID
	 * @return the site content list
	 */
	private SiteContentList getSiteContentList(Integer menuID) {
		
		SiteContentList scList = new SiteContentList();

		switch (menuID) {
		case -1: 
			scList.addContentListItem(DynamicContentExampleFactory.createSiteContentText(1, 0,  false, null, "Example Text"));
			scList.addContentListItem(DynamicContentExampleFactory.createSiteContentText(2, 10, false, null, "Example Text that should be updated every 10 seconds"));
			scList.addContentListItem(DynamicContentExampleFactory.createSiteContentText(3, 0,  true,  null, "Example Text that is to edit"));
			break;
			
		case -2:
			String image1 = ImageHelper.getImageIconBase64Encoded("awb48.png");
			scList.addContentListItem(DynamicContentExampleFactory.createSiteContentImage(4, 0,  false, "image/png", image1));

			String image2 = ImageHelper.getImageIconBase64Encoded("components.gif");
			scList.addContentListItem(DynamicContentExampleFactory.createSiteContentImage(5, 10, true,  "image/gif", image2));
			
			String image3 = ImageHelper.getImageIconBase64Encoded("splash.bmp");
			scList.addContentListItem(DynamicContentExampleFactory.createSiteContentImage(6, 0,  false, "image/bmp", image3));

			String image4 = ImageHelper.getImageIconBase64Encoded("Survey_Logo.jpg");
			scList.addContentListItem(DynamicContentExampleFactory.createSiteContentImage(7, 0,  true,  "image/jpg", image4));
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + menuID);
		}
		return scList;
	}
	
	
}
