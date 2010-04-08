package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Point
* @author ontology bean generator
* @version 2010/04/7, 20:19:12
*/
public class Point implements Concept {

   /**
   * The point's y coordinate
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
