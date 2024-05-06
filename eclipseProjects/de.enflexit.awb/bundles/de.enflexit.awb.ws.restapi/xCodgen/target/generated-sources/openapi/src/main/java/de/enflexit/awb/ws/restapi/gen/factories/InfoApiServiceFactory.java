package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.InfoApiService;
import de.enflexit.awb.ws.restapi.gen.impl.InfoApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T23:49:08.451118100+02:00[Europe/Berlin]")
public class InfoApiServiceFactory {
    private static final InfoApiService service = new InfoApiServiceImpl();

    public static InfoApiService getInfoApi() {
        return service;
    }
}
