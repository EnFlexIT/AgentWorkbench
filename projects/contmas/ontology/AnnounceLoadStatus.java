package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AnnounceLoadStatus
* @author ontology bean generator
* @version 2010/05/25, 12:32:32
*/
public class AnnounceLoadStatus implements AgentAction {

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
