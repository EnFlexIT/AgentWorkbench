package mas.environment.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents passive objects in a physical 2D environment. Passive objects can't move on their own, but can be moved by an agent.
* Protege name: PassiveObject
* @author ontology bean generator
* @version 2010/10/8, 20:12:51
*/
public class PassiveObject extends Physical2DObject{ 

   /**
   * The ActiveObject instance changing the passive object's position
* Protege name: controlledBy
   */
   private ActiveObject controlledBy;
   public void setControlledBy(ActiveObject value) { 
    this.controlledBy=value;
   }
   public ActiveObject getControlledBy() {
     return this.controlledBy;
   }

}
