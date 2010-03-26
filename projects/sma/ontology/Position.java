package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents an object's position in a 2D environment
* Protege name: Position
* @author ontology bean generator
* @version 2010/03/25, 19:30:25
*/
public class Position implements Concept {

   /**
   * The object's x coordinate
* Protege name: xPos
   */
   private int xPos;
   public void setXPos(int value) { 
    this.xPos=value;
   }
   public int getXPos() {
     return this.xPos;
   }

   /**
   * The object's y coordinate
* Protege name: yPos
   */
   private int yPos;
   public void setYPos(int value) { 
    this.yPos=value;
   }
   public int getYPos() {
     return this.yPos;
   }

}
