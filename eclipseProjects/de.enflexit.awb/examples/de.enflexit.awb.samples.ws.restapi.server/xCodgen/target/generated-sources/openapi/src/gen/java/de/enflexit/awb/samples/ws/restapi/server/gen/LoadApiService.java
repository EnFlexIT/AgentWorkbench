package de.enflexit.awb.samples.ws.restapi.server.gen;

import de.enflexit.awb.samples.ws.restapi.server.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.samples.ws.restapi.server.gen.model.SystemLoad;

import java.util.List;
import de.enflexit.awb.samples.ws.restapi.server.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-08-15T11:14:24.521899400+02:00[Europe/Berlin]")
public abstract class LoadApiService {
    public abstract Response loadGet(SecurityContext securityContext) throws NotFoundException;
}
