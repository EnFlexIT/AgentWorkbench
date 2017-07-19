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

import java.io.Serializable;

/**
 * The Class GraphEdgeDirection.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphEdgeDirection implements Serializable {

	private static final long serialVersionUID = -5039217366665937971L;

	/** The graph edge id. */
	private String graphEdgeID = null;
	/** The graph node id from. */
	private String graphNodeIDFrom = null; 	
	/** The graph node id to. */
	private String graphNodeIDTo = null;

	private boolean fixedDirected = false;
	
	/**
	 * Instantiates a new graph edge direction.
	 */
	public GraphEdgeDirection() {
		
	}
	/**
	 * Instantiates a new graph edge direction.
	 */
	public GraphEdgeDirection(String graphEdgeID, String graphNodeIDFrom, String graphNodeIDTo, boolean fixedDirected) {
		this.graphEdgeID = graphEdgeID;
		this.graphNodeIDFrom = graphNodeIDFrom; 	
		this.graphNodeIDTo = graphNodeIDTo;
		this.fixedDirected = fixedDirected;
	}

	/**
	 * Returns a copy of the current instance.
	 * @return the copy
	 */
	public GraphEdgeDirection getCopy() {
		GraphEdgeDirection ged = new GraphEdgeDirection(this.graphEdgeID, this.graphNodeIDFrom, this.graphNodeIDTo, this.fixedDirected);
		return ged;
	}
	
	/**
	 * Gets the graph edge id.
	 * @return the graphEdgeID
	 */
	public String getGraphEdgeID() {
		return graphEdgeID;
	}
	/**
	 * Sets the graph edge id.
	 * @param graphEdgeID the graphEdgeID to set
	 */
	public void setGraphEdgeID(String graphEdgeID) {
		this.graphEdgeID = graphEdgeID;
	}
	
	/**
	 * Gets the graph node id from.
	 * @return the graphNodeIDFrom
	 */
	public String getGraphNodeIDFrom() {
		return graphNodeIDFrom;
	}
	/**
	 * Sets the graph node id from.
	 * @param graphNodeIDFrom the graphNodeIDFrom to set
	 */
	public void setGraphNodeIDFrom(String graphNodeIDFrom) {
		this.graphNodeIDFrom = graphNodeIDFrom;
	}

	/**
	 * Gets the graph node id to.
	 * @return the graphNodeIDTo
	 */
	public String getGraphNodeIDTo() {
		return graphNodeIDTo;
	}
	/**
	 * Sets the graph node id to.
	 * @param graphNodeIDTo the graphNodeIDTo to set
	 */
	public void setGraphNodeIDTo(String graphNodeIDTo) {
		this.graphNodeIDTo = graphNodeIDTo;
	}
	
	/**
	 * Sets the fixed directed.
	 * @param fixedDirected the new fixed directed
	 */
	public void setFixedDirected(boolean fixedDirected) {
		this.fixedDirected = fixedDirected;
	}
	/**
	 * Checks if is fixed directed.
	 * @return true, if is fixed directed
	 */
	public boolean isFixedDirected() {
		return fixedDirected;
	}

}
