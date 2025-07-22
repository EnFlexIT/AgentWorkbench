package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.ExecutionStateApiService;
import de.enflexit.awb.ws.restapi.gen.impl.ExecutionStateApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-22T11:28:21.945168200+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class ExecutionStateApiServiceFactory {
    private static final ExecutionStateApiService service = new ExecutionStateApiServiceImpl();

    public static ExecutionStateApiService getExecutionStateApi() {
        return service;
    }
}
