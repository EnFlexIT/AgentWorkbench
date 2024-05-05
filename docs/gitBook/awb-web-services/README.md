---
description: >-
  Agent.Workbench allows to use and provide Web-Services. This is enabled by the
  de.enflexit.awb.ws.feature.  Following, a broad overview of the
  de.enflexit.awb.ws.feature wil be given.
---

# AWB Web-Services

A feature can consist of multiple feature bundled to one feature. In case of the _de.enflexit.awb.ws.feature_, three feaures build the core of the _de.enflexit.awb.ws.feature_: The _de.enflexit.awb.ws.jersey.feature,_ _de.enflexit.awb.ws.jackson.feature_ and the _de.enflexit.awb.ws.swagger feature_.

The features provides four core components for developing, maintaining, using and creating web-services. A core component is [Jetty](https://www.eclipse.org/jetty/documentation.php), which is used to develop and deploy servers. It is currently used in the version 10.0.12. [Jersey ](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/index.html)is a REST-framework and another core component. Additionally, [Jackson ](https://github.com/FasterXML/jackson)is used to read XML and JSON-files and [Swagger ](https://swagger.io/)for generating Client code. Following, we will discuss the structure of the three features in detail.

## Structure of the WS-Feature

The swagger feature consist of the de.enflexit.awb.ws.swagger1x and de.enflexit.awb.ws.swagger2x bundle. These bundles are libary bundles, they provide the AWB with the libaries for[ Swagger 1.x](https://swagger.io/docs/) and [Swagger 2.x](https://swagger.io/docs/specification/2-0/basic-structure/). Swagger is based on the [OpenAPi ](https://www.openapis.org/)standard, which builds the foundation for creating a YAML-file.

A similiar function has the _de.enflexit.awb.ws.jackson.feature_ and the _de.enflexit.awb.ws.swagger._feature. The jackson-feature of the AWB provides libaries to use the functionalities of [Jackson](https://github.com/FasterXML/jackson). It is a JSON-Parser for JAVA and is essential for REST in order to parse REST-messages. Since JSON is the standard format for messages in the REST-protocol.

Additionally, the _de.enflexit.awb.ws.jersey.feature_ is also a library bundle. It provides functionalities of [Jersey](https://eclipse-ee4j.github.io/jersey/). It is build and developed by the Eclipse Foundation and simplifies the development of REST-based web services. You can develop REST-clients and -services with Jersey, which is based on [Jakarta](https://eclipse-ee4j.github.io/jersey/). It extends the API of Jakarta, to simplify the development. You can find a guide for Jersey [here](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest31x/index.html).

| Bundle                           | Task                                                                                                                                                                                                        |
| -------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **de.enflexit.awb.ws.core**      | Start/Stops local Jetty server and registrates AwbWebServerService or AwbWebHandlerService (OSGI-Services) at the OSGI-Service registry.                                                                    |
| **de.enflexit.awb.ws.core.ui**   | Contains all user interfaces for the configuration of REST-clients and servers                                                                                                                              |
| **de.enflexit.awb.ws.client**    | Registrates AwbApiRegistrationServices (OSGI-Service) at the OSGi-Service registry. AwbApiRegistrationsServices are authentification services for clients, which enable the authentification with a server. |
| **de.enflexit.awb.ws.jetty**     | Library-bundle for Jetty                                                                                                                                                                                    |
| **de.enflexit.awb.ws.swagger1x** | Library-bundle for Swagger 1.X                                                                                                                                                                              |
| **de.enflexit.awb.ws.swagger2x** | Library-bundle for Swagger 2.X                                                                                                                                                                              |
| **de.enflexit.awb.ws.restapi**   | Implementation of the AWB-Webserver. This REST-API provides remote access of functionalities of AWB through REST                                                                                            |

However, the _de.enflexit.awb.ws.feature_ also includes bundles as plug-ins. Four of them are provided by the AWB: de.enflexit.awb.ws.core,  de.enflexit.awb.ws.core.ui, de.enflexit.awb.ws.client and de.enflexit.awb.ws.restapi bundle. Their tasks are described in detail in the table above.&#x20;

The de.enflexit.awb.ws.restapi is essential for developing servers with the AWB. You can find a guide on how to develop based on the AWB [here](providing-web-services.md). If you want to develop and modify a client, you can find an explanation [here](using-web-services.md).
