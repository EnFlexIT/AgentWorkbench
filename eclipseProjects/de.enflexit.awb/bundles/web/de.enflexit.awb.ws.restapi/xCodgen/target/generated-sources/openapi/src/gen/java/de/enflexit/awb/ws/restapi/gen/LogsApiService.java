package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.io.File;
import java.time.LocalDate;

import java.util.List;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-08-06T09:07:31.407970700+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public abstract class LogsApiService {
    public abstract Response downloadLogArchive( @NotNull String from, @NotNull String to,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getLogFiles(SecurityContext securityContext) throws NotFoundException;
}
