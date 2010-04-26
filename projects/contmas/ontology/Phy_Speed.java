package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents the speed of an object moving in a 2D environment
* Protege name: phy:Speed
* @author ontology bean generator
* @version 2010/04/22, 16:03:29
*/
public class Phy_Speed implements Concept {

   /**
   * // The speed (currently px/step)
* Protege name: phy:speed
   */
   private float phy_speed;
   public void setPhy_speed(float value) { 
    this.phy_speed=value;
   }
   public float getPhy_speed() {
     return this.phy_speed;
   }

}
