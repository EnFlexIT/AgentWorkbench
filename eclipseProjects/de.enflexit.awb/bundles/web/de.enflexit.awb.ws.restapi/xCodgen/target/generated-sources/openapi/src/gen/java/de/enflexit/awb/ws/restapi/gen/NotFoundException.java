package de.enflexit.awb.ws.restapi.gen;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-08T12:08:47.460531100+02:00[Europe/Berlin]", comments = "Generator version: 7.22.0")
public class NotFoundException extends ApiException {
    private int code;
    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
