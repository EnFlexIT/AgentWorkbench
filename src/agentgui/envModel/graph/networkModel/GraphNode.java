/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.envModel.graph.networkModel;

import java.awt.geom.Point2D;

/**
 * This class represents a graph node in an environment model of the type graph / network
 * 
 * @see GraphEdge
 * @see GraphElement
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * 
 */
public class GraphNode extends GraphElement {

    private static final long serialVersionUID = 7676853104978228247L;

    /**
     * The GraphNode's position in a visualization
     */
    private Point2D position = null;
    /**
     * The ontology object instance representing this component, serialized as a base64 encoded String for saving via JAXB
     */
    private String encodedOntologyRepresentation;

    public final static String GRAPH_NODE_PREFIX = "PP";

    /**
     * Default constructor
     */
    public GraphNode() {
	this.position = new Point2D.Double(50.0, 50.0);
    }

    public GraphNode(String id, Point2D position) {
	this.id = id;
	this.position = position;
    }

    /**
     * Gets the copy of the current instance.
     * 
     * @return the copy
     */
    @Override
    public GraphNode getCopy() {
	GraphNode nodeCopy = new GraphNode(id, new Point2D.Double(position.getX(), this.position.getY()));
	nodeCopy.setEncodedOntologyRepresentation(this.encodedOntologyRepresentation);
	return nodeCopy;
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
    public void setEncodedOntologyRepresentation(String encodedOntologyRepresentation) {
	this.encodedOntologyRepresentation = encodedOntologyRepresentation;
    }

}
