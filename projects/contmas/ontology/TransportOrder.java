package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TransportOrder
* @author ontology bean generator
* @version 2010/05/7, 17:07:53
*/
public class TransportOrder implements Concept {

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

   /**
* Protege name: takes_until
   */
   private String takes_until;
   public void setTakes_until(String value) { 
    this.takes_until=value;
   }
   public String getTakes_until() {
     return this.takes_until;
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

}
