package de.enflexit.awb.ws.dynSiteApi.gen;

import de.enflexit.awb.ws.dynSiteApi.gen.ContentElementApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.factories.ContentElementApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent;

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

@Path("/contentElement")


@Tag(description = "the contentElement API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-23T11:55:38.634832400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class ContentElementApi  {

   private final ContentElementApiService delegate;

   public ContentElementApi(@Context ServletConfig servletContext) {

      ContentElementApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("ContentElementApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (ContentElementApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = ContentElementApiServiceFactory.getContentElementApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Path("/{elementID}")
    @Produces({ "application/json" })
    @Operation(summary = "Will return the content element with the specified ID", description = "", responses = {
            @ApiResponse(responseCode = "202", description = "A single content element", content = 
                @Content(schema = @Schema(implementation = AbstractSiteContent.class))),
            }, tags={  }) 
    public Response contentElementElementIDGet(@Schema(description= "numeric ID of the content element", required = true) @PathParam("elementID") @NotNull  Integer elementID,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.contentElementElementIDGet(elementID, securityContext);
    }

    @jakarta.ws.rs.PUT
    @Consumes({ "application/json" })
    @Operation(summary = "Enables to update content elements", description = "", responses = {
            @ApiResponse(responseCode = "201", description = "The content element was updated!", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            }, tags={  }) 
    public Response contentElementPut(@Schema(description = "") @Valid  AbstractSiteContent abstractSiteContent,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.contentElementPut(abstractSiteContent, securityContext);
    }
}
