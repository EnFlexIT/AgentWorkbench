package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.LoadApiService;
import de.enflexit.awb.ws.restapi.gen.impl.LoadApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-04-29T11:33:26.991574300+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class LoadApiServiceFactory {
    private static final LoadApiService service = new LoadApiServiceImpl();

    public static LoadApiService getLoadApi() {
        return service;
    }
}
