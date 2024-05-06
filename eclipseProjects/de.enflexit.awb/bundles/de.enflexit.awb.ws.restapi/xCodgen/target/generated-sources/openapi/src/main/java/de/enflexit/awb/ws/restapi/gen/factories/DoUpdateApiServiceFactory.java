package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.DoUpdateApiService;
import de.enflexit.awb.ws.restapi.gen.impl.DoUpdateApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T16:29:27.241008700+02:00[Europe/Berlin]")
public class DoUpdateApiServiceFactory {
    private static final DoUpdateApiService service = new DoUpdateApiServiceImpl();

    public static DoUpdateApiService getDoUpdateApi() {
        return service;
    }
}
