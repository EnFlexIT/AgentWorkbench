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

import jade.util.leap.Serializable;

/**
 * Abstract super class for nodes and edges in an environment model of the type graph / network.
 * 
 * @see GraphEdge
 * @see GraphNode
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public abstract class GraphElement implements Serializable {

	private static final long serialVersionUID = -8008053317555768852L;

	protected String id = null;
	protected transient GraphElementLayout graphElementLayout = null;
	
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
	 * @param networkModel the current network model (required because of the {@link NetworkComponentAdapter})
	 * @return the copy of the current instance
	 */
	public abstract GraphElement getCopy(NetworkModel networkModel);

	
	/**
	 * Sets the graph element layout.
	 * @param graphElementLayout the new graph element layout
	 */
	public void setGraphElementLayout(GraphElementLayout graphElementLayout) {
		this.graphElementLayout=graphElementLayout;
	}
	/**
	 * Returns the graph element layout.
	 * @return the graph element layout
	 */
	public GraphElementLayout getGraphElementLayout(NetworkModel networkModel) {
		if (this.graphElementLayout==null && networkModel!=null) {
			this.graphElementLayout = new GraphElementLayout(this);
			this.graphElementLayout.setNetworkModel(networkModel);
		}
		return this.graphElementLayout;
	}
	/**
	 * Resets the graph element layout.
	 */
	public void resetGraphElementLayout(NetworkModel networkModel) {
		this.graphElementLayout = null;
		this.getGraphElementLayout(networkModel);
	}
	
}
