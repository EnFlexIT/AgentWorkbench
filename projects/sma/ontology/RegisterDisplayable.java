package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Registration request from a Displayable Agent
* Protege name: RegisterDisplayable
* @author ontology bean generator
* @version 2010/03/16, 20:55:13
*/
public class RegisterDisplayable implements AgentAction {

   /**
   * The displayable agent requesting registration
* Protege name: dispalyableAgent
   */
   private AgentObject dispalyableAgent;
   public void setDispalyableAgent(AgentObject value) { 
    this.dispalyableAgent=value;
   }
   public AgentObject getDispalyableAgent() {
     return this.dispalyableAgent;
   }

}
