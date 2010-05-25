package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents an object's position in a 2D environment
* Protege name: phy:Position
* @author ontology bean generator
* @version 2010/05/25, 12:32:32
*/
public class Phy_Position implements Concept {

   /**
   * The point's x coordinate
* Protege name: phy:x
   */
   private float phy_x;
   public void setPhy_x(float value) { 
    this.phy_x=value;
   }
   public float getPhy_x() {
     return this.phy_x;
   }

   /**
   * The pont's y coordinate
* Protege name: phy:y
   */
   private float phy_y;
   public void setPhy_y(float value) { 
    this.phy_y=value;
   }
   public float getPhy_y() {
     return this.phy_y;
   }

}
