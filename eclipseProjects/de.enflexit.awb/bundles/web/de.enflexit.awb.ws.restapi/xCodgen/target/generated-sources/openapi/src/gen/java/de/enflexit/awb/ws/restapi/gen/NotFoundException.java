package de.enflexit.awb.ws.restapi.gen;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-03-25T12:33:25.416793100+01:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class NotFoundException extends ApiException {
    private int code;
    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
