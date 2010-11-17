package agentgui.physical2Denvironment.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents the whole environment model.
* Protege name: Physical2DEnvironment
* @author ontology bean generator
* @version 2010/11/17, 20:10:06
*/
public class Physical2DEnvironment implements Concept {

   /**
   * The physical 2D environment's scale
* Protege name: scale
   */
   private Scale scale;
   public void setScale(Scale value) { 
    this.scale=value;
   }
   public Scale getScale() {
     return this.scale;
   }

   /**
* Protege name: projectName
   */
   private String projectName;
   public void setProjectName(String value) { 
    this.projectName=value;
   }
   public String getProjectName() {
     return this.projectName;
   }

   /**
   * The main area of the physical 2D environment, containing all sub-environments and objects
* Protege name: rootPlayground
   */
   private PlaygroundObject rootPlayground;
   public void setRootPlayground(PlaygroundObject value) { 
    this.rootPlayground=value;
   }
   public PlaygroundObject getRootPlayground() {
     return this.rootPlayground;
   }

}
