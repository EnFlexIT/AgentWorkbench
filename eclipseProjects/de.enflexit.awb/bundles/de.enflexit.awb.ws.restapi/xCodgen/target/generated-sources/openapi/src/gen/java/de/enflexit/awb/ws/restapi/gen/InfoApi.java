package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.*;
import de.enflexit.awb.ws.restapi.gen.InfoApiService;
import de.enflexit.awb.ws.restapi.gen.factories.InfoApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.enflexit.awb.ws.restapi.gen.model.SystemInformation;

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

@Path("/info")


@io.swagger.annotations.Api(description = "the info API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T16:29:27.241008700+02:00[Europe/Berlin]")
public class InfoApi  {
   private final InfoApiService delegate;

   public InfoApi(@Context ServletConfig servletContext) {
      InfoApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("InfoApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (InfoApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = InfoApiServiceFactory.getInfoApi();
      }

      this.delegate = delegate;
   }

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns system information", notes = "Returns Hardware and system  inforamtion. ", response = SystemInformation.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bearerAuth")
    }, tags={ "admins", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "AWB-State", response = SystemInformation.class)
    })
    public Response infoGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.infoGet(securityContext);
    }
}
