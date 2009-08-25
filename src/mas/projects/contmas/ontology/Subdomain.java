package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Subdomain
* @author ontology bean generator
* @version 2009/08/25, 14:09:13
*/
public class Subdomain implements Concept {

   /**
* Protege name: lies_in
   */
   private Domain lies_in;
   public void setLies_in(Domain value) { 
    this.lies_in=value;
   }
   public Domain getLies_in() {
     return this.lies_in;
   }

}
