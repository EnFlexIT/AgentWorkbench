package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.LoadApiService;
import de.enflexit.awb.ws.restapi.gen.factories.LoadApiServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import de.enflexit.awb.ws.restapi.gen.model.SystemInformation;
import de.enflexit.awb.ws.restapi.gen.model.SystemLoad;

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

@Path("/load")
<<<<<<< HEAD
@Tag(name="Load EndPoint", description = "The Loaf API")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-04-01T19:11:06.016775900+02:00[Europe/Berlin]")
=======


@io.swagger.annotations.Api(description = "the load API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-21T17:19:35.482673500+01:00[Europe/Berlin]")
>>>>>>> refs/heads/master
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
    
    
<<<<<<< HEAD
    @Produces({ "applicaion/json" })
//    @io.swagger.annotations.ApiOperation(value = "Returns the current System load", notes = "Returns the current system load measured by Agent.Workbench that includes CPU-, memory- and Java Heap - load. Further, the number of threads and agents will be returnes ", response = SystemLoad.class, authorizations = {
//        @io.swagger.annotations.Authorization(value = "AwbApiKey")
//    }, tags={ "admins", })
//    @io.swagger.annotations.ApiResponses(value = {
//        @io.swagger.annotations.ApiResponse(code = 200, message = "System Load", response = SystemLoad.class)
//    })
    @Operation(summary = "Returns the current System load", description = "Returns the current system load measured by Agent.Workbench that includes CPU-, memory- and Java Heap - load. Further, the number of threads and agents will be returnes ", 
   	responses = {@ApiResponse(responseCode = "OK", description = "OK_MSG", 
   					content = @Content(schema = @Schema(implementation = SystemLoad.class))),
   					@ApiResponse(responseCode = "NOT_FOUND", description = "NOT_FOUND_MSG")})
=======
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns the current System load", notes = "Returns the current system load measured by Agent.Workbench that includes CPU-, memory- and Java Heap - load. Further, the number of threads and agents will be returnes ", response = SystemLoad.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "BearerAuth")
    }, tags={ "admins", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "System Load", response = SystemLoad.class)
    })
>>>>>>> refs/heads/master
    public Response loadGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.loadGet(securityContext);
    }
}
