package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ProvideOntologyRepresentation
* @author ontology bean generator
* @version 2010/06/9, 14:40:33
*/
public class ProvideOntologyRepresentation implements AgentAction {

   /**
* Protege name: according_ontrep
   */
   private ContainerHolder according_ontrep;
   public void setAccording_ontrep(ContainerHolder value) { 
    this.according_ontrep=value;
   }
   public ContainerHolder getAccording_ontrep() {
     return this.according_ontrep;
   }

}
