package de.enflexit.awb.samples.ws.restapi.server.gen;

import de.enflexit.awb.samples.ws.restapi.server.gen.StateApiService;
import de.enflexit.awb.samples.ws.restapi.server.gen.factories.StateApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.samples.ws.restapi.server.gen.model.ExecutionState;

import java.util.Map;
import java.util.List;
import de.enflexit.awb.samples.ws.restapi.server.gen.NotFoundException;

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

@Path("/state")


@Tag(description = "the state API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-11-25T18:29:52.472926+01:00[Europe/Berlin]", comments = "Generator version: 7.17.0")
public class StateApi  {

   private final StateApiService delegate;

   public StateApi(@Context ServletConfig servletContext) {

      StateApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("StateApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (StateApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = StateApiServiceFactory.getStateApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Produces({ "applicaion/json" })
    @Operation(summary = "Returns the current AWB state", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "AWB-State", content = 
                @Content(schema = @Schema(implementation = ExecutionState.class))),
            },security = {
            @SecurityRequirement(name = "AwbApiKey")
        }, tags={ "admins", }) 
    public Response stateGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.stateGet(securityContext);
    }
}
