package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AnnounceLoadStatus
* @author ontology bean generator
* @version 2010/04/20, 14:23:52
*/
public class AnnounceLoadStatus implements AgentAction {

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

   /**
* Protege name: load_status
   */
   private String load_status;
   public void setLoad_status(String value) { 
    this.load_status=value;
   }
   public String getLoad_status() {
     return this.load_status;
   }

}
