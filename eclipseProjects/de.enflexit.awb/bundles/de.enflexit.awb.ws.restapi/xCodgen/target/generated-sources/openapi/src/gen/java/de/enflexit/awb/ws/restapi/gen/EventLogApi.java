package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.*;
import de.enflexit.awb.ws.restapi.gen.EventLogApiService;
import de.enflexit.awb.ws.restapi.gen.factories.EventLogApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.enflexit.awb.ws.restapi.gen.model.Event;
import de.enflexit.awb.ws.restapi.gen.model.EventLogTypes;

import java.util.Map;
import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/eventLog")


@io.swagger.annotations.Api(description = "the eventLog API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T16:29:27.241008700+02:00[Europe/Berlin]")
public class EventLogApi  {
   private final EventLogApiService delegate;

   public EventLogApi(@Context ServletConfig servletContext) {
      EventLogApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("EventLogApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (EventLogApiService) Class.forName(implClass).newInstance();
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

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "get logs of specific type", notes = "", response = Event.class, responseContainer = "List", authorizations = {
        @io.swagger.annotations.Authorization(value = "bearerAuth")
    }, tags={ "info", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Return Logs of specified type", response = Event.class, responseContainer = "List")
    })
    public Response eventLogGet(@ApiParam(value = "The type which is retrieved by awb", required = true, allowableValues="AWB, Project, Setup, Agent") @QueryParam("type") @NotNull @Valid  EventLogTypes type,@ApiParam(value = "the maximum number of logs to retrieve. If no number is specified the default is 10", defaultValue = "10") @DefaultValue("10") @QueryParam("amount")  @Min(1) @Max(100) Integer amount,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.eventLogGet(type, amount, securityContext);
    }
}
