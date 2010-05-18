package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: RequestPopulatedBayMap
* @author ontology bean generator
* @version 2010/05/18, 12:29:37
*/
public class RequestPopulatedBayMap implements AgentAction {

   /**
* Protege name: populate_on
   */
   private BayMap populate_on;
   public void setPopulate_on(BayMap value) { 
    this.populate_on=value;
   }
   public BayMap getPopulate_on() {
     return this.populate_on;
   }

}
