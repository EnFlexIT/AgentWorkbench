package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.DoRestartApiService;
import de.enflexit.awb.ws.restapi.gen.impl.DoRestartApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T23:49:08.451118100+02:00[Europe/Berlin]")
public class DoRestartApiServiceFactory {
    private static final DoRestartApiService service = new DoRestartApiServiceImpl();

    public static DoRestartApiService getDoRestartApi() {
        return service;
    }
}
