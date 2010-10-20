package mas.environment.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents passive objects in a physical 2D environment. Passive objects can't move on their own, but can be moved by an agent.
* Protege name: PassiveObject
* @author ontology bean generator
* @version 2010/10/20, 18:27:58
*/
public class PassiveObject extends Physical2DObject{ 

   /**
   * The ID of the ActiveObject controlling this object.
* Protege name: controllingObjectID
   */
   private String controllingObjectID;
   public void setControllingObjectID(String value) { 
    this.controllingObjectID=value;
   }
   public String getControllingObjectID() {
     return this.controllingObjectID;
   }

}
