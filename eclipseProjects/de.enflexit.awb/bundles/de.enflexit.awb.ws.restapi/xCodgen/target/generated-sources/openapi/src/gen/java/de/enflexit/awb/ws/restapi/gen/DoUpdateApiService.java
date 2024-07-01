package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;


import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-07-01T09:05:28.373135400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public abstract class DoUpdateApiService {
    public abstract Response doUpdatePost(SecurityContext securityContext) throws NotFoundException;
}
