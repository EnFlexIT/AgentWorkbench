package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ContainerHolder
* @author ontology bean generator
* @version 2009/10/6, 22:51:49
*/
public class ContainerHolder implements Concept {

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
