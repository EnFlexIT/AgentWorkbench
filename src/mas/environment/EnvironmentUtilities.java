package mas.environment;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;

import sma.ontology.AbstractObject;
import sma.ontology.PlaygroundObject;

public class EnvironmentUtilities {
	
	public static boolean pgContains(PlaygroundObject playground, AbstractObject object){
				
		Point pgPos = new Point(playground.getPosition().getXPos(), playground.getPosition().getYPos());
		Dimension pgSize = new Dimension(playground.getSize().getWidth(), playground.getSize().getHeight());
		Area pgArea = new Area(new Rectangle(pgPos, pgSize));
		
		Point objectPos = new Point(object.getPosition().getXPos(), object.getPosition().getYPos());
		Dimension objectSize = new Dimension(object.getSize().getWidth(), object.getSize().getHeight());
		Rectangle objectRect = new Rectangle(objectPos, objectSize);
		
		return pgArea.contains(objectRect);
		
		
	}
}
