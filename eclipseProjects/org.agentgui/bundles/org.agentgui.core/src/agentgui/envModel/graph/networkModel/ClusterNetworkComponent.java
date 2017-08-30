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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import de.enflexit.common.SerialClone;

/**
 * The Class ClusterNetworkComponent.
 */
public class ClusterNetworkComponent extends NetworkComponent {

	private static final long serialVersionUID = 682444875338053459L;

	/** The Constant defaultPrototypeClassName. */
	private static final String DEFAUL_PROTOTYPE_CLASS_NAME = agentgui.envModel.graph.prototypes.ClusterGraphElement.class.getName();
	/** The domain. which this cluster contains to */
	private String domain;
	/** The cluster network model. */
	private NetworkModel clusterNetworkModel;

	/**
	 * Instantiates a new cluster network component.
	 *
	 * @param id the id
	 * @param type the type
	 * @param agentClassName the agent class name
	 * @param graphElements the graph elements
	 * @param directed the directed
	 * @param domain the domain
	 * @param clusterNetworkModel the cluster network model
	 */
	public ClusterNetworkComponent(String id, String type, String agentClassName, HashSet<GraphElement> graphElements, boolean directed, String domain, NetworkModel clusterNetworkModel) {
		super(id, type, ClusterNetworkComponent.DEFAUL_PROTOTYPE_CLASS_NAME, agentClassName, graphElements, directed);
		this.domain = domain;
		this.clusterNetworkModel = clusterNetworkModel;
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponent#getCopy()
	 */
	@Override
	public ClusterNetworkComponent getCopy(NetworkModel networkModel) {
		
		ClusterNetworkComponent copy = null;	
		boolean cloneInstance = true;
		if (cloneInstance==true) {
			// -------------------------------------------------------
			// --- Make a serialisation copy the NetworkModel -------- 
			// -------------------------------------------------------
			try {
				copy = SerialClone.clone(this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		} else {
			// -------------------------------------------------------
			// --- Make a serialisation copy the NetworkModel -------- 
			// -------------------------------------------------------
			String newID = null;
			String newType = null;
			String newPrototypeClassName = null; 
			String newAgentClassName = null;
			String newDomain = null;
			NetworkModel newClusterNetworkModel = null;
			
			if (this.id!=null) newID = new String(this.id);
			if (this.type!=null) newType = new String(this.type);
			if (this.prototypeClassName!=null) newPrototypeClassName = new String(this.prototypeClassName);
			if (this.agentClassName!=null) newAgentClassName = new String(this.agentClassName);
			if (this.domain!=null) newDomain = new String(this.domain);
			if (this.clusterNetworkModel!=null) newClusterNetworkModel = this.clusterNetworkModel.getCopy();
			
			copy = new ClusterNetworkComponent(newID, newType, newAgentClassName, null, this.directed, newDomain, newClusterNetworkModel);
			HashSet<String> gaphElementIDs = new HashSet<String>(this.getGraphElementIDs());
			copy.setGraphElementIDs(gaphElementIDs);
			if (newPrototypeClassName!=null) {
				copy.setPrototypeClassName(newPrototypeClassName);
			}
			
			// --- Copy the data model ------------------------
			if (this.dataModel!=null) {
				NetworkComponentAdapter adapter = networkModel.getNetworkComponentAdapter(null, this);
				NetworkComponentAdapter4DataModel adapter4DataModel = adapter.getStoredDataModelAdapter();
				if (this.dataModelBase64==null) {
					this.dataModelBase64 = adapter4DataModel.getDataModelBase64Encoded(this.dataModel);
				} 
				Object dataModelCopy = adapter4DataModel.getDataModelBase64Decoded(this.dataModelBase64);
				copy.setDataModel(dataModelCopy);
			}
			// --- Copy the Base64 data model -----------------
			if (this.dataModelBase64!=null) {
				Vector<String> dataModelCopy64 = new Vector<String>();
				for (String single64 : this.dataModelBase64) {
					if (single64!=null & single64.equals("")==false) {
						dataModelCopy64.add(new String(single64));
					} else {
						dataModelCopy64.add(null);
					}
				}
				copy.setDataModelBase64(dataModelCopy64);
			}
	
			if (this.edgeDirections!=null && this.edgeDirections.size()!=0) {
				HashMap<String, GraphEdgeDirection> edgeDirectionsCopy = new HashMap<String, GraphEdgeDirection>();
				for (String graphEdgeID : this.edgeDirections.keySet()) {
					GraphEdgeDirection ged = this.edgeDirections.get(graphEdgeID); 
					edgeDirectionsCopy.put(graphEdgeID, ged.getCopy());
				}
				copy.setEdgeDirections(edgeDirectionsCopy);
			}
		}
		return copy;
	}

	/**
	 * Gets the network component IDs.
	 * 
	 * @return the network component IDs
	 */
	public HashSet<String> getNetworkComponentIDs() {
		return new HashSet<String>(clusterNetworkModel.getNetworkComponents().keySet());
	}

	/**
	 * Gets the network components.
	 *
	 * @return the network components
	 */
	public ArrayList<NetworkComponent> getNetworkComponents() {
		return new ArrayList<NetworkComponent>(clusterNetworkModel.getNetworkComponents().values());
	}

	/**
	 * Sets the cluster network model.
	 * 
	 * @param clusterNetworkModel the new cluster network model
	 */
	public void setClusterNetworkModel(NetworkModel clusterNetworkModel) {
		this.clusterNetworkModel = clusterNetworkModel;
	}

	/**
	 * Gets the cluster network model.
	 * 
	 * @return the cluster network model
	 */
	public NetworkModel getClusterNetworkModel() {
		return clusterNetworkModel;
	}


	/**
	 * Gets the Component represented by Node and of the ClusterStar.
	 *
	 * @param graphNode the graph node
	 * @return the receive
	 */
	public NetworkComponent getReceiver(GraphNode graphNode) {
		return clusterNetworkModel.getNetworkComponents((GraphNode) clusterNetworkModel.getGraphElement(graphNode.getId())).iterator().next();
	}

	/**
	 * Sets the domain.
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Returns the domain.
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * get all connection NetworkComponents (all NetworkComonent with a FreeNode).
	 *
	 * @return all connection NetworkComponents
	 */
	public ArrayList<NetworkComponent> getConnectionNetworkComponents() {
		ArrayList<NetworkComponent> connectionNC = new ArrayList<NetworkComponent>();
		for (GraphNode graphNode : clusterNetworkModel.getGraph().getVertices()) {
			if (clusterNetworkModel.isFreeGraphNode(graphNode))
				connectionNC.addAll(clusterNetworkModel.getNetworkComponents(graphNode));
		}
		return connectionNC;
	}
}
