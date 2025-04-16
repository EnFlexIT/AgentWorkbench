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

import java.util.List;

import org.awb.env.networkModel.adapter.AbstractDynamicGraphElementLayout;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGui;
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
	private transient Long lastDynamicGraphElementLayoutUpdate;
	
	
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
	 * Returns - based on the general settings in the specified {@link NetworkModel} - the <b>static {@link GraphElementLayout} only</b>.
	 * Only use this method, if you are sure that no {@link AbstractDynamicGraphElementLayout} is specified. <br>
	 * Regularly, you should use the newer method {@link #getGraphElementLayout(GraphEnvironmentController)}
	 *
	 * @param networkModel the current NetworkModel
	 * @return the graph element layout
	 * @see GeneralGraphSettings4MAS
	 */
	@Deprecated
	public GraphElementLayout getGraphElementLayout(NetworkModel networkModel) {
		if (this.graphElementLayout==null && networkModel!=null) {
			// --- Initiate static layout -----------------------
			this.graphElementLayout = new GraphElementLayout(this);
			this.graphElementLayout.setGraphElementLayout(networkModel);
		}
		return this.graphElementLayout;
	}
	
	/**
	 * Returns - based on the general settings in the specified {@link GraphEnvironmentController} - the {@link GraphElementLayout}
	 * or the {@link AbstractDynamicGraphElementLayout}.
	 *
	 * @param graphController the current GraphEnvironmentController
	 * @return the graph element layout
	 * @see GeneralGraphSettings4MAS
	 */
	public GraphElementLayout getGraphElementLayout(GraphEnvironmentController graphController) {
		
		if (graphController==null) return graphElementLayout;
		
		AbstractDynamicGraphElementLayout dynGEL = null;
		if (this.graphElementLayout==null) {
			// --- Check if there is a dynamic layout defined ------- 
			dynGEL = this.getDynamicGraphElementLayout(graphController);
			if (dynGEL!=null) {
				// --- Initiate & update dynamic layout -------------
				dynGEL.setGraphElementLayout(graphController);
				this.reduceDynamicGraphElementLayoutUpdateCalls(graphController, dynGEL);
				this.graphElementLayout = dynGEL;
			} else {
				// --- Initiate static layout -----------------------
				this.graphElementLayout = new GraphElementLayout(this);
				this.graphElementLayout.setGraphElementLayout(graphController);
			}
			
		} else {
			if (graphElementLayout instanceof AbstractDynamicGraphElementLayout) {
				// --- Update DynamicGraphElementLayout ------------- 
				dynGEL = (AbstractDynamicGraphElementLayout) this.graphElementLayout;
				this.reduceDynamicGraphElementLayoutUpdateCalls(graphController, dynGEL);
			}
		}
		return this.graphElementLayout;
	}
	/**
	 * This method reduces the system load by reducing the number of calls for a {@link AbstractDynamicGraphElementLayout} update.<br><br>
	 * <b>Background:</b> the local method {@link #getGraphElementLayout(GraphEnvironmentController)} will be called by the visualization viewer
	 * of the {@link BasicGraphGui} several times during the rendering process (e.g. for a size value, a color or an image reference).<br>
	 * Since it can not be expected that the corresponding data model will change that often, this method manages to reduce the number
	 * of calls and the corresponding data model checks.
	 *
	 * @param graphController the current graph controller
	 * @param dynGEL the DynamicGraphElementLayout to update
	 */
	private void reduceDynamicGraphElementLayoutUpdateCalls(GraphEnvironmentController graphController, AbstractDynamicGraphElementLayout dynGEL) {
		
		if (graphController==null || dynGEL==null) return;
		
		long updateWaitTimeMillis = 10; 
		if (this.lastDynamicGraphElementLayoutUpdate==null || System.currentTimeMillis() - this.lastDynamicGraphElementLayoutUpdate >= updateWaitTimeMillis) {
			dynGEL.updateGraphElementLayout(graphController);
			this.lastDynamicGraphElementLayoutUpdate = System.currentTimeMillis();
		}
	}

	
	/**
	 * Returns the {@link AbstractDynamicGraphElementLayout}, if specified with the corresponding {@link NetworkComponentAdapter}.
	 *
	 * @param graphController the graph controller
	 * @return the dynamic graph element layout
	 */
	private AbstractDynamicGraphElementLayout getDynamicGraphElementLayout(GraphEnvironmentController graphController) {
		NetworkComponentAdapter nca = this.getNetworkComponentAdapter(graphController);
		if (nca!=null) {
			return nca.getDynamicGraphElementLayout(this);
		}
		return null;
	}
	
	/**
	 * Returns the {@link NetworkComponentAdapter} that is responsible for the current graph element.
	 *
	 * @param graphController the graph controller
	 * @return the network component adapter
	 */
	private NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController) {
		
		if (graphController==null || graphController.getNetworkModel()==null) return null;
		
		NetworkComponentAdapter nca = null;
		NetworkModel networkModel = graphController.getNetworkModel();
		if (this instanceof GraphNode) {
			// ----------------------------------------------------------------
			// --- Case GraphNode ---------------------------------------------
			// ----------------------------------------------------------------
			List<NetworkComponent> componentList = networkModel.getNetworkComponents((GraphNode)this);
			if (componentList.size()==0) return null;
			
			NetworkComponent distributionNode = networkModel.getDistributionNode(componentList);
			if (distributionNode!=null) {
				// --- For a distribution node --------------------------------
				nca = networkModel.getNetworkComponentAdapter(graphController, distributionNode);
			} else {
				// --- For normal, intermediate GraphNodes --------------------
				nca = networkModel.getNetworkComponentAdapter(graphController, (GraphNode)this);
			}
			
		} else if (this instanceof GraphEdge) {
			// ----------------------------------------------------------------
			// --- Case GraphEdge ---------------------------------------------
			// ----------------------------------------------------------------
			NetworkComponent netComp = networkModel.getNetworkComponent((GraphEdge)this);
			if (netComp!=null) {
				nca = networkModel.getNetworkComponentAdapter(graphController, netComp);	
			}
		}
		return nca;
	}
	
}
