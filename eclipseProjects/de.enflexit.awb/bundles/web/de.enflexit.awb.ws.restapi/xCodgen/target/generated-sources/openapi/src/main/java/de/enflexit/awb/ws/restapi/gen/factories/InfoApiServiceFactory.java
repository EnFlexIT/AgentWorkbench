package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.InfoApiService;
import de.enflexit.awb.ws.restapi.gen.impl.InfoApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-02T10:38:19.398049500+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class InfoApiServiceFactory {
    private static final InfoApiService service = new InfoApiServiceImpl();

    public static InfoApiService getInfoApi() {
        return service;
    }
}
