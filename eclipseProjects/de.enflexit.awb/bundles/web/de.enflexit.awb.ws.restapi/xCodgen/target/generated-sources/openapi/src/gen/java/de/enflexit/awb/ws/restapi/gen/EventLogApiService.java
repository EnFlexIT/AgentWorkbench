package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.ws.restapi.gen.model.Event;
import de.enflexit.awb.ws.restapi.gen.model.EventLogTypes;

import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-12-19T14:09:40.842261300+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public abstract class EventLogApiService {
    public abstract Response eventLogGet( @NotNull EventLogTypes type, @Min(1) @Max(100)Integer amount,SecurityContext securityContext) throws NotFoundException;
}
