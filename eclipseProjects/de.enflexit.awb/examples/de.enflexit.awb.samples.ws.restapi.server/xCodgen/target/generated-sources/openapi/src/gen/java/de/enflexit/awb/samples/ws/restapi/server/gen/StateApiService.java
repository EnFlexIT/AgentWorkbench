package de.enflexit.awb.samples.ws.restapi.server.gen;

import de.enflexit.awb.samples.ws.restapi.server.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.samples.ws.restapi.server.gen.model.ExecutionState;

import java.util.List;
import de.enflexit.awb.samples.ws.restapi.server.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-11T13:57:08.002830400+02:00[Europe/Berlin]", comments = "Generator version: 7.23.0")
public abstract class StateApiService {
    public abstract Response stateGet(SecurityContext securityContext) throws NotFoundException;
}
