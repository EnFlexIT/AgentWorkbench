package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.LogsApiService;
import de.enflexit.awb.ws.restapi.gen.factories.LogsApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.File;
import java.time.LocalDate;

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

@Path("/logs")


@Tag(description = "the logs API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-08-06T09:07:31.407970700+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class LogsApi  {

   private final LogsApiService delegate;

   public LogsApi(@Context ServletConfig servletContext) {

      LogsApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("LogsApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (LogsApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = LogsApiServiceFactory.getLogsApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Path("/archive")
    @Produces({ "application/zip" })
    @Operation(summary = "Download multiple log files as ZIP archive", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "ZIP archive download successful", content = 
                @Content(schema = @Schema(implementation = File.class))),
            @ApiResponse(responseCode = "500", description = "Failed to create log archive", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            },security = {
            @SecurityRequirement(name = "bearerAuth")
        }, tags={ "admins", }) 
    public Response downloadLogArchive(@Schema(description = "Start date (inclusive)") @QueryParam("from") @NotNull  String from,@Schema(description = "End date (inclusive)") @QueryParam("to") @NotNull  String to,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.downloadLogArchive(from, to, securityContext);
    }

    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "Returns all available log files", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "List of available log files", content = 
                @Content(schema = @Schema(implementation = LocalDate.class))),
            },security = {
            @SecurityRequirement(name = "bearerAuth")
        }, tags={ "admins", }) 
    public Response getLogFiles(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getLogFiles(securityContext);
    }
}
