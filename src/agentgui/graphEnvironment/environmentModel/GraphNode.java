package agentgui.graphEnvironment.environmentModel;

import java.awt.Point;
import java.awt.geom.Point2D;

public class GraphNode extends GraphElement{
	/**
	 * The GraphNode's position in a visualization
	 */
	private Point2D position;
	
	public GraphNode(){
		this.position = new Point(50, 50);
	}

	/**
	 * @param point2d the position to set
	 */
	public void setPosition(Point2D point2d) {
		this.position = point2d;
	}

	/**
	 * @return the position
	 */
	public Point2D getPosition() {
		return position;
	}

}
