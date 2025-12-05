## Example project for a REST API-Client

In this bundle, an example for the creation of a REST API-Client is given.

How it works:  
- The actual REST-API is specified in the yaml-file under *./xCodegen/RestApi.yaml* and represents an OpenAPI specification of the desired REST structure.
- Beside this yaml-file, the required Maven pom-file can be found, where some important parameter can be set, such as the desired base package name and other.
- This Maven POM-file needs to be executed, e.g. by using the Eclipse context menu *Run As* => *Maven generate-sources* with the selected file
- After an successful execution, the required java source files can be found in the sub directory structure of the director *./xCodgen/target/**.
- The required sources out of the generated files are linked through the OSGI-bundle as additional source directories (see *Properties* => *Java Build Path* => *Source*).
- The locally configured OSGI - bundle structure, defined with the dependencies in the MANIFEST.MF, serves as a kind of runtime- or library-environment for a generated source code.  
  It provides an REST API-client by providing the required classes for the generated *java - jersey3* structure (see. pom configuration at *<generatorName>java</generatorName>* and *<library>jersey3</library>*) 

In fact, and in contrast to a server-structure, no further customization is required to use a REST API-client. Everything can be used 'as is'.  

A simple example for the usage can be found in the class *de.enflexit.awb.samples.ws.restapi.client.RestAskingAgent*.  
In this example, the entry class to access a server API as a client can be found and is called *AdminsApi*.  
For this, a so-called *ApiClient* is specified that enables to authenticate the client and grant access to the API.  
Further on, to put or get information to or from the server-site, the AdminsApi provides further access methods, such as *infoGet()* and other.


