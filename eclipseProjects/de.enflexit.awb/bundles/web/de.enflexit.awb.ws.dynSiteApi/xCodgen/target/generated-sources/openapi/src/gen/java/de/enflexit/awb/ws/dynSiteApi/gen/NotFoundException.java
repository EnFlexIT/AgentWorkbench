package de.enflexit.awb.ws.dynSiteApi.gen;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-22T09:53:42.299228200+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public class NotFoundException extends ApiException {
    private int code;
    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
