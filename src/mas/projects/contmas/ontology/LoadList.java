package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * collection of transport orders for one long-time loading or unloading process
* Protege name: LoadList
* @author ontology bean generator
* @version 2009/09/15, 02:37:50
*/
public class LoadList implements Concept {

   /**
* Protege name: consists_of
   */
   private TransportOrderChain consists_of;
   public void setConsists_of(TransportOrderChain value) { 
    this.consists_of=value;
   }
   public TransportOrderChain getConsists_of() {
     return this.consists_of;
   }

}
