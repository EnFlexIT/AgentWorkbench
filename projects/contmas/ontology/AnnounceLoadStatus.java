package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AnnounceLoadStatus
* @author ontology bean generator
* @version 2010/06/8, 22:03:06
*/
public class AnnounceLoadStatus implements AgentAction {

   /**
* Protege name: happend_at
   */
   private String happend_at;
   public void setHappend_at(String value) { 
    this.happend_at=value;
   }
   public String getHappend_at() {
     return this.happend_at;
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
