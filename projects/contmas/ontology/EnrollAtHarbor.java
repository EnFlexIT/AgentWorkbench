package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: EnrollAtHarbor
* @author ontology bean generator
* @version 2010/05/6, 12:41:04
*/
public class EnrollAtHarbor implements AgentAction {

   /**
* Protege name: required_turnover_capacity
   */
   private LoadList required_turnover_capacity;
   public void setRequired_turnover_capacity(LoadList value) { 
    this.required_turnover_capacity=value;
   }
   public LoadList getRequired_turnover_capacity() {
     return this.required_turnover_capacity;
   }

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
