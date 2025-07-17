package de.enflexit.awb.ws.dynSiteApi.impl;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.awb.ws.dynSiteApi.RestApiConfiguration;
import de.enflexit.awb.ws.dynSiteApi.content.DynamicContentFactory;
import de.enflexit.awb.ws.dynSiteApi.content.ImageHelper;
import de.enflexit.awb.ws.dynSiteApi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.dynSiteApi.gen.GetContentApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;
import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractValuePair;
import de.enflexit.awb.ws.dynSiteApi.gen.model.DataSeries;
import de.enflexit.awb.ws.dynSiteApi.gen.model.PropertyEntry;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentList;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentPieChart;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTimeSeriesChart;
import de.enflexit.awb.ws.dynSiteApi.gen.model.ValueType;
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
			
		case -4:
			scList.addContentListItem(this.createExamplePieChart());
			scList.addContentListItem(this.createExampleTimeSeriesChart());
			break;
			
			
		default:
			throw new IllegalArgumentException("Unexpected value: " + menuID);
		}
		return scList;
	}
	
	/**
	 * Creates an example pie chart.
	 * @return the site content pie chart
	 */
	private SiteContentPieChart createExamplePieChart() {
		List<AbstractValuePair> entries = new ArrayList<AbstractValuePair>();
		entries.add(DynamicContentFactory.createValuePairCategory("Erdgas", 78.4));
		entries.add(DynamicContentFactory.createValuePairCategory("Steinkohle", 27.2));
		entries.add(DynamicContentFactory.createValuePairCategory("Braunkohle", 79.2));
		entries.add(DynamicContentFactory.createValuePairCategory("Erneuerbare", 284.0));
		entries.add(DynamicContentFactory.createValuePairCategory("Sonstige", 79.2));
		
		List<DataSeries> series = DynamicContentFactory.createDataSeriesList("Strommix Detschland 2024 (Mrd. kWh)", entries);
		
		SiteContentPieChart pieChart = DynamicContentFactory.createSiteContentPieChart(1, 0, false, series, null, false);
		
		return pieChart;
	}

	private SiteContentTimeSeriesChart createExampleTimeSeriesChart() {
		List<AbstractValuePair> entries = new ArrayList<AbstractValuePair>();
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 00:00", "dd.MM.yyyy HH:mm", 492.8666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 00:15", "dd.MM.yyyy HH:mm", 600.9333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 00:30", "dd.MM.yyyy HH:mm", 659.2666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 00:45", "dd.MM.yyyy HH:mm", 534.4666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 01:00", "dd.MM.yyyy HH:mm", 476.6));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 01:15", "dd.MM.yyyy HH:mm", 244.5333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 01:30", "dd.MM.yyyy HH:mm", 199.7333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 01:45", "dd.MM.yyyy HH:mm", 239.9333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 02:00", "dd.MM.yyyy HH:mm", 232.8));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 02:15", "dd.MM.yyyy HH:mm", 208.7333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 02:30", "dd.MM.yyyy HH:mm", 221.6666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 02:45", "dd.MM.yyyy HH:mm", 194.3333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 03:00", "dd.MM.yyyy HH:mm", 155.0666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 03:15", "dd.MM.yyyy HH:mm", 155.7333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 03:30", "dd.MM.yyyy HH:mm", 132.8));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 03:45", "dd.MM.yyyy HH:mm", 126));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 04:00", "dd.MM.yyyy HH:mm", 116.1333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 04:15", "dd.MM.yyyy HH:mm", 174.7333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 04:30", "dd.MM.yyyy HH:mm", 174.2));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 04:45", "dd.MM.yyyy HH:mm", 265.5333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 05:00", "dd.MM.yyyy HH:mm", 187.1333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 05:15", "dd.MM.yyyy HH:mm", 228.8));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 05:30", "dd.MM.yyyy HH:mm", 285.0666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 05:45", "dd.MM.yyyy HH:mm", 238.4));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 06:00", "dd.MM.yyyy HH:mm", 191.3333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 06:15", "dd.MM.yyyy HH:mm", 222.2));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 06:30", "dd.MM.yyyy HH:mm", 189.0666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 06:45", "dd.MM.yyyy HH:mm", 137));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 07:00", "dd.MM.yyyy HH:mm", 167.4));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 07:15", "dd.MM.yyyy HH:mm", 129.9333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 07:30", "dd.MM.yyyy HH:mm", 136.2));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 07:45", "dd.MM.yyyy HH:mm", 179.2));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 08:00", "dd.MM.yyyy HH:mm", 205.8));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 08:15", "dd.MM.yyyy HH:mm", 187.2666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 08:30", "dd.MM.yyyy HH:mm", 216.2666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 08:45", "dd.MM.yyyy HH:mm", 299.6));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 09:00", "dd.MM.yyyy HH:mm", 304.8));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 09:15", "dd.MM.yyyy HH:mm", 378.0666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 09:30", "dd.MM.yyyy HH:mm", 473.4666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 09:45", "dd.MM.yyyy HH:mm", 562.7333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 10:00", "dd.MM.yyyy HH:mm", 378.4));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 10:15", "dd.MM.yyyy HH:mm", 914.8666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 10:30", "dd.MM.yyyy HH:mm", 402));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 10:45", "dd.MM.yyyy HH:mm", 757.7333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 11:00", "dd.MM.yyyy HH:mm", 804.1333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 11:15", "dd.MM.yyyy HH:mm", 835.3333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 11:30", "dd.MM.yyyy HH:mm", 475.6));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 11:45", "dd.MM.yyyy HH:mm", 439.6666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 12:00", "dd.MM.yyyy HH:mm", 477.9333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 12:15", "dd.MM.yyyy HH:mm", 522.8666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 12:30", "dd.MM.yyyy HH:mm", 1016.533333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 12:45", "dd.MM.yyyy HH:mm", 1662.866667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 13:00", "dd.MM.yyyy HH:mm", 1093.733333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 13:15", "dd.MM.yyyy HH:mm", 1566.533333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 13:30", "dd.MM.yyyy HH:mm", 1226));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 13:45", "dd.MM.yyyy HH:mm", 270.7333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 14:00", "dd.MM.yyyy HH:mm", 180.8666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 14:15", "dd.MM.yyyy HH:mm", 201.8666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 14:30", "dd.MM.yyyy HH:mm", 181.6666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 14:45", "dd.MM.yyyy HH:mm", 176.3333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 15:00", "dd.MM.yyyy HH:mm", 296.7333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 15:15", "dd.MM.yyyy HH:mm", 425.4));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 15:30", "dd.MM.yyyy HH:mm", 666.6));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 15:45", "dd.MM.yyyy HH:mm", 665.6));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 16:00", "dd.MM.yyyy HH:mm", 840.6));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 16:15", "dd.MM.yyyy HH:mm", 464.9333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 16:30", "dd.MM.yyyy HH:mm", 689.6));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 16:45", "dd.MM.yyyy HH:mm", 727.4));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 17:00", "dd.MM.yyyy HH:mm", 594.5333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 17:15", "dd.MM.yyyy HH:mm", 399.6666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 17:30", "dd.MM.yyyy HH:mm", 181.3333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 17:45", "dd.MM.yyyy HH:mm", 193.0666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 18:00", "dd.MM.yyyy HH:mm", 199.6666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 18:15", "dd.MM.yyyy HH:mm", 206));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 18:30", "dd.MM.yyyy HH:mm", 277.3333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 18:45", "dd.MM.yyyy HH:mm", 405.0666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 19:00", "dd.MM.yyyy HH:mm", 400.6));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 19:15", "dd.MM.yyyy HH:mm", 283.9333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 19:30", "dd.MM.yyyy HH:mm", 461.8));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 19:45", "dd.MM.yyyy HH:mm", 1332.333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 20:00", "dd.MM.yyyy HH:mm", 2236.066667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 20:15", "dd.MM.yyyy HH:mm", 1490.533333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 20:30", "dd.MM.yyyy HH:mm", 1748.466667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 20:45", "dd.MM.yyyy HH:mm", 2051.733333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 21:00", "dd.MM.yyyy HH:mm", 919.7333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 21:15", "dd.MM.yyyy HH:mm", 1189));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 21:30", "dd.MM.yyyy HH:mm", 1180.733333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 21:45", "dd.MM.yyyy HH:mm", 1321.666667));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 22:00", "dd.MM.yyyy HH:mm", 1301));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 22:15", "dd.MM.yyyy HH:mm", 1537.2));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 22:30", "dd.MM.yyyy HH:mm", 1376.933333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 22:45", "dd.MM.yyyy HH:mm", 1153.6));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 23:00", "dd.MM.yyyy HH:mm", 925.1333333));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 23:15", "dd.MM.yyyy HH:mm", 677));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 23:30", "dd.MM.yyyy HH:mm", 784));
		entries.add(DynamicContentFactory.createValuePairDateTime("18.07.2025 23:45", "dd.MM.yyyy HH:mm", 480.9333333));
		
		
		
		SiteContentTimeSeriesChart tsChart = DynamicContentFactory.createSiteContentTimeSeriesChart(2, 0, false, DynamicContentFactory.createDataSeriesList("Power Consumption(W)", entries), "Power Consumption", false, "Time", "P(W)", null, "HH:mm");
		return tsChart;
	}
	
	
}
