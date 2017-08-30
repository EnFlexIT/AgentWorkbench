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

import java.util.HashMap;
import java.util.HashSet;

import agentgui.envModel.graph.prototypes.DistributionNode;

/**
 * The Class DirectionSettings.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkComponentDirectionSettings {

	private NetworkModel networkModel = null;
	private NetworkComponent networkComponent = null;
	
	private HashSet<GraphNode> innerGraphNodes = new HashSet<GraphNode>();
	private HashSet<GraphNode> outerGraphNodes = new HashSet<GraphNode>();
	
	private HashSet<GraphEdgeConnection> edgeConnections = null;
	private HashMap<String, GraphEdgeDirection> edgeDirections = null;
	
	
	/**
	 * Instantiates a new direction settings.
	 *
	 * @param networkModel the network model
	 * @param networkComponent the network component
	 */
	public NetworkComponentDirectionSettings(NetworkModel networkModel, NetworkComponent networkComponent) {
	
		this.networkModel = networkModel;
		this.networkComponent = networkComponent;
		
		this.evaluate();
	}
	
	/**
	 * Evaluates the available GraphEdges.
	 */
	private void evaluate() {
		
		// --- Get all edges of this NetworkComponent's by the IDs ------------
		for (String graphElementID : this.networkComponent.getGraphElementIDs()) {

			// --- Get the description of the connections to the edge ---------
			GraphElement graphElement = this.networkModel.getGraphElement(graphElementID);
			if (graphElement instanceof GraphEdge) {
				GraphEdgeConnection graphEdgeConnection = new GraphEdgeConnection(this.networkModel, this.networkComponent, (GraphEdge) graphElement);
				if (this.edgeConnections==null) {
					this.edgeConnections = new HashSet<GraphEdgeConnection>();
				}
				this.edgeConnections.add(graphEdgeConnection);
				
				String graphEdgeID = graphEdgeConnection.getGraphEdge().getId();
				GraphEdgeDirection graphEdgeDirection = null;
				if (graphEdgeConnection.isFixedDirected()) {
					String graphNodeIDFrom = graphEdgeConnection.getGraphNode1().getId();
					String graphNodeIDTo   = graphEdgeConnection.getGraphNode2().getId();
					graphEdgeDirection = new GraphEdgeDirection(graphEdgeID, graphNodeIDFrom, graphNodeIDTo, true);
				} else {
					graphEdgeDirection = new GraphEdgeDirection(graphEdgeID, null, null, false);	
				}
				
				if (this.edgeDirections==null) {
					this.edgeDirections = new HashMap<String, GraphEdgeDirection>();
				}
				this.edgeDirections.put(graphEdgeID, graphEdgeDirection);
			}
		}

		// --- Set inner and outer GraphNodes ---------------------------------
		this.setInnerAndOuterGraphNodes();
		
	}
	
	/**
	 * Checks if the current NetworkComponent is a DistributionNode.
	 * @return true, if it is a DistributionNode
	 */
	public boolean isDistributionNode() {
		if (this.networkComponent.getPrototypeClassName().equals(DistributionNode.class.getName())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the inner and outer GraphNodes of this NetworkComponent.
	 */
	private void setInnerAndOuterGraphNodes() {
		
		if (isDistributionNode()) {
			// --- This is a DistributionNonde ------------
			GraphElement node = this.networkModel.getGraphElement(this.networkComponent.getGraphElementIDs().iterator().next());
			this.outerGraphNodes.add((GraphNode) node);
			
		} else {
			// --- Is there a node, which is there more than one time? --
			HashMap<GraphNode, Integer> noderCountings = new HashMap<GraphNode, Integer>();
			for (GraphEdgeConnection connection : this.edgeConnections) {
				
				// --- GraphNode 1 ----------------------------------
				GraphNode node = connection.getGraphNode1();
				if (noderCountings.get(node)==null) {
					noderCountings.put(node, 1);
				} else {
					Integer counting = noderCountings.get(node);
					counting++;
					noderCountings.put(node, counting);
				}
				// --- GraphNode 2 ----------------------------------
				node = connection.getGraphNode2();
				if (noderCountings.get(node)==null) {
					noderCountings.put(node, 1);
				} else {
					Integer counting = noderCountings.get(node);
					counting++;
					noderCountings.put(node, counting);
				}

			}
			
			// --- Assign nodes to inner or outer GraphNodes --------
			for (GraphNode node : noderCountings.keySet()) {
				Integer countings = noderCountings.get(node);
				if (countings==1) {
					this.outerGraphNodes.add(node);
				} else {
					this.innerGraphNodes.add(node);
				}
			}
		}
	}
	
	/**
	 * Gets the edge connections.
	 * @return the edgeConnections
	 */
	public HashSet<GraphEdgeConnection> getEdgeConnections() {
		return this.edgeConnections;
	}
	/**
	 * Gets the edge directions.
	 * @return the edgeDirections
	 */
	public HashMap<String, GraphEdgeDirection> getEdgeDirections() {
		return this.edgeDirections;
	}
	
	/**
	 * Returns the outer GraphNodes of the NetworkComponent.
	 * @return the outer GraphNodes
	 */
	public HashSet<GraphNode> getOuterNodes() {
		return this.outerGraphNodes;
	}
	/**
	 * Returns the inner GraphNodes of the NetworkComponent.
	 * @return the inner GraphNodes
	 */
	public HashSet<GraphNode> getInnerNodes() {
		return this.innerGraphNodes;
	}
	
	/**
	 * Gets the neighbour NetworkComponent(s) of a GraphNode.
	 *
	 * @param graphNode the graph node
	 * @return the neighbour network component
	 */
	public HashSet<NetworkComponent> getNeighbourNetworkComponent(GraphNode graphNode) {
		
		HashSet<NetworkComponent> neighbours = null;
		if (isDistributionNode()) {
			neighbours = new HashSet<NetworkComponent>(this.networkModel.getNeighbourNetworkComponents(this.networkComponent));
			
		} else {
		
			String searchID = graphNode.getId();
			for (GraphEdgeConnection conn : this.edgeConnections) {
				
				// --- GraphNode 1 --------------
				if (conn.getGraphNode1().getId().equals(searchID)) {
					 if (conn.getExternalNetworkComponent1()!=null) {
						 neighbours =  new HashSet<NetworkComponent>();
						 neighbours.add(conn.getExternalNetworkComponent1());
						 break;
					 }
				}
				// --- GraphNode 2 --------------
				if (conn.getGraphNode2().getId().equals(searchID)) {
					 if (conn.getExternalNetworkComponent2()!=null) {
						 neighbours =  new HashSet<NetworkComponent>();
						 neighbours.add(conn.getExternalNetworkComponent2());
						 break;
					 }
				}
			}
			
		}
		return neighbours;
	}
	
	/**
	 * Returns the graph node of neighbour network component.
	 * @param neighbourNetworkComponent the neighbour network component
	 * @return the graph node of neighbour network component
	 */
	public GraphNode getGraphNodeOfNeighbourNetworkComponent(NetworkComponent neighbourNetworkComponent) {
		
		GraphNode graphNode = null;
		if (isDistributionNode()) {
			graphNode = (GraphNode) this.networkModel.getGraphElement(this.networkComponent.getGraphElementIDs().iterator().next());
			
		} else {
		
			String searchID = neighbourNetworkComponent.getId();
			for (GraphEdgeConnection conn : this.edgeConnections) {
				// --- GraphNode 1 --------------
				if (conn.getExternalNetworkComponent1()!=null) {
					if (conn.getExternalNetworkComponent1().getId().equals(searchID)) {
						graphNode = conn.getGraphNode1(); 
						break;
					}	
				}
				// --- GraphNode 2 --------------
				if (conn.getExternalNetworkComponent2()!=null) {
					if (conn.getExternalNetworkComponent2().getId().equals(searchID)) {
						graphNode = conn.getGraphNode2(); 
						break;
					}	
				}
			}
			
		}
		return graphNode;
	}
	
	/**
	 * Translate network component HashSet.
	 * @param netCompHash the NetworkComponent-Hash
	 * @return the hash set
	 */
	public HashSet<GraphNode> translateNetworkComponentHashSet(HashSet<NetworkComponent> netCompHash) {
	
		HashSet<GraphNode> resultHash = new HashSet<GraphNode>();
		for (NetworkComponent netComp : netCompHash) {
			GraphNode node = this.getGraphNodeOfNeighbourNetworkComponent(netComp);
			if (resultHash==null){
				resultHash = new HashSet<GraphNode>();
			}
			resultHash.add(node);
		}
		return resultHash; 
	}
	
	/**
	 * Translate graph node HashSet.
	 * @param graphNodeHash the graph node hash
	 * @return the hash set
	 */
	public HashSet<NetworkComponent> translateGraphNodeHashSet(HashSet<GraphNode> graphNodeHash) {
	
		HashSet<NetworkComponent> resultHash = new HashSet<NetworkComponent>();
		for (GraphNode graphNode : graphNodeHash) {
			HashSet<NetworkComponent> netComp = this.getNeighbourNetworkComponent(graphNode);
			if (netComp!=null) {
				if (resultHash==null){
					resultHash = new HashSet<NetworkComponent>();
				}
				resultHash.addAll(netComp);	
			}
		}
		return resultHash;
		
	}
	
	
	/**
	 * Sets the graph edge direction.
	 *
	 * @param graphEdgeID the graph edge id
	 * @param graphNodeFromID the graph node from id
	 * @param graphNodeToID the graph node to id
	 */
	public void setGraphEdgeDirection(String graphEdgeID, String graphNodeFromID, String graphNodeToID) {

		String error = null;
		GraphEdgeDirection graphEdgeDirection = null;

		if (graphEdgeID==null)  	error = "Got no valid GraphEdgeID for the edge!";
		if (graphNodeFromID==null)  error = "Got no valid GraphNodeID for node 'From'!";
		if (graphNodeToID==null)  	error = "Got no valid GraphNodeID for node 'To'!";

		if (error==null) {
			graphEdgeDirection = this.edgeDirections.get(graphEdgeID);
			if (graphEdgeDirection==null)  error = "Got no valid GraphEdgeID for the edge!";
		}

		if (error==null) {
			// --- Assign direction setting, if allowed ---
			if (graphEdgeDirection.isFixedDirected()) {
				System.out.println("-> Direction of edge '" + graphEdgeDirection.getGraphEdgeID() + "' is fixed and can not be changed!");
			} else {
				graphEdgeDirection.setGraphNodeIDFrom(graphNodeFromID);
				graphEdgeDirection.setGraphNodeIDTo(graphNodeToID);	
			}
			
		} else {
			// --- Throw an exception ---------------------
			try {
				throw new NetworkComponentDirectionException(error);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Sets the graph edge direction by using the GraphElements.
	 *
	 * @param graphEdge the graph edge
	 * @param graphNodeFrom the graph node from
	 * @param graphNodeTo the graph node to
	 */
	public void setGraphEdgeDirection(GraphEdge graphEdge, GraphNode graphNodeFrom, GraphNode graphNodeTo) {
		this.setGraphEdgeDirection(graphEdge.getId(), graphNodeFrom.getId(), graphNodeTo.getId());
	}
	
	
	/**
	 * Sets the graph edge direction by using either the GraphNodes or the NetworkComponents, which
	 * are located at the outer boundaries of the NetworkComponent. Be aware that the types in the HasSet have to be the same. <br>
	 * You can use:<br>
	 * HashSet<{@link GraphNode}> or <br>
	 * HashSet<{@link NetworkComponent}><br>
	 *
	 * @param inNodesOrComponents the HashSet of incoming {@link GraphNode}'s or {@link NetworkComponent}'s
	 * @param outNodesOrComponents the HashSet of outgoing {@link GraphNode}'s or {@link NetworkComponent}'s
	 */
	@SuppressWarnings("unchecked")
	public void setGraphEdgeDirection(HashSet<?> inNodesOrComponents, HashSet<?> outNodesOrComponents) {
		
		// ----------------------------------------------------------
		// --- Error handling / validation --------------------------
		String error = null;
		Class<?> argInClass = null;
		Class<?> argOutClass = null;
		
		if (inNodesOrComponents==null) error = "IN parameter should not be Null!";
		if (outNodesOrComponents==null) error = "OUT parameter should not be Null!";
//		if (inNodesOrComponents.size()==0)  error = "IN parameter should not be from the size 0!";
//		if (outNodesOrComponents.size()==0) error = "OUT parameter should not be from the size 0!";
//		if ((inNodesOrComponents.size()+outNodesOrComponents.size())!=outerGraphNodes.size()) error = "Number of IN and OUT parameter should be equal to the number of outer GraphNodes!";
		
		Object type = null;
		if (error==null) {
			// --- Working with GraphNode's or NetworkComponent's --- 
			if (inNodesOrComponents.iterator().hasNext()) {
				type = inNodesOrComponents.iterator().next();
			} else {
				if (outNodesOrComponents.iterator().hasNext()) {
					type = outNodesOrComponents.iterator().next();
				} else {
					error = "Whether IN or OUT parameter have to be set!";
				}
			}
			if (type!=null) {
				if (!(type instanceof NetworkComponent) && !(type instanceof GraphNode) ){
					error = "IN and OUT parameter have to be of the type GraphNode or NetworkComponent!";
				}	
			}
		}
		if (this.innerGraphNodes.size()>1) {
			error = "This method can only be applied for NetworkComponents with NO or ONE inner node!";
		}
		if (error==null) {
			if (inNodesOrComponents.iterator().hasNext()) {
				argInClass = inNodesOrComponents.iterator().next().getClass();	
			}
			if (outNodesOrComponents.iterator().hasNext()) {
				argOutClass = outNodesOrComponents.iterator().next().getClass();	
			}
			if (argInClass!=null && argOutClass!=null) {
				if (argInClass.equals(argOutClass)==false) {
					error = "Values of IN and OUT parameter are not of the same type!";
				}
			}
		}
		if (error!=null) {
			try {
				throw new NetworkComponentDirectionException(error);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return;
		}

		// ----------------------------------------------------------
		// --- Type conversion to HashSet<GraphNode> ----------------
		HashSet<GraphNode> inNodes  = null;
		HashSet<GraphNode> outNodes = null;
		if (type instanceof NetworkComponent) {
			// --- Translate to GraphNode HashSets ------------------
			inNodes  = this.translateNetworkComponentHashSet((HashSet<NetworkComponent>) inNodesOrComponents);
			outNodes = this.translateNetworkComponentHashSet((HashSet<NetworkComponent>) outNodesOrComponents);
			this.setGraphEdgeDirection(inNodes, outNodes);
			return;
			
		} else  {
			// --- Take the HashSet informations --------------------
			inNodes  = (HashSet<GraphNode>) inNodesOrComponents;
			outNodes = (HashSet<GraphNode>) outNodesOrComponents;
			
		}
		
		// ----------------------------------------------------------
		// --- Do Assignment ----------------------------------------
		if (this.innerGraphNodes.size()==0) {
			// --- This applies only for simple graph elements ------ 
			GraphEdgeConnection connection = this.edgeConnections.iterator().next();
			GraphEdge graphEdge = connection.getGraphEdge();
			GraphNode graphNodeFrom = inNodes.iterator().next();
			GraphNode graphNodeTo   = outNodes.iterator().next();
			
			this.setGraphEdgeDirection(graphEdge, graphNodeFrom, graphNodeTo);
			
		} else if (this.innerGraphNodes.size()==1) {
			// --- This applies for StarElements --------------------
			GraphNode graphNodeCenter = this.innerGraphNodes.iterator().next();
			for (GraphEdgeConnection connection : this.edgeConnections) {

				GraphNode graphNodeFrom = null; 
				GraphNode graphNodeTo = null;

				GraphEdge graphEdge = connection.getGraphEdge();
				GraphNode graphNode1 = connection.getGraphNode1();
				GraphNode graphNode2 = connection.getGraphNode2();

				if (graphNode1.getId().equalsIgnoreCase(graphNodeCenter.getId())) {
					// --- graphNode1 is center node ------
					if (this.getGraphNodeFromHashSet(inNodes, graphNode2)!=null) {
						graphNodeFrom = graphNode2; 
						graphNodeTo   = graphNode1;
					} else if (this.getGraphNodeFromHashSet(outNodes, graphNode2)!=null) {
						graphNodeFrom = graphNode1; 
						graphNodeTo   = graphNode2;
					}
					
				} else {
					// --- graphNode2 is center node ------					
					if (this.getGraphNodeFromHashSet(inNodes, graphNode1)!=null) {
						graphNodeFrom = graphNode1; 
						graphNodeTo   = graphNode2;
					} else if (this.getGraphNodeFromHashSet(outNodes, graphNode1)!=null) {
						graphNodeFrom = graphNode2; 
						graphNodeTo   = graphNode1;
					}
					
				}
				if (graphNodeFrom!=null && graphNodeTo!=null) {
					this.setGraphEdgeDirection(graphEdge, graphNodeFrom, graphNodeTo);	
				}
			}
		} 
		
	}
	
	/**
	 * Gets the graph node from hash set.
	 *
	 * @param graphNodeHash the graph node hash
	 * @param graphNodeSearch the graph node search
	 * @return the graph node from hash set
	 */
	private GraphNode getGraphNodeFromHashSet(HashSet<GraphNode> graphNodeHash, GraphNode graphNodeSearch) {
		
		GraphNode nodeFound = null;
		for (GraphNode node : graphNodeHash) {
			if (node.getId().equals(graphNodeSearch.getId())) {
				nodeFound = node;
				break;
			}
		}
		return nodeFound;
	}
	
}
