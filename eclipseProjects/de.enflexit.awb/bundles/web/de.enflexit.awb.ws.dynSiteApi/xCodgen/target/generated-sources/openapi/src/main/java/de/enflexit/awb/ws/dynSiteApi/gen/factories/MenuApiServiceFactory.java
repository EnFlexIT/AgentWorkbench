package de.enflexit.awb.ws.dynSiteApi.gen.factories;

import de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.impl.MenuApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-11-26T14:48:39.860426700+01:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public class MenuApiServiceFactory {
    private static final MenuApiService service = new MenuApiServiceImpl();

    public static MenuApiService getMenuApi() {
        return service;
    }
}
