package de.enflexit.awb.samples.ws.restapi.server.gen;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.*;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;

public class Bootstrap extends HttpServlet {

  private static final long serialVersionUID = 20230810;   

  @Override
  public void init(ServletConfig config) throws ServletException {
  
    Info info = new Info()
      .title("OpenAPI Server")
      .description("This is the REST-API for Agent.Workbench in an embbedded system mode.")
      .termsOfService("")
      .contact(new Contact()
        .email("admin@enflex.it"))
      .license(new License()
        .name("Apache 2.0")
        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));

    OpenAPI oas = new OpenAPI();
    oas.info(info);

    SwaggerConfiguration openApiConfig = new SwaggerConfiguration()
        .openAPI(oas)
        .prettyPrint(true)
        .resourcePackages(Stream.of("io.swagger.sample.resource").collect(Collectors.toSet()));
    
    try {
        new JaxrsOpenApiContextBuilder()
            .servletConfig(config)
            .openApiConfiguration(openApiConfig)
            .buildContext(true);
            
    } catch (OpenApiConfigurationException e) {
        throw new RuntimeException(e.getMessage(), e);
    }
  }
}
