package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AcceptLoadOffer
* @author ontology bean generator
* @version 2010/04/22, 16:03:29
*/
public class AcceptLoadOffer implements AgentAction {

   /**
* Protege name: corresponds_to
   */
   private TransportOrderChain corresponds_to;
   public void setCorresponds_to(TransportOrderChain value) { 
    this.corresponds_to=value;
   }
   public TransportOrderChain getCorresponds_to() {
     return this.corresponds_to;
   }

}
