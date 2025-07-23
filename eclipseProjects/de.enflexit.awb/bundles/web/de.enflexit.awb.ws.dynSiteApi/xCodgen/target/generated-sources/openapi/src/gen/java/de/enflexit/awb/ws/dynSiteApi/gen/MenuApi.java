package de.enflexit.awb.ws.dynSiteApi.gen;

import de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.factories.MenuApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuList;

import java.util.Map;
import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/menu/")


@Tag(description = "the menu API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-23T11:55:38.634832400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class MenuApi  {

   private final MenuApiService delegate;

   public MenuApi(@Context ServletConfig servletContext) {

      MenuApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("MenuApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (MenuApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = MenuApiServiceFactory.getMenuApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "Enables to build-up the menu structure", description = "", responses = {
            @ApiResponse(responseCode = "202", description = "The menus to be seen by the current user", content = 
                @Content(schema = @Schema(implementation = MenuList.class))),
            }, tags={  }) 
    public Response menuGet(@Schema(description = "the language locale to use") @QueryParam("lang")  String lang,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.menuGet(lang, securityContext);
    }
}
