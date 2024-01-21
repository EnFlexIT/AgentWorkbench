package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.LoginApiService;
import de.enflexit.awb.ws.restapi.gen.impl.LoginApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-01-18T14:16:57.078043800+01:00[Europe/Berlin]")
public class LoginApiServiceFactory {
    private static final LoginApiService service = new LoginApiServiceImpl();

    public static LoginApiService getLoginApi() {
        return service;
    }
}
