package agentgui.physical2Denvironment.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents passive environment objects. PassiveObjects can't move on their own, but they can be moved by an ActiveObject.
* Protege name: PassiveObject
* @author ontology bean generator
* @version 2010/11/17, 20:10:06
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
