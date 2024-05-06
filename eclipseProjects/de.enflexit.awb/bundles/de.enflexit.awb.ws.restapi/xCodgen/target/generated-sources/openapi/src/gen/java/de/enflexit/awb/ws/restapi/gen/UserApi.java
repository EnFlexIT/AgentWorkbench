package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.model.*;
import de.enflexit.awb.ws.restapi.gen.UserApiService;
import de.enflexit.awb.ws.restapi.gen.factories.UserApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.enflexit.awb.ws.restapi.gen.model.PasswordChange;

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

@Path("/user")


@io.swagger.annotations.Api(description = "the user API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T16:29:27.241008700+02:00[Europe/Berlin]")
public class UserApi  {
   private final UserApiService delegate;

   public UserApi(@Context ServletConfig servletContext) {
      UserApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("UserApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (UserApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = UserApiServiceFactory.getUserApi();
      }

      this.delegate = delegate;
   }

    @POST
    @Path("/pswd-change")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Changes the user password", notes = "", response = Void.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bearerAuth")
    }, tags={ "user", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "successfully logged-in", response = Void.class),
        @io.swagger.annotations.ApiResponse(code = 200, message = "Invalid username/password supplied", response = Void.class)
    })
    public Response changePassword(@ApiParam(value = "The credentials to login.") @Valid  PasswordChange passwordChange,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.changePassword(passwordChange, securityContext);
    }
    @GET
    @Path("/login")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Logs user into the system", notes = "Does NOT use Bearer Auth like whole other application. Only Endpoint that uses Basic Authentication. Expects previously configured Credentials and returns appropriate Bearer Token", response = String.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "basicAuth")
    }, tags={ "user", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "successfully logged-in", response = String.class),
        @io.swagger.annotations.ApiResponse(code = 200, message = "Invalid username/password supplied", response = Void.class)
    })
    public Response loginUser(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.loginUser(securityContext);
    }
    @GET
    @Path("/logout")
    
    
    @io.swagger.annotations.ApiOperation(value = "Logs the user out of the system", notes = "Deletes the jwt that has been used to make this request and does not return a new jwt. Effectively logging the user out of the System ", response = Void.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bearerAuth")
    }, tags={ "user", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "successfully logged the user out", response = Void.class)
    })
    public Response logout(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.logout(securityContext);
    }
}
