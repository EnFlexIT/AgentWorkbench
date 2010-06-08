package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TransportOrderChainState
* @author ontology bean generator
* @version 2010/06/8, 22:03:06
*/
public class TransportOrderChainState implements Concept {

   /**
* Protege name: at_address
   */
   private BlockAddress at_address;
   public void setAt_address(BlockAddress value) { 
    this.at_address=value;
   }
   public BlockAddress getAt_address() {
     return this.at_address;
   }

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
