package de.enflexit.awb.ws.dynSiteApi.impl;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.awb.ws.dynSiteApi.RestApiConfiguration;
import de.enflexit.awb.ws.dynSiteApi.content.DynamicContentFactory;
import de.enflexit.awb.ws.dynSiteApi.content.ImageHelper;
import de.enflexit.awb.ws.dynSiteApi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.dynSiteApi.gen.ContentApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;
import de.enflexit.awb.ws.dynSiteApi.gen.model.PropertyEntry;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentList;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentListUpdate;
import de.enflexit.awb.ws.dynSiteApi.gen.model.ValueType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * The Class ContentApiServiceImpl.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-04-02T11:34:41.198040400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class ContentApiServiceImpl extends ContentApiService {

	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.dynSiteApi.gen.ContentApiService#contentMenuIDGet(java.lang.Integer, jakarta.ws.rs.core.SecurityContext)
	 */
	@Override
    public Response contentMenuIDGet(Integer menueID, SecurityContext securityContext) throws NotFoundException {
        
    	if (menueID==null) {
            return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Missing menueID!")).build();
    	}
		
    	// --- Create the content according to the menueID -------------------
		SiteContentList scList = this.getSiteContentList(menueID);
		if (scList!=null) {
			return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(scList).build();
		}
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Unknown menueID!")).build();
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
			scList.addContentListItem(DynamicContentFactory.createSiteContentText(1, 0,  false, null, "Example Text"));
			scList.addContentListItem(DynamicContentFactory.createSiteContentText(2, 10, false, null, "Example Text that should be updated every 10 seconds"));
			scList.addContentListItem(DynamicContentFactory.createSiteContentText(3, 0,  true,  null, "Example Text that is to edit"));
			break;
			
		case -2:
			String image1 = ImageHelper.getImageIconBase64Encoded("awb48.png");
			scList.addContentListItem(DynamicContentFactory.createSiteContentImage(4, 0,  false, "image/png", image1, null, null));

			String image2 = ImageHelper.getImageIconBase64Encoded("components.gif");
			scList.addContentListItem(DynamicContentFactory.createSiteContentImage(5, 10, true,  "image/gif", image2, 200, null));
			
			String image3 = ImageHelper.getImageIconBase64Encoded("splash.bmp");
			scList.addContentListItem(DynamicContentFactory.createSiteContentImage(6, 0,  false, "image/bmp", image3, null, 150));

			String image4 = ImageHelper.getImageIconBase64Encoded("Survey_Logo.jpg");
			scList.addContentListItem(DynamicContentFactory.createSiteContentImage(7, 0,  true,  "image/jpg", image4, 100, 100));
			break;

		case -3:
			List<PropertyEntry> peList = new ArrayList<>();
			peList.add(DynamicContentFactory.createPropertyEntry("example.String", ValueType.STRING, "TestValue"));
			peList.add(DynamicContentFactory.createPropertyEntry("example.boolean", ValueType.BOOLEAN, "true"));
			peList.add(DynamicContentFactory.createPropertyEntry("example.integer", ValueType.INTEGER, "1"));
			peList.add(DynamicContentFactory.createPropertyEntry("example.double", ValueType.DOUBLE, "3.1415"));
			peList.add(DynamicContentFactory.createPropertyEntry("example.long", ValueType.LONG, String.valueOf(Long.MAX_VALUE)));
			
			scList.addContentListItem(DynamicContentFactory.createSiteContentProperties(8, 0, false, peList));
			scList.addContentListItem(DynamicContentFactory.createSiteContentProperties(9, 0, true, peList));
			break;
			
		default:
			throw new IllegalArgumentException("Unexpected value: " + menuID);
		}
		return scList;
	}


	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.dynSiteApi.gen.ContentApiService#contentPut(de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentListUpdate, jakarta.ws.rs.core.SecurityContext)
	 */
	@Override
	public Response contentPut(SiteContentListUpdate siteContentListUpdate, SecurityContext securityContext) throws NotFoundException {
		
		
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
	
}
