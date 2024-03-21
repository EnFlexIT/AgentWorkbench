package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.InfoApiService;
import de.enflexit.awb.ws.restapi.gen.impl.InfoApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-21T17:19:35.482673500+01:00[Europe/Berlin]")
public class InfoApiServiceFactory {
    private static final InfoApiService service = new InfoApiServiceImpl();

    public static InfoApiService getInfoApi() {
        return service;
    }
}
