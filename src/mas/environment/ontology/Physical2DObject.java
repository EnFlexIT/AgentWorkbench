package mas.environment.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Abstract superclass for objects in a physical 2D environment.
* Protege name: Physical2DObject
* @author ontology bean generator
* @version 2010/10/8, 20:12:51
*/
public class Physical2DObject implements Concept {

//////////////////////////// User code
/**
    * Checks if this Physical2DObject completely contains another one 
    * @param object The other Physical2DObject
    * @return True or flase
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
//	   java.awt.geom.Rectangle2D.Float myRect = new java.awt.geom.Rectangle2D.Float(getPosition().getXPos(), getPosition().getYPos(), getSize().getWidth(), getSize().getHeight());
//	   java.awt.geom.Rectangle2D.Float objectRect = new java.awt.geom.Rectangle2D.Float(object.getPosition().getXPos(), object.getPosition().getYPos(), object.getSize().getWidth(), object.getSize().getHeight());
//	   
//	   return myRect.intersects(objectRect);
	   
	   boolean result = false;
	   
	   float minX = position.getXPos()-size.getWidth()/2;
	   float maxX = position.getXPos()+size.getWidth()/2;
	   
	   float minY = position.getYPos()-size.getHeight()/2;
	   float maxY = position.getYPos()+size.getHeight()/2;
	   
	   float objMinX = object.getPosition().getXPos()-object.getSize().getWidth()/2;
	   float objMaxX = object.getPosition().getXPos()+object.getSize().getWidth()/2;
	   
	   float objMinY = object.getPosition().getYPos()-object.getSize().getHeight()/2;
	   float objMaxY = object.getPosition().getYPos()+object.getSize().getHeight()/2;
	   
	   result = (
			   minX > objMinX && minX < objMaxX && minY > objMinY && minY < objMaxY
			   || maxX > objMinX && maxX < objMaxX && minY > objMinY && minY < objMaxY
			   || minX > objMinX && minX < objMaxX && maxY > objMinY && maxY < objMaxY
			   || maxX > objMinX && maxX < objMaxX && maxY > objMinY && maxY < objMaxY
	   ) ;
	   
	   return result;
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

}
