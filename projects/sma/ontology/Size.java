package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents an objects size in a 2D environment
* Protege name: Size
* @author ontology bean generator
* @version 2010/03/16, 20:55:13
*/
public class Size implements Concept {

   /**
   * The object's height
* Protege name: height
   */
   private int height;
   public void setHeight(int value) { 
    this.height=value;
   }
   public int getHeight() {
     return this.height;
   }

   /**
   * The object's width
* Protege name: width
   */
   private int width;
   public void setWidth(int value) { 
    this.width=value;
   }
   public int getWidth() {
     return this.width;
   }

}
