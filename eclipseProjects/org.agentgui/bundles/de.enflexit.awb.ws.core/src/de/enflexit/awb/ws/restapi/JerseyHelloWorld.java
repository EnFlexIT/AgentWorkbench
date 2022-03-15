package de.enflexit.awb.ws.restapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/helloworld")
public class JerseyHelloWorld {

	public static final String CLICHED_MESSAGE = "Hello World!";

	@GET
	@Produces("text/plain")
	public String getHello() {
		return CLICHED_MESSAGE;
	}
}

