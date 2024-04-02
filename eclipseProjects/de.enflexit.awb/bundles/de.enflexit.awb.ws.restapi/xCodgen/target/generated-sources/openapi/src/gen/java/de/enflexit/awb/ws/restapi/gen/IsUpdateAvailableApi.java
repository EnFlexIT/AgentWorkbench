package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.*;
import de.enflexit.awb.ws.restapi.gen.IsUpdateAvailableApiService;
import de.enflexit.awb.ws.restapi.gen.factories.IsUpdateAvailableApiServiceFactory;

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

@Path("/isUpdateAvailable")


@io.swagger.annotations.Api(description = "the isUpdateAvailable API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-21T17:19:35.482673500+01:00[Europe/Berlin]")
public class IsUpdateAvailableApi  {
   private final IsUpdateAvailableApiService delegate;

   public IsUpdateAvailableApi(@Context ServletConfig servletContext) {
      IsUpdateAvailableApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("IsUpdateAvailableApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (IsUpdateAvailableApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = IsUpdateAvailableApiServiceFactory.getIsUpdateAvailableApi();
      }

      this.delegate = delegate;
   }

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Checks wether an update for the AWB is available or not", notes = "", response = Boolean.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "BearerAuth")
    }, tags={ "info", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "request successful. boolean in response indicates wether update is available or not", response = Boolean.class)
    })
    public Response isUpdateAvailableGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.isUpdateAvailableGet(securityContext);
    }
}
