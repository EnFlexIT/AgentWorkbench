## Example project for a Server-Site REST - API

In this bundle, an example for the creation of a server-site REST-API is given.

How it works:  
- The actual REST-API is specified in the yaml-file under *./xCodegen/RestApi.yaml* and represents an OpenAPI specification of the desired REST structure.
- Beside this yaml-file, the required Maven pom-file can be found, where some important parameter can be set, such as the desired base package name and other.
- This Maven POM-file needs to be executed, e.g. by using the Eclipse context menu *Run As* => *Maven generate-sources* with the selected file
- After an successful execution, the required java source files can be found in the sub directory structure of the director *./xCodgen/target/**.
- The required sources out of the generated files are linked through the OSGI-bundle as additional source directories (see *Properties* => *Java Build Path* => *Source*).
- The locally configured OSGI - bundle structure, defined with the dependencies in the MANIFEST.MF, serves as a kind of runtime- or library-environment for a generated REST-API.
  It connects the AWB server runtime and the API-bundle, by providing the required libraries for the generated *jaxrs-jersey* file structure (see. pom configuration at *<generatorName>jaxrs-jersey</generatorName>*) 

To finally customize and use the generated API-structure with own implementations of end-point reactions, some further steps are required.  
These basically correspond to the number of end-points, a REST-API specifies and needs to be adjusted in several files. The affected files in general are:

- **./META-INF/MANIFEST.MF:** links the OSGI-service description that is the actual line 'Service-Component: OSGI-INF/awbWebHandlerApiService.xml'
- **./OSGI_INF/awbWebHandlerApiService**: description file for the OSGI-service. Links the actual implementation that is the *AwbRestApiServiceHandler* (see next point)
- **de.enflexit.awb.samples.ws.restapi.server.AwbRestApiServiceHandler**: This class is the actual implementation of the *AwbWebHandlerService* and returns the Handler or ServletContextHandler respectively after an initial configuration.
- **de.enflexit.awb.samples.ws.restapi.server.RestApiConfiguration:** Specifies necessary parameter as for example the classes to use as REST end-points, the return parameter type or the base path of the API on the web server
- **de.enflexit.awb.samples.ws.restapi.server.ServletInitParameter:** The mapping for specific generated API end-points to our individually implemented classes.
- **de.enflexit.awb.samples.ws.restapi.server.impl**: In this package, the individual implementation Java classes or files can be found.

For as single end-point implementation, such as for the implementation of the */info* end-point (see RestApi.yaml), the following steps are to be done:
- Check for the abstract super class of that end-point: Here, it is the class *de.enflexit.awb.samples.ws.restapi.server.gen.InfoApiService.java*
- Check for the default implementation for the end-point: In the same package as the above class, this is given with the class *InfoApi.java*.  
  This class checks if the servlet init parameter (see above class *ServletInitParameter*) contain a class name for an *InfoApiService* by checking with the key *InfoApi.implementation*  
  If an implementation class can be found, it will be initialized, while the REST request will be forwarded to that new instance.
- Please, keep in mind that: 
    - a default implementation, as for example the class **InfoApi**, needs to be specified in the class **RestApiConfiguration**, while 
    - your individual implementation class **InfoApiImpl** needs to be specified within the **ServletInitParameter** by registering the specific class name


