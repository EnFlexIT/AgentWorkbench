package mas.environment.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents active objects, i.e. environment objects modeling agents. ActiveObjects can move on their own.
* Protege name: ActiveObject
* @author ontology bean generator
* @version 2010/10/22, 21:15:52
*/
public class ActiveObject extends Physical2DObject{ 

   /**
   * The ActiveObject's current movement.
* Protege name: movement
   */
   private Movement movement;
   public void setMovement(Movement value) { 
    this.movement=value;
   }
   public Movement getMovement() {
     return this.movement;
   }

   /**
   * Passive objects currently controlled by this active object
* Protege name: payload
   */
   private List payload = new ArrayList();
   public void addPayload(PassiveObject elem) { 
     List oldList = this.payload;
     payload.add(elem);
   }
   public boolean removePayload(PassiveObject elem) {
     List oldList = this.payload;
     boolean result = payload.remove(elem);
     return result;
   }
   public void clearAllPayload() {
     List oldList = this.payload;
     payload.clear();
   }
   public Iterator getAllPayload() {return payload.iterator(); }
   public List getPayload() {return payload; }
   public void setPayload(List l) {payload = l; }

   /**
   * The active object's maximum speed
* Protege name: maxSpeed
   */
   private float maxSpeed;
   public void setMaxSpeed(float value) { 
    this.maxSpeed=value;
   }
   public float getMaxSpeed() {
     return this.maxSpeed;
   }

}
