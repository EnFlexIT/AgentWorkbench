package de.enflexit.awb.ws.dynSiteApi.gen.factories;

import de.enflexit.awb.ws.dynSiteApi.gen.GetContentApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.impl.GetContentApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-17T17:38:27.629521900+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class GetContentApiServiceFactory {
    private static final GetContentApiService service = new GetContentApiServiceImpl();

    public static GetContentApiService getGetContentApi() {
        return service;
    }
}
