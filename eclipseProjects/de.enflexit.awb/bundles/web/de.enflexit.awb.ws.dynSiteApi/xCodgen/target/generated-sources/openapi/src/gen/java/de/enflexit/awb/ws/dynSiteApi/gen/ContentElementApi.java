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
import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateContentElement201Response;
import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateContentRequest;

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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-23T10:17:15.337896100+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
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


    @jakarta.ws.rs.POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @Operation(summary = "Create new content", description = "", responses = {
            @ApiResponse(responseCode = "201", description = "Content created", content = 
                @Content(schema = @Schema(implementation = CreateContentElement201Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            }, tags={  }) 
    public Response createContentElement(@Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED) @NotNull @Valid  CreateContentRequest createContentRequest,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.createContentElement(createContentRequest, securityContext);
    }

    @jakarta.ws.rs.DELETE
    @Path("/{elementID}")
    @Operation(summary = "Deletes the specified content element", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "The content element was deleted", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            }, tags={  }) 
    public Response deleteContentElement(@Schema(description= "The unique ID of the content element to delete", requiredMode = Schema.RequiredMode.REQUIRED) @PathParam("elementID") @NotNull  Integer elementID,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.deleteContentElement(elementID, securityContext);
    }

    @jakarta.ws.rs.GET
    @Path("/{elementID}")
    @Produces({ "application/json" })
    @Operation(summary = "Returns the content element for the specified ID", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "A single content element", content = 
                @Content(schema = @Schema(implementation = AbstractSiteContent.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            }, tags={  }) 
    public Response getContentElement(@Schema(description= "unique ID of the content element", requiredMode = Schema.RequiredMode.REQUIRED) @PathParam("elementID") @NotNull  Integer elementID,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getContentElement(elementID, securityContext);
    }

    @jakarta.ws.rs.PUT
    @Path("/{elementID}")
    @Consumes({ "application/json" })
    @Operation(summary = "Enables to update content elements", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "The element was updated!", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            }, tags={  }) 
    public Response updateContentElement(@Schema(description= "The unique ID of the content element to update", requiredMode = Schema.RequiredMode.REQUIRED) @PathParam("elementID") @NotNull  Integer elementID,@Schema(description = "") @Valid  AbstractSiteContent abstractSiteContent,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.updateContentElement(elementID, abstractSiteContent, securityContext);
    }
}
