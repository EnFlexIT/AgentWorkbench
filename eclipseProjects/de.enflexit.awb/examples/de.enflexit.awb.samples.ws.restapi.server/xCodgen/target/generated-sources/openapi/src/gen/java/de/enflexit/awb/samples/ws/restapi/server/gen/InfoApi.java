package de.enflexit.awb.samples.ws.restapi.server.gen;

import de.enflexit.awb.samples.ws.restapi.server.gen.InfoApiService;
import de.enflexit.awb.samples.ws.restapi.server.gen.factories.InfoApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.samples.ws.restapi.server.gen.model.SystemInformation;

import java.util.Map;
import java.util.List;
import de.enflexit.awb.samples.ws.restapi.server.gen.NotFoundException;

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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-08-15T11:14:24.521899400+02:00[Europe/Berlin]")
public class InfoApi  {
   private final InfoApiService delegate;

   public InfoApi(@Context ServletConfig servletContext) {
      InfoApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("InfoApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (InfoApiService) Class.forName(implClass).newInstance();
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
    
    
    @Produces({ "applicaion/json" })
    @Operation(summary = "Returns system information", description = "", 
        responses = {
            @ApiResponse(responseCode = "200", description = "AWB-State", content = @Content(schema = @Schema(implementation = SystemInformation.class))),
            }
    , tags={ "admins", }) 
//    ==> Previous Swagger1 annotations <==    
//    @io.swagger.annotations.ApiOperation(value = "Returns system information", notes = "Returns Hardware and system  inforamtion. ", response = SystemInformation.class, authorizations = {
//        @io.swagger.annotations.Authorization(value = "AwbApiKey")
//    }, tags={ "admins", })
//    @io.swagger.annotations.ApiResponses(value = {
//        
//        @io.swagger.annotations.ApiResponse(code = 200, message = "AWB-State", response = SystemInformation.class)
//        
//    })
    public Response infoGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.infoGet(securityContext);
    }
}
