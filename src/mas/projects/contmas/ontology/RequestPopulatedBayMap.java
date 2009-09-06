package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Request a randomly populated bay map at the RandomGeneratorAgent for development purposes
* Protege name: RequestPopulatedBayMap
* @author ontology bean generator
* @version 2009/09/6, 17:44:13
*/
public class RequestPopulatedBayMap implements AgentAction {

   /**
   * the bay map to be populated
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
