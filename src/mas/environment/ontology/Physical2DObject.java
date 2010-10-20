package mas.environment.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Abstract superclass for objects in a physical 2D environment.
* Protege name: Physical2DObject
* @author ontology bean generator
* @version 2010/10/20, 12:21:21
*/
public class Physical2DObject implements Concept {

//////////////////////////// User code
/**
    * Checks if this Physical2DObject completely contains another one 
    * @param object The other Physical2DObject
    * @return True or false
    */
   public boolean objectContains(Physical2DObject object){
	   java.awt.geom.Rectangle2D.Float myRect = new java.awt.geom.Rectangle2D.Float(getPosition().getXPos(), getPosition().getYPos(), getSize().getWidth(), getSize().getHeight());
	   java.awt.geom.Rectangle2D.Float objectRect = new java.awt.geom.Rectangle2D.Float(object.getPosition().getXPos(), object.getPosition().getYPos(), object.getSize().getWidth(), object.getSize().getHeight());
	   
	   return myRect.contains(objectRect);
   }
   
   /**
    * Checks if this Physical2DObject intersects another one
    * @param object The other Physical2DObject
    * @return True or false
    */
   public boolean objectIntersects(Physical2DObject object){
	   java.awt.geom.Rectangle2D.Float myRect = new java.awt.geom.Rectangle2D.Float(getPosition().getXPos(), getPosition().getYPos(), getSize().getWidth(), getSize().getHeight());
	   java.awt.geom.Rectangle2D.Float objectRect = new java.awt.geom.Rectangle2D.Float(object.getPosition().getXPos(), object.getPosition().getYPos(), object.getSize().getWidth(), object.getSize().getHeight());
	   
	   return myRect.intersects(objectRect);
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
