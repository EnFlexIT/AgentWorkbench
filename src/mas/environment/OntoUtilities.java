package mas.environment;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import sma.ontology.AbstractObject;
import sma.ontology.PlaygroundObject;
import sma.ontology.Position;
/**
 * Some constants, static utility methods etc.
 * @author Nils
 *
 */
public class OntoUtilities {
	/**
	 * Gets the objects center Position,
	 * i.e. the distance from the objects center to the root playgrounds top left corner
	 * @param object The object
	 * @return The object's center position
	 */
	public static Position getCenterPosition(AbstractObject object){
		Position centerPos = new Position();
		float x = object.getPosition().getX() + object.getSize().getWidth()/2;
		float y = object.getPosition().getY() + object.getSize().getHeight()/2;
		centerPos.setX(x);
		centerPos.setY(y);
		return centerPos;
	}
	/**
	 * Gets the objects local Position
	 * i.e. the distance from the objects top left corner to its parent's top left corner
	 * @param object The object
	 * @return The object's local position
	 */
	public static Position getLocalPosition(AbstractObject object){
		Position localPos = new Position();
		localPos.setX(object.getPosition().getX() - object.getParent().getPosition().getX());
		localPos.setY(object.getPosition().getY() - object.getParent().getPosition().getY());
		return localPos;
	}
	/**
	 * Gets the object's local center position
	 * i.e. the distance from the object's center to its parent's top left corner
	 * @param object The object
	 * @return The object's local center position
	 */
	public static Position getLocalCenterPosition(AbstractObject object){
		Position localPos = new Position();
		localPos.setX(object.getPosition().getX() - object.getParent().getPosition().getX());
		localPos.setY(object.getPosition().getY() - object.getParent().getPosition().getY());
		return localPos;
	}
	
	/**
	 * Sets the objects standard (= top left global) position according to the passed center (global) position  
	 * @param object The object
	 * @param centerPosition The center related position
	 */
	public static void setCenterPosition(AbstractObject object, Position centerPosition){
		float x = centerPosition.getX() - object.getSize().getWidth()/2;
		float y = centerPosition.getY() - object.getSize().getHeight()/2;
		
		object.getPosition().setX(x);
		object.getPosition().setY(y);
	}
	
	/**
	 * Sets the objects standard (= top left global) position according to the passed local (top left) position 
	 * @param object The object
	 * @param localPosition The local position
	 */
	public static void setLocalPosition(AbstractObject object, Position localPosition){
		float x = localPosition.getX() + object.getParent().getPosition().getX();
		float y = localPosition.getY() + object.getParent().getPosition().getY();
		
		object.getPosition().setX(x);
		object.getPosition().setY(y);
	}
	
	/**
	 * Sets the objects standard (= top left global) position according to the passed local center position
	 * @param object The object
	 * @param localCenterPosition The local center position
	 */
	public static void setLocalCenterPosition(AbstractObject object, Position localCenterPosition){
		setCenterPosition(object, localCenterPosition);		// Conversion center -> top left
		setLocalPosition(object, object.getPosition());		// Conversion local -> global
	}
	
	/**
	 * Checks if an environment object is inside a playgrounds area
	 * @param playground The playground 
	 * @param object The environment object
	 * @return True if the object is inside the playgrounds area, false otherwise
	 */
	public static boolean pgContains(PlaygroundObject playground, AbstractObject object){
		Area pgArea = new Area(new Rectangle2D.Float(
				playground.getPosition().getX(),
				playground.getPosition().getY(),
				playground.getSize().getWidth(),
				playground.getSize().getHeight())
		);
		
		
		Rectangle2D.Float objectRect = new Rectangle2D.Float(
				object.getPosition().getX(), 
				object.getPosition().getY(), 
				object.getSize().getWidth(), 
				object.getSize().getHeight()
		);
		
		return pgArea.contains(objectRect);
	}
}
