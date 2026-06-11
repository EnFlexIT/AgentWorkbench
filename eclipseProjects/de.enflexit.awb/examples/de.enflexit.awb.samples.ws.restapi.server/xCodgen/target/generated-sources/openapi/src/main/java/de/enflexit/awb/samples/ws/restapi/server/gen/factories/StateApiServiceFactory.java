package de.enflexit.awb.samples.ws.restapi.server.gen.factories;

import de.enflexit.awb.samples.ws.restapi.server.gen.StateApiService;
import de.enflexit.awb.samples.ws.restapi.server.gen.impl.StateApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-11T13:57:08.002830400+02:00[Europe/Berlin]", comments = "Generator version: 7.23.0")
public class StateApiServiceFactory {
    private static final StateApiService service = new StateApiServiceImpl();

    public static StateApiService getStateApi() {
        return service;
    }
}
