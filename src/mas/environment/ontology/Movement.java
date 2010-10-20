package mas.environment.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * An object's current movement.
* Protege name: Movement
* @author ontology bean generator
* @version 2010/10/20, 18:27:57
*/
public class Movement implements Concept {

//////////////////////////// User code
/**
    * This method calculates movement's speed
    * This method calculates the speed from the movement's x and y components using the Pythagorean theorem
    * @return The speed
    */
   public float getSpeed(){
	   return (float) Math.sqrt(xPosChange*xPosChange + yPosChange*yPosChange);
   }
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
