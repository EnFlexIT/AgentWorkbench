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
package agentgui.envModel.graph.visualisation.notifications;

import java.util.Vector;

import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphElementLayout;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class GraphLayoutNotification can be used in order to define
 * or change the Layout of {@link GraphElement}'s.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphLayoutNotification extends DisplayAgentNotificationGraph {

	private static final long serialVersionUID = 880272547644138841L;

	private Vector<GraphElementLayout> graphElementLayouts = null; 
 	
	
	/**
	 * Neutral constructor for this class. Use the add methods!
	 */
	public GraphLayoutNotification() {
	}
	/**
	 * Constructor with a given list of GraphElementLayout.
	 * @param graphElementLayouts the graph element layouts
	 */
	public GraphLayoutNotification(Vector<GraphElementLayout> graphElementLayouts) {
		this.graphElementLayouts = graphElementLayouts;
	}
	
	/**
	 * Returns the Vector of GraphElementLayout's.
	 * @return the graph element layouts
	 */
	public Vector<GraphElementLayout> getGraphElementLayouts() {
		if (graphElementLayouts==null) {
			graphElementLayouts = new Vector<GraphElementLayout>();	
		}
		return graphElementLayouts;
	}
	/**
	 * Sets the Vector of GraphElementLayout's.
	 * @param graphElementLayouts the graph element layouts
	 */
	public void setGraphElementLayouts(Vector<GraphElementLayout> graphElementLayouts) {
		this.graphElementLayouts = graphElementLayouts;
	}
	
	
	/**
	 * Adds a GraphElementLayout to the listing of Layouts.
	 * @param graphElement the graph element
	 */
	public void addGraphElementLayout(GraphElement graphElement, NetworkModel networkModel) {
		GraphElementLayout graphElementLayout = graphElement.getGraphElementLayout(networkModel);
		if (graphElementLayout!=null) {
			this.getGraphElementLayouts().add(graphElementLayout);	
		}
	}
	/**
	 * Adds a GraphElementLayout to the listing of Layouts.
	 * @param graphElementLayout the graph element layout
	 */
	public void addGraphElementLayout(GraphElementLayout graphElementLayout) {
		this.getGraphElementLayouts().add(graphElementLayout);
	}
	/**
	 * Adds a list of GraphElementLayout's.
	 * @param graphElementLayouts the graph element layouts
	 */
	public void addGraphElementLayouts(Vector<GraphElementLayout> graphElementLayouts) {
		this.getGraphElementLayouts().addAll(graphElementLayouts);
	}
	
}
