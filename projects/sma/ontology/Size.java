package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents an objects size in a 2D environment
* Protege name: Size
* @author ontology bean generator
* @version 2010/04/7, 20:19:12
*/
public class Size implements Concept {

   /**
   * The object's height
* Protege name: height
   */
   private float height;
   public void setHeight(float value) { 
    this.height=value;
   }
   public float getHeight() {
     return this.height;
   }

   /**
   * The object's width
* Protege name: width
   */
   private float width;
   public void setWidth(float value) { 
    this.width=value;
   }
   public float getWidth() {
     return this.width;
   }

}
