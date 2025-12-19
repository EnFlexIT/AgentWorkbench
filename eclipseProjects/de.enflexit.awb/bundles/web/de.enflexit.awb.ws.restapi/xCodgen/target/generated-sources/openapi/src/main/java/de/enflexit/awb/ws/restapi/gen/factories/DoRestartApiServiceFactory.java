package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.DoRestartApiService;
import de.enflexit.awb.ws.restapi.gen.impl.DoRestartApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-12-19T14:09:40.842261300+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class DoRestartApiServiceFactory {
    private static final DoRestartApiService service = new DoRestartApiServiceImpl();

    public static DoRestartApiService getDoRestartApi() {
        return service;
    }
}
