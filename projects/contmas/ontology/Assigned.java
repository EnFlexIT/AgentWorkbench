package contmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Assigned
* @author ontology bean generator
* @version 2010/05/14, 17:12:06
*/
public class Assigned extends Holding{ 

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
