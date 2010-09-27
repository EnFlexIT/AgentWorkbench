package mas.environment.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents a physical 2D environment
* Protege name: Physical2DEnvironment
* @author ontology bean generator
* @version 2010/09/21, 16:26:57
*/
public class Physical2DEnvironment implements Concept {

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

}
