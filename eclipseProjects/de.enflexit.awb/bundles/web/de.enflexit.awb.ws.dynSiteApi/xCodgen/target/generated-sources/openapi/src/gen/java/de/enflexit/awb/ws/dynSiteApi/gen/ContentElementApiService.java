package de.enflexit.awb.ws.dynSiteApi.gen;

import de.enflexit.awb.ws.dynSiteApi.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent;
import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateContentElement201Response;
import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateContentRequest;
import de.enflexit.awb.ws.dynSiteApi.gen.model.UpdateContentElementRequest;

import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-10-01T10:08:21.991650800+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public abstract class ContentElementApiService {
    public abstract Response createContentElement(CreateContentRequest createContentRequest,SecurityContext securityContext) throws NotFoundException;
    public abstract Response deleteContentElement(Integer elementId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getContentElement(Integer elementId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response updateContentElement(Integer elementId,UpdateContentElementRequest updateContentElementRequest,SecurityContext securityContext) throws NotFoundException;
}
