package de.enflexit.awb.samples.ws.api.endPoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.google.gson.Gson;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition (info =
	@Info(
		title = "the title",
        version = "0.0",
        description = "My API",
        license = @License(name = "Apache 2.0", url = "http://foo.bar"),
        contact = @Contact(url = "http://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
	)
)
@Path("/helloworld")
public class HelloWorld {

	public static final String CLICHED_MESSAGE = "Hello World!";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getHello() {
		return new Gson().toJson(new String(CLICHED_MESSAGE));
	}
	
	@GET
	@Path("/health")
	@Produces(MediaType.APPLICATION_JSON)
	
	@Operation(summary = "SampleApi", description = "returns a list of clients")
		@ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
		@ApiResponse(responseCode = "200", description = "Ok")
		@ApiResponse(responseCode = "400", description = "Bad Request")
		@ApiResponse(responseCode = "404", description = "Error")
		@ApiResponse(responseCode = "500", description = "Internal Server Error")
		@ApiResponse(responseCode = "503", description = "Service Unavailable")
		@Tag(name = "MyApi")
	public Health getHealth() {
		return new Health(CLICHED_MESSAGE, "123.123.123.123");
	}

}

