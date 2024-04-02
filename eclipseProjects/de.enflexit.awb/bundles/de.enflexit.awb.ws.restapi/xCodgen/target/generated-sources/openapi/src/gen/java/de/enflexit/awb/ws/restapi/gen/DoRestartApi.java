package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.*;
import de.enflexit.awb.ws.restapi.gen.DoRestartApiService;
import de.enflexit.awb.ws.restapi.gen.factories.DoRestartApiServiceFactory;

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

@Path("/doRestart")


@io.swagger.annotations.Api(description = "the doRestart API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-21T17:19:35.482673500+01:00[Europe/Berlin]")
public class DoRestartApi  {
   private final DoRestartApiService delegate;

   public DoRestartApi(@Context ServletConfig servletContext) {
      DoRestartApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("DoRestartApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (DoRestartApiService) Class.forName(implClass).newInstance();
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

    @POST
    
    
    
    @io.swagger.annotations.ApiOperation(value = "", notes = "tries to shedule restart", response = Void.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "BearerAuth")
    }, tags={ "doAction", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 423, message = "Restart could not be initiated", response = Void.class),
        @io.swagger.annotations.ApiResponse(code = 200, message = "Restart will happen. History can be accessed via eventLog", response = Void.class)
    })
    public Response doRestartPost(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.doRestartPost(securityContext);
    }
}
