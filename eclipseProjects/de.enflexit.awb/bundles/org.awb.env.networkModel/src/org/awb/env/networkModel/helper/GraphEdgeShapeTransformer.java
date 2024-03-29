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
package org.awb.env.networkModel.helper;

import java.awt.Shape;
import java.awt.geom.Line2D;

import org.awb.env.networkModel.GraphEdge;

import com.google.common.base.Function;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

/**
 * The Class GraphEdgeShapeTransformer transforms and thus manages the appearance of each single GraphEdge.
 * Such, can be a Line2D, a QuadCurve2D, a GeneralPath or an Orthogonal connection between two points.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphEdgeShapeTransformer<V, E> implements Function<E, Shape> {

	private Graph<V, E> graph;
	
	private Line2D line;
	private EdgeShape<V, E>.Loop loop;    

	/**
	 * Instantiates a new graph edge shape transformer.
	 * @param graph the graph
	 */
	public GraphEdgeShapeTransformer(Graph<V, E> graph) {
		this.graph = graph;
	}	
	
    /* (non-Javadoc)
	 * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
	 */
	@Override
	public Shape apply(E e) {

    	// --- Check for a loop ---------------------------
    	V graphNodeFrom = graph.getSource(e);
    	V graphNodeTo = graph.getDest(e);
        if (graphNodeFrom!=null && graphNodeTo!=null && graphNodeFrom.equals(graphNodeTo)) {
        	return this.getLoop().apply(e);
        }
        // --- Return the configured edge shape -----------
        return this.getEdgeShape(e);
	}

	/**
	 * Returns the shape for the specified GraphEdge.
	 *
	 * @param graphEdge the graph edge
	 * @return the graph edge shape
	 */
	private Shape getEdgeShape(E e) {

		// --- By default take the shared line instance ---
		Shape graphEdgeShape = this.getLine();
		if (e instanceof GraphEdge) {
			GraphEdge graphEdge = (GraphEdge) e;
			if (graphEdge.getEdgeShapeConfiguration()!=null) {
				try {
					// --- Get the specific edge shape --------
					graphEdgeShape = graphEdge.getEdgeShapeConfiguration().getShape();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
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
	private EdgeShape<V, E>.Loop getLoop() {
		if (loop==null) {
			 loop = new EdgeShape<V, E>(this.graph).new Loop();
		}
		return loop;
	}
	
}
