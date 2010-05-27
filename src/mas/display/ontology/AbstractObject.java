package mas.display.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * 
* Protege name: AbstractObject
* @author ontology bean generator
* @version 2010/05/24, 17:28:23
*/
public class AbstractObject implements Concept {

//////////////////////////// User code
/** 	 * Gets the objects center Position, 	 * i.e. the distance from the objects center to the root playgrounds top left corner 	 * @return The object's center position 	 */ 	public Position getCenterPosition(){ 		Position centerPos = new Position(); 		float x = getPosition().getX() + getSize().getWidth()/2; 		float y = getPosition().getY() + getSize().getHeight()/2; 		centerPos.setX(x); 		centerPos.setY(y); 		return centerPos; 	} 	/** 	 * Gets the objects local Position 	 * i.e. the distance from the objects top left corner to its parent's top left corner 	 * @return The object's local position 	 */ 	public Position getLocalPosition(){ 		Position localPos = new Position(); 		localPos.setX(getPosition().getX() - getParent().getPosition().getX()); 		localPos.setY(getPosition().getY() - getParent().getPosition().getY()); 		return localPos; 	} 	/** 	 * Gets the object's local center position 	 * i.e. the distance from the object's center to its parent's top left corner 	 * @return The object's local center position 	 */ 	public Position getLocalCenterPosition(){ 		Position localPos = new Position(); 		localPos.setX(getPosition().getX() - getParent().getPosition().getX()); 		localPos.setY(getPosition().getY() - getParent().getPosition().getY()); 		return localPos; 	} 	 	/** 	 * Sets the objects standard (= top left global) position according to the passed center (global) position   	 * @param centerPosition The center related position 	 */ 	public void setCenterPosition(Position centerPosition){ 		float x = centerPosition.getX() - getSize().getWidth()/2; 		float y = centerPosition.getY() - getSize().getHeight()/2; 		 		getPosition().setX(x); 		getPosition().setY(y); 	} 	 	/** 	 * Sets the objects standard (= top left global) position according to the passed local (top left) position  	 * @param localPosition The local position 	 */ 	public void setLocalPosition(Position localPosition){ 		float x = localPosition.getX() + getParent().getPosition().getX(); 		float y = localPosition.getY() + getParent().getPosition().getY(); 		 		getPosition().setX(x); 		getPosition().setY(y); 	} 	 	/** 	 * Sets the objects standard (= top left global) position according to the passed local center position 	 * @param localCenterPosition The local center position 	 */ 	public void setLocalCenterPosition(Position localCenterPosition){ 		setCenterPosition(localCenterPosition);		/* Conversion center -> top left  */ 		setLocalPosition(getPosition());		/* Conversion local -> global */	}
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
