package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.UserApiService;
import de.enflexit.awb.ws.restapi.gen.impl.UserApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-02T10:38:19.398049500+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class UserApiServiceFactory {
    private static final UserApiService service = new UserApiServiceImpl();

    public static UserApiService getUserApi() {
        return service;
    }
}
