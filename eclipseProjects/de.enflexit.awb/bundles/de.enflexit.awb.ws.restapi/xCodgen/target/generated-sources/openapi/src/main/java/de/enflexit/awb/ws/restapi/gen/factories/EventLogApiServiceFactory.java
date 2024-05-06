package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.EventLogApiService;
import de.enflexit.awb.ws.restapi.gen.impl.EventLogApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T16:29:27.241008700+02:00[Europe/Berlin]")
public class EventLogApiServiceFactory {
    private static final EventLogApiService service = new EventLogApiServiceImpl();

    public static EventLogApiService getEventLogApi() {
        return service;
    }
}
