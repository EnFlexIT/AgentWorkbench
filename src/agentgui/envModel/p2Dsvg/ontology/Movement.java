package agentgui.envModel.p2Dsvg.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class models an ActiveObject's movement by defining the change of its x and y coordinate per second.
* Protege name: Movement
* @author ontology bean generator
* @version 2011/01/27, 16:56:56
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

}
