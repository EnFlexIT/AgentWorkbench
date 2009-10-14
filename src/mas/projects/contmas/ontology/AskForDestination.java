package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AskForDestination
* @author ontology bean generator
* @version 2009/10/13, 22:18:34
*/
public class AskForDestination implements AgentAction {

   /**
   * transport order of object which is to be stored
* Protege name: to_be_stored
   */
   private TransportOrderChain to_be_stored;
   public void setTo_be_stored(TransportOrderChain value) { 
    this.to_be_stored=value;
   }
   public TransportOrderChain getTo_be_stored() {
     return this.to_be_stored;
   }

}
