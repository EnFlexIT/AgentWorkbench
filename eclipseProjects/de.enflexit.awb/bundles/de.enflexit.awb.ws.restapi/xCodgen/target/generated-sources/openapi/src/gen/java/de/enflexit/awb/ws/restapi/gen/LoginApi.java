package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.*;
import de.enflexit.awb.ws.restapi.gen.LoginApiService;
import de.enflexit.awb.ws.restapi.gen.factories.LoginApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.enflexit.awb.ws.restapi.gen.model.Credentials;
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

@Path("/login")


@io.swagger.annotations.Api(description = "the login API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-01-18T14:16:57.078043800+01:00[Europe/Berlin]")
public class LoginApi  {
   private final LoginApiService delegate;

   public LoginApi(@Context ServletConfig servletContext) {
      LoginApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("LoginApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (LoginApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = LoginApiServiceFactory.getLoginApi();
      }

      this.delegate = delegate;
   }

    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "checks credentials and returns jwt", notes = "Checks wether the given credentials match any users in the database and returns a jwt which encapsulates all of the users rights ", response = JWT.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "AwbApiKey")
    }, tags={ "authentication", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "The Credentials matched a user in the database and therefore the login was successful", response = JWT.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = "The Credentials did not match any users in the database. The Credentials were wrongs", response = Void.class)
    })
    public Response loginPost(@ApiParam(value = "", required = true) @NotNull @Valid  Credentials credentials,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.loginPost(credentials, securityContext);
    }
}
