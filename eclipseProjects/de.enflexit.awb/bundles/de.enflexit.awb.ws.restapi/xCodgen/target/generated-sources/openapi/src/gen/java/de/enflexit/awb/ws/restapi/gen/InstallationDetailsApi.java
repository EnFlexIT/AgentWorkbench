package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.*;
import de.enflexit.awb.ws.restapi.gen.InstallationDetailsApiService;
import de.enflexit.awb.ws.restapi.gen.factories.InstallationDetailsApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.enflexit.awb.ws.restapi.gen.model.BundleInformation;

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

@Path("/installationDetails")


@io.swagger.annotations.Api(description = "the installationDetails API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T16:29:27.241008700+02:00[Europe/Berlin]")
public class InstallationDetailsApi  {
   private final InstallationDetailsApiService delegate;

   public InstallationDetailsApi(@Context ServletConfig servletContext) {
      InstallationDetailsApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("InstallationDetailsApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (InstallationDetailsApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = InstallationDetailsApiServiceFactory.getInstallationDetailsApi();
      }

      this.delegate = delegate;
   }

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get the details about an AWB Installation", notes = "", response = BundleInformation.class, responseContainer = "List", authorizations = {
        @io.swagger.annotations.Authorization(value = "bearerAuth")
    }, tags={ "info", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Return the Details about an AWB Installtion", response = BundleInformation.class, responseContainer = "List")
    })
    public Response installationDetailsGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.installationDetailsGet(securityContext);
    }
}
