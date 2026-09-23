package de.enflexit.awb.ws.dynSiteApi.gen.factories;

import de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService;
import de.enflexit.awb.ws.dynSiteApi.impl.MenuApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-23T10:17:15.337896100+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public class MenuApiServiceFactory {
    private static final MenuApiService service = new MenuApiServiceImpl();

    public static MenuApiService getMenuApi() {
        return service;
    }
}
