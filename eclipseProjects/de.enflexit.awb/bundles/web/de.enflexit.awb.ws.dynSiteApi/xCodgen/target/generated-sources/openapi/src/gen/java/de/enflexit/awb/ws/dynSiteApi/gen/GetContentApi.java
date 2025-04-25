package de.enflexit.awb.ws.dynSiteApi.gen;

import de.enflexit.awb.ws.dynSiteApi.gen.GetContentApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.factories.GetContentApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentList;

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

@Path("/getContent/{menueID}")


@Tag(description = "the getContent API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-04-25T15:51:31.606082900+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class GetContentApi  {

   private final GetContentApiService delegate;

   public GetContentApi(@Context ServletConfig servletContext) {

      GetContentApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("GetContentApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (GetContentApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = GetContentApiServiceFactory.getGetContentApi();
      }
      this.delegate = delegate;
   }


    @jakarta.ws.rs.GET
    @Produces({ "application/json" })
    @Operation(summary = "Returns the content for the specified ID", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "Success - Content", content = 
                @Content(schema = @Schema(implementation = SiteContentList.class))),
            }, tags={  }) 
    public Response getContentMenueIDGet(@Schema(description= "numeric ID of the content", required = true) @PathParam("menueID") @NotNull  Integer menueID,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getContentMenueIDGet(menueID, securityContext);
    }
}
