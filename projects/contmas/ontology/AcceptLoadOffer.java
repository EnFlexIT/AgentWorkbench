package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AcceptLoadOffer
* @author ontology bean generator
* @version 2010/05/25, 11:52:34
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
