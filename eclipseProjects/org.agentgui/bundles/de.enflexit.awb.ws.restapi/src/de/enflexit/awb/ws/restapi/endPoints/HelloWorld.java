package de.enflexit.awb.ws.restapi.endPoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

@Path("/helloworld")
public class HelloWorld {

	public static final String CLICHED_MESSAGE = "Hello World!";

	@GET
	@Produces("application/json")
	public String getHello() {
		return new Gson().toJson(new String(CLICHED_MESSAGE));
	}
	
	@GET
	@Path("/health")
	@Produces(MediaType.APPLICATION_JSON)
	public Health getHealth() {
		return new Health(CLICHED_MESSAGE, "123.123.123.123");
	}

	
}

