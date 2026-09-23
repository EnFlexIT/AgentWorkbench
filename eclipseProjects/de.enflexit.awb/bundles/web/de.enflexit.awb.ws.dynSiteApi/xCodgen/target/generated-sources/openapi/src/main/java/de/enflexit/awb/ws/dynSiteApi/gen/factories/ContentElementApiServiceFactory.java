package de.enflexit.awb.ws.dynSiteApi.gen.factories;

import de.enflexit.awb.ws.dynSiteApi.gen.ContentElementApiService;
import de.enflexit.awb.ws.dynSiteApi.impl.ContentElementApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-23T10:17:15.337896100+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public class ContentElementApiServiceFactory {
    private static final ContentElementApiService service = new ContentElementApiServiceImpl();

    public static ContentElementApiService getContentElementApi() {
        return service;
    }
}
