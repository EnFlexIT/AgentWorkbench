package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.*;
import de.enflexit.awb.ws.restapi.gen.model.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.ws.restapi.gen.model.PasswordChange;

import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T23:49:08.451118100+02:00[Europe/Berlin]")
public abstract class UserApiService {
    public abstract Response changePassword(PasswordChange passwordChange,SecurityContext securityContext) throws NotFoundException;
    public abstract Response loginUser(SecurityContext securityContext) throws NotFoundException;
    public abstract Response logout(SecurityContext securityContext) throws NotFoundException;
}
