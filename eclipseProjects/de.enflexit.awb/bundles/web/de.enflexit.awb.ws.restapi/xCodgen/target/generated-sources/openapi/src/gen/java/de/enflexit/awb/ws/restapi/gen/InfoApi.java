package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.InfoApiService;
import de.enflexit.awb.ws.restapi.gen.factories.InfoApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.restapi.gen.model.SystemInformation;

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

@Path("/info")


@Tag(description = "the info API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-07-01T09:05:28.373135400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class InfoApi  {

   private final InfoApiService delegate;

   public InfoApi(@Context ServletConfig servletContext) {

      InfoApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("InfoApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (InfoApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
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


    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "Returns system information", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "AWB-State", content = 
                @Content(schema = @Schema(implementation = SystemInformation.class))),
            },security = {
            @SecurityRequirement(name = "bearerAuth")
        }, tags={ "admins", }) 
    public Response infoGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.infoGet(securityContext);
    }
}
