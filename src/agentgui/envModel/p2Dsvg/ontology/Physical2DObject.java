package agentgui.envModel.p2Dsvg.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Abstract super class for physical objects that are part of the environment model.
* Protege name: Physical2DObject
* @author ontology bean generator
* @version 2010/11/17, 20:10:06
*/
public class Physical2DObject implements Concept {

//////////////////////////// User code
/**
    * Checks if this Physical2DObject completely contains another one 
    * @param object The other Physical2DObject
    * @return True or false
    */
   public boolean objectContains(Physical2DObject object){
	   // Calculate top left coordinates from center coordinates and size
	   float myTopLeftX = getPosition().getXPos() - getSize().getWidth() / 2;
	   float myTopLeftY = getPosition().getYPos() - getSize().getHeight() / 2;
	   
	   float objectTopLeftX = object.getPosition().getXPos() - object.getSize().getWidth() / 2;
	   float objectTopLeftY = object.getPosition().getYPos() - object.getSize().getHeight() / 2;
	   
	   // Create Rectangle2D representations
	   java.awt.geom.Rectangle2D.Float myRect = new java.awt.geom.Rectangle2D.Float(myTopLeftX, myTopLeftY, getSize().getWidth(), getSize().getHeight());
	   java.awt.geom.Rectangle2D.Float objectRect = new java.awt.geom.Rectangle2D.Float(objectTopLeftX, objectTopLeftY, object.getSize().getWidth(), object.getSize().getHeight());
	   
	   // Check for containment
	   return myRect.contains(objectRect);
   }
   
   /**
    * Checks if this Physical2DObject intersects another one
    * @param object The other Physical2DObject
    * @return True or false
    */
   public boolean objectIntersects(Physical2DObject object){
	// Calculate top left coordinates from center coordinates and size
	   float myTopLeftX = getPosition().getXPos() - getSize().getWidth() / 2;
	   float myTopLeftY = getPosition().getYPos() - getSize().getHeight() / 2;
	   
	   float objectTopLeftX = object.getPosition().getXPos() - object.getSize().getWidth() / 2;
	   float objectTopLeftY = object.getPosition().getYPos() - object.getSize().getHeight() / 2;
	   
	   // Create Rectangle2D representations
	   java.awt.geom.Rectangle2D.Float myRect = new java.awt.geom.Rectangle2D.Float(myTopLeftX, myTopLeftY, getSize().getWidth(), getSize().getHeight());
	   java.awt.geom.Rectangle2D.Float objectRect = new java.awt.geom.Rectangle2D.Float(objectTopLeftX, objectTopLeftY, object.getSize().getWidth(), object.getSize().getHeight());
	   
	   // Check for intersection
	   return myRect.intersects(objectRect);
   }
   /**
   * The object's size
* Protege name: size
   */
   private Size size;
   public void setSize(Size value) { 
    this.size=value;
   }
   public Size getSize() {
     return this.size;
   }

   /**
   * The ID of the PlaygroundObject this Physical2DObject lives in.
* Protege name: parentPlaygroundID
   */
   private String parentPlaygroundID;
   public void setParentPlaygroundID(String value) { 
    this.parentPlaygroundID=value;
   }
   public String getParentPlaygroundID() {
     return this.parentPlaygroundID;
   }

   /**
   * ID used to identify the object
* Protege name: id
   */
   private String id;
   public void setId(String value) { 
    this.id=value;
   }
   public String getId() {
     return this.id;
   }

   /**
   * The object's position
* Protege name: position
   */
   private Position position;
   public void setPosition(Position value) { 
    this.position=value;
   }
   public Position getPosition() {
     return this.position;
   }

}
