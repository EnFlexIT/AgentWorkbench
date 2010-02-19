package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ContainerHolder
* @author ontology bean generator
* @version 2009/10/20, 22:25:25
*/
public class ContainerHolder implements Concept {

   /**
* Protege name: administers
   */
   private LoadList administers;
   public void setAdministers(LoadList value) { 
    this.administers=value;
   }
   public LoadList getAdministers() {
     return this.administers;
   }

   /**
   * the domain, in which the containerholder is located
* Protege name: lives_in
   */
   private Domain lives_in;
   public void setLives_in(Domain value) { 
    this.lives_in=value;
   }
   public Domain getLives_in() {
     return this.lives_in;
   }

   /**
   * the storage area provided by the holder
* Protege name: contains
   */
   private BayMap contains;
   public void setContains(BayMap value) { 
    this.contains=value;
   }
   public BayMap getContains() {
     return this.contains;
   }

}
