package sma.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents an agent that can move in the environment
* Protege name: AgentObject
* @author ontology bean generator
* @version 2010/03/25, 19:30:25
*/
public class AgentObject extends AbstractObject{ 

   /**
* Protege name: currentSpeed
   */
   private Speed currentSpeed;
   public void setCurrentSpeed(Speed value) { 
    this.currentSpeed=value;
   }
   public Speed getCurrentSpeed() {
     return this.currentSpeed;
   }

   /**
   * Name of the agent class (subclass of jade.core.agent) this agent is an instance of
* Protege name: agentClass
   */
   private String agentClass;
   public void setAgentClass(String value) { 
    this.agentClass=value;
   }
   public String getAgentClass() {
     return this.agentClass;
   }

   /**
* Protege name: maxSpeed
   */
   private Speed maxSpeed;
   public void setMaxSpeed(Speed value) { 
    this.maxSpeed=value;
   }
   public Speed getMaxSpeed() {
     return this.maxSpeed;
   }

   /**
   * This agent's AID
* Protege name: aid
   */
   private AID aid;
   public void setAid(AID value) { 
    this.aid=value;
   }
   public AID getAid() {
     return this.aid;
   }

}
