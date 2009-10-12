package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ProposeLoadOffer
* @author ontology bean generator
* @version 2009/10/10, 17:32:32
*/
public class ProposeLoadOffer implements AgentAction {

   /**
* Protege name: load_offer
   */
   private TransportOrder load_offer;
   public void setLoad_offer(TransportOrder value) { 
    this.load_offer=value;
   }
   public TransportOrder getLoad_offer() {
     return this.load_offer;
   }

}
