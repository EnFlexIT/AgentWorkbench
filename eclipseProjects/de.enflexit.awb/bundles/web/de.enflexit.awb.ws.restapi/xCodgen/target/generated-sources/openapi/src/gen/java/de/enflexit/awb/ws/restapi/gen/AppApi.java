package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.factories.AppApiServiceFactory;
import de.enflexit.awb.ws.restapi.gen.model.Properties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/app/settings")


@Tag(description = "the app API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-12-19T14:09:40.842261300+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class AppApi  {

   private final AppApiService delegate;

   public AppApi(@Context ServletConfig servletContext) {

      AppApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("AppApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (AppApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = AppApiServiceFactory.getAppApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Path("/get")
    @Produces({ "application/json" })
    @Operation(summary = "Returns required base configuration settings for the curren web application", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "Current application settings", content = 
                @Content(schema = @Schema(implementation = Properties.class))),
            },security = {
            @SecurityRequirement(name = "bearerAuth")
        }, tags={ "info", }) 
    public Response getAppSettings(@Context HttpServletRequest request,@Schema(description = "" )@HeaderParam("X-Performative") String xPerformative,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getAppSettings(xPerformative, request, securityContext);
    }

    @jakarta.ws.rs.PUT
    @Path("/set")
    @Consumes({ "application/json" })
    @Operation(summary = "Enables to update or set the required base configuration settings for the curren web application", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully changed settings", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            },security = {
            @SecurityRequirement(name = "bearerAuth")
        }, tags={ "info", }) 
    public Response setAppSettings(@Schema(description = "The new settings") @Valid  Properties properties,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.setAppSettings(properties, securityContext);
    }
}
