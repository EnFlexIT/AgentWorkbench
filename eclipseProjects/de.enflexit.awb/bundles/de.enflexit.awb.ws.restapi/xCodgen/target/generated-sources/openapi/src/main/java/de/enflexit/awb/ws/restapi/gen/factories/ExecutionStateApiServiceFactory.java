package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.ExecutionStateApiService;
import de.enflexit.awb.ws.restapi.gen.impl.ExecutionStateApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T22:50:45.497388700+02:00[Europe/Berlin]")
public class ExecutionStateApiServiceFactory {
    private static final ExecutionStateApiService service = new ExecutionStateApiServiceImpl();

    public static ExecutionStateApiService getExecutionStateApi() {
        return service;
    }
}
