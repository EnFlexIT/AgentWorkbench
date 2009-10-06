package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * step-like transport orders
* Protege name: TransportOrder
* @author ontology bean generator
* @version 2009/10/5, 23:56:07
*/
public class TransportOrder implements Concept {

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

   /**
* Protege name: links
   */
   private TransportOrderChain links;
   public void setLinks(TransportOrderChain value) { 
    this.links=value;
   }
   public TransportOrderChain getLinks() {
     return this.links;
   }

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

}
