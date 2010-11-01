package mas.environment.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents passive environment objects. PassiveObjects can't move on their own, but they can be moved by an ActiveObject.
* Protege name: PassiveObject
* @author ontology bean generator
* @version 2010/10/22, 21:15:52
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
