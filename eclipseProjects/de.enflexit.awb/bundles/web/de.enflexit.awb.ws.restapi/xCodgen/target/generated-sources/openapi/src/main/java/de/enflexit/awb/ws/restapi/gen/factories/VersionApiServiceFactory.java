package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.VersionApiService;
import de.enflexit.awb.ws.restapi.gen.impl.VersionApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-03-04T17:07:13.922796800+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class VersionApiServiceFactory {
    private static final VersionApiService service = new VersionApiServiceImpl();

    public static VersionApiService getVersionApi() {
        return service;
    }
}
