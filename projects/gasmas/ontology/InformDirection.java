package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: InformDirection
* @author ontology bean generator
* @version 2012/05/21, 12:49:01
*/
public class InformDirection implements AgentAction {

   /**
* Protege name: PropPoint
   */
   private PropagationPoint propPoint;
   public void setPropPoint(PropagationPoint value) { 
    this.propPoint=value;
   }
   public PropagationPoint getPropPoint() {
     return this.propPoint;
   }

   /**
* Protege name: FreeSlot
   */
   private Object freeSlot;
   public void setFreeSlot(Object value) { 
    this.freeSlot=value;
   }
   public Object getFreeSlot() {
     return this.freeSlot;
   }

}
