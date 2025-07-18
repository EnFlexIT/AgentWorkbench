package de.enflexit.awb.ws.dynSiteApi.gen;

import de.enflexit.awb.ws.dynSiteApi.gen.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent;

import java.util.List;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-18T09:11:14.660026+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public abstract class ContentElementApiService {
    public abstract Response contentElementElementIDGet(Integer elementID,SecurityContext securityContext) throws NotFoundException;
    public abstract Response contentElementPut(AbstractSiteContent abstractSiteContent,SecurityContext securityContext) throws NotFoundException;
}
