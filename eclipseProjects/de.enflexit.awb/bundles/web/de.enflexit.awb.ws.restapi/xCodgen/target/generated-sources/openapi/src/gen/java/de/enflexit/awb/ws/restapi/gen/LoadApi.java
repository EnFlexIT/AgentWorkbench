package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.LoadApiService;
import de.enflexit.awb.ws.restapi.gen.factories.LoadApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.restapi.gen.model.SystemLoad;

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

@Path("/load")


@Tag(description = "the load API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-22T11:28:21.945168200+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class LoadApi  {

   private final LoadApiService delegate;

   public LoadApi(@Context ServletConfig servletContext) {

      LoadApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("LoadApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (LoadApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = LoadApiServiceFactory.getLoadApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "Returns the current System load", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "System Load", content = 
                @Content(schema = @Schema(implementation = SystemLoad.class))),
            },security = {
            @SecurityRequirement(name = "bearerAuth")
        }, tags={ "admins", }) 
    public Response loadGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.loadGet(securityContext);
    }
}
