package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * A project's environment definition
* Protege name: Environment
* @author ontology bean generator
* @version 2010/03/4, 21:33:23
*/
public class Environment implements Concept {

   /**
   * AID of the EnvironmentControllerAgent controlling this environment
* Protege name: masterAgent
   */
   private AID masterAgent;
   public void setMasterAgent(AID value) { 
    this.masterAgent=value;
   }
   public AID getMasterAgent() {
     return this.masterAgent;
   }

   /**
   * Root of the project's environment representation, containing all it's EnvironmentObjects
* Protege name: rootPlayground
   */
   private String rootPlayground;
   public void setRootPlayground(String value) { 
    this.rootPlayground=value;
   }
   public String getRootPlayground() {
     return this.rootPlayground;
   }

   /**
   * The SVG document used to visualize the project
* Protege name: svgFile
   */
   private String svgFile;
   public void setSvgFile(String value) { 
    this.svgFile=value;
   }
   public String getSvgFile() {
     return this.svgFile;
   }

   /**
   * The project's name
* Protege name: projectName
   */
   private String projectName;
   public void setProjectName(String value) { 
    this.projectName=value;
   }
   public String getProjectName() {
     return this.projectName;
   }

}
