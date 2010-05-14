package contmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Holding
* @author ontology bean generator
* @version 2010/05/14, 17:12:06
*/
public class Holding extends TransportOrderChainState{ 

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
