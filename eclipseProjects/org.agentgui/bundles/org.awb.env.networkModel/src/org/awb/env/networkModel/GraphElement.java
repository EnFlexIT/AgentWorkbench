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
package org.awb.env.networkModel;

import org.awb.env.networkModel.adapter.DynamicGraphElementLayout;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;

import jade.util.leap.Serializable;

/**
 * Abstract super class for {@link GraphNode}s and {@link GraphEdge}s in the Graph- and Network Model Environment.
 * 
 * @see GraphEdge
 * @see GraphNode
 * @see NetworkModel
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class GraphElement implements Serializable {

	private static final long serialVersionUID = -8008053317555768852L;

	protected String id;
	protected transient GraphElementLayout graphElementLayout;
	
	/**
	 * Gets the id. 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns a copy of the current GraphElement (GraphNode or GraphEdge).
	 * 
	 * @see GraphNode
	 * @see GraphEdge
	 *
	 * @return the copy of the current instance
	 */
	public abstract GraphElement getCopy();

	
	/**
	 * Sets the graph element layout.
	 * @param graphElementLayout the new graph element layout
	 */
	public void setGraphElementLayout(GraphElementLayout graphElementLayout) {
		this.graphElementLayout = graphElementLayout;
	}
	/**
	 * Returns - based on the general settings in the specified {@link NetworkModel} - the {@link GraphElementLayout} .
	 *
	 * @param graphController the graph controller
	 * @return the graph element layout
	 * @see GeneralGraphSettings4MAS
	 */
	public GraphElementLayout getGraphElementLayout(GraphEnvironmentController graphController) {
		
		if (graphController==null) return graphElementLayout;
		
		DynamicGraphElementLayout dynGEL = null;
		if (this.graphElementLayout==null) {
			// --- Check if there is a dynamic layout --------------- 
			dynGEL = this.getDynamicGraphElementLayout();
			if (dynGEL!=null) {
				dynGEL.updateLayout(graphController);
				this.graphElementLayout = dynGEL;
			} else {
				this.graphElementLayout = new GraphElementLayout(this);
				this.graphElementLayout.updateLayout(graphController);
			}
			
		} else {
			// --- Invoke to update the DynamicGraphElementLayout? -- 
			if (graphElementLayout instanceof DynamicGraphElementLayout) {
				dynGEL = (DynamicGraphElementLayout) graphElementLayout;
				dynGEL.updateLayout(graphController);
			}
		}
		return this.graphElementLayout;
	}
	
	/**
	 * Returns the {@link DynamicGraphElementLayout}, if specified with the corresponding {@link NetworkComponentAdapter}.
	 * @return the dynamic graph element layout
	 */
	private DynamicGraphElementLayout getDynamicGraphElementLayout() {
		NetworkComponentAdapter nca = this.getNetworkComponentAdapter();
		if (nca!=null) {
			return nca.getDynamicGraphElementLayout();
		}
		return null;
	}
	
	/**
	 * Returns the {@link NetworkComponentAdapter} that is responsible for the current graph element.
	 * @return the network component adapter
	 */
	private NetworkComponentAdapter getNetworkComponentAdapter() {
		
		NetworkComponentAdapter nca = null;
		// --- Get NetworkComponentAdapter for the current GraphElement ------- 
		if (this instanceof GraphNode) {
			// --- Case GraphNode ---------------------------------------------
			
		} else if (this instanceof GraphEdge) {
			// --- Case GraphEdge ---------------------------------------------
			
		}
		return nca;
	}
	
}
