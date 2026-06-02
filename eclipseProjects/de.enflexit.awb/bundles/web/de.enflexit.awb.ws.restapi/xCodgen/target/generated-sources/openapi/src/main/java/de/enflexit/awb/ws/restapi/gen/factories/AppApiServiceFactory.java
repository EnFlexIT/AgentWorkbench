package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.AppApiService;
import de.enflexit.awb.ws.restapi.gen.impl.AppApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-05-20T10:58:36.851262400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class AppApiServiceFactory {
    private static final AppApiService service = new AppApiServiceImpl();

    public static AppApiService getAppApi() {
        return service;
    }
}
