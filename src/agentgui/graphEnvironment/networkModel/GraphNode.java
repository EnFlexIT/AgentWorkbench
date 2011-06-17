package agentgui.graphEnvironment.networkModel;

import java.awt.Point;
import java.awt.geom.Point2D;
/**
 * This class represents a graph node in an environment model of the type graph / network
 * 
 * @see GraphEdge
 * @see GraphElement
 * 
 * @author Nils
 *
 */
public class GraphNode extends GraphElement{
	/**
	 * The GraphNode's position in a visualization
	 */
	private Point2D position;
	/**
	 * The ontology object instance representing this component, serialized as a base64 encoded String for saving via JAXB
	 */
	private String encodedOntologyRepresentation;
	
	/**
	 * Default constructor
	 */
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

	/**
	 * @return the encodedOntologyRepresentation
	 */
	public String getEncodedOntologyRepresentation() {
		return encodedOntologyRepresentation;
	}

	/**
	 * @param encodedOntologyRepresentation the encodedOntologyRepresentation to set
	 */
	public void setEncodedOntologyRepresentation(
			String encodedOntologyRepresentation) {
		this.encodedOntologyRepresentation = encodedOntologyRepresentation;
	}

}
