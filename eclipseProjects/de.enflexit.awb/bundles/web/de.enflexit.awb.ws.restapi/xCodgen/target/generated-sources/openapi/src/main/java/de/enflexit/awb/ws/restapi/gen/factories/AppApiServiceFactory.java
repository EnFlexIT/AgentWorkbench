package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.AppApiService;
import de.enflexit.awb.ws.restapi.gen.impl.AppApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-03-25T12:33:25.416793100+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class AppApiServiceFactory {
    private static final AppApiService service = new AppApiServiceImpl();

    public static AppApiService getAppApi() {
        return service;
    }
}
