package de.enflexit.awb.ws.dynSiteApi.gen;

import de.enflexit.awb.ws.dynSiteApi.gen.TmpApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.factories.TmpApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.Map;
import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

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

@Path("/tmp")


@Tag(description = "the tmp API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-03-31T09:56:23.457253900+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class TmpApi  {

   private final TmpApiService delegate;

   public TmpApi(@Context ServletConfig servletContext) {

      TmpApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("TmpApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (TmpApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = TmpApiServiceFactory.getTmpApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Operation(summary = "", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "temporary path, so openapi file gets parsed correctly", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            }, tags={  }) 
    public Response tmpGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.tmpGet(securityContext);
    }
}
