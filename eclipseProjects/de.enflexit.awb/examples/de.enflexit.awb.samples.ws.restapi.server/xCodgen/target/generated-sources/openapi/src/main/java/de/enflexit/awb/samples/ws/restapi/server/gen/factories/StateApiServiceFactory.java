package de.enflexit.awb.samples.ws.restapi.server.gen.factories;

import de.enflexit.awb.samples.ws.restapi.server.gen.StateApiService;
import de.enflexit.awb.samples.ws.restapi.server.gen.impl.StateApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-04-17T17:09:44.461949400+02:00[Europe/Berlin]")
public class StateApiServiceFactory {
    private static final StateApiService service = new StateApiServiceImpl();

    public static StateApiService getStateApi() {
        return service;
    }
}
