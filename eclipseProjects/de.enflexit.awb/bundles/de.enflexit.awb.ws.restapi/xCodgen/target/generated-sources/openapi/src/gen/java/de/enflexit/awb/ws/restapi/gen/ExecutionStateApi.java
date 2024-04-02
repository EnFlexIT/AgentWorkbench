package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.ExecutionStateApiService;
import de.enflexit.awb.ws.restapi.gen.factories.ExecutionStateApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.restapi.gen.model.ExecutionState;

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

@Path("/executionState")


@Tag(description = "the executionState API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-04-02T17:38:36.020041800+02:00[Europe/Berlin]", comments = "Generator version: 7.4.0")
public class ExecutionStateApi  {

   private final ExecutionStateApiService delegate;

   public ExecutionStateApi(@Context ServletConfig servletContext) {

      ExecutionStateApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("ExecutionStateApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (ExecutionStateApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = ExecutionStateApiServiceFactory.getExecutionStateApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "Returns the current AWB state", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "AWB-State", content = 
                @Content(schema = @Schema(implementation = ExecutionState.class))),
            },security = {
            @SecurityRequirement(name = "BearerAuth")
        }, tags={ "admins", }) 
    public Response executionStateGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.executionStateGet(securityContext);
    }
}
