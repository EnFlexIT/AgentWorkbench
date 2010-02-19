package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * step-like transport orders
* Protege name: TransportOrder
* @author ontology bean generator
* @version 2009/10/20, 22:25:25
*/
public class TransportOrder implements Concept {

   /**
   * required time to perform
* Protege name: takes
   */
   private float takes;
   public void setTakes(float value) { 
    this.takes=value;
   }
   public float getTakes() {
     return this.takes;
   }

   /**
* Protege name: starts_at
   */
   private Designator starts_at;
   public void setStarts_at(Designator value) { 
    this.starts_at=value;
   }
   public Designator getStarts_at() {
     return this.starts_at;
   }

   /**
* Protege name: ends_at
   */
   private Designator ends_at;
   public void setEnds_at(Designator value) { 
    this.ends_at=value;
   }
   public Designator getEnds_at() {
     return this.ends_at;
   }

}
