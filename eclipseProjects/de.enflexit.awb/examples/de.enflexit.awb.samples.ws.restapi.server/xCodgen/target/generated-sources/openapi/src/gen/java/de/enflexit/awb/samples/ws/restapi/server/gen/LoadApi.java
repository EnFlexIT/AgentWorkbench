package de.enflexit.awb.samples.ws.restapi.server.gen;

import de.enflexit.awb.samples.ws.restapi.server.gen.LoadApiService;
import de.enflexit.awb.samples.ws.restapi.server.gen.factories.LoadApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.samples.ws.restapi.server.gen.model.SystemLoad;

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

@Path("/load")


@Tag(description = "the load API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-08-15T11:14:24.521899400+02:00[Europe/Berlin]")
public class LoadApi  {
   private final LoadApiService delegate;

   public LoadApi(@Context ServletConfig servletContext) {
      LoadApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("LoadApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (LoadApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = LoadApiServiceFactory.getLoadApi();
      }

      this.delegate = delegate;
   }

    @jakarta.ws.rs.GET
    
    
    @Produces({ "applicaion/json" })
    @Operation(summary = "Returns the current System load", description = "", 
        responses = {
            @ApiResponse(responseCode = "200", description = "System Load", content = @Content(schema = @Schema(implementation = SystemLoad.class))),
            }
    , tags={ "admins", }) 
//    ==> Previous Swagger1 annotations <==    
//    @io.swagger.annotations.ApiOperation(value = "Returns the current System load", notes = "Returns the current system load measured by Agent.Workbench that includes CPU-, memory- and Java Heap - load. Further, the number of threads and agents will be returnes ", response = SystemLoad.class, authorizations = {
//        @io.swagger.annotations.Authorization(value = "AwbApiKey")
//    }, tags={ "admins", })
//    @io.swagger.annotations.ApiResponses(value = {
//        
//        @io.swagger.annotations.ApiResponse(code = 200, message = "System Load", response = SystemLoad.class)
//        
//    })
    public Response loadGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.loadGet(securityContext);
    }
}
