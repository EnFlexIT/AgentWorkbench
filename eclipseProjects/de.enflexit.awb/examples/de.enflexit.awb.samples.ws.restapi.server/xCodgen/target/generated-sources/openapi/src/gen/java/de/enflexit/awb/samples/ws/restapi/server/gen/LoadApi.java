package de.enflexit.awb.samples.ws.restapi.server.gen;

import de.enflexit.awb.samples.ws.restapi.server.gen.LoadApiService;
import de.enflexit.awb.samples.ws.restapi.server.gen.factories.LoadApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.enflexit.awb.samples.ws.restapi.server.gen.model.SystemLoad;

import java.util.Map;
import java.util.List;
import de.enflexit.awb.samples.ws.restapi.server.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/load")


@io.swagger.annotations.Api(description = "the load API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-04-17T17:09:44.461949400+02:00[Europe/Berlin]")
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

    @javax.ws.rs.GET
    
    
    @Produces({ "applicaion/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns the current System load", notes = "Returns the current system load measured by Agent.Workbench that includes CPU-, memory- and Java Heap - load. Further, the number of threads and agents will be returnes ", response = SystemLoad.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "AwbApiKey")
    }, tags={ "admins", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "System Load", response = SystemLoad.class)
    })
    public Response loadGet(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.loadGet(securityContext);
    }
}
