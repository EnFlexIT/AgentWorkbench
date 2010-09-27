package mas.display.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents an agent that can move in the environment
* Protege name: ActiveObject
* @author ontology bean generator
* @version 2010/05/24, 17:28:23
*/
public class AgentObject extends AbstractObject{ 

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
   * Points for collision checks
* Protege name: collisionPoints
   */
   private List collisionPoints = new ArrayList();
   public void addCollisionPoints(Object elem) { 
     List oldList = this.collisionPoints;
     collisionPoints.add(elem);
   }
   public boolean removeCollisionPoints(Object elem) {
     List oldList = this.collisionPoints;
     boolean result = collisionPoints.remove(elem);
     return result;
   }
   public void clearAllCollisionPoints() {
     List oldList = this.collisionPoints;
     collisionPoints.clear();
   }
   public Iterator getAllCollisionPoints() {return collisionPoints.iterator(); }
   public List getCollisionPoints() {return collisionPoints; }
   public void setCollisionPoints(List l) {collisionPoints = l; }

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

}
