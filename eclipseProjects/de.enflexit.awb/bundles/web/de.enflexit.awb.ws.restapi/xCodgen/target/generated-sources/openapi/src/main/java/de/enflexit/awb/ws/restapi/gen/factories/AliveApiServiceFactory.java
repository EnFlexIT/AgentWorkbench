package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.AliveApiService;
import de.enflexit.awb.ws.restapi.gen.impl.AliveApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-02-20T16:32:18.722136900+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class AliveApiServiceFactory {
    private static final AliveApiService service = new AliveApiServiceImpl();

    public static AliveApiService getAliveApi() {
        return service;
    }
}
