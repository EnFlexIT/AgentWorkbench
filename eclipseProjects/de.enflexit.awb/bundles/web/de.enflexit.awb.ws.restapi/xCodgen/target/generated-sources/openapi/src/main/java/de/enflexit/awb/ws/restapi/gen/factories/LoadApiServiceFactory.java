package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.LoadApiService;
import de.enflexit.awb.ws.restapi.gen.impl.LoadApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-08T12:08:47.460531100+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class LoadApiServiceFactory {
    private static final LoadApiService service = new LoadApiServiceImpl();

    public static LoadApiService getLoadApi() {
        return service;
    }
}
