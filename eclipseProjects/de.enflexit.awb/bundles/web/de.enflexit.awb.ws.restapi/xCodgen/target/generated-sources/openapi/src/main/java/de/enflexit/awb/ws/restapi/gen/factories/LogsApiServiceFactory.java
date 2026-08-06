package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.LogsApiService;
import de.enflexit.awb.ws.restapi.impl.LogsApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-08-06T09:07:31.407970700+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class LogsApiServiceFactory {
    private static final LogsApiService service = new LogsApiServiceImpl();

    public static LogsApiService getLogsApi() {
        return service;
    }
}
