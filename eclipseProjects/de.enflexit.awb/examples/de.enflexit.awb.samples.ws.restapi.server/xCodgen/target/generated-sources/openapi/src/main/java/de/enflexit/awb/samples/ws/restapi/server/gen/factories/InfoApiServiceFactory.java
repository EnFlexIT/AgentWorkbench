package de.enflexit.awb.samples.ws.restapi.server.gen.factories;

import de.enflexit.awb.samples.ws.restapi.server.gen.InfoApiService;
import de.enflexit.awb.samples.ws.restapi.server.gen.impl.InfoApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-11T13:57:08.002830400+02:00[Europe/Berlin]", comments = "Generator version: 7.23.0")
public class InfoApiServiceFactory {
    private static final InfoApiService service = new InfoApiServiceImpl();

    public static InfoApiService getInfoApi() {
        return service;
    }
}
