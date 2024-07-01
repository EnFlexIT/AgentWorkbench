package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.DoRestartApiService;
import de.enflexit.awb.ws.restapi.gen.factories.DoRestartApiServiceFactory;

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

@Path("/doRestart")


@Tag(description = "the doRestart API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-07-01T09:05:28.373135400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class DoRestartApi  {

   private final DoRestartApiService delegate;

   public DoRestartApi(@Context ServletConfig servletContext) {

      DoRestartApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("DoRestartApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (DoRestartApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = DoRestartApiServiceFactory.getDoRestartApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.POST
    @Operation(summary = "", description = "", responses = {
            @ApiResponse(responseCode = "423", description = "Restart could not be initiated", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "200", description = "Restart will happen. History can be accessed via eventLog", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            },security = {
            @SecurityRequirement(name = "bearerAuth")
        }, tags={ "doAction", }) 
    public Response doRestartPost(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.doRestartPost(securityContext);
    }
}
