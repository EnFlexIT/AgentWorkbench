package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * step-like transport orders
* Protege name: TransportOrder
* @author ontology bean generator
* @version 2009/10/10, 17:32:32
*/
public class TransportOrder implements Concept {

   /**
* Protege name: ends_at
   */
   private Object ends_at;
   public void setEnds_at(Object value) { 
    this.ends_at=value;
   }
   public Object getEnds_at() {
     return this.ends_at;
   }

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
   private Object starts_at;
   public void setStarts_at(Object value) { 
    this.starts_at=value;
   }
   public Object getStarts_at() {
     return this.starts_at;
   }

}
