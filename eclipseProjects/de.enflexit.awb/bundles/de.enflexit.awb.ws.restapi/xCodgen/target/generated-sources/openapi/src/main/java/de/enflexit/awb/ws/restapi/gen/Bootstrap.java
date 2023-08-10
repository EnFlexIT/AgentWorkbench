package de.enflexit.awb.ws.restapi.gen;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletContext;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;

public class Bootstrap extends HttpServlet {
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

//    ServletContext context = config.getServletContext();
//    Swagger swagger = new Swagger().info(info);

//    new SwaggerContextService().withServletConfig(config).updateSwagger(swagger);
  }
}
