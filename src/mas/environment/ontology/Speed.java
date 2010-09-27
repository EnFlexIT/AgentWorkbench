package mas.environment.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * An object's current Speed
* Protege name: Speed
* @author ontology bean generator
* @version 2010/09/21, 16:26:57
*/
public class Speed implements Concept {

   /**
   * The change of the objects x coordinate per second.
* Protege name: xPosChange
   */
   private float xPosChange;
   public void setXPosChange(float value) { 
    this.xPosChange=value;
   }
   public float getXPosChange() {
     return this.xPosChange;
   }

   /**
   * The y coordinate change per second
* Protege name: yPosChange
   */
   private float yPosChange;
   public void setYPosChange(float value) { 
    this.yPosChange=value;
   }
   public float getYPosChange() {
     return this.yPosChange;
   }

}
