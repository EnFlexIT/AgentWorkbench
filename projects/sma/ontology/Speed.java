package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents the speed of an object moving in a 2D environment
* Protege name: Speed
* @author ontology bean generator
* @version 2010/05/16, 13:04:47
*/
public class Speed implements Concept {

   /**
   * // The speed (currently px/step)
* Protege name: speed
   */
   private float speed;
   public void setSpeed(float value) { 
    this.speed=value;
   }
   public float getSpeed() {
     return this.speed;
   }

}
