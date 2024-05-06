package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.*;
import de.enflexit.awb.ws.restapi.gen.model.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;


import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T23:49:08.451118100+02:00[Europe/Berlin]")
public abstract class IsUpdateAvailableApiService {
    public abstract Response isUpdateAvailableGet(SecurityContext securityContext) throws NotFoundException;
}
