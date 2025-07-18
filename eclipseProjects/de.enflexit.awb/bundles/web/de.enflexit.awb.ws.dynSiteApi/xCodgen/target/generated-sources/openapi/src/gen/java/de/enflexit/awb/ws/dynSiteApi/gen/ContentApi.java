package de.enflexit.awb.ws.dynSiteApi.gen;

import de.enflexit.awb.ws.dynSiteApi.gen.ContentApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.factories.ContentApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentList;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentListUpdate;

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

@Path("/content")


@Tag(description = "the content API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-18T09:11:14.660026+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class ContentApi  {

   private final ContentApiService delegate;

   public ContentApi(@Context ServletConfig servletContext) {

      ContentApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("ContentApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (ContentApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = ContentApiServiceFactory.getContentApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Path("/{menuID}")
    @Produces({ "application/json" })
    @Operation(summary = "Returns the content for the specified ID", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "List of content elements", content = 
                @Content(schema = @Schema(implementation = SiteContentList.class))),
            }, tags={  }) 
    public Response contentMenuIDGet(@Schema(description= "numeric ID of the content", required = true) @PathParam("menuID") @NotNull  Integer menuID,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.contentMenuIDGet(menuID, securityContext);
    }

    @jakarta.ws.rs.PUT
    @Consumes({ "application/json" })
    @Operation(summary = "Enables to update content", description = "", responses = {
            @ApiResponse(responseCode = "201", description = "Content was updated!", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            }, tags={  }) 
    public Response contentPut(@Schema(description = "") @Valid  SiteContentListUpdate siteContentListUpdate,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.contentPut(siteContentListUpdate, securityContext);
    }
}
