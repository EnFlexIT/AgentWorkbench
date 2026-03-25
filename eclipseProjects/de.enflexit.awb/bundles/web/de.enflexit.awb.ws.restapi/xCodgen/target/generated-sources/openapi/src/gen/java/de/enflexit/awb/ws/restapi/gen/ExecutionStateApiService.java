package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.ws.restapi.gen.model.ExecutionState;

import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-03-25T12:33:25.416793100+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public abstract class ExecutionStateApiService {
    public abstract Response executionStateGet(SecurityContext securityContext) throws NotFoundException;
}
