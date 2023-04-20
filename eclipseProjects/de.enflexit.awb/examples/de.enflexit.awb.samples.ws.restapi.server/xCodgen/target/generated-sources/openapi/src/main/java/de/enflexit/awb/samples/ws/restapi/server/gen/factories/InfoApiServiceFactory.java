package de.enflexit.awb.samples.ws.restapi.server.gen.factories;

import de.enflexit.awb.samples.ws.restapi.server.gen.InfoApiService;
import de.enflexit.awb.samples.ws.restapi.server.gen.impl.InfoApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-04-17T17:09:44.461949400+02:00[Europe/Berlin]")
public class InfoApiServiceFactory {
    private static final InfoApiService service = new InfoApiServiceImpl();

    public static InfoApiService getInfoApi() {
        return service;
    }
}
