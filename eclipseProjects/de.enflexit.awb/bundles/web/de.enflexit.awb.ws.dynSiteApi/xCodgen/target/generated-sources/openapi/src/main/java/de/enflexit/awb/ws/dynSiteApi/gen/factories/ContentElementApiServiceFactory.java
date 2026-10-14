package de.enflexit.awb.ws.dynSiteApi.gen.factories;

import de.enflexit.awb.ws.dynSiteApi.gen.ContentElementApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.impl.ContentElementApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-10-13T16:03:03.677731300+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public class ContentElementApiServiceFactory {
    private static final ContentElementApiService service = new ContentElementApiServiceImpl();

    public static ContentElementApiService getContentElementApi() {
        return service;
    }
}
