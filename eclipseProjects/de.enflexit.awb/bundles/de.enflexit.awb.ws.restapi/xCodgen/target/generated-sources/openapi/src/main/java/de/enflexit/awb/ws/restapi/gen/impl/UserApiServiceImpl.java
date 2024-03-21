package de.enflexit.awb.ws.restapi.gen.impl;

import de.enflexit.awb.ws.restapi.gen.*;
import de.enflexit.awb.ws.restapi.gen.model.*;

import de.enflexit.awb.ws.restapi.gen.model.PasswordChange;
import de.enflexit.awb.ws.restapi.gen.model.UserLogin;

import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-21T17:19:35.482673500+01:00[Europe/Berlin]")
public class UserApiServiceImpl extends UserApiService {
    @Override
    public Response changePassword(PasswordChange passwordChange, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response loginUser(UserLogin userLogin, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
