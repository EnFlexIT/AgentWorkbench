package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.ExecutionStateApiService;
import de.enflexit.awb.ws.restapi.gen.impl.ExecutionStateApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-04-02T17:38:36.020041800+02:00[Europe/Berlin]", comments = "Generator version: 7.4.0")
public class ExecutionStateApiServiceFactory {
    private static final ExecutionStateApiService service = new ExecutionStateApiServiceImpl();

    public static ExecutionStateApiService getExecutionStateApi() {
        return service;
    }
}
