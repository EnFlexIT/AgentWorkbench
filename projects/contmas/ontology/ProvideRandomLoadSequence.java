package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ProvideRandomLoadSequence
* @author ontology bean generator
* @version 2010/05/25, 12:32:32
*/
public class ProvideRandomLoadSequence implements AgentAction {

   /**
* Protege name: next_step
   */
   private LoadList next_step;
   public void setNext_step(LoadList value) { 
    this.next_step=value;
   }
   public LoadList getNext_step() {
     return this.next_step;
   }

}
