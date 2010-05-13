package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: RequestBlockAddress
* @author ontology bean generator
* @version 2010/05/13, 15:13:19
*/
public class RequestBlockAddress implements AgentAction {

   /**
* Protege name: provides
   */
   private BayMap provides;
   public void setProvides(BayMap value) { 
    this.provides=value;
   }
   public BayMap getProvides() {
     return this.provides;
   }

   /**
* Protege name: subjected_toc
   */
   private TransportOrderChain subjected_toc;
   public void setSubjected_toc(TransportOrderChain value) { 
    this.subjected_toc=value;
   }
   public TransportOrderChain getSubjected_toc() {
     return this.subjected_toc;
   }

}
