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

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape.Loop;


/**
 * The Class EdgeShapePolyline.
 *
 * @param <V> the value type
 * @param <E> the element type
 */
public class EdgeShapePolyline<V, E> extends AbstractEdgeShapeTransformer<V, E> {

	private static GeneralPath path;
	private static Line2D line = new Line2D.Float(0.0f, 0.0f, 1.0f, 0.0f);
	private Loop<V, E> loop;    
    

    /* (non-Javadoc)
	 * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
	 */
	@Override
	public Shape transform(Context<Graph<V, E>, E> context) {

		// --- Get the shape for this edge, returning either the --------------
		// --- shared instance or, in the case of self-loop edges, the --------
		// --- SimpleLoop shared instance.
		
		Graph<V,E> graph = context.graph;
    	E e = context.element;
        
        Pair<V> endpoints = graph.getEndpoints(e);
        if(endpoints != null) {
        	boolean isLoop = endpoints.getFirst().equals(endpoints.getSecond());
        	if (isLoop) {
        		return this.getLoop().transform(context);
        	}
        }
        // --- Return the edge shape ------------------------------------------
        if (e instanceof GraphEdge) {
        	return this.getGeneralPath((GraphEdge)e);
        } else {
        	return this.getLine();
        }
	}

	/**
	 * Returns the general path for polyline edge.
	 *
	 * @param graphEdge the graph edge
	 * @return the general path
	 */
	private GeneralPath getGeneralPath(GraphEdge graphEdge) {
		if (path==null) {
			path = new GeneralPath();
			path.moveTo(0.0f, 0.0f);
			path.lineTo(1.0f, 0.0f);
		}
		return path;
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
	 * Returns the loop.
	 * @return the loop
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Loop<V, E> getLoop() {
		if (loop==null) {
			 loop = new Loop();
		}
		return loop;
	}
	
}
