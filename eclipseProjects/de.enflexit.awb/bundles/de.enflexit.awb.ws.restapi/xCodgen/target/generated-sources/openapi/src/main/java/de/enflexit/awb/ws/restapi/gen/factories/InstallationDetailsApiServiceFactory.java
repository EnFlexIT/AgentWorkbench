package de.enflexit.awb.ws.restapi.gen.factories;

import de.enflexit.awb.ws.restapi.gen.InstallationDetailsApiService;
import de.enflexit.awb.ws.restapi.gen.impl.InstallationDetailsApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T16:29:27.241008700+02:00[Europe/Berlin]")
public class InstallationDetailsApiServiceFactory {
    private static final InstallationDetailsApiService service = new InstallationDetailsApiServiceImpl();

    public static InstallationDetailsApiService getInstallationDetailsApi() {
        return service;
    }
}
