package de.enflexit.awb.ws.dynSiteApi.gen.factories;

import de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.impl.MenuApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-23T11:55:38.634832400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class MenuApiServiceFactory {
    private static final MenuApiService service = new MenuApiServiceImpl();

    public static MenuApiService getMenuApi() {
        return service;
    }
}
