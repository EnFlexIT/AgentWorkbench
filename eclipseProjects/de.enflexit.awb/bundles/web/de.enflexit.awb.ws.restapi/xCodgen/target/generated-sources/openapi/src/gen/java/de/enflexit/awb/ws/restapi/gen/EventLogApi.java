package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.EventLogApiService;
import de.enflexit.awb.ws.restapi.gen.factories.EventLogApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.restapi.gen.model.Event;
import de.enflexit.awb.ws.restapi.gen.model.EventLogTypes;

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

@Path("/eventLog")


@Tag(description = "the eventLog API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-04-29T11:33:26.991574300+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class EventLogApi  {

   private final EventLogApiService delegate;

   public EventLogApi(@Context ServletConfig servletContext) {

      EventLogApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("EventLogApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (EventLogApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = EventLogApiServiceFactory.getEventLogApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "get logs of specific type", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "Return Logs of specified type", content = 
                @Content(schema = @Schema(implementation = Event.class))),
            },security = {
            @SecurityRequirement(name = "bearerAuth")
        }, tags={ "info", }) 
    public Response eventLogGet(@Schema(description = "The type which is retrieved by awb", allowableValues="AWB, Project, Setup, Agent") @QueryParam("type") @NotNull  EventLogTypes type,@Schema(description = "the maximum number of logs to retrieve. If no number is specified the default is 10", defaultValue = "10") @DefaultValue("10") @QueryParam("amount")  @Min(1) @Max(100) Integer amount,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.eventLogGet(type, amount, securityContext);
    }
}
