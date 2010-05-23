package mas.display.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents an object's position in a 2D environment
* Protege name: Position
* @author ontology bean generator
* @version 2010/05/23, 13:16:47
*/
public class Position implements Concept {

   /**
   * The pont's y coordinate
* Protege name: y
   */
   private float y;
   public void setY(float value) { 
    this.y=value;
   }
   public float getY() {
     return this.y;
   }

   /**
   * The point's x coordinate
* Protege name: x
   */
   private float x;
   public void setX(float value) { 
    this.x=value;
   }
   public float getX() {
     return this.x;
   }

}
