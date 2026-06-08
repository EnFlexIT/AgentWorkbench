package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.DoRestartApiService;
import de.enflexit.awb.ws.restapi.gen.impl.DoRestartApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-08T12:08:47.460531100+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class DoRestartApiServiceFactory {
    private static final DoRestartApiService service = new DoRestartApiServiceImpl();

    public static DoRestartApiService getDoRestartApi() {
        return service;
    }
}
