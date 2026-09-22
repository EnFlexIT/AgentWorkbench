package de.enflexit.awb.ws.dynSiteApi.gen;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-22T13:48:54.857749600+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public class NotFoundException extends ApiException {
    private int code;
    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
