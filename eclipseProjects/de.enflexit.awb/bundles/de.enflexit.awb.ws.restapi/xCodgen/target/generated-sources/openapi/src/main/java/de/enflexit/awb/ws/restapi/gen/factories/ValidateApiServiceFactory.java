package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.ValidateApiService;
import de.enflexit.awb.ws.restapi.gen.impl.ValidateApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-01-18T14:16:57.078043800+01:00[Europe/Berlin]")
public class ValidateApiServiceFactory {
    private static final ValidateApiService service = new ValidateApiServiceImpl();

    public static ValidateApiService getValidateApi() {
        return service;
    }
}
