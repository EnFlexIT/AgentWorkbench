package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.AliveApiService;
import de.enflexit.awb.ws.restapi.gen.factories.AliveApiServiceFactory;

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

@Path("/alive")


@Tag(description = "the alive API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-02-20T16:32:18.722136900+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class AliveApi  {

   private final AliveApiService delegate;

   public AliveApi(@Context ServletConfig servletContext) {

      AliveApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("AliveApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (AliveApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = AliveApiServiceFactory.getAliveApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "server system is allive", content = 
                @Content(schema = @Schema(implementation = String.class))),
            },security = {
            @SecurityRequirement(name = "bearerAuth")
        }, tags={ "info", }) 
    public Response aliveGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.aliveGet(securityContext);
    }
}
