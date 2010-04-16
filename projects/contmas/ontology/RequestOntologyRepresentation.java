package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: RequestOntologyRepresentation
* @author ontology bean generator
* @version 2010/04/16, 17:21:34
*/
public class RequestOntologyRepresentation implements AgentAction {

   /**
* Protege name: agent_in_question
   */
   private AID agent_in_question;
   public void setAgent_in_question(AID value) { 
    this.agent_in_question=value;
   }
   public AID getAgent_in_question() {
     return this.agent_in_question;
   }

}
