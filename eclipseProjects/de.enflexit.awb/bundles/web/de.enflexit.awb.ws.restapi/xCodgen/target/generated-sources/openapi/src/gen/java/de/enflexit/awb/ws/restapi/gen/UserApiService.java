package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.ws.restapi.gen.model.PasswordChange;
import de.enflexit.awb.ws.restapi.gen.model.SessionTimes;

import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-08T12:08:47.460531100+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public abstract class UserApiService {
    public abstract Response changePassword(PasswordChange passwordChange,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getSessionTime(SecurityContext securityContext) throws NotFoundException;
    public abstract Response loginUser(SecurityContext securityContext) throws NotFoundException;
    public abstract Response logout(SecurityContext securityContext) throws NotFoundException;
}
