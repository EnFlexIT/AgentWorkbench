package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.*;
import de.enflexit.awb.ws.restapi.gen.StateApiService;
import de.enflexit.awb.ws.restapi.gen.factories.StateApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.enflexit.awb.ws.restapi.gen.model.ExecutionState;

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

@Path("/state")


@io.swagger.annotations.Api(description = "the state API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2022-04-19T14:42:04.744880100+02:00[Europe/Berlin]")
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

    @GET
    
    
    @Produces({ "applicaion/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns the current AWB state", notes = "Returns the current state of Agent.Workbench consisiting information  about the execution mode, the currently open project and other. ", response = ExecutionState.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "AwbApiKey")
    }, tags={ "admins", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "AWB-State", response = ExecutionState.class)
    })
    public Response stateGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.stateGet(securityContext);
    }
}
