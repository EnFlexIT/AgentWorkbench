package mas.environment.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Abstract superclass for objects in a physical 2D environment.
* Protege name: Physical2DObject
* @author ontology bean generator
* @version 2010/10/3, 17:33:18
*/
public class Physical2DObject implements Concept {

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

}
