package de.enflexit.awb.ws.restapi.gen;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public class Bootstrap extends HttpServlet {

	private static final long serialVersionUID = -8834792761453040174L;

	@Override
	public void init(ServletConfig config) throws ServletException {

		Info info = new Info()
			.title("OpenAPI Server")
			.description("This is the REST-API for Agent.Workbench in an embbedded system mode.")
			.termsOfService("Hello World")
			.contact(new Contact()
			.email("admin@enflex.it"))
			.license(new License()
			.name("Apache 2.0")
			.url("http://www.apache.org/licenses/LICENSE-2.0.html"));

		OpenAPI oas = new OpenAPI();
		oas.info(info);

		SwaggerConfiguration openApiConfig = new SwaggerConfiguration()
			.openAPI(oas).prettyPrint(true)
			.resourcePackages(Stream.of("de.enflexit.awb.ws.restapi;de.enflexit.awb.ws.restapi.gen")
			.collect(Collectors.toSet()));

		try {
			new JaxrsOpenApiContextBuilder<>()
				.servletConfig(config)
				.openApiConfiguration(openApiConfig)
				.buildContext(true);

		} catch (OpenApiConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
