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
package org.awb.env.networkModel.networkModel;

import java.awt.Shape;
import java.awt.geom.Line2D;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape.Loop;


/**
 * The Class GraphEdgeShapeTransformer transforms and thus manages the appearance of each single GraphEdge.
 * Such, can be a Line2D, a QuadCurve2D, a GeneralPath or an Orthogonal connection between two points.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphEdgeShapeTransformer<V, E> extends AbstractEdgeShapeTransformer<GraphNode, GraphEdge> {

	private Line2D line;
	private Loop<GraphNode, GraphEdge> loop;    
    

    /* (non-Javadoc)
	 * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
	 */
	@Override
	public Shape transform(Context<Graph<GraphNode, GraphEdge>, GraphEdge> context) {

		Graph<GraphNode, GraphEdge> graph = context.graph;
    	GraphEdge graphEdge = context.element;
        
    	// --- Check for a loop ---------------------------
    	GraphNode graphNodeFrom = graph.getSource(graphEdge);
    	GraphNode graphNodeTo = graph.getDest(graphEdge);
        if (graphNodeFrom!=null && graphNodeTo!=null && graphNodeFrom.equals(graphNodeTo)) {
        	return this.getLoop().transform(context);
        }
        // --- Return the configured edge shape -----------
        return this.getEdgeShape(graphEdge, graphNodeFrom, graphNodeTo);
	}

	/**
	 * Returns the shape for the specified GraphEdge.
	 *
	 * @param graphEdge the graph edge
	 * @return the graph edge shape
	 */
	private Shape getEdgeShape(GraphEdge graphEdge, GraphNode graphNodeFrom, GraphNode graphNodeTo) {
		// --- By default take the shared line instance ---
		Shape graphEdgeShape = this.getLine();
		if (graphEdge.getEdgeShapeConfiguration()!=null) {
			try {
				// --- Get the specific edge shape --------
				graphEdgeShape = graphEdge.getEdgeShapeConfiguration().getShape(graphNodeFrom, graphNodeTo);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}		
		return graphEdgeShape;
	}
	
	/**
	 * Returns a simple line.
	 * @return the line
	 */
	private Line2D getLine() {
		if (line==null) {
			line = new Line2D.Float(0.0f, 0.0f, 1.0f, 0.0f);
		}
		return line;
	}
	/**
	 * Returns a loop instance.
	 * @return a loop
	 */
	private Loop<GraphNode, GraphEdge> getLoop() {
		if (loop==null) {
			 loop = new Loop<GraphNode, GraphEdge>();
		}
		return loop;
	}
	
}
