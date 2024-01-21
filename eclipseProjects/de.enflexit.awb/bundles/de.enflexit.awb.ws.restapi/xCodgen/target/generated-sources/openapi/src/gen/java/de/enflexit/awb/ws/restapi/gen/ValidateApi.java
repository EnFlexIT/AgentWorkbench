package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.*;
import de.enflexit.awb.ws.restapi.gen.ValidateApiService;
import de.enflexit.awb.ws.restapi.gen.factories.ValidateApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.enflexit.awb.ws.restapi.gen.model.JWT;

import java.util.Map;
import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

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

@Path("/validate")


@io.swagger.annotations.Api(description = "the validate API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-01-18T14:16:57.078043800+01:00[Europe/Berlin]")
public class ValidateApi  {
   private final ValidateApiService delegate;

   public ValidateApi(@Context ServletConfig servletContext) {
      ValidateApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("ValidateApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (ValidateApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = ValidateApiServiceFactory.getValidateApi();
      }

      this.delegate = delegate;
   }

    @POST
    
    @Consumes({ "application/json" })
    
    @io.swagger.annotations.ApiOperation(value = "Checks if a jwt is valid", notes = "Validates the integrity of a jwt by checking the signature and its expiration time", response = Void.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "AwbApiKey")
    }, tags={ "authentication", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "the jwt is valid", response = Void.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = "the jwt is not valid", response = Void.class)
    })
    public Response validatePost(@ApiParam(value = "this is the jwt that will be validated", required = true) @NotNull @Valid  JWT JWT,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.validatePost(JWT, securityContext);
    }
}
