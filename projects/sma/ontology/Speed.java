package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents the speed of an object moving in a 2D environment
* Protege name: Speed
* @author ontology bean generator
* @version 2010/03/16, 20:55:13
*/
public class Speed implements Concept {

   /**
   * Object's position change in y direction per step
* Protege name: ySpeed
   */
   private int ySpeed;
   public void setYSpeed(int value) { 
    this.ySpeed=value;
   }
   public int getYSpeed() {
     return this.ySpeed;
   }

   /**
   * Object's position change in x direction per step
* Protege name: xSpeed
   */
   private int xSpeed;
   public void setXSpeed(int value) { 
    this.xSpeed=value;
   }
   public int getXSpeed() {
     return this.xSpeed;
   }

}
