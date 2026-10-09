package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.LogsApiService;
import de.enflexit.awb.ws.restapi.gen.impl.LogsApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-10-05T09:18:49.152285200+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class LogsApiServiceFactory {
    private static final LogsApiService service = new LogsApiServiceImpl();

    public static LogsApiService getLogsApi() {
        return service;
    }
}
