package mas.environment;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import sma.ontology.AbstractObject;
import sma.ontology.PlaygroundObject;
import sma.ontology.Point;
/**
 * Some constants, static utility methods etc.
 * @author Nils
 *
 */
public class EnvironmentUtilities {
	/**
	 * The service type of the EnvironmentControllerAgent's environment provider functionality
	 */
	public static final String ENVIRONMENT_PROVIDER_SERVICE = "EnvironmentProvider";
	/**
	 * The conversation id used for environment requests
	 */
	public static final String ENVIRONMENT_REQUEST_CONV_ID = "EnvironmentRequest";
	/**
	 * Translates the objects position from TopLeft to Center coordinates
	 * @param object
	 * @return
	 */
	public static Point getCenterCoordinates(AbstractObject object){
		Point centerPos = new Point();
		float x = object.getPosition().getX() + object.getSize().getWidth()/2;
		float y = object.getPosition().getY() + object.getSize().getHeight()/2;
		centerPos.setX(x);
		centerPos.setY(y);
		return centerPos;
	}
	/**
	 * Translates the objects position from Center to TopLeft coordinates
	 * @param object
	 * @return
	 */
	public static Point getTopLeftCoordinates(AbstractObject object){
		Point topLeftPos = new Point();
		float x = object.getPosition().getX() - object.getSize().getWidth()/2;
		float y = object.getPosition().getY() - object.getSize().getHeight()/2;
		topLeftPos.setX(x);
		topLeftPos.setY(y);
		return topLeftPos;
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
