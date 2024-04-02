package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.IsUpdateAvailableApiService;
import de.enflexit.awb.ws.restapi.gen.factories.IsUpdateAvailableApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.Map;
import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

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

@Path("/isUpdateAvailable")


@Tag(description = "the isUpdateAvailable API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-04-02T17:38:36.020041800+02:00[Europe/Berlin]", comments = "Generator version: 7.4.0")
public class IsUpdateAvailableApi  {

   private final IsUpdateAvailableApiService delegate;

   public IsUpdateAvailableApi(@Context ServletConfig servletContext) {

      IsUpdateAvailableApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("IsUpdateAvailableApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (IsUpdateAvailableApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = IsUpdateAvailableApiServiceFactory.getIsUpdateAvailableApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "Checks wether an update for the AWB is available or not", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "request successful. boolean in response indicates wether update is available or not", content = 
                @Content(schema = @Schema(implementation = Boolean.class))),
            },security = {
            @SecurityRequirement(name = "BearerAuth")
        }, tags={ "info", }) 
    public Response isUpdateAvailableGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.isUpdateAvailableGet(securityContext);
    }
}
