package de.enflexit.awb.ws.dynSiteApi.gen.factories;

import de.enflexit.awb.ws.dynSiteApi.gen.ContentApiService;
import de.enflexit.awb.ws.dynSiteApi.impl.ContentApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-23T10:17:15.337896100+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public class ContentApiServiceFactory {
    private static final ContentApiService service = new ContentApiServiceImpl();

    public static ContentApiService getContentApi() {
        return service;
    }
}
