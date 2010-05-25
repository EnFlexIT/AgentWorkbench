package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: RouteInformation
* @author ontology bean generator
* @version 2010/05/25, 12:32:32
*/
public class RouteInformation implements Concept {

   /**
* Protege name: target
   */
   private Domain target;
   public void setTarget(Domain value) { 
    this.target=value;
   }
   public Domain getTarget() {
     return this.target;
   }

   /**
* Protege name: next_hop
   */
   private Domain next_hop;
   public void setNext_hop(Domain value) { 
    this.next_hop=value;
   }
   public Domain getNext_hop() {
     return this.next_hop;
   }

}
