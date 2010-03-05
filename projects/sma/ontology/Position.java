package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents an object's position in a 2D environment
* Protege name: Position
* @author ontology bean generator
* @version 2010/03/4, 21:33:23
*/
public class Position implements Concept {

   /**
   * The object's y coordinate
* Protege name: yPos
   */
   private String yPos;
   public void setYPos(String value) { 
    this.yPos=value;
   }
   public String getYPos() {
     return this.yPos;
   }

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

}
