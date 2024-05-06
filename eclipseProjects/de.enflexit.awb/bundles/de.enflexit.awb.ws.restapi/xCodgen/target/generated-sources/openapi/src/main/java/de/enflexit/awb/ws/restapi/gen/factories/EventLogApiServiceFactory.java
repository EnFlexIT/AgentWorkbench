package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.EventLogApiService;
import de.enflexit.awb.ws.restapi.gen.impl.EventLogApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T23:49:08.451118100+02:00[Europe/Berlin]")
public class EventLogApiServiceFactory {
    private static final EventLogApiService service = new EventLogApiServiceImpl();

    public static EventLogApiService getEventLogApi() {
        return service;
    }
}
