package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.StateApiService;
import de.enflexit.awb.ws.restapi.gen.impl.StateApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2022-04-14T15:57:37.291147100+02:00[Europe/Berlin]")
public class StateApiServiceFactory {
    private static final StateApiService service = new StateApiServiceImpl();

    public static StateApiService getStateApi() {
        return service;
    }
}
