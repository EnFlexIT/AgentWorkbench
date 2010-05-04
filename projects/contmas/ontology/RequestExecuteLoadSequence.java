package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: RequestExecuteLoadSequence
* @author ontology bean generator
* @version 2010/05/3, 22:21:54
*/
public class RequestExecuteLoadSequence implements AgentAction {

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
