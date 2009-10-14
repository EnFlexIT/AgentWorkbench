package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ProposeLoadOffer
* @author ontology bean generator
* @version 2009/10/13, 22:18:34
*/
public class ProposeLoadOffer implements AgentAction {

   /**
* Protege name: load_offer
   */
   private TransportOrderChain load_offer;
   public void setLoad_offer(TransportOrderChain value) { 
    this.load_offer=value;
   }
   public TransportOrderChain getLoad_offer() {
     return this.load_offer;
   }

}
