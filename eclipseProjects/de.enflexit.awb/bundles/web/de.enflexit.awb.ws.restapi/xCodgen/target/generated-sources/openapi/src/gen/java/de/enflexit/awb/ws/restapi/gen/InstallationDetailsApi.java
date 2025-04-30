package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.InstallationDetailsApiService;
import de.enflexit.awb.ws.restapi.gen.factories.InstallationDetailsApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.restapi.gen.model.BundleInformation;

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

@Path("/installationDetails")


@Tag(description = "the installationDetails API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-04-29T11:33:26.991574300+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class InstallationDetailsApi  {

   private final InstallationDetailsApiService delegate;

   public InstallationDetailsApi(@Context ServletConfig servletContext) {

      InstallationDetailsApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("InstallationDetailsApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (InstallationDetailsApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
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


    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "Get the details about an AWB Installation", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "Return the Details about an AWB Installtion", content = 
                @Content(schema = @Schema(implementation = BundleInformation.class))),
            },security = {
            @SecurityRequirement(name = "bearerAuth")
        }, tags={ "info", }) 
    public Response installationDetailsGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.installationDetailsGet(securityContext);
    }
}
