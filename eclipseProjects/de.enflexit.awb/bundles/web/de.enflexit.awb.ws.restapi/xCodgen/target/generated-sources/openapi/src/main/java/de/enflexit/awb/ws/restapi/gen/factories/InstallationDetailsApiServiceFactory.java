package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.InstallationDetailsApiService;
import de.enflexit.awb.ws.restapi.gen.impl.InstallationDetailsApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-07-01T09:05:28.373135400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class InstallationDetailsApiServiceFactory {
    private static final InstallationDetailsApiService service = new InstallationDetailsApiServiceImpl();

    public static InstallationDetailsApiService getInstallationDetailsApi() {
        return service;
    }
}
