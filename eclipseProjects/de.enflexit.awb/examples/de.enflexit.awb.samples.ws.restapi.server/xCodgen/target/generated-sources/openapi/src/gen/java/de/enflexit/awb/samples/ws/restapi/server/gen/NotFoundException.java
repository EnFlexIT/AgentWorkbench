package de.enflexit.awb.samples.ws.restapi.server.gen;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-11-25T18:29:52.472926+01:00[Europe/Berlin]", comments = "Generator version: 7.17.0")
public class NotFoundException extends ApiException {
    private int code;
    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
