package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.LoadApiService;
import de.enflexit.awb.ws.restapi.gen.impl.LoadApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T22:50:45.497388700+02:00[Europe/Berlin]")
public class LoadApiServiceFactory {
    private static final LoadApiService service = new LoadApiServiceImpl();

    public static LoadApiService getLoadApi() {
        return service;
    }
}
