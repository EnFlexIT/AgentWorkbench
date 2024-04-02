package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.StateApiService;
import de.enflexit.awb.ws.restapi.gen.factories.StateApiServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import de.enflexit.awb.ws.restapi.gen.model.ExecutionState;
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

@Path("/state")


@Tag(name="State EndPoint", description = "The State API")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-04-01T19:11:06.016775900+02:00[Europe/Berlin]")
public class StateApi  {
   private final StateApiService delegate;

   public StateApi(@Context ServletConfig servletContext) {
      StateApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("StateApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (StateApiService) Class.forName(implClass).newInstance();
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
//    @io.swagger.annotations.ApiOperation(value = "Returns the current AWB state", notes = "Returns the current state of Agent.Workbench consisiting information  about the execution mode, the currently open project and other. ", response = ExecutionState.class, authorizations = {
//        @io.swagger.annotations.Authorization(value = "AwbApiKey")
//    }, tags={ "admins", })
//    @io.swagger.annotations.ApiResponses(value = {
//        @io.swagger.annotations.ApiResponse(code = 200, message = "AWB-State", response = ExecutionState.class)
//    })
    @Operation(summary = "Returns the current AWB state", description = "Returns the current state of Agent.Workbench consisiting information  about the execution mode, the currently open project and other.", 
   	responses = {@ApiResponse(responseCode = "OK", description = "OK_MSG", 
   					content = @Content(schema = @Schema(implementation = ExecutionState.class))),
   					@ApiResponse(responseCode = "NOT_FOUND", description = "NOT_FOUND_MSG")})
    public Response stateGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.stateGet(securityContext);
    }
}