package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.*;
import de.enflexit.awb.ws.restapi.gen.VersionApiService;
import de.enflexit.awb.ws.restapi.gen.factories.VersionApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.enflexit.awb.ws.restapi.gen.model.Version;

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

@Path("/version")


@io.swagger.annotations.Api(description = "the version API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T22:50:45.497388700+02:00[Europe/Berlin]")
public class VersionApi  {
   private final VersionApiService delegate;

   public VersionApi(@Context ServletConfig servletContext) {
      VersionApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("VersionApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (VersionApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = VersionApiServiceFactory.getVersionApi();
      }

      this.delegate = delegate;
   }

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Return most up to date version number", notes = "", response = Version.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bearerAuth")
    }, tags={ "info", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Request was successful and user receives versionNumber", response = Version.class)
    })
    public Response versionGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.versionGet(securityContext);
    }
}
