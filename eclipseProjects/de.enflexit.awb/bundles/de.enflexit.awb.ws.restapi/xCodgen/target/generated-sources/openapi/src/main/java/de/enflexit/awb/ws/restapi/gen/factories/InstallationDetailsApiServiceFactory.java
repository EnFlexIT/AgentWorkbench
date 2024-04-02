package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.InstallationDetailsApiService;
import de.enflexit.awb.ws.restapi.gen.impl.InstallationDetailsApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-21T17:19:35.482673500+01:00[Europe/Berlin]")
public class InstallationDetailsApiServiceFactory {
    private static final InstallationDetailsApiService service = new InstallationDetailsApiServiceImpl();

    public static InstallationDetailsApiService getInstallationDetailsApi() {
        return service;
    }
}
