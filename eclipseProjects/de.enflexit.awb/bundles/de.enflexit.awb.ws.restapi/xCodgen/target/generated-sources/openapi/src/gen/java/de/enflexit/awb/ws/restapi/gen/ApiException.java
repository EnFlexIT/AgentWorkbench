package de.enflexit.awb.ws.restapi.gen;

/**
 * The exception that can be used to store the HTTP status code returned by an API response.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2022-04-19T14:42:04.744880100+02:00[Europe/Berlin]")
public class ApiException extends Exception {

    /** The HTTP status code. */
    private int code;

    /**
     * Constructor.
     *
     * @param code The HTTP status code.
     * @param msg The error message.
     */
    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    /**
     * Get the HTTP status code.
     *
     * @return The HTTP status code.
     */
    public int getCode() {
        return code;
    }

}