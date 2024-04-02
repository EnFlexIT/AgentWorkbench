package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.EventLogApiService;
import de.enflexit.awb.ws.restapi.gen.impl.EventLogApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-04-02T17:38:36.020041800+02:00[Europe/Berlin]", comments = "Generator version: 7.4.0")
public class EventLogApiServiceFactory {
    private static final EventLogApiService service = new EventLogApiServiceImpl();

    public static EventLogApiService getEventLogApi() {
        return service;
    }
}
