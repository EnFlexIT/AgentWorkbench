package de.enflexit.awb.samples.ws.restapi.server.gen;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-06-11T13:57:08.002830400+02:00[Europe/Berlin]", comments = "Generator version: 7.23.0")
public class NotFoundException extends ApiException {
    private int code;
    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
