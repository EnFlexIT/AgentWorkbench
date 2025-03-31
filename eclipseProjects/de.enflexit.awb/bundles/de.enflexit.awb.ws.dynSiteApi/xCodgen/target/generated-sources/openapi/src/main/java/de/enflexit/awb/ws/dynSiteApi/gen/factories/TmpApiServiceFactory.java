package de.enflexit.awb.ws.dynSiteApi.gen.factories;

import de.enflexit.awb.ws.dynSiteApi.gen.TmpApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.impl.TmpApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-03-31T09:56:23.457253900+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class TmpApiServiceFactory {
    private static final TmpApiService service = new TmpApiServiceImpl();

    public static TmpApiService getTmpApi() {
        return service;
    }
}
