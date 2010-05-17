package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TransportOrderChainState
* @author ontology bean generator
* @version 2010/05/17, 22:08:19
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

}
