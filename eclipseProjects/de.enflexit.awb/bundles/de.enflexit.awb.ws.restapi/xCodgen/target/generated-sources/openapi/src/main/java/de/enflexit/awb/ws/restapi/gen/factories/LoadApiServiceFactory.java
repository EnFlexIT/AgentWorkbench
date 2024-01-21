package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.LoadApiService;
import de.enflexit.awb.ws.restapi.gen.impl.LoadApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-01-18T14:16:57.078043800+01:00[Europe/Berlin]")
public class LoadApiServiceFactory {
    private static final LoadApiService service = new LoadApiServiceImpl();

    public static LoadApiService getLoadApi() {
        return service;
    }
}
