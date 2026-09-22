package de.enflexit.awb.ws.dynSiteApi.gen;

import de.enflexit.awb.ws.dynSiteApi.gen.MenuApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.factories.MenuApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuList;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuPost201Response;

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

@Path("/menu")


@Tag(description = "the menu API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-21T17:30:55.433568700+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class MenuApi  {

   private final MenuApiService delegate;

   public MenuApi(@Context ServletConfig servletContext) {

      MenuApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("MenuApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (MenuApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = MenuApiServiceFactory.getMenuApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "Enables to build-up the menu structure", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "The menus to be seen by the current user", content = 
                @Content(schema = @Schema(implementation = MenuList.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            }, tags={  }) 
    public Response menuGet(@Schema(description = "the language locale to use") @QueryParam("lang")  String lang,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.menuGet(lang, securityContext);
    }

    @jakarta.ws.rs.PUT
    @Path("/{menuID}/contentElement/{elementID}")
    @Operation(summary = "Add the specified content element to the specified menu", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "Content element was added to the menu", content = 
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
    public Response menuMenuIDContentElementElementIDPut(@Schema(description= "The unique ID of the menu", required = true) @PathParam("menuID") @NotNull  Integer menuID,@Schema(description= "The unique ID of the content element", required = true) @PathParam("elementID") @NotNull  Integer elementID,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.menuMenuIDContentElementElementIDPut(menuID, elementID, securityContext);
    }

    @jakarta.ws.rs.DELETE
    @Path("/{menuID}")
    @Operation(summary = "Deletes the specified menu", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "The menu was deleted", content = 
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
    public Response menuMenuIDDelete(@Schema(description= "The unique ID of the menu to be deleted", required = true) @PathParam("menuID") @NotNull  Integer menuID,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.menuMenuIDDelete(menuID, securityContext);
    }

    @jakarta.ws.rs.PUT
    @Path("/{menuID}/permissions")
    @Consumes({ "application/json" })
    @Operation(summary = "Enables to set the required rights to access the specified menu", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "The right was set", content = 
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
    public Response menuMenuIDPermissionsPut(@Schema(description= "The unique ID of the menu", required = true) @PathParam("menuID") @NotNull  Integer menuID,@Schema(description = "", required = true) @NotNull  String body,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.menuMenuIDPermissionsPut(menuID, body, securityContext);
    }

    @jakarta.ws.rs.PUT
    @Path("/{menuID}")
    @Consumes({ "application/json" })
    @Operation(summary = "Enables to update a menu", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "The menu was updated!", content = 
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
    public Response menuMenuIDPut(@Schema(description= "The unique ID of the menu to be updated", required = true) @PathParam("menuID") @NotNull  Integer menuID,@Schema(description = "", required = true) @NotNull @Valid  MenuItem menuItem,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.menuMenuIDPut(menuID, menuItem, securityContext);
    }

    @jakarta.ws.rs.POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @Operation(summary = "Create a new menu", description = "", responses = {
            @ApiResponse(responseCode = "201", description = "Menu created", content = 
                @Content(schema = @Schema(implementation = MenuPost201Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            }, tags={  }) 
    public Response menuPost(@Schema(description = "", required = true) @NotNull @Valid  MenuItem menuItem,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.menuPost(menuItem, securityContext);
    }
}
