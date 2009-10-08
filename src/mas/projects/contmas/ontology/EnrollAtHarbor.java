package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Register with the HarborMaster and get a berthing place
* Protege name: EnrollAtHarbor
* @author ontology bean generator
* @version 2009/10/6, 22:51:49
*/
public class EnrollAtHarbor implements AgentAction {

   /**
* Protege name: ship_length
   */
   private float ship_length;
   public void setShip_length(float value) { 
    this.ship_length=value;
   }
   public float getShip_length() {
     return this.ship_length;
   }

}
