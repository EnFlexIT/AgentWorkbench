# Execution of the local projects pom4RepoBuild.xml 
**(Not recommended / required)**

The projects local pom-file basically serves as an **example** file for later use in different projects and 
should demonstrate how to use the *'p2-maven-plugin'*. Thus, it is not required to run the maven build, since
the required libraries will manually be placed in the projects '*/lib' directory.

  The specified *.pom file is configured to create a p2 update-site for all components required for JAXB.
It uses the maven *'p2-maven-plugin'* that enables to download non-OSGI jar files from the central maven 
repository, convert them into OSGI bundles and place them in a local p2 update-site.   

To do so, just execute the *pom.xml* with the argument

```
mvn clean p2:site
```
or, if you are using Eclipse, right click on the *pom.xm* and select *'Run As'* => *'Maven Build ...'* and 
type **clean p2:site** into the *Goals* section. 

For more information about the *'p2-maven-plugin'*, have a look at: <https://github.com/reficio/p2-maven-plugin>! 