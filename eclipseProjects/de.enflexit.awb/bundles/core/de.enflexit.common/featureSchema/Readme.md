# EOM - License Classes Generation
## Feature Classes Generation by using JAXB with MAVEN

To generate the license classes of the Energy Option Model, located in the package **energyLicense.model**  
and based on the schema file **04_EOM_LicenseStructure.xsd** and the XJB bindings define in file  
**04_JAXB_LicenseStructureGeneration.xjb**, just run the local MAVEN pom file with the argument **generate-sources**.
  
In case that the generation process notifies: **"No changes detected in schema or binding files - skipping JAXB generation".**,  
run the pom file with **clean**. Afterwards, re-run the pom with the above argument. 

In Eclipse, the easiest way is to use the according context menu entries under **Run As ...**! 