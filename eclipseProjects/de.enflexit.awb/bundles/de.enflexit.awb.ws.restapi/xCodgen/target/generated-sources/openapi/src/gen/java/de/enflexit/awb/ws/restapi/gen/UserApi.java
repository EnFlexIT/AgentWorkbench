package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.UserApiService;
import de.enflexit.awb.ws.restapi.gen.factories.UserApiServiceFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import de.enflexit.awb.ws.restapi.gen.model.PasswordChange;
import de.enflexit.awb.ws.restapi.gen.model.UserLogin;

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

@Path("/user")


@Tag(description = "the user API", name = "")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-04-02T17:38:36.020041800+02:00[Europe/Berlin]", comments = "Generator version: 7.4.0")
public class UserApi  {

   private final UserApiService delegate;

   public UserApi(@Context ServletConfig servletContext) {

      UserApiService delegate = null;
      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("UserApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (UserApiService) Class.forName(implClass).getDeclaredConstructor().newInstance();
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


    @jakarta.ws.rs.POST
    @Path("/pswd-change")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @Operation(summary = "Changes the user password", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "successfully logged-in", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "200", description = "Invalid username/password supplied", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            },security = {
            @SecurityRequirement(name = "BearerAuth")
        }, tags={ "user", }) 
    public Response changePassword(@Schema(description = "The credentials to login.") @Valid  PasswordChange passwordChange,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.changePassword(passwordChange, securityContext);
    }

    @jakarta.ws.rs.POST
    @Path("/login")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @Operation(summary = "Logs user into the system", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "successfully logged-in", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "200", description = "Invalid username/password supplied", content = 
                @Content(schema = @Schema(implementation = Void.class))),
            },security = {
            @SecurityRequirement(name = "BearerAuth")
        }, tags={ "user", }) 
    public Response loginUser(@Schema(description = "The credentials to login.") @Valid  UserLogin userLogin,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.loginUser(userLogin, securityContext);
    }
}
