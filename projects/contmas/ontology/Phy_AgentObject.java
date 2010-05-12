package contmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents an agent that can move in the environment
* Protege name: phy:AgentObject
* @author ontology bean generator
* @version 2010/05/12, 12:12:40
*/
public class Phy_AgentObject extends Phy_AbstractObject{ 

   /**
   * Name of the agent class (subclass of jade.core.agent) this agent is an instance of
* Protege name: phy:agentClass
   */
   private String phy_agentClass;
   public void setPhy_agentClass(String value) { 
    this.phy_agentClass=value;
   }
   public String getPhy_agentClass() {
     return this.phy_agentClass;
   }

   /**
   * This agent's AID
* Protege name: phy:aid
   */
   private AID phy_aid;
   public void setPhy_aid(AID value) { 
    this.phy_aid=value;
   }
   public AID getPhy_aid() {
     return this.phy_aid;
   }

   /**
* Protege name: phy:currentSpeed
   */
   private Phy_Speed phy_currentSpeed;
   public void setPhy_currentSpeed(Phy_Speed value) { 
    this.phy_currentSpeed=value;
   }
   public Phy_Speed getPhy_currentSpeed() {
     return this.phy_currentSpeed;
   }

   /**
* Protege name: phy:maxSpeed
   */
   private Phy_Speed phy_maxSpeed;
   public void setPhy_maxSpeed(Phy_Speed value) { 
    this.phy_maxSpeed=value;
   }
   public Phy_Speed getPhy_maxSpeed() {
     return this.phy_maxSpeed;
   }

   /**
   * Points for collision checks
* Protege name: phy:collisionPoints
   */
   private List phy_collisionPoints = new ArrayList();
   public void addPhy_collisionPoints(Object elem) { 
     List oldList = this.phy_collisionPoints;
     phy_collisionPoints.add(elem);
   }
   public boolean removePhy_collisionPoints(Object elem) {
     List oldList = this.phy_collisionPoints;
     boolean result = phy_collisionPoints.remove(elem);
     return result;
   }
   public void clearAllPhy_collisionPoints() {
     List oldList = this.phy_collisionPoints;
     phy_collisionPoints.clear();
   }
   public Iterator getAllPhy_collisionPoints() {return phy_collisionPoints.iterator(); }
   public List getPhy_collisionPoints() {return phy_collisionPoints; }
   public void setPhy_collisionPoints(List l) {phy_collisionPoints = l; }

}
