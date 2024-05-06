package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.*;
import de.enflexit.awb.ws.restapi.gen.DoUpdateApiService;
import de.enflexit.awb.ws.restapi.gen.factories.DoUpdateApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;


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

@Path("/doUpdate")


@io.swagger.annotations.Api(description = "the doUpdate API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T22:50:45.497388700+02:00[Europe/Berlin]")
public class DoUpdateApi  {
   private final DoUpdateApiService delegate;

   public DoUpdateApi(@Context ServletConfig servletContext) {
      DoUpdateApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("DoUpdateApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (DoUpdateApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = DoUpdateApiServiceFactory.getDoUpdateApi();
      }

      this.delegate = delegate;
   }

    @POST
    
    
    
    @io.swagger.annotations.ApiOperation(value = "", notes = "tries to initiate update of awb", response = Void.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bearerAuth")
    }, tags={ "doAction", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 423, message = "AWB cant currently be updated due to unknown circumstances", response = Void.class),
        @io.swagger.annotations.ApiResponse(code = 200, message = "AWB Update was sheduled. The state of the update can be received by calling /eventLog", response = Void.class)
    })
    public Response doUpdatePost(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.doUpdatePost(securityContext);
    }
}
