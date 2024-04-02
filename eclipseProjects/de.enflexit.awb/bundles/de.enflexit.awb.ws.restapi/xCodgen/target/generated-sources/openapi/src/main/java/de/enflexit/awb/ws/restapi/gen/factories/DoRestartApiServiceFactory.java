package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.DoRestartApiService;
import de.enflexit.awb.ws.restapi.gen.impl.DoRestartApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-21T17:19:35.482673500+01:00[Europe/Berlin]")
public class DoRestartApiServiceFactory {
    private static final DoRestartApiService service = new DoRestartApiServiceImpl();

    public static DoRestartApiService getDoRestartApi() {
        return service;
    }
}
