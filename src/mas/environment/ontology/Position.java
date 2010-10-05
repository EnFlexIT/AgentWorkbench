package mas.environment.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * A 2D object's position
* Protege name: Position
* @author ontology bean generator
* @version 2010/10/4, 16:36:15
*/
public class Position implements Concept {

   /**
   * The objects x coordinate
* Protege name: xPos
   */
   private float xPos;
   public void setXPos(float value) { 
    this.xPos=value;
   }
   public float getXPos() {
     return this.xPos;
   }

   /**
   * The environments y coordinate
* Protege name: yPos
   */
   private float yPos;
   public void setYPos(float value) { 
    this.yPos=value;
   }
   public float getYPos() {
     return this.yPos;
   }

}
