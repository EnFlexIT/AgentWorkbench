package de.enflexit.awb.ws.dynSiteApi.gen.factories;

import de.enflexit.awb.ws.dynSiteApi.gen.ContentApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.impl.ContentApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-18T09:11:14.660026+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class ContentApiServiceFactory {
    private static final ContentApiService service = new ContentApiServiceImpl();

    public static ContentApiService getContentApi() {
        return service;
    }
}
