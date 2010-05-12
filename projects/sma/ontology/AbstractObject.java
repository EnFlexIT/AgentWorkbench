package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * 
* Protege name: AbstractObject
* @author ontology bean generator
* @version 2010/05/12, 16:14:24
*/
public class AbstractObject implements Concept {

   /**
   * Unique id to identify this object
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
   * The playground containing this EnvironmentObject
* Protege name: parent
   */
   private PlaygroundObject parent;
   public void setParent(PlaygroundObject value) { 
    this.parent=value;
   }
   public PlaygroundObject getParent() {
     return this.parent;
   }

   /**
   * This EnvironmentObject's size
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
   * This EnvironmentObject's center position
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
