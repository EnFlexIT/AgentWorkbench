package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.VersionApiService;
import de.enflexit.awb.ws.restapi.gen.factories.VersionApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.restapi.gen.model.Version;

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

@Path("/version")


@Tag(description = "the version API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-07-01T09:05:28.373135400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class VersionApi  {

   private final VersionApiService delegate;

   public VersionApi(@Context ServletConfig servletContext) {

      VersionApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("VersionApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (VersionApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
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


    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "Return the current version number of Agent.Workbench", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "Request was successful and user receives versionNumber", content = 
                @Content(schema = @Schema(implementation = Version.class))),
            },security = {
            @SecurityRequirement(name = "bearerAuth")
        }, tags={ "info", }) 
    public Response versionGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.versionGet(securityContext);
    }
}
