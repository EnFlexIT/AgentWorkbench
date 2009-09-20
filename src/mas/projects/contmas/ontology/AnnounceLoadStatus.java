package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AnnounceLoadStatus
* @author ontology bean generator
* @version 2009/09/15, 23:06:53
*/
public class AnnounceLoadStatus implements AgentAction {

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
