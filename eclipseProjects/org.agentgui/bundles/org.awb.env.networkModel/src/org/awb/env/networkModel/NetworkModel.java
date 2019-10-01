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

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.awb.env.networkModel.adapter.BundlingNetworkComponentAdapter;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter;
import org.awb.env.networkModel.adapter.NetworkComponentToGraphNodeAdapter;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.helper.GraphEdgeDirection;
import org.awb.env.networkModel.helper.GraphNodePairs;
import org.awb.env.networkModel.helper.GraphNodePairsRevert;
import org.awb.env.networkModel.maps.MapSettings;
import org.awb.env.networkModel.persistence.GraphModelReader;
import org.awb.env.networkModel.persistence.GraphModelWriter;
import org.awb.env.networkModel.persistence.NetworkComponentList;
import org.awb.env.networkModel.persistence.NetworkModelFileContent;
import org.awb.env.networkModel.prototypes.ClusterGraphElement;
import org.awb.env.networkModel.prototypes.DistributionNode;
import org.awb.env.networkModel.prototypes.AbstractGraphElementPrototype;
import org.awb.env.networkModel.prototypes.StarGraphElement;
import org.awb.env.networkModel.settings.ComponentTypeSettings;
import org.awb.env.networkModel.settings.DomainSettings;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;
import org.awb.env.networkModel.settings.LayoutSettings;
import org.awb.env.networkModel.visualisation.DisplayAgent;

import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.simulationService.environment.DisplaytEnvironmentModel;
import de.enflexit.common.SerialClone;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.GraphIOException;


/**
 * The Environment Network Model. This class encapsulates a JUNG graph representing a grid, with edges representing the grid components.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkModel extends DisplaytEnvironmentModel {

	private static final long serialVersionUID = -5712689010090750522L;

	/** This attribute stores layout settings like the DomainSettings and the ComponentTypeSettings. */
	private GeneralGraphSettings4MAS generalGraphSettings4MAS;
	
	/** The layout (name) used for this NetworkModel. */
	private String layoutID;
	private TreeMap<String, MapSettings> mapSettingsTreeMap;

	
	/** The original JUNG graph created or imported in the application. */
	private Graph<GraphNode, GraphEdge> graph;
	/** HashMap that provides faster access to the GraphElement's. */
	private HashMap<String, GraphElement> graphElements;
	/** A list of all NetworkComponents in the NetworkModel, accessible by ID. */
	private TreeMap<String, NetworkComponent> networkComponents;
	/** A list of {@link GraphElement} (that are {@link GraphNode} or {@link GraphEdge}) mapped to one or more {@link NetworkComponent}'s */
	private transient HashMap<GraphElement, List<NetworkComponent>> graphElementToNetworkComponents;  
	
	/** The Hash of NetworkComponentAdapter. */
	private transient HashMap<String, NetworkComponentAdapter> networkComponentAdapterHash;
	
	/**
	 * This HashMap can hold alternative NetworkModel's that can be used to 
	 * reduce the complexity of the original graph (e.g after clustering). 
	 * The NetworkModel's placed in this HashMap will be also displayed 
	 * by the {@link DisplayAgent}.
	 */
	private HashMap<String, NetworkModel> alternativeNetworkModel;

	/** The outer network components of this NetworkModel with no Connections */
	private transient ArrayList<String> outerNetworkComponents;
	private transient int connectionsOfBiggestBranch;


	/**
	 * Default constructor.
	 */
	public NetworkModel() { }

	/**
	 * Sets the general graph settings for the MAS.
	 * @param generalGraphSettings4MAS the new general graph settings for the MAS
	 */
	public void setGeneralGraphSettings4MAS(GeneralGraphSettings4MAS generalGraphSettings4MAS) {
		this.generalGraphSettings4MAS = generalGraphSettings4MAS;
		this.resetGraphElementLayout();
	}
	/**
	 * Gets the general graph settings for the MAS.
	 * @return the general graph settings for the MAS
	 */
	public GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
		if (generalGraphSettings4MAS == null) {
			generalGraphSettings4MAS = new GeneralGraphSettings4MAS();
		}
		return generalGraphSettings4MAS;
	}

	// ------------------------------------------------------------------------
	// --- From here, the handling of the layout can be found -----------------
	// ------------------------------------------------------------------------
	/**
	 * Returns the layout ID, used by this NetworkModel.
	 * @return the current layout name
	 */
	public String getLayoutID() {
		if (layoutID==null) {
			layoutID = this.getGeneralGraphSettings4MAS().getLayoutIdByLayoutName(GeneralGraphSettings4MAS.DEFAULT_LAYOUT_SETTINGS_NAME);
		}
		return layoutID;
	}
	
	/**
	 * Sets the layout name to be used by this NetworkModel. If the ID is not defined in the graph settings, the layout ID of the 'Default Layout' will be used and returned. 
	 *
	 * @param newLayoutID the new layout name
	 * @return the layoutID that was actually assigned
	 * 
	 * @see GeneralGraphSettings4MAS#DEFAULT_LAYOUT_SETTINGS_NAME
	 */
	public String setLayoutID(String newLayoutID) {
		
		if (newLayoutID!=null && newLayoutID.equals(this.getLayoutID())==false) {

			// --- Check if the layout ID exists ------------------------------
			if (this.getGeneralGraphSettings4MAS().getLayoutSettings().get(newLayoutID)==null) {
				// --- Correct layoutID to default setting name ---------------
				System.err.println("[" + this.getClass().getSimpleName() +  "] Unknow layout ID '" + newLayoutID + "' - using ID of " + GeneralGraphSettings4MAS.DEFAULT_LAYOUT_SETTINGS_NAME + " instead.");
				newLayoutID = this.getGeneralGraphSettings4MAS().getLayoutIdByLayoutName(GeneralGraphSettings4MAS.DEFAULT_LAYOUT_SETTINGS_NAME);
			}
			// --- Finally, set new value -------------------------------------
			this.layoutID = newLayoutID;
		}
		return this.layoutID;
	}
	/**
	 * Sets the layout ID and exchanges the layout settings.
	 * @param newLayoutID the new layout ID exchange layout settings
	 */
	public void setLayoutIdAndExchangeLayoutSettings(String newLayoutID) {
		
		String layoutIdOld = this.getLayoutID();
		String layoutIdNew = this.setLayoutID(newLayoutID);
		if (layoutIdNew.equals(layoutIdOld)==true) return;
		
		// ----------------------------------------------------------------
		// --- Edit layout specific information of the GraphElements ------
		// ----------------------------------------------------------------
		List<GraphElement> graphElementList = new ArrayList<>(this.getGraphElements().values());
		for (int i = 0; i < graphElementList.size(); i++) {
			GraphElement graphElement = graphElementList.get(i);
			if (graphElement instanceof GraphNode) {
				// --- Change position of all GraphNode position ----------
				GraphNode graphNode = (GraphNode) graphElement;
				// --- Remind position in old layout ----------------------
				graphNode.getPositionTreeMap().put(layoutIdOld, graphNode.getPosition());
				// --- Check for a position in the new layout -------------
				Point2D newLayoutPosition = graphNode.getPositionTreeMap().get(layoutIdNew);
				if (newLayoutPosition!=null) {
					graphNode.setPosition(newLayoutPosition);
				}
				
			} else if (graphElement instanceof GraphEdge) {
				// --- Change shape configuration of GraphEdges -----------
				GraphEdge graphEdge = (GraphEdge) graphElement;
				// --- Remind current shape configuration -----------------
				graphEdge.getEdgeShapeConfigurationTreeMap().put(layoutIdOld, graphEdge.getEdgeShapeConfiguration());
				// --- Check for shape configuration for new layout -------  
				GraphEdgeShapeConfiguration<?> shapeConfig = graphEdge.getEdgeShapeConfigurationTreeMap().get(layoutIdNew);
				graphEdge.setEdgeShapeConfiguration(shapeConfig);
			}
		}
		
	}
	
	/**
	 * Returns the {@link LayoutSettings} that correspond to {@link #getLayoutID()}.
	 * @return the layout settings
	 */
	public LayoutSettings getLayoutSettings() {
		
		LayoutSettings ls = this.getGeneralGraphSettings4MAS().getLayoutSettings().get(this.getLayoutID());
		if (ls==null) {
			// --- Change to default layout settings ------
			System.err.println("[" + this.getClass().getSimpleName() + "] Could not find LayoutSettings with ID '" + this.getLayoutID() + "' - use defaults now.");
			String defaultLayoutID = this.generalGraphSettings4MAS.getLayoutIdByLayoutName(GeneralGraphSettings4MAS.DEFAULT_LAYOUT_SETTINGS_NAME);
			this.setLayoutID(defaultLayoutID);
			// --- Get the default layout settings --------
			ls = this.getGeneralGraphSettings4MAS().getLayoutSettings().get(this.getLayoutID());
		}
		return ls;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, the handling of the Map settings can be found -----------
	// ------------------------------------------------------------------------	
	/**
	 * Returns the tree map of layout-dependent {@link MapSettings}.
	 * @return the map settings tree map
	 */
	public TreeMap<String, MapSettings> getMapSettingsTreeMap() {
		if (mapSettingsTreeMap==null) {
			mapSettingsTreeMap = new TreeMap<>();
		}
		return mapSettingsTreeMap;
	}
	/**
	 * Sets the tree map of {@link MapSettings} .
	 * @param mapSettingsTreeMap the map settings tree map
	 */
	private void setMapSettingsTreeMap(TreeMap<String, MapSettings> mapSettingsTreeMap) {
		this.mapSettingsTreeMap = mapSettingsTreeMap;
	}
	
	/**
	 * Returns the {@link MapSettings} if the current layout is a geographical layout, otherwise
	 * the method returns <code>null</code>.
	 * @return the layout-dependent MapSettings
	 * @see #getLayoutSettings()
	 * @see LayoutSettings#isGeographicalLayout()
	 */
	public MapSettings getMapSettings() {
		
		String layoutID = this.getLayoutID();
		MapSettings ms = this.getMapSettingsTreeMap().get(layoutID);

		LayoutSettings layoutSettings = this.getLayoutSettings();
		if (layoutSettings.isGeographicalLayout()==true) {
			// --- If geographical layout, return instance of MapSettings -----
			if (ms==null) {
				ms = new MapSettings();
				this.getMapSettingsTreeMap().put(layoutID, ms);
			}
			return ms;
			
		} else {
			if (ms!=null) {
				// --- Clean-up the map settings hash -------------------------
				this.getMapSettingsTreeMap().remove(layoutID);
			}
			
		}
		return null;
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here, the handling of the Network model graph can be found ----
	// ------------------------------------------------------------------------
	/**
	 * Returns the JUNG graph.
	 * @return the JUNG Graph
	 */
	public Graph<GraphNode, GraphEdge> getGraph() {
		if (graph==null) {
			graph = new SparseGraph<GraphNode, GraphEdge>();
		}
		return graph;
	}
	/**
	 * Sets the the graph of the network model.
	 * @param newGraph the new graph
	 */
	public void setGraph(Graph<GraphNode, GraphEdge> newGraph) {
		this.graph = newGraph;
		this.resetGraphElements();
		this.refreshGraphElements();
	}
	
	/**
	 * Returns the GraphElement with the given ID, or null if not found.
	 * @param id The ID to look for
	 * @return the GraphElement
	 */
	public GraphElement getGraphElement(String id) {
		return this.getGraphElements().get(id);
	}
	/**
	 * Gets the graph elements.
	 * @return graphElements The hashmap of GraphElements
	 */
	public HashMap<String, GraphElement> getGraphElements() {
		if (graphElements==null) {
			graphElements = new HashMap<>();
		}
		return graphElements;
	}
	/**
	 * Reset the HashMap for the graph elements to null.
	 */
	private void resetGraphElements() {
		this.graphElements = null;
	}
	/**
	 * Gets the network components.
	 * @return the networkComponents
	 */
	public TreeMap<String, NetworkComponent> getNetworkComponents() {
		if (networkComponents==null) {
			networkComponents = new TreeMap<>();
		}
		return networkComponents;
	}
	/**
	 * Sets the network components.
	 * @param networkComponents the networkComponents to set
	 */
	public void setNetworkComponents(TreeMap<String, NetworkComponent> networkComponents) {
		this.networkComponents = networkComponents;
		this.refreshGraphElements();
	}
	/**
	 * This method gets the NetworkComponent with the given ID from the GridModel's networkComponents HashMap.
	 * 
	 * @param id The ID
	 * @return The NetworkComponent
	 */
	public NetworkComponent getNetworkComponent(String id) {
		if (id==null || id.isEmpty()) {
			return null;
		} else {
			return this.getNetworkComponents().get(id);	
		}
	}

	
	/**
	 * Returns the graph element to network component hash.
	 * @return the graph element to network component hash
	 */
	public synchronized HashMap<GraphElement, List<NetworkComponent>> getGraphElementToNetworkComponentHash() {
		if (graphElementToNetworkComponents==null) {
			graphElementToNetworkComponents = new HashMap<>();
			this.refreshGraphElements();	
		}
		return graphElementToNetworkComponents;
	}
	/**
	 * Clears the graph element to network component hash.
	 */
	public void clearGraphElementToNetworkComponentHash() {
		this.graphElementToNetworkComponents = new HashMap<>();
	}
	
	/**
	 * Adds a new relation reminder between a GraphElement and a NetworkComponent.
	 * @param graphElement the graph element
	 * @param networkComponent the network component
	 * @return true, if successful
	 */
	public boolean addGraphElementToNetworkComponentRelation(GraphElement graphElement, NetworkComponent networkComponent) {
		
		boolean done = false;
		if (graphElement==null) return false;
		if (networkComponent==null) return false;
		
		List<NetworkComponent> netComps = this.getGraphElementToNetworkComponentHash().get(graphElement);
		if (netComps==null) {
			netComps = new ArrayList<>();
			netComps.add(networkComponent);
			this.getGraphElementToNetworkComponentHash().put(graphElement, netComps);
			done = true;
			
		} else {
			if (netComps.contains(networkComponent)) {
				done = true;
			} else {
				done = netComps.add(networkComponent);
			}	
		}
		return done;
	}
	/**
	 * Removes a relation reminder between a GraphElement and a NetworkComponent.
	 * @param graphElement the graph element
	 * @param networkComponent the network component
	 */
	private boolean removeGraphElementToNetworkComponentRelation(GraphElement graphElement, NetworkComponent networkComponent) {
		
		boolean done = false;
		if (graphElement==null) return false;
		if (networkComponent==null) return false;
		
		List<NetworkComponent> netComps = this.getGraphElementToNetworkComponentHash().get(graphElement);
		if (netComps==null) {
			done = true;
		} else {
			if (netComps.contains(networkComponent)) {
				done = netComps.remove(networkComponent);	
			} else {
				done = true;
			}
			if (netComps.size()==0) {
				this.getGraphElementToNetworkComponentHash().remove(graphElement);
			}
		}
		return done;
	}
	/**
	 * Removes the specified GraphElement from the GraphElementToNetworkComponentHash.
	 * @param graphElement the {@link GraphElement} to remove
	 * @see #getGraphElementToNetworkComponentHash()
	 */
	public void removeGraphElementToNetworkComponent(GraphElement graphElement) {
		this.getGraphElementToNetworkComponentHash().remove(graphElement);
	}
	
	/**
	 * Sets the alternative network model.
	 * @param alternativeNetworkModel the alternativeNetworkModel to set
	 */
	public void setAlternativeNetworkModel(HashMap<String, NetworkModel> alternativeNetworkModel) {
		this.alternativeNetworkModel = alternativeNetworkModel;
	}
	/**
	 * Gets the alternative network models.
	 * @return the alternativeNetworkModel
	 */
	public HashMap<String, NetworkModel> getAlternativeNetworkModel() {
		if (alternativeNetworkModel == null) {
			alternativeNetworkModel = new HashMap<String, NetworkModel>();
		}
		return alternativeNetworkModel;
	}

	/**
	 * Creates a clone of the current instance.
	 * @return the copy
	 */
	public NetworkModel getCopy() {
		synchronized (this) {
			NetworkModel networkModelCopy = null;	
			try {
				networkModelCopy = SerialClone.clone(this);
				networkModelCopy.refreshGraphElements();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return networkModelCopy;
		} 
	}

	/**
	 * This method gets the GraphElements that are part of the given NetworkComponent
	 * 
	 * @param networkComponent The NetworkComponent
	 * @return The GraphElements
	 */
	public Vector<GraphElement> getGraphElementsFromNetworkComponent(NetworkComponent networkComponent) {
		Vector<GraphElement> elements = new Vector<GraphElement>();
		for (String graphElementID : networkComponent.getGraphElementIDs()) {
			GraphElement ge = this.getGraphElement(graphElementID);
			if (ge != null) {
				elements.add(ge);
			}
		}
		return elements;
	}

	/**
	 * Reloads the the GraphElementsMap.
	 */
	public void refreshGraphElements() {
		if (this.getGraph()!=null) {
			this.resetGraphElements();
			this.registerGraphElement(this.getGraph().getVertices().toArray(new GraphNode[0]));
			this.registerGraphElement(this.getGraph().getEdges().toArray(new GraphEdge[0]));
		}
		
		// --- Refresh the reminder of the relation between GraphElement and NetworkComonent ------ 
		this.clearGraphElementToNetworkComponentHash();
		List<NetworkComponent> netCompList = new ArrayList<NetworkComponent>(this.getNetworkComponents().values());
		for (int i = 0; i < netCompList.size(); i++) {
			NetworkComponent nc = netCompList.get(i);
			for (String graphElementID : nc.getGraphElementIDs()) {
				GraphElement ge = this.getGraphElements().get(graphElementID);
				if (ge==null) {
					System.err.println("[" + this.getClass().getSimpleName() + "] RefreshGraphElements: Could not find GraphElement '" + graphElementID + "' for NetworkComponent '" + nc.getId() + "'");
				} else {
					this.addGraphElementToNetworkComponentRelation(ge, nc);	
				}
			}
		}
	}

	/**
	 * Register all GraphElemnents used when adding a Component.
	 * @param graphElements the graph elements
	 */
	private void registerGraphElement(GraphElement[] graphElements) {
		for (GraphElement graphElement : graphElements) {
			this.getGraphElements().put(graphElement.getId(), graphElement);
		}
	}

	/**
	 * Adds a network component to the NetworkModel.
	 *
	 * @param networkComponent the network component
	 * @return the network component
	 */
	public NetworkComponent addNetworkComponent(NetworkComponent networkComponent) {
		return this.addNetworkComponent(networkComponent, true);
	}
	
	/**
	 * Adds a network component to the NetworkModel.
	 *
	 * @param networkComponent the network component
	 * @param refreshGraphElements set true, if the graph elements have to be refreshed
	 * @return the network component
	 */
	public NetworkComponent addNetworkComponent(NetworkComponent networkComponent, boolean refreshGraphElements) {
		this.getNetworkComponents().put(networkComponent.getId(), networkComponent);
		if (refreshGraphElements==true) {
			this.refreshGraphElements();
		}
		return networkComponent;
	}

	/**
	 * Rename a graph element (GraphNode or GraphEdge). In case that a GraphNode
	 * is renamed, the changes will also apply to all connected NetworkComponent's. 
	 *
	 * @param oldGraphNodeID the old GraphNode ID
	 * @param newGraphNodeID the new GraphNode ID
	 */
	public void renameGraphNode(String oldGraphNodeID, String newGraphNodeID) {
		
		if (oldGraphNodeID==null) return;
		if (newGraphNodeID==null || newGraphNodeID.length()==0) {
			throw new IllegalArgumentException(this.getClass().getSimpleName() + ": The new GraphNode ID is not allowed to be null or an empty String!");
		}
		if (newGraphNodeID.equals(oldGraphNodeID)==true) return;
		
		// --- Check if such an ID already exists ---------------------------------------
		GraphElement checkGraphElement = this.getGraphElement(newGraphNodeID);
		if (checkGraphElement!=null) {
			throw new IllegalArgumentException(this.getClass().getSimpleName() + ": A GraphElement with the ID '" + newGraphNodeID + "' already exists!");
		}
		
		// --- Rename the GraphNode -----------------------------------------------------
		GraphElement graphElement = this.getGraphElement(oldGraphNodeID);
		if (graphElement instanceof GraphNode) {
			
			// --- Look for NetworkComponents, that are knowing this graphElement -------
			List<NetworkComponent> netCompList = this.getNetworkComponents((GraphNode) graphElement);
			for (int i = 0; i < netCompList.size(); i++) {
				// --- Replace the old ID with the new one ------------------------------
				NetworkComponent component = netCompList.get(i);
				HashSet<String> compIDs = component.getGraphElementIDs();
				compIDs.remove(oldGraphNodeID);
				compIDs.add(newGraphNodeID);
			}
			
			this.getGraphElements().remove(oldGraphNodeID);
			graphElement.setId(newGraphNodeID);
			this.getGraphElements().put(newGraphNodeID, graphElement);
		}
		
	}

	/**
	 * Renames a NetworkComponent.
	 * 
	 * @param oldCompID the old ID of the NetworkComponent 
	 * @param newCompID the new ID of the NetworkComponent
	 */
	public void renameNetworkComponent(String oldCompID, String newCompID) {
		
		if (oldCompID==null) return;
		if (newCompID==null || newCompID.length()==0) {
			throw new IllegalArgumentException(this.getClass().getSimpleName() + ": The new NetworkComponent ID is not allowed to be null or an empty String!");
		}
		if (newCompID.equals(oldCompID)==true) return;
		
		// --- Check if such an ID already exists ---------------------------------------
		NetworkComponent checkNetworkComponent = this.getNetworkComponents().get(newCompID);
		if (checkNetworkComponent!=null) {
			throw new IllegalArgumentException(this.getClass().getSimpleName() + ": A NetworkComponent with the ID '" + newCompID + "' already exists!");
		}
		
		// --- Rename NetworkComponent --------------------------------------------------
		NetworkComponent networkComponent = this.getNetworkComponents().get(oldCompID);
		if (networkComponent!=null) {
			// --- Define a new HashSet for related GraphElements -----------------------
			HashSet<String> newGraphElementIDs = new HashSet<String>(networkComponent.getGraphElementIDs());
			// --- Rename the corresponding edges of the network component --------------
			for (String oldGraphElementID : networkComponent.getGraphElementIDs()) {
				
				// --- For edges only ! -------------------------------------------------
				GraphElement graphElement = this.getGraphElement(oldGraphElementID);
				if (graphElement instanceof GraphEdge) {
					// --- Define new edge ID -------------------------------------------
					String newGraphEdgeID = oldGraphElementID.replaceFirst(oldCompID, newCompID);
					if (newGraphEdgeID.equals(oldGraphElementID)==false) {
						// --- Delete old reference -----------------------------------------
						newGraphElementIDs.remove(oldGraphElementID);
						// --- rename the edges ---------------------------------------------
						this.getGraphElements().remove(oldGraphElementID);
						graphElement.setId(newGraphEdgeID);
						this.getGraphElements().put(newGraphEdgeID, graphElement);	
						// --- Add new reference --------------------------------------------
						newGraphElementIDs.add(newGraphEdgeID);	
					}					
				}
			}

			// --- Update the NetworkComponent ------------------------------------------
			networkComponent.setGraphElementIDs(newGraphElementIDs);
			networkComponent.setId(newCompID);
			this.getNetworkComponents().remove(oldCompID);
			this.getNetworkComponents().put(newCompID, networkComponent);
			
			// --- Update the NetworkComponent-Layout -----------------------------------
			for (String graphElementID : networkComponent.getGraphElementIDs()) {
				GraphElement graphElement = this.getGraphElements().get(graphElementID);
				if (graphElement!=null) {
					graphElement.resetGraphElementLayout(this);
				}
			}
		}
		
	}

	/**
	 * This method removes a NetworkComponent from the GridModel's networkComponents 
	 * HashMap, using its' ID as key.
	 * 
	 * @param networkComponent The NetworkComponent to remove
	 */
	public void removeNetworkComponent(NetworkComponent networkComponent) {
		this.removeNetworkComponent(networkComponent, true, true);
	}
	/**
	 * This method removes a NetworkComponent from the GridModel's networkComponents
	 * HashMap, using its' ID as key.
	 *
	 * @param networkComponent The NetworkComponent to remove
	 * @param removeDistributionNodes the remove distribution nodes
	 * @param refreshGraphElements true, if the graph elements have to be refreshed
	 */
	public void removeNetworkComponent(NetworkComponent networkComponent, boolean removeDistributionNodes, boolean refreshGraphElements) {

		// --- Make sure that the NetworkComponent is on its own ----
		Vector<GraphNodePairs> graphNodePairsToReMerge = new Vector<>();
		HashSet<GraphElement> graphElements = this.getGraphElementsOfNetworkComponent(networkComponent, new GraphNode());
		if (graphElements!=null) {
			for (GraphElement graphElement : graphElements) {
				GraphNode node = (GraphNode) graphElement;
				boolean isDistributionGraphNode = this.isDistributionNode(node);
				if (isDistributionGraphNode==false || (isDistributionGraphNode==true && removeDistributionNodes==true)) {
					List<NetworkComponent> networkComponents = this.getNetworkComponents(node);
					if (networkComponents.size() > 1) {
						graphNodePairsToReMerge.add(this.splitNetworkModelAtNode(node));
					}	
				}
			}	
		}
		
		// --- Remove the graph elements of this component ----------
		for (GraphElement graphElement : this.getGraphElementsFromNetworkComponent(networkComponent)) {

			if (graphElement instanceof GraphEdge) {
				this.getGraph().removeEdge((GraphEdge) graphElement);
				this.getGraphElements().remove(graphElement.getId());
				
			} else if (graphElement instanceof GraphNode) {
				GraphNode node = (GraphNode) graphElement;
				boolean isDistributionGraphNode = this.isDistributionNode(node);
				if (isDistributionGraphNode==false || (isDistributionGraphNode==true && removeDistributionNodes==true)) {
					this.getGraph().removeVertex((GraphNode) graphElement);
					this.getGraphElements().remove(graphElement.getId());
				}
				
			}
			this.removeGraphElementToNetworkComponentRelation(graphElement, networkComponent);

		}
		
		// --- Remove the NetworkComponent --------------------------
		this.getNetworkComponents().remove(networkComponent.getId());
		
		// --- Re-Merge the previously split nodes, if needed ------- 
		for (GraphNodePairs graphNodePairs : graphNodePairsToReMerge) {
			this.mergeNodes(graphNodePairs);
		}
		
		// --- Refresh the GraphElements ----------------------------
		if (refreshGraphElements==true) {
			this.refreshGraphElements();
		}
	}

	/**
	 * Removes a list of network components.
	 *
	 * @param networkComponents the network components
	 * @return the set of removed components 
	 */
	public List<NetworkComponent> removeNetworkComponents(List<NetworkComponent> networkComponents) {
		
		List<GraphNode> graphNodes2Remove = new ArrayList<>();
		List<GraphEdge> graphEdges2Remove = new ArrayList<>();
		
		for (int i = 0; i < networkComponents.size(); i++) {
		
			// --- Remove from the list of NetworkComponents --------
			NetworkComponent networkComponent = networkComponents.get(i);
			this.getNetworkComponents().remove(networkComponent.getId());
			
			// --- Get graph elements of the components -------------
			for (String graphElemID : networkComponent.getGraphElementIDs()) {
				GraphElement graphElement = this.getGraphElement(graphElemID);
				if (graphElement instanceof GraphNode) {
					if (graphNodes2Remove.contains(graphElement)==false) {
						graphNodes2Remove.add((GraphNode) graphElement);
					}
				} else if (graphElement instanceof GraphEdge) {
					if (graphEdges2Remove.contains(graphElement)==false) {
						graphEdges2Remove.add((GraphEdge) graphElement);
					}
				}
			}
		}
		
		// --- Remove edges from the graph --------------------------
		for (int i = 0; i < graphEdges2Remove.size(); i++) {
			GraphEdge graphEdge = graphEdges2Remove.get(i);
			this.getGraph().removeEdge(graphEdge);
		}
		// --- Remove edges from the graph --------------------------
		for (int i = 0; i < graphNodes2Remove.size(); i++) {
			GraphNode graphNode = graphNodes2Remove.get(i);
			if (this.getGraph().getIncidentEdges(graphNode).size()==0) {
				this.getGraph().removeVertex(graphNode);
			} 
		}
		
		this.refreshGraphElements();
		return networkComponents;
	}

	/**
	 * Removes all NetworkComponent's from the current NetworkModel, except of the specified ones.
	 *
	 * @param networkComponentsToKeep the network components to keep when deleting network components
	 * @return the hash set of removed NetworkComponents
	 */
	public List<NetworkComponent> removeNetworkComponentsInverse(List<NetworkComponent> networkComponentsToKeep) {
		// --- Get the inverse list of NetworkComponents ------------
		List<NetworkComponent> networkComponentsToRemove = this.getNetworkComponentsInverse(networkComponentsToKeep);
		// --- Now, call the regular remove method ------------------
		this.removeNetworkComponents(networkComponentsToRemove);
		return networkComponentsToRemove;
	}
	
	
	
	/**
	 * Gets the a node from network component.
	 * 
	 * @param networkComponent the network component
	 * @return the a node from network component
	 */
	public Vector<GraphNode> getNodesFromNetworkComponent(NetworkComponent networkComponent) {
		Vector<GraphNode> nodeList = new Vector<GraphNode>();
		for (String graphElementID : networkComponent.getGraphElementIDs()) {
			GraphElement graphElement = getGraphElements().get(graphElementID);
			if (graphElement instanceof GraphNode) {
				nodeList.add((GraphNode) graphElement);
			}
		}
		return nodeList;
	}

	/**
	 * Returns the neighbour NetworkComponent's based on a Vector of NetworkComponent's.
	 *
	 * @param networkComponents the network components
	 * @return the neighbour network components
	 */
	public Vector<NetworkComponent> getNeighbourNetworkComponents(Vector<NetworkComponent> networkComponents) {
		Vector<NetworkComponent> neighbourNetworkComponents = new Vector<NetworkComponent>();
		for (NetworkComponent networkComponent : networkComponents) {
			Vector<NetworkComponent> neighboursFound = getNeighbourNetworkComponents(networkComponent);
			for (NetworkComponent neighbour : neighboursFound) {
				if (neighbourNetworkComponents.contains(neighbour)==false) {
					neighbourNetworkComponents.add(neighbour);	
				}
			}
		}
		return neighbourNetworkComponents;
	}

	/**
	 * Returns the neighbour NetworkComponent's of a single NetworkComponent.
	 * 
	 * @param networkComponent the network component
	 * @return the neighbour network components
	 */
	public Vector<NetworkComponent> getNeighbourNetworkComponents(NetworkComponent networkComponent) {
		Vector<NetworkComponent> comps = null;
		if (networkComponent!=null) {
			comps = new Vector<NetworkComponent>();
			for (GraphNode node : this.getNodesFromNetworkComponent(networkComponent)) {
				List<NetworkComponent> componetsFound = this.getGraphElementToNetworkComponentHash().get(node);
				for (int i = 0; i < componetsFound.size(); i++) {
					NetworkComponent nc = componetsFound.get(i);
					if (nc!= networkComponent && comps.contains(nc)==false) {
						comps.add(nc);
					}
				}
			}			
		}
		return comps;
	}

	/**
	 * Returns all {@link NetworkComponent}s of the network model ascending sorted by the numeric value of the ID of the component.
	 * @return an ascending sorted NetworkComponent vector sorted
	 */
	public Vector<NetworkComponent> getNetworkComponentVectorSorted() {
		return this.getNetworkComponentVectorSorted(true);
	}
	/**
	 * Returns all {@link NetworkComponent}s of the network model sorted by the numeric value of the ID of the component.
	 * @param ascending set true to sort ascending, false to sort descending
	 * @return a sorted NetworkComponent vector sorted
	 */
	public Vector<NetworkComponent> getNetworkComponentVectorSorted(boolean ascending) {
		
		Vector<NetworkComponent> netCompVector = new Vector<NetworkComponent>(this.getNetworkComponents().values());
		if (ascending==true) {
			Collections.sort(netCompVector);
		} else {
			Collections.sort(netCompVector, Collections.reverseOrder());
		}
		return netCompVector;
	}
	
	/**
	 * Gets the network component by graph edge id.
	 * 
	 * @param graphEdge the graph edge
	 * @return the network component by graph edge id
	 */
	public NetworkComponent getNetworkComponent(GraphEdge graphEdge) {
		
		NetworkComponent ncFound = null;
		List<NetworkComponent> nCompsFound = this.getGraphElementToNetworkComponentHash().get(graphEdge);
		if (nCompsFound!=null && nCompsFound.isEmpty()==false) {
			// --- Can be just on result ----------------------------
			ncFound = nCompsFound.iterator().next();
			
		} else {
			// --- Search manually ----------------------------------
			List<NetworkComponent> netCompList = new ArrayList<NetworkComponent>(this.getNetworkComponents().values());
			for (int i = 0; i < netCompList.size(); i++) {
				NetworkComponent networkComponent = netCompList.get(i);
				if (networkComponent.getGraphElementIDs().contains(graphEdge.getId())) {
					ncFound = networkComponent;
					break;
				}
			}	
		}
		return ncFound;
	}

	/**
	 * Gets all networkComponents contained in a GraphNode Set
	 *
	 * @param graphNodes the graph nodes
	 * @return the network components
	 */
	public List<NetworkComponent> getNetworkComponents(Set<GraphNode> graphNodes) {
		HashSet<NetworkComponent> netCompHashSet = new HashSet<>();
		List<GraphNode> graphNodeList = new ArrayList<>(graphNodes);
		for (int i = 0; i < graphNodeList.size(); i++) {
			GraphNode graphNode = graphNodeList.get(i);
			netCompHashSet.addAll(this.getNetworkComponents(graphNode));
		}
		return new ArrayList<>(netCompHashSet);
	}

	/**
	 * Returns the {@link NetworkComponent}'s that are fully selected by the given set of GraphNodes.<br>
	 * As an example: if you have selected one vertex of a simple directed edge with two vertices, this
	 * method will return null.  
	 *
	 * @param graphNodesSelected the set of selected {@link GraphNode}'s
	 * @return the {@link NetworkComponent}'s that are fully selected by the given nodes and edges
	 */
	public List<NetworkComponent> getNetworkComponentsFullySelected(Set<GraphNode> graphNodesSelected) {
		
		List<NetworkComponent> componentsFound = new ArrayList<>();
		List<NetworkComponent> componentsAffected = this.getNetworkComponents(graphNodesSelected);

		// --- Take affected components and run through them --------
		for (int i = 0; i < componentsAffected.size(); i++) {
		
			NetworkComponent networkComponent = componentsAffected.get(i);
			
			// --- Get all GraphNodes of the NetworkComponent -------
			int graphElementsInSelection = 0; 
			HashSet<GraphElement> graphElementsOfNetworkComponent = this.getGraphElementsOfNetworkComponent(networkComponent, new GraphNode());
			for (GraphElement graphNodeOfNetworkComponent : graphElementsOfNetworkComponent) {
				// --- Increase counter, if in selection ------------ 
				if (graphNodesSelected.contains(graphNodeOfNetworkComponent)) {
					graphElementsInSelection++;
				}
			}
			// --- If all GraphNodes of a component were selected ---
			if (graphElementsInSelection==graphElementsOfNetworkComponent.size() && componentsFound.contains(networkComponent)==false) {
				componentsFound.add(networkComponent);
			}
		}
		
		if (componentsFound.size()==0) componentsFound = null;
		return componentsFound;
	}

	/**
	 * Returns an 'inverse' list of network components with respect to all NetworkComponents in the NetworkModel.
	 * The result will be equal to <b>All NetworkComponents in the NetworkModel</b> minus <b>the specified list of NetworkComponents</b>.  
	 *
	 * @param networkComponentListToInvert the network component list to invert
	 * @return the resulting inverse network component list 
	 */
	public List<NetworkComponent> getNetworkComponentsInverse(List<NetworkComponent> networkComponentListToInvert) {

		List<NetworkComponent> networkComponentsToRemove = new ArrayList<>();
		
		// --- Create the list of NetworkComponets to remove -------- 
		HashSet<String> networkComponentIDs = this.getNetworkComponentsIDs(networkComponentListToInvert);
		List<NetworkComponent> netCompList = new ArrayList<>(this.getNetworkComponents().values());
		for (int i = 0; i < netCompList.size(); i++) {
			NetworkComponent networkComponent = netCompList.get(i);
			if (networkComponentIDs.contains(networkComponent.getId())==false) {
				networkComponentsToRemove.add(networkComponent);
			}
		}
		return networkComponentsToRemove;
	}
	/**
	 * Extracts Network IDs from NetworkComponenList and returns an ID List.
	 * @param networkComponents the network components
	 * @return HashSet<String> of the IDs
	 */
	private HashSet<String> getNetworkComponentsIDs(List<NetworkComponent> networkComponents) {
		HashSet<String> networkComponentIDs = new HashSet<>();
		for (int i = 0; i < networkComponents.size(); i++) {
			networkComponentIDs.add(networkComponents.get(i).getId());
		}
		return networkComponentIDs;
	}
	
	
	/**
	 * Extracts the ID's of the GraphElements of the specified NetworkComponent. The parameter 'searchForInstance' can be
	 * an exemplary instance of {@link GraphNode}, {@link GraphEdge} or null. This can be seen as a filter. 
	 * If a {@link GraphNode} is specified, the method will return all GraphNodes of the {@link NetworkComponent}. 
	 * The same applies for an instance of {@link GraphEdge}, while with Null the method will returns all
	 * {@link GraphElement}'s.
	 *
	 * @param networkComponent the network component
	 * @param searchForInstance the search for instance
	 * @return the hash set
	 */
	public HashSet<String> extractGraphElementIDs(NetworkComponent networkComponent, GraphElement searchForInstance) {
		
		// --- Create the result set -----------------
		HashSet<String> elementIDsFound = new HashSet<String>();
		if (networkComponent==null) return elementIDsFound;
		
		// --- Check the GraphElements ---------------
		Set<String> elements = networkComponent.getGraphElementIDs();
		for (String elementID : elements) {
			GraphElement element = this.getGraphElement(elementID);
			if (element!=null) {
				if (searchForInstance==null) {
					// --- search for everything -----
					elementIDsFound.add(elementID);
				} else if (searchForInstance instanceof GraphNode) {
					// --- search for GraphNode's ----
					if (element instanceof GraphNode) {
						elementIDsFound.add(elementID);
					}
				} else if (searchForInstance instanceof GraphEdge) {
					// --- search for GraphEdge's ----
					if (element instanceof GraphEdge) {
						elementIDsFound.add(elementID);
					}
				}
			}
		}
		return elementIDsFound;
	}
	
	/**
	 * Returns the graph elements of a specified NetworkComponent. The parameter 'searchForInstance' can be
	 * an exemplary instance of {@link GraphNode}, {@link GraphEdge} or null. This can be seen as a filter. 
	 * If a {@link GraphNode} is specified, the method will return all GraphNodes of the {@link NetworkComponent}. 
	 * The same applies for an instance of {@link GraphEdge}, while with Null the method will returns all
	 * {@link GraphElement}'s.
	 *
	 * @param networkComponent the network component
	 * @param searchForInstance the search for instance
	 * @return the graph elements of network component
	 */
	public HashSet<GraphElement> getGraphElementsOfNetworkComponent(NetworkComponent networkComponent, GraphElement searchForInstance) {
		
		HashSet<GraphElement> graphElements = null;
		HashSet<String> graphElementIDs = this.extractGraphElementIDs(networkComponent, searchForInstance);
		if (graphElementIDs!=null & graphElementIDs.size()!=0) {
			graphElements = new HashSet<GraphElement>();
			for (String graphElementID : graphElementIDs) {
				GraphElement graphElement = this.getGraphElement(graphElementID);
				if (graphElement!=null) {
					if (graphElements==null) {
						graphElements = new HashSet<GraphElement>();
					}
					graphElements.add(graphElement);
				}
			}
		}
		return graphElements;
	}
	
	/**
	 * Return the list of {@link NetworkComponent}s that contain the given node.
	 *
	 * @param graphNode the GraphNode to check
	 * @return the network components
	 */
	public List<NetworkComponent> getNetworkComponents(GraphNode graphNode) {
		
		List<NetworkComponent> networkComponentsFound = this.getGraphElementToNetworkComponentHash().get(graphNode);
		if (networkComponentsFound==null) {
			// --- In case that it could not be found, search manually --------
			networkComponentsFound = new ArrayList<>();
			NetworkComponent[] netComps = new NetworkComponent[this.getNetworkComponents().values().size()];
			this.getNetworkComponents().values().toArray(netComps);
			for (int i = 0; i < netComps.length; i++) {
				NetworkComponent networkComponent = netComps[i];
				if (networkComponent.getGraphElementIDs().contains(graphNode.getId())) {
					networkComponentsFound.add(networkComponent);
				}
			}
			
		}
		return networkComponentsFound;
	}

	/**
	 * Generates the next unique network component ID in the series n1, n2, n3, ...
	 * @return the next unique network component ID
	 */
	public String nextNetworkComponentID() {
		// --- Finds the current maximum network component ID and returns the next one to it. -----
		int startInt = this.getNetworkComponents().size();
		while (this.getNetworkComponents().get((GeneralGraphSettings4MAS.PREFIX_NETWORK_COMPONENT + startInt)) != null) {
			startInt++;
		}
		return GeneralGraphSettings4MAS.PREFIX_NETWORK_COMPONENT + (startInt);
	}

	/**
	 * Generates the next unique node ID in the series PP0, PP1, PP2, ...
	 * @return String The next unique node ID that can be used.
	 */
	public String nextNodeID() {
		return this.nextNodeID(false);
	}
	/**
	 * Generates the next unique node ID in the series PP0, PP1, PP2, ...
	 * 
	 * @param skipNullEntries the skip null entries
	 * @return String The next unique node ID that can be used.
	 */
	private String nextNodeID(boolean skipNullEntries) {
	
		// Finds the current maximum node ID and returns the next one to it.
		long max = -1;
		boolean errEntry = false;
	
		Collection<GraphNode> nodeCollection = getGraph().getVertices();
		GraphNode[] nodes = nodeCollection.toArray(new GraphNode[0]);
		for (int i = 0; i < nodes.length; i++) {
			GraphNode node = nodes[i];
			String id = node.getId();
			errEntry = (id == null || id.equals("null")) ? true : false;
	
			if (errEntry == true && skipNullEntries == false) {
				id = this.nextNodeID(true);
				node.setId(id);
				errEntry = false;
			}
			// --- normal operation -------------
			if (errEntry == false) {
				Long num = extractNumericalValue(id);
				if (num!=null && num > max) {
					max = num;
				}
			}
		}
		return GraphNode.GRAPH_NODE_PREFIX + (max + 1);
	}

	/**
	 * Corrects the name definitions of a supplement NetworkModel in  
	 * order to avoid name clashes with the current NetworkModel.
	 *
	 * @param supplementNetworkModel a supplement NetworkModel
	 * @return the NetworkModel with corrected names
	 */
	public NetworkModel adjustNameDefinitionsOfSupplementNetworkModel(NetworkModel supplementNetworkModel) {

		// --- Early exit of this adjustment method? --------------------------
		if (supplementNetworkModel==this) return supplementNetworkModel;
		if (this.getGraphElementToNetworkComponentHash().size()==0 && this.getGraphElements().size()==0) return supplementNetworkModel;

		
		// --- Get the general counting information for components and edges --
		String nextCompID = this.nextNetworkComponentID().replace(GeneralGraphSettings4MAS.PREFIX_NETWORK_COMPONENT, "");
		String nextNodeID = this.nextNodeID().replace(GraphNode.GRAPH_NODE_PREFIX, "");
		long nextCompIDCounter = Long.parseLong(nextCompID);
		long nextNodeIDCounter = Long.parseLong(nextNodeID);

		
		// --- Make sure that the information are up to date ------------------
		supplementNetworkModel.refreshGraphElements();
		
		// --- Get and rename the list of GraphNodes --------------------------
		Graph<GraphNode, GraphEdge> graph = supplementNetworkModel.getGraph();
		Vector<GraphNode> nodes = new Vector<GraphNode>(graph.getVertices());
		// --- Change node names and positions --------------------------------
		String newNodeID = GraphNode.GRAPH_NODE_PREFIX + nextNodeIDCounter;
		for (GraphNode graphNode : nodes) {
			
			// --- Find new GraphNodeID ---------------------------------------
			while (this.getGraphElement(newNodeID)!=null || supplementNetworkModel.getGraphElement(newNodeID)!=null) {
				nextNodeIDCounter++;
				newNodeID = GraphNode.GRAPH_NODE_PREFIX + nextNodeIDCounter;
			}
			
			// --- Configure new name -----------------------------------------
			String oldNodeID = graphNode.getId();
			graphNode.setId(newNodeID);
			supplementNetworkModel.getGraphElements().remove(oldNodeID);
			supplementNetworkModel.getGraphElements().put(newNodeID, graphNode);
			
			// --- Change to new ID also in the other affected components ----- 
			List<NetworkComponent> netComps = supplementNetworkModel.getGraphElementToNetworkComponentHash().get(graphNode);
			if (netComps!=null) {
				for (int i = 0; i < netComps.size(); i++) {
					NetworkComponent netComp = netComps.get(i);
					netComp.getGraphElementIDs().remove(oldNodeID);
					netComp.getGraphElementIDs().add(newNodeID);
				}	
			}

		}
		
		// --- Get the list of NetworkComponents ------------------------------
		String newCompID = GeneralGraphSettings4MAS.PREFIX_NETWORK_COMPONENT + nextCompIDCounter;
		NetworkComponent[] netComps = new NetworkComponent[supplementNetworkModel.getNetworkComponents().values().size()]; 
		supplementNetworkModel.getNetworkComponents().values().toArray(netComps);
		for (int i = 0; i < netComps.length; i++) {
			
			// --- Find new NetworkComponentID --------------------------------
			while (this.getNetworkComponent(newCompID)!=null || supplementNetworkModel.getNetworkComponent(newCompID)!=null) {
				nextCompIDCounter++;
				newCompID = GeneralGraphSettings4MAS.PREFIX_NETWORK_COMPONENT + nextCompIDCounter;
			}
			
			// --- Configure new name -----------------------------------------
			NetworkComponent nc = netComps[i];
			String oldCompID = nc.getId();
			nc.setId(newCompID);
			supplementNetworkModel.getNetworkComponents().remove(oldCompID);
			supplementNetworkModel.getNetworkComponents().put(newCompID, nc);
			
			HashSet<GraphElement> edgesFound = supplementNetworkModel.getGraphElementsOfNetworkComponent(nc, new GraphEdge(null, null));
			if (edgesFound!=null && edgesFound.size()>0) {
				for (GraphElement element : edgesFound) {
					
					GraphEdge edge = (GraphEdge) element;
					String edgeIDOld = edge.getId();
					String edgeIDNew = edge.getId().replace(oldCompID, newCompID);
					edge.setId(edgeIDNew);
					
					supplementNetworkModel.getGraphElements().remove(edgeIDOld);
					supplementNetworkModel.getGraphElements().put(edgeIDNew, edge);
					
					List <NetworkComponent> netCompsToChange = supplementNetworkModel.getGraphElementToNetworkComponentHash().get(edge);
					for (int j = 0; j < netCompsToChange.size(); j++) {
						NetworkComponent netCompToChange = netCompsToChange.get(j);
						netCompToChange.getGraphElementIDs().remove(edgeIDOld);
						netCompToChange.getGraphElementIDs().add(edgeIDNew);
					}
				}	
			}
			
		}
		supplementNetworkModel.refreshGraphElements();
		return supplementNetworkModel;
	}

	/**
	 * Extract the numerical value from a String.
	 * @param expression the expression
	 * @return the integer value
	 */
	private Long extractNumericalValue(String expression) {
		String numericString = "";
		Long numeric = new Long(-1);
		for (int i = 0; i < expression.length(); i++) {
			String letter = Character.toString(expression.charAt(i));
			if (letter.matches("[0-9]")) {
				numericString += letter;
			}
		}
		if (numericString.equals("") == false) {
			try {
				numeric = Long.parseLong(numericString);	
			} catch (Exception e) {
				numeric = new Long(-1);
			}
			
		}
		return numeric;
	}
	
	/**
	 * Merge two Clusters which are part of the same model and connected to each other
	 *
	 * @param clusterNC the cluster nc
	 * @param supplementNC the supplement nc
	 */
	public void mergeClusters(ClusterNetworkComponent clusterNC, ClusterNetworkComponent supplementNC) {
		GraphNode centerOfCluster = null;
		for (GraphNode graphNode : getNodesFromNetworkComponent(clusterNC)) {
			if (isCenterNodeOfStar(graphNode, clusterNC)) {
				centerOfCluster = graphNode;
			}
		}

		// get all Nodes which remain as connections, remove the rest
		Vector<GraphElement> graphElements = getGraphElementsFromNetworkComponent(supplementNC);
		for (GraphElement graphElement : new ArrayList<GraphElement>(graphElements)) {
			if (graphElement instanceof GraphEdge) {
				graphElements.remove(graphElement);
			}
			if (graphElement instanceof GraphNode) {
				GraphNode graphNode = (GraphNode) graphElement;
				List<NetworkComponent> components = getNetworkComponents(graphNode);
				if (components.size() == 1 && components.contains(supplementNC)) {
					graphElements.remove(graphNode);
				}
				if (getNetworkComponents(graphNode).contains(clusterNC)) {
					clusterNC.getGraphElementIDs().remove(graphNode.getId());
					this.getGraph().removeVertex(graphNode);
					graphElements.remove(graphNode);
				}
			}
		}
		// add new Edges
		HashSet<String> graphElementIDs = clusterNC.getGraphElementIDs();
		int counter = 0;
		for (GraphElement graphElement : graphElements) {
			while( graphElementIDs.contains(clusterNC +"_" + counter) ){
				counter++;
			}
			GraphEdge edge = new GraphEdge(clusterNC + "_" + counter, GeneralGraphSettings4MAS.NETWORK_COMPONENT_TYPE_4_CLUSTER);
			this.getGraph().addEdge(edge, centerOfCluster, (GraphNode) graphElement, EdgeType.UNDIRECTED);
			graphElementIDs.add(edge.getId());
			graphElementIDs.add(graphElement.getId());
		}
		
		removeNetworkComponent(supplementNC);
		clusterNC.getClusterNetworkModel().replaceClusterByComponents(supplementNC);
	}

	/**
	 * Merges the current NetworkModel with an incoming NetworkModel as supplement.
	 *
	 * @param supplementNetworkModel the supplement network model
	 * @param nodes2Merge the nodes2 merge
	 * @return the graph node pairs
	 */
	public GraphNodePairs mergeNetworkModel(NetworkModel supplementNetworkModel, GraphNodePairs nodes2Merge) {
		return this.mergeNetworkModel(supplementNetworkModel, nodes2Merge, true);	
	}
	
	/**
	 * Merges the current NetworkModel with an incoming NetworkModel as supplement.
	 *
	 * @param supplementNetworkModel the supplement network model
	 * @param nodes2Merge the merge description
	 * @param adjustNameDefinitions the adjust name definitions
	 * @return the residual GraphNode, which connects the two NetworkModel's
	 */
	public GraphNodePairs mergeNetworkModel(NetworkModel supplementNetworkModel, GraphNodePairs nodes2Merge, boolean adjustNameDefinitions) {

		NetworkModel srcNM = supplementNetworkModel;
		
		// --- 1. Adjust the names of the supplement NetworkModel, in order to avoid name clashes -
		if (adjustNameDefinitions==true) {
			srcNM = this.adjustNameDefinitionsOfSupplementNetworkModel(supplementNetworkModel);	
		}

		// --- 2. Add the new graph to the current graph ------------------------------------------
		Graph<GraphNode, GraphEdge> suppGraph = supplementNetworkModel.getGraph();

		// --- 3. Run through the list of new NetworkComponents -----------------------------------
		for (String netCompName : srcNM.getNetworkComponents().keySet()) {
			// --- Get the network component ------------------------------------------------------
			NetworkComponent nc = srcNM.getNetworkComponents().get(netCompName);

			// --- Get the graph elements of this NetworkComponent -------------------------------- 
			Vector<GraphElement> graphElements = supplementNetworkModel.getGraphElementsFromNetworkComponent(nc);
			for (int run=1; run<=2; run++) {
				// --- Run twice through this list of graphElements -------------------------------
				for (int i = 0; i < graphElements.size(); i++) {
					if (run==1 && graphElements.get(i) instanceof GraphNode) {
						// --------------------------------------------------------------
						// --- First run: GraphNodes ------------------------------------
						// --------------------------------------------------------------
						GraphNode node = (GraphNode)graphElements.get(i);
						if (this.getGraphElements().get(node.getId())==null) {
							// --- GraphNode locally new --------------------------------
							this.getGraph().addVertex(node);	
							this.getGraphElements().put(node.getId(), node);
						} else {
							// --- GraphNode locally available --------------------------
							node = (GraphNode) this.getGraphElements().get(node.getId());
						}
						this.addGraphElementToNetworkComponentRelation(node, nc);
						
					} else if (run==2 && graphElements.get(i) instanceof GraphEdge) {
						// --------------------------------------------------------------
						// --- Second run: GraphEdge ------------------------------------
						// --------------------------------------------------------------
						GraphEdge edge = (GraphEdge) graphElements.get(i);
						GraphNode node1 = (GraphNode) this.getGraphElements().get(suppGraph.getEndpoints(edge).getFirst().getId());
						GraphNode node2 = (GraphNode) this.getGraphElements().get(suppGraph.getEndpoints(edge).getSecond().getId());
						this.getGraph().addEdge(edge, node1, node2, suppGraph.getEdgeType(edge));
						this.getGraphElements().put(edge.getId(), edge);
						this.addGraphElementToNetworkComponentRelation(edge, nc);
						
					}
				}	
			} 
			// --- Add this network component 
			this.addNetworkComponent(srcNM.getNetworkComponents().get(netCompName), false);
		}
		
		// --- 4. Merge the specified nodes -------------------------------------------------------
		return this.mergeNodes(nodes2Merge);

	}

	/**
	 * Gets the valid configuration for a GraphNodePair, that can be used for merging nodes.
	 *
	 * @param graphNodePairs the graph node pairs
	 * @return the valid GraphNodePair for merging couples of GraphNodes
	 */
	public GraphNodePairs getValidGraphNodePairConfig4Merging(GraphNodePairs graphNodePairs) {
		
		GraphNodePairs validConfig = null;
		HashSet<GraphNode> nodes2Merge = new HashSet<GraphNode>();
		HashSet<GraphNode> distributionNodes = new HashSet<GraphNode>();
		
		// --- Get the first component --------------------
		if (containsDistributionNode(this.getNetworkComponents(graphNodePairs.getGraphNode1()))==true) {
			distributionNodes.add(graphNodePairs.getGraphNode1());
		}
		nodes2Merge.add(graphNodePairs.getGraphNode1());
		// --- Get all other components -------------------
		for (GraphNode node : graphNodePairs.getGraphNode2Hash()) {
			if (containsDistributionNode(this.getNetworkComponents(node))==true) {
				distributionNodes.add(node);
			}
			nodes2Merge.add(node);
		}
		
		// ------------------------------------------------
		// --- Validate current configuration -------------
		// ------------------------------------------------
		if (distributionNodes.size()==0) {
			// --- Not more than two nodes can be merged --
			if (nodes2Merge.size()==1) {
				// Nothing to merge -----------------------
				return null;
			} else if (nodes2Merge.size()>2) {
				// Without DistributionNode, not more ----- 
				// than two nodes can be merged       -----
				return null;
			}
			return graphNodePairs;
			
		} else if (distributionNodes.size()>1) {
			// --- Found more than one DistributionNode ---
			// --- That is not a valid configuration    ---
			return null;
			
		} 
		
		// --------------------------------------------------------------------
		// --- Is the single DistributionNode on the GraphNode1 position ? ----
		// --------------------------------------------------------------------
		GraphNode distributionNode = distributionNodes.iterator().next(); 
		if (graphNodePairs.getGraphNode1()==distributionNode) {
			// --- That is OK ---------------------------------------
			return graphNodePairs;
		} 

		// --- In case of merging, a DistributionNode should -------- 
		// --- always on the graphNode1 position             --------
		nodes2Merge.remove(distributionNode);
		validConfig = new GraphNodePairs(distributionNode, nodes2Merge);
		return validConfig;
	}
	
	
	/**
	 * Merges the network model by using at least two (selected) nodes.
	 *
	 * @param nodes2Merge the nodes that have to be merge, as GraphNodePairs
	 * @return the residual GraphNode, after the merge process
	 */
	public GraphNodePairs mergeNodes(GraphNodePairs nodes2Merge) {

		// --- Preliminary check ----------------------------------------------
		if (nodes2Merge==null)return null;
		if (nodes2Merge.getGraphNode1()==null) return null;
		if (nodes2Merge.getGraphNode2Hash()==null) return null;
		
		// --- Have a look to the case of one or more DistributionNode's ------
		nodes2Merge = this.getValidGraphNodePairConfig4Merging(nodes2Merge);
		if (nodes2Merge==null) return null;
		
		// --- Create revert information --------------------------------------
		HashSet<GraphNodePairsRevert> revertInfos = new HashSet<GraphNodePairsRevert>();

		// --------------------------------------------------------------------
		// --- Get first GraphNode and NetworkCommponent ----------------------
		GraphNode graphNode1 = (GraphNode) this.getGraphElement(nodes2Merge.getGraphNode1().getId());
		if (graphNode1==null) return null;
		NetworkComponent comp1 = this.getNetworkComponents(graphNode1).iterator().next();
		
		// --------------------------------------------------------------------
		// --- Walk through the list of GraphNode that have to be merged ------
		for (GraphNode graphNode2 : nodes2Merge.getGraphNode2Hash() ) {
			
			// --- Make sure that this is a current GraphNode -----------------
			graphNode2 = (GraphNode) this.getGraphElement(graphNode2.getId());
			if (graphNode2==null) continue;
			NetworkComponent comp2 = this.getNetworkComponents(graphNode2).iterator().next();

			// --- Find the intersection set of the Graph elements of the two NetworkComponent
			// --- NetworkComponent in order to make sure that they are not already connected 
			HashSet<String> intersection = new HashSet<String>(comp1.getGraphElementIDs());
			intersection.retainAll(comp2.getGraphElementIDs());
			// Checking the constraint - Two network components can have maximum one node in common
			if (intersection.size() == 0) {
				// --- No intersection node found - proceed -------------------
				for (GraphEdge edgeOld : this.getGraph().getIncidentEdges(graphNode2)) {
					// --- switch connection to graphNode1 ----------
					GraphEdge edgeNew = this.switchEdgeBetweenGraphNodes(edgeOld, graphNode1, graphNode2);
					this.removeGraphElementToNetworkComponent(edgeOld);
					this.addGraphElementToNetworkComponentRelation(edgeNew, comp2);
					
					// --- store revert information -----------------
					GraphNodePairsRevert revert = new GraphNodePairsRevert(graphNode2, edgeNew);
					revertInfos.add(revert);
				}
				// --- Updating the graph element IDs of the component --------
				comp2.getGraphElementIDs().remove(graphNode2.getId());
				comp2.getGraphElementIDs().add(graphNode1.getId());
				this.addGraphElementToNetworkComponentRelation(graphNode1, comp2);
				
				// --- Removing node2 from the graph and network model --------
				this.getGraph().removeVertex(graphNode2);
				this.getGraphElements().remove(graphNode2.getId());
			}
		}
		
		nodes2Merge.setRevertInfos(revertInfos);
		return nodes2Merge;
	}

	/**
	 * Merge nodes revert.
	 *
	 * @param nodes2Merge the nodes2 merge
	 */
	public void mergeNodesRevert(GraphNodePairs nodes2Merge) {
	
		if (nodes2Merge==null) return;
		if (nodes2Merge.getGraphNode1()==null) return;
		if (nodes2Merge.getGraphNode2Hash()==null) return;
		
		// --------------------------------------------------------------------
		// --- Walk through the list of revert informations -------------------
		
		GraphNode mergedGraphNode = (GraphNode) this.getGraphElement(nodes2Merge.getGraphNode1().getId());
		for (GraphNodePairsRevert revertInfo : nodes2Merge.getRevertInfos() ) {
			
			GraphEdge graphEdge = (GraphEdge) this.getGraphElement(revertInfo.getGraphEdge().getId());
			GraphNode graphNode = (GraphNode) this.getGraphElement(revertInfo.getGraphNode().getId());
			if (graphNode==null) {
				graphNode = revertInfo.getGraphNode();
				this.getGraph().addVertex(graphNode);
			}
			this.switchEdgeBetweenGraphNodes(graphEdge, graphNode, mergedGraphNode);
			
			NetworkComponent comp1 = this.getNetworkComponent(graphEdge);
			comp1.getGraphElementIDs().remove(mergedGraphNode.getId());
			comp1.getGraphElementIDs().add(graphNode.getId());
			this.getGraphElements().put(graphNode.getId(), graphNode);
		}
		
	}
	
	/**
	 * Splits the network model at a specified node.
	 * @param node2SplitAt the node
	 * @return the GraphNodePairs that can be used to undo this operation
	 */
	public GraphNodePairs splitNetworkModelAtNode(GraphNode node2SplitAt) {
		return this.splitNetworkModelAtNode(node2SplitAt, false);
	}
	/**
	 * Splits the network model at a specified node.
	 *
	 * @param node2SplitAt the GraphNode where the NetworkModel should be split
	 * @param moveOppositeNode the move opposite node
	 * @return the GraphNodePairs that can be used to undo this operation
	 */
	public GraphNodePairs splitNetworkModelAtNode(GraphNode node2SplitAt, boolean moveOppositeNode) {

		GraphNodePairs graphNodePair = null;
		HashSet<GraphNode> graphNodeConnections = new HashSet<GraphNode>();
		
		// --- Get the components containing the node -------------------------
		List<NetworkComponent> netCompList = this.getNetworkComponents(node2SplitAt);
		// --- Sort the list of component: ------------------------------------
		// --- If the component list contains a DistributionNode, -------------
		// --- this component should be the last one in the list! -------------
		Vector<NetworkComponent> netCompVector = this.getNetworkComponentVectorWithDistributionNodeAsLast(netCompList);
		NetworkComponent lastNetComp = netCompVector.get(netCompVector.size()-1);
		if (this.isDistributionNode(lastNetComp)==true) {
			moveOppositeNode = true;
		}
		
		// --- Work on all NetworkComponents than the last one ---------------- 
		for (int i = 0; i < (netCompVector.size()-1); i++) {
			
			NetworkComponent netComp = netCompVector.get(i);
			
			// --- Incident Edges on the node ---------------------------------
			for (GraphEdge oldGraphEdge : this.getGraph().getIncidentEdges(node2SplitAt)) { // for each incident edge
				// --- If the edge is in comp2 --------------------------------
				if (netComp.getGraphElementIDs().contains(oldGraphEdge.getId())) {
					// --- Creating a new Graph node --------------------------
					GraphNode newGraphNode = new GraphNode();
					newGraphNode.setId(this.nextNodeID());
					newGraphNode.setPosition(node2SplitAt.getPosition());
					this.getGraphElements().put(newGraphNode.getId(), newGraphNode);

					// --- Switch the connection to the new node --------------
					GraphEdge newEdge = this.switchEdgeBetweenGraphNodes(oldGraphEdge, newGraphNode, node2SplitAt);
					this.removeGraphElementToNetworkComponent(oldGraphEdge);
					this.addGraphElementToNetworkComponentRelation(newEdge, netComp);

					netComp.getGraphElementIDs().add(newGraphNode.getId());
					netComp.getGraphElementIDs().remove(node2SplitAt.getId());
					
					graphNodeConnections.add(newGraphNode);
					
					// --- Shift position of the new node a bit? --------------
					if (moveOppositeNode==true) {
						GraphNode otherNode = this.getGraph().getOpposite(newGraphNode, newEdge);
						newGraphNode.setPosition(this.getShiftedPosition(otherNode, newGraphNode));	
					}
				
				}
			}
			// --- Updating the graph element IDs of the component ------------
			netComp.getGraphElementIDs().remove(node2SplitAt.getId());

		} // --- 'end for' for components -------------------------------------
		this.refreshGraphElements();
		
		// -- Set return value ------------------------------------------------
		graphNodePair = new GraphNodePairs(node2SplitAt, graphNodeConnections);
		return graphNodePair;
		
	}

	/**
	 * Switches the coupling of an edge between an old and a new GraphNode. 
	 * This is used for splitting and merging GraphNodes.
	 *
	 * @param edge the edge to switch between GraphNodes
	 * @param newGraphNode the new GraphNode for the edge
	 * @param oldGraphNode the old GraphNode for the edge
	 * @return the graph node
	 */
	private GraphEdge switchEdgeBetweenGraphNodes(GraphEdge edge, GraphNode newGraphNode, GraphNode oldGraphNode) {
		
		// Find the node on the other side of the edge
		GraphNode otherNode = this.getGraph().getOpposite(oldGraphNode, edge);
		// Create a new edge with the same ID and type
		GraphEdge newEdge = new GraphEdge(edge.getId(), edge.getComponentType());

		if (this.getGraph().getSource(edge) != null) {
			// if the edge is directed
			if (this.getGraph().getSource(edge)==oldGraphNode) {
				this.getGraph().addEdge(newEdge, newGraphNode, otherNode, EdgeType.DIRECTED);
			} else if (this.getGraph().getDest(edge)==oldGraphNode) {
				this.getGraph().addEdge(newEdge, otherNode, newGraphNode, EdgeType.DIRECTED);
			}
		} else {
			// if the edge is undirected
			this.getGraph().addEdge(newEdge, newGraphNode, otherNode, EdgeType.UNDIRECTED);
		}
		// Removing the old edge from the graph and network model
		this.getGraph().removeEdge(edge);
		getGraphElements().remove(edge.getId());
		getGraphElements().put(newEdge.getId(), newEdge);

		return newEdge;
	}

	/**
	 * Gets a shifted position for a node in relation to the raster size of the component type settings.
	 * @param fixedNode the fixed node
	 * @param shiftNode the shift node
	 * @return the shifted position
	 */
	public Point2D getShiftedPosition(GraphNode fixedNode, GraphNode shiftNode) {
		double move = this.getLayoutSettings().getSnapRaster() * 2;
		return this.getShiftedPosition(fixedNode, shiftNode, move);
	}
	
	/**
	 * Gets a shifted position for a node in relation to the raster size of the component type settings.
	 *
	 * @param fixedNode the fixed node
	 * @param shiftNode the shift node
	 * @param move the move
	 * @return the shifted position
	 */
	public Point2D getShiftedPosition(GraphNode fixedNode, GraphNode shiftNode, double move) {

		double fixedNodeX = fixedNode.getPosition().getX();
		double fixedNodeY = fixedNode.getPosition().getY();

		double shiftNodeX = shiftNode.getPosition().getX();
		double shiftNodeY = shiftNode.getPosition().getY();

		double radians = Math.atan2((shiftNodeY - fixedNodeY), (shiftNodeX - fixedNodeX));

		shiftNodeX = shiftNodeX - move * Math.cos(radians);
		shiftNodeY = shiftNodeY - move * Math.sin(radians);

		Point2D newPosition = new Point2D.Double(shiftNodeX, shiftNodeY);
		return newPosition;
	}

	/**
	 * Returns the network component vector with the DistributionNode as last.
	 *
	 * @param netCompList the NetworkComponent list
	 * @return the network component vector with distribution node as last
	 */
	public Vector<NetworkComponent> getNetworkComponentVectorWithDistributionNodeAsLast(List<NetworkComponent> netCompList) {

		NetworkComponent distributionNodeComponent = null;
		Vector<NetworkComponent> newComponentVector = new Vector<NetworkComponent>();
		
		for (int i = 0; i < netCompList.size(); i++) {
			NetworkComponent netComp = netCompList.get(i);
			if (netComp.getPrototypeClassName().equals(DistributionNode.class.getName())) {
				distributionNodeComponent = netComp;
			} else {
				newComponentVector.add(netComp);
			}
		}
		if (distributionNodeComponent!=null) {
			newComponentVector.add(distributionNodeComponent);
		}
		return newComponentVector;
	}
	
	/**
	 * Returns the first {@link DistributionNode} that can be found in the specified list of {@link NetworkComponent}s.
	 * 
	 * @param netCompList the list of NetworkComonent's
	 * @return the first DistributionNode found or <code>null</code> 
	 */
	public NetworkComponent getDistributionNode(List<NetworkComponent> netCompList) {
		NetworkComponent distributionNodeComponent = null;
		for (int i = 0; i < netCompList.size(); i++) {
			NetworkComponent netComp = netCompList.get(i);
			if (this.isDistributionNode(netComp)==true) {
				distributionNodeComponent = netComp;
				break;
			}
		}
		return distributionNodeComponent;
	}
	/**
	 * Return the DistributionNode - {@link NetworkComponent} that belongs to the specified GraphNode.
	 * @param graphNode the GraphNode
	 * @return the NetworkComponent that is a DistributionNode or <code>null</code>
	 */
	public NetworkComponent getDistributionNode(GraphNode graphNode) {
		List<NetworkComponent> netCompList = this.getNetworkComponents(graphNode);
		if (netCompList!=null) {
			for (int i = 0; i < netCompList.size(); i++) {
				NetworkComponent component = netCompList.get(i);
				if (this.isDistributionNode(component)) {
					return component;
				}
			}
		}
		return null;
	}
	/**
	 * Returns the GraphNode from the specified NetworkComponent if this component is a distribution node.
	 *
	 * @param networkComponent the network component
	 * @return the graph node from distribution node
	 */
	public GraphNode getGraphNodeFromDistributionNode(NetworkComponent networkComponent) {
		GraphNode graphNodeFound = null;
		if (this.isDistributionNode(networkComponent)==true) {
			String graphNodeID = networkComponent.getGraphElementIDs().iterator().next();
			graphNodeFound = (GraphNode) this.getGraphElement(graphNodeID);
		}
		return graphNodeFound;
	}
	
	/**
	 * Checks, if the specified list of {@link NetworkComponent}s contains a DistributionNode.
	 * 
	 * @param netCompList the NetworkComponent list to evaluate
	 * @return the NetworkComponent that IS the first DistributionNode or null
	 */
	public boolean containsDistributionNode(List<NetworkComponent> netCompList) {
		if (netCompList!=null) {
			for (int i = 0; i < netCompList.size(); i++) {
				NetworkComponent component = netCompList.get(i);
				if (this.isDistributionNode(component)==true) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if the specified GraphNode is the main GraphNode of a DistributionNode.
	 * @param graphNode the GraphNode
	 * @return the NetworkComponent of the DistributionNode, otherwise <code>null</code>
	 */
	public boolean isDistributionNode(GraphNode graphNode) {
		return this.containsDistributionNode(this.getNetworkComponents(graphNode));
	}
	/**
	 * Checks if the specified NetworkComponent is a distribution node that is that a NetworkComponent is a single GraphNode
	 *
	 * @param networkComponent the network component
	 * @return true, if is distribution node
	 */
	public boolean isDistributionNode(NetworkComponent networkComponent) {
		if (networkComponent==null) return false;
		return networkComponent.getPrototypeClassName().equals(DistributionNode.class.getName());
	}
	
	
	/**
	* Returns the cluster components of the NetworkModel.
	* @return the cluster components
	*/
	public List<ClusterNetworkComponent> getClusterComponents() {
		return getClusterNetworkComponents(new ArrayList<NetworkComponent>(this.getNetworkComponents().values()));
	}

	/**
	 * Gets the cluster components of a collection of clusterComponents.
	 *
	 * @param networkComponentList the components
	 * @return the cluster components
	 */
	public List<ClusterNetworkComponent> getClusterNetworkComponents(List<NetworkComponent> networkComponentList) {
		List<ClusterNetworkComponent> clusterComponents = new ArrayList<>();
		for (int i = 0; i < networkComponentList.size(); i++) {
			NetworkComponent networkComponent = networkComponentList.get(i);
			if (networkComponent instanceof ClusterNetworkComponent) {
				clusterComponents.add((ClusterNetworkComponent) networkComponent);
			}
		}
		return clusterComponents;
	}

	/**
	 * Checks whether a network component is in the star graph element
	 * 
	 * @param comp the network component
	 * @return true if the component is a star graph element
	 */
	public boolean isStarGraphElement(NetworkComponent comp) {

		AbstractGraphElementPrototype graphElement = null;
		try {
			graphElement = (AbstractGraphElementPrototype) ClassLoadServiceUtility.newInstance(comp.getPrototypeClassName());

		} catch (ClassNotFoundException ex) {
			System.err.println(" AbstractGraphElementPrototype class must be in class path.");
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			System.err.println(ex + " AbstractGraphElementPrototype class must be concrete.");
		} catch (IllegalAccessException ex) {
			System.err.println(ex + " AbstractGraphElementPrototype class must have a no-arg constructor.");
		}
		// --- StarGraphElement is the super class of all star graph elements ---
		if (graphElement instanceof StarGraphElement) {
			return true;
		}
		return false;
	}

	/**
	 * Given a node and a graph component of star prototype, checks whether the node is the center of the star or not.
	 *
	 * @param node The node to be checked
	 * @param networkComponent The network component containing the node having the star prototype
	 * @return true, if is center node of star
	 */
	public boolean isCenterNodeOfStar(GraphNode node, NetworkComponent networkComponent) {
		for (String graphElementID : networkComponent.getGraphElementIDs()) {
			GraphElement elem = getGraphElement(graphElementID);
			// The center node should be incident on all the edges of the component
			if (elem instanceof GraphEdge && !this.getGraph().isIncident(node, (GraphEdge) elem)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if is free node.
	 *
	 * @param graphNode the GraphNode
	 * @return true, if is free node
	 */
	public boolean isFreeGraphNode(GraphNode graphNode) {

		// --- The number of network components containing this node ------
		List<NetworkComponent> networkComponents = getNetworkComponents(graphNode);
		if (networkComponents.size() == 1) {
			NetworkComponent networkComponent = networkComponents.iterator().next();
			// --- Node is present in only one component and not center of a star ------------------
			if (isStarGraphElement(networkComponent) && isCenterNodeOfStar(graphNode, networkComponent)) {
				return false;
			}
			return true;
		}
		
		for (int i = 0; i < networkComponents.size(); i++) {
			NetworkComponent networkComponent = networkComponents.get(i);
			if (networkComponent.getPrototypeClassName().equals(DistributionNode.class.getName())) {
				// --- Node is present in several components ------------------
				return true;
			}
		}

		return false;
	}
	

	/**
	 * Resets the GraphElementLayout for every GraphNode or GraphEdge.
	 */
	public void resetGraphElementLayout() {
		
		List<GraphElement> graphElementList = new ArrayList<>(this.getGraphElements().values());
		for (int i = 0; i < graphElementList.size(); i++) {
			GraphElement graphElement = graphElementList.get(i);
			if (graphElement.graphElementLayout!=null) {
				graphElement.resetGraphElementLayout(this);	
			}
		}
	}
	
	/**
	 * Replace NetworkComponents by one ClusterComponent.
	 *
	 * @param networkComponentsToCluster The List of {@link NetworkComponent}'s that are to be grouped into a cluster
	 * @param distributionNodesAreOuterNodes the distribution nodes are outer nodes
	 * @return the new {@link ClusterNetworkComponent} that was inserted into the model
	 */
	public ClusterNetworkComponent replaceComponentsByCluster(HashSet<NetworkComponent> networkComponentsToCluster, boolean distributionNodesAreOuterNodes) {
		
		// --- Maybe another instance: Get real group of NetworkComponents ---- 
		List<NetworkComponent> clusterNC2Use = new ArrayList<>();
		for (NetworkComponent netComp : networkComponentsToCluster) {
			clusterNC2Use.add(this.getNetworkComponent(netComp.getId()));
		}
		
		// ---------- Get Domain of current NetworkComponent -------------------
		String domain = null;
		NetworkComponent networkComponent = clusterNC2Use.iterator().next();
		if (networkComponent instanceof ClusterNetworkComponent) {
			domain = ((ClusterNetworkComponent) networkComponent).getDomain();
		} else {
			String compType = networkComponent.getType();
			ComponentTypeSettings cts = this.generalGraphSettings4MAS.getCurrentCTS().get(compType);
			domain = cts.getDomain();	
		}
		// --- Get cluster agent class name -----------------------------------
		DomainSettings dSettings = this.generalGraphSettings4MAS.getDomainSettings().get(domain);
		String clusterAgentClassName = null;
		if (dSettings!=null) {
			clusterAgentClassName = dSettings.getClusterAgent();
		}
		
		// ---------- Prepare Parameters for ClusterComponent -----------------
		NetworkModel clusterNetworkModel = this.getCopy();
		clusterNetworkModel.setAlternativeNetworkModel(null);
		clusterNetworkModel.removeNetworkComponentsInverse(clusterNC2Use);
		clusterNetworkModel.resetGraphElementLayout();
		
		// ----------- Get outer GraphNode of the NetworkModel ----------------
		Vector<GraphNode> outerNodes = this.getOuterConnectionNodes(clusterNC2Use, distributionNodesAreOuterNodes);
		// ----------- Get remaining GraphElements of current outer nodes -----
		HashMap<GraphNode, HashSet<GraphElement>> outerNodesRemainingGraphElements = this.getOuterConnectionRemainingGraphElements(outerNodes, clusterNC2Use);
		// ----------- Remove clustered NetworkComponents ---------------------
		for (NetworkComponent netComp : clusterNC2Use) {
			if (distributionNodesAreOuterNodes==false) {
				this.removeNetworkComponent(netComp, true, false);
			} else {
				if (netComp.getPrototypeClassName().equals(DistributionNode.class.getName())==false) {
					this.removeNetworkComponent(netComp, false, false);
				}
			}
		}
		// --- Correct outer nodes after NetworkComponents were removed -------
		outerNodes = this.getOuterConnectionNodesCorrected(outerNodesRemainingGraphElements);

		// ----------- Create Cluster GraphElement ----------------------------
		ClusterGraphElement clusterGraphElement = new ClusterGraphElement(outerNodes, this.nextNetworkComponentID());
		HashSet<GraphElement> graphElements = clusterGraphElement.addToGraph(this);
		// ----------- Set position of central GraphNode ----------------------
		Rectangle2D rectangle = GraphGlobals.getGraphSpreadDimension(clusterNetworkModel.getGraph().getVertices());
		clusterGraphElement.getCentralGraphNode().setPosition(new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY()));
		// ----------- Create ClusterNetworkComponent -------------------------
		ClusterNetworkComponent clusterComponent = new ClusterNetworkComponent(clusterGraphElement.getId(), clusterGraphElement.getType(), clusterAgentClassName, graphElements, clusterGraphElement.isDirected(), domain, clusterNetworkModel);
		this.addNetworkComponent(clusterComponent);
		this.refreshGraphElements();

		// --- Add the created cluster as an alternative network model --------
		this.getAlternativeNetworkModel().put(clusterComponent.getId(), clusterNetworkModel);
		
		return clusterComponent;
	}

	/**
	 * Merges Cluster NetworkModel with this NetworkModel and removes the Cluster if it's part of this model
	 *
	 * @param clusterNetworkComponent the cluster network component
	 */
	public void replaceClusterByComponents(ClusterNetworkComponent clusterNetworkComponent) {
		removeNetworkComponent(clusterNetworkComponent);
		for( GraphNode graphNode : clusterNetworkComponent.getClusterNetworkModel().getGraph().getVertices()) {
			
			if (getGraphElement(graphNode.getId()) == null) {
				GraphNode graphNodeCopy = graphNode.getCopy();
				this.getGraph().addVertex(graphNodeCopy);
				this.getGraphElements().put(graphNodeCopy.getId(), graphNodeCopy);
			}
		}
		
		for (GraphEdge graphEdge : clusterNetworkComponent.getClusterNetworkModel().getGraph().getEdges()) {
			GraphEdge graphEdgeNew = new GraphEdge(graphEdge.getId(), graphEdge.getComponentType());
			EdgeType edgeType = clusterNetworkComponent.getClusterNetworkModel().getGraph().getEdgeType(graphEdge);
			GraphNode first = clusterNetworkComponent.getClusterNetworkModel().getGraph().getEndpoints(graphEdge).getFirst();
			GraphNode second = clusterNetworkComponent.getClusterNetworkModel().getGraph().getEndpoints(graphEdge).getSecond();

			GraphNode copyFirst = (GraphNode) this.getGraphElements().get(first.getId());
			GraphNode copySecond = (GraphNode) this.getGraphElements().get(second.getId());
			this.getGraph().addEdge(graphEdgeNew, copyFirst, copySecond, edgeType);
		}

		for (NetworkComponent networkComponent : clusterNetworkComponent.getClusterNetworkModel().getNetworkComponents().values()) {
			addNetworkComponent(networkComponent);
		}
		refreshGraphElements();
	}

	/**
	 * Returns the outer, not connected GraphNodes of a NetworkModel.
	 *
	 * @param networkComponents the {@link NetworkComponent}'s that aare building a group within the graph (BE AWARE OF THE RIGHT INSTANCE)
	 * @param setDistributionNodesToOuterNodes if true, distribution nodes will always set to outer nodes
	 * @return the outer nodes
	 */
	public Vector<GraphNode> getOuterConnectionNodes(List<NetworkComponent> networkComponents, boolean setDistributionNodesToOuterNodes) {
		
		Vector<GraphNode> outerNodes = new Vector<GraphNode>();
		
		// --- Walk through the list of specified NetworkCompnents ------------
		for (int i = 0; i < networkComponents.size(); i++) {
		
			NetworkComponent netComp = networkComponents.get(i);
		
			// --- Get all GraphNodes of a NetworkComponent -------------------
			HashSet<GraphElement> nodeElements = this.getGraphElementsOfNetworkComponent(netComp, new GraphNode());
			for (GraphElement nodeElement : nodeElements) {
				// --- Get connected NetworkComponents of this GraphNode ------
				List<NetworkComponent> connNetComps = this.getGraphElementToNetworkComponentHash().get(nodeElement);
				// --- Distribution Node within neighbours and set outer? -----
				if (setDistributionNodesToOuterNodes==true && this.containsDistributionNode(connNetComps)==true && outerNodes.contains((GraphNode)nodeElement)==false) {
					// --- Add to outer nodes ---------------------------------
					outerNodes.add((GraphNode)nodeElement);	
					
				} else {
					// --- Are the connected NetworkComponents in the group -------	
					for (NetworkComponent connNetComp : connNetComps) {
						if (networkComponents.contains(connNetComp)==false && outerNodes.contains((GraphNode)nodeElement)==false) {
							// --- Found outer node -------------------------------
							outerNodes.add((GraphNode)nodeElement);	
							break;
						}
					} 
				} // end if
			}
		}
		return outerNodes;
	}

	/**
	 * Gets the outer connection remaining graph elements.
	 *
	 * @param outerNodes the outer nodes 
	 * @param networkComponentsToCluster the network components to cluster
	 * @return the outer connection remaining graph elements
	 */
	private HashMap<GraphNode, HashSet<GraphElement>> getOuterConnectionRemainingGraphElements(Vector<GraphNode> outerNodes, List<NetworkComponent> networkComponentsToClusterList) {
		
		HashSet<NetworkComponent> networkComponentsToCluster = new HashSet<>(networkComponentsToClusterList);
		HashMap<GraphNode, HashSet<GraphElement>> ocge = new HashMap<GraphNode, HashSet<GraphElement>>();
		for (GraphNode graphNode1 : outerNodes) {
			
			// --- Get edges that are leaving / reaching this GraphNode ------- 
			Vector<GraphEdge> edges = new Vector<GraphEdge>(this.getGraph().getIncidentEdges(graphNode1));
			for (GraphEdge edge : edges) {
				// --- Is the edge part of the NetworkComponets to Cluster? ---
				if (networkComponentsToCluster.contains(this.getNetworkComponent(edge))==false) {
					// --- Get opposite GraphNode -----------------------------
					GraphNode graphNode2 = this.getGraph().getOpposite(graphNode1, edge);
					HashSet<GraphElement> remainingGE = new HashSet<GraphElement>();
					remainingGE.add(edge);
					remainingGE.add(graphNode2);
					ocge.put(graphNode1, remainingGE);
					// --- one reminder is enough ! ---------------------------
					break;
				}
			} // end for edges 
		} // end for outerNodes
		return ocge;
	}
	
	/**
	 * Gets the outer connection nodes corrected.
	 *
	 * @param outerNodesRemainingGraphElements the outer nodes remaining graph elements
	 * @return the outer connection nodes corrected
	 */
	private Vector<GraphNode> getOuterConnectionNodesCorrected(HashMap<GraphNode, HashSet<GraphElement>> outerNodesRemainingGraphElements) {
		
		Vector<GraphNode> outerNodes = new Vector<GraphNode>();
		for (GraphNode graphNode : outerNodesRemainingGraphElements.keySet()) {
			
			// --- Check, if the GraphNode is still there -----------
			GraphElement graphNodeCheck = this.getGraphElement(graphNode.getId());
			if (graphNodeCheck!=null) {
				// --- Node is still there - just take it -----------
				outerNodes.add((GraphNode) graphNodeCheck);
				
			} else {
				// --- Search by using remaining GraphElements ------
				GraphEdge edge = null;
				GraphNode node1 = null;
				HashSet<GraphElement> rGE = outerNodesRemainingGraphElements.get(graphNode);
				for (GraphElement graphElement : rGE) {
					GraphElement geFound = this.getGraphElement(graphElement.getId());
					if (geFound instanceof GraphNode) {
						node1 = (GraphNode) geFound;
					} else if (geFound instanceof GraphEdge) {
						edge = (GraphEdge) geFound;
					}
				} // end for
				
				if (edge!=null && node1!=null) {
					GraphNode graphNodeFound = this.getGraph().getOpposite(node1, edge);
					if (graphNodeFound!=null) {
						outerNodes.add(graphNodeFound);	
					}
				}
				
			}
		}
		return outerNodes;
	}
	
	/**
	 * Gets the outer network components. Is build only one time, after generation or after copy
	 * 
	 * @return the outer network components
	 */
	public ArrayList<String> getOuterNetworkComponentIDs() {
		if (outerNetworkComponents != null) {
			return outerNetworkComponents;
		}
		outerNetworkComponents = new ArrayList<String>();
		for (GraphNode graphNode : this.getGraph().getVertices()) {
			if (isFreeGraphNode(graphNode)) {
				NetworkComponent networkComponent = getNetworkComponents(graphNode).iterator().next();
				outerNetworkComponents.add(networkComponent.getId());
			}
		}
		return outerNetworkComponents;
	}

	/**
	 * Gets the connections of biggest branch.
	 * @return the connections of biggest branch
	 */
	public int getConnectionsOfBiggestBranch() {
		if (connectionsOfBiggestBranch < 1) {
			
			List<NetworkComponent> netCompList = new ArrayList<NetworkComponent>(this.getNetworkComponents().values());
			for (int i = 0; i < netCompList.size(); i++) {
				NetworkComponent networkComponent = netCompList.get(i);
				if (!(networkComponent instanceof ClusterNetworkComponent)) {
					int nodes = getNodesFromNetworkComponent(networkComponent).size() - 1;
					if (nodes > connectionsOfBiggestBranch) {
						connectionsOfBiggestBranch = nodes;
					}
				}
			}
		}
		return connectionsOfBiggestBranch;
	}


	/**
	 * Sets the directions of the specified NetworkComponent.
	 * @param networkComponent the NetworkComponent
	 */
	public void setDirectionsOfNetworkComponent(NetworkComponent networkComponent) {
		
		HashMap<String, GraphEdgeDirection> edgeHash = networkComponent.getEdgeDirections();
		for (GraphEdgeDirection direction : edgeHash.values()) {
			// --- Set direction in this NetworkModel -------------------------
			this.setGraphEdgeDirection(direction);

			// ----------------------------------------------------------------
			// --- Set the direction also to the alternative NetworkModels ----
			// ----------------------------------------------------------------
			if (this.alternativeNetworkModel!=null && this.alternativeNetworkModel.size()>0) {
				for (NetworkModel altNetModel : this.alternativeNetworkModel.values()) {
					if (altNetModel.getNetworkComponent(networkComponent.getId())!=null) {
						altNetModel.setGraphEdgeDirection(direction);
					}
				}
			}
			// ----------------------------------------------------------------
		}
	}
	
	/**
	 * Sets the GraphEdge direction.
	 * @param graphEdgeDirection the new graph edge direction
	 */
	public void setGraphEdgeDirection(GraphEdgeDirection graphEdgeDirection) {
		
		GraphEdge graphEdge 	= (GraphEdge) this.getGraphElements().get(graphEdgeDirection.getGraphEdgeID());
		GraphNode graphNodeFrom = (GraphNode) this.getGraphElements().get(graphEdgeDirection.getGraphNodeIDFrom());
		GraphNode graphNodeTo   = (GraphNode) this.getGraphElements().get(graphEdgeDirection.getGraphNodeIDTo());
		
		if (graphEdge!=null && graphNodeFrom!=null && graphNodeTo!=null) {
			// --- Set graph directed ----------------
			this.getGraph().removeEdge(graphEdge);
			this.getGraph().addEdge(graphEdge, graphNodeFrom, graphNodeTo, EdgeType.DIRECTED);
		} 
		
	}

	
	/**
	 * Returns the NetworkComponentAdapter for the specified NetworkComponent.
	 *
	 * @param graphController the graph controller
	 * @param networkComponent the NetworkComponent
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController, NetworkComponent networkComponent) {
		return this.getNetworkComponentAdapter(graphController, networkComponent, null);
	}
	/**
	 * Returns the NetworkComponentAdapter for the specified NetworkComponent.
	 *
	 * @param graphController the graph controller
	 * @param networkComponent the NetworkComponent
	 * @param externalReminderHashMap an optional external reminder HashMap. If <code>null</code>, the local HashMap will be used
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController, NetworkComponent networkComponent, HashMap<String, NetworkComponentAdapter> externalReminderHashMap) {
		
		NetworkComponentAdapter netCompAdapter = null; 
		if (this.isUsesNetworkComponentToGraphNodeAdapter(networkComponent)==true) {
			// --- Mapping from NetworkComponent to GraphNode was chosen ------
			GraphNode graphNode = this.getGraphNodeFromDistributionNode(networkComponent);
			NetworkComponentAdapter netCompAdapterGraphNode = this.getNetworkComponentAdapter(graphController, graphNode, externalReminderHashMap);
			if (netCompAdapterGraphNode!=null) {
				netCompAdapter = netCompAdapterGraphNode;
				netCompAdapter.setNetworkComponent(networkComponent);
				netCompAdapter.setGraphNode(graphNode);
			}
			
		} else {
			// --- A regular NetworkComponentAdapter --------------------------
			netCompAdapter = this.getNetworkComponentAdapter(graphController, networkComponent.getType(), externalReminderHashMap);
			if (netCompAdapter!=null) {
				netCompAdapter.setNetworkComponent(networkComponent);
				netCompAdapter.setGraphNode(null);
			}
		}
		return netCompAdapter;
	}
	/**
	 * Check, if the specified {@link NetworkComponent} uses the {@link NetworkComponentToGraphNodeAdapter} for editing the data model.
	 *
	 * @param networkComponent the network component
	 * @return true, if the configured adapter class is the NetworkComponentToGraphNodeAdapter
	 */
	public boolean isUsesNetworkComponentToGraphNodeAdapter(NetworkComponent networkComponent) {
		if (networkComponent==null) return false;
		ComponentTypeSettings cts = this.getGeneralGraphSettings4MAS().getCurrentCTS().get(networkComponent.getType());
		boolean isUsesNetworkComponentToGraphNodeAdapter = cts.getAdapterClass()!=null && cts.getAdapterClass().equals(NetworkComponentToGraphNodeAdapter.class.getName());
		boolean isDistributionNode = this.isDistributionNode(networkComponent);
		if (isUsesNetworkComponentToGraphNodeAdapter==true & isDistributionNode==false) {
			System.err.println("[" + this.getClass().getSimpleName() + "] The NetworkComponentAdapter for '" + networkComponent.toString() + "' was set to a NetworkComponentToGraphNodeAdapter, but the used Graph-Prototype of the component is not of type DistributionNode!");
		}
		return isUsesNetworkComponentToGraphNodeAdapter & isDistributionNode;
	}
	
	
	/**
	 * Returns the NetworkComponentAdapter for the specified GraphNode.
	 *
	 * @param graphController the current graph controller, if there is one. Can also be null.
	 * @param graphNode the graph node
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController, GraphNode graphNode) {
		return this.getNetworkComponentAdapter(graphController, graphNode, null);
	}
	/**
	 * Returns the NetworkComponentAdapter for the specified GraphNode.
	 *
	 * @param graphController the current graph controller, if there is one. Can also be null.
	 * @param graphNode the graph node
	 * @param externalReminderHashMap an optional external reminder HashMap. If <code>null</code>, the local HashMap will be used
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController, GraphNode graphNode, HashMap<String, NetworkComponentAdapter> externalReminderHashMap) {
		
		List<String> domainList = this.getDomain(graphNode);
		if (domainList!=null && domainList.size()!=0) {
			
			// --- Find all NetworkComponentAdapter for the domains found ----- 
			TreeMap<String, NetworkComponentAdapter> netCompAdapterMap = new TreeMap<>();
			for (int i = 0; i < domainList.size(); i++) {
				// --- Check each domain --------------------------------------
				String domain = domainList.get(i);
				String searchFor = GeneralGraphSettings4MAS.GRAPH_NODE_NETWORK_COMPONENT_ADAPTER_PREFIX + domain;
				NetworkComponentAdapter netCompAdapter = this.getNetworkComponentAdapter(graphController, searchFor, externalReminderHashMap);
				if (netCompAdapter!=null) {
					netCompAdapter.setNetworkComponent(this.getNetworkComponentForNetworkComponentToGraphNodeAdapter(graphNode));
					netCompAdapter.setGraphNode(graphNode);
					// -- Add to TreeMap --------------------------------------
					netCompAdapterMap.put(domain, netCompAdapter);
				}
			}
			
			// --- Check number of adapter found ------------------------------
			if (netCompAdapterMap.size()==0) {
				// --- Found nothing ------------------------------------------
			} else if (netCompAdapterMap.size()==1) {
				// --- Found single adapter -----------------------------------
				return netCompAdapterMap.get(domainList.get(0));
			} else {
				// --- Create and return a BundlingNetworkComponentAdapter ----
				BundlingNetworkComponentAdapter bundlingNetCompAdapter = new BundlingNetworkComponentAdapter(netCompAdapterMap);
				bundlingNetCompAdapter.setGraphEnvironmentController(graphController);
				bundlingNetCompAdapter.setGraphNode(graphNode);
				return bundlingNetCompAdapter; 
			}
		}
		return null;
	}
	/**
	 * Returns the {@link NetworkComponent} for the specified {@link GraphNode} if the GraphNode is part of a {@link NetworkComponentToGraphNodeAdapter}.
	 *
	 * @param graphNode the graph node
	 * @return the network component for network component to graph node adapter
	 */
	public NetworkComponent getNetworkComponentForNetworkComponentToGraphNodeAdapter(GraphNode graphNode) {
		NetworkComponent distributionNode = this.getDistributionNode(graphNode);
		if (distributionNode!=null && this.isUsesNetworkComponentToGraphNodeAdapter(distributionNode)==true) {
			return distributionNode;
		}
		return null;
	}
	
	/**
	 * Returns the NetworkComponentAdapter HashMap that serves as reminder for known adapter.
	 * @return the network component adapter hash
	 */
	public HashMap<String, NetworkComponentAdapter> getNetworkComponentAdapterHash() {
		if (this.networkComponentAdapterHash==null) {
			this.networkComponentAdapterHash = new HashMap<String, NetworkComponentAdapter>();
		}
		return networkComponentAdapterHash;
	}
	/**
	 * Returns the NetworkComponentAdapter for the specified type of component.
	 *
	 * @param graphController the graph controller
	 * @param componentTypeName the component type name
	 * @param externalReminderHashMap an optional external reminder HashMap. If <code>null</code>, the local HashMap will be used
	 * @return the network component adapter
	 */
	private NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController, String componentTypeName, HashMap<String, NetworkComponentAdapter> externalReminderHashMap) {
		
		// --- Chose the working reminder HashMap -------------------
		HashMap<String, NetworkComponentAdapter> workingHashMap = null;
		if (externalReminderHashMap==null) {
			workingHashMap = this.getNetworkComponentAdapterHash();
		} else {
			workingHashMap = externalReminderHashMap;
		}
		
		// --- Check for an adapter in the working HashMap ----------
		NetworkComponentAdapter netCompAdapter = workingHashMap.get(componentTypeName);
		if (netCompAdapter==null) {
			// --- Create corresponding NetworkComponentAdapter -----
			netCompAdapter = this.createNetworkComponentAdapter(graphController, componentTypeName);
			workingHashMap.put(componentTypeName, netCompAdapter);
		}
		return netCompAdapter;
	}
	/**
	 * Creates the {@link NetworkComponentAdapter} that is specified with the component type name.
	 *
	 * @param graphController the current graph controller
	 * @param componentTypeName the component type name
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter createNetworkComponentAdapter(GraphEnvironmentController graphController, String componentTypeName) {
		
		// --------------------------------------------------------------------------
		// --- Find and initialize the corresponding NetworkComponentAdapter --------
		// --------------------------------------------------------------------------
		String adapterClassname = null;
		if (componentTypeName.startsWith(GeneralGraphSettings4MAS.GRAPH_NODE_NETWORK_COMPONENT_ADAPTER_PREFIX)) {
			// --- Find the NetworkComponentAdapter for the GraphNode ---------------
			String searchFor = componentTypeName.replace(GeneralGraphSettings4MAS.GRAPH_NODE_NETWORK_COMPONENT_ADAPTER_PREFIX, "");
			DomainSettings ds = this.generalGraphSettings4MAS.getDomainSettings().get(searchFor);
			if (ds!=null) {
				adapterClassname = ds.getAdapterClass();	
			}
			
		} else {
			// --- Create the NetworkComponentAdapter, if it exists -----------------
			ComponentTypeSettings cts = this.generalGraphSettings4MAS.getCurrentCTS().get(componentTypeName);
			if (cts!=null) {
				adapterClassname = cts.getAdapterClass();	
			}
			
		}
		// --------------------------------------------------------------------------
		// --- Initialize the found NetworkComponentAdapter -------------------------
		// --------------------------------------------------------------------------
		NetworkComponentAdapter netCompAdapter = null;
		if (adapterClassname!=null) {
			try {
				Object classInstance = ClassLoadServiceUtility.newInstance(adapterClassname);
				if (classInstance instanceof NetworkComponentAdapter) {
					netCompAdapter = (NetworkComponentAdapter) classInstance;
					netCompAdapter.setGraphEnvironmentController(graphController);
					netCompAdapter.initialize();
				}
				
			} catch (Exception ex) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Could not initiate class '" + adapterClassname + "'");
				System.err.println(ex.getMessage());
				//ex.printStackTrace();
			}
		}
		return netCompAdapter;
	}
	
	
	/**
	 * Returns all related domains for the specified GraphNode.
	 *
	 * @param graphNode the graph node
	 * @return the domain list (with unique domain entries)
	 */
	public List<String> getDomain(GraphNode graphNode) {

		List<String> domainList = new ArrayList<>();
		
		// --- Check all connected NetworkComponents ------
		List<NetworkComponent> netCompList = this.getNetworkComponents(graphNode);
		for (int i = 0; i < netCompList.size(); i++) {

			String domain = null;
			NetworkComponent netComp =  netCompList.get(i);
			if (netComp instanceof ClusterNetworkComponent) {
				domain = ((ClusterNetworkComponent)netComp).getDomain();
			} else {
				domain = this.generalGraphSettings4MAS.getCurrentCTS().get(netComp.getType()).getDomain();
			}
			
			if (domain!=null && domain.isEmpty()==false && domainList.contains(domain)==false) {
				domainList.add(domain);
			}
		}
		// --- Finally sort the domains found -------------
		Collections.sort(domainList);
		return domainList;
	}
	/**
	 * Returns the domain for the specified GraphEdge.
	 *
	 * @param graphEdge the graph edge
	 * @return the domain
	 */
	public String getDomain(GraphEdge graphEdge) {
		String domain = null;
		if (graphEdge!=null) {
			NetworkComponent networkComponent = this.getNetworkComponent(graphEdge);
			if (networkComponent!=null) {
				domain = this.generalGraphSettings4MAS.getCurrentCTS().get(networkComponent.getType()).getDomain();
			}
		}
		return domain;
	}
	/**
	 * Returns the domain of the specified NetworkComponent.
	 *
	 * @param networkComponent the network component
	 * @return the domain
	 */
	public String getDomain(NetworkComponent networkComponent) {
		String domain = null;
		if (networkComponent!=null) {
			domain = this.generalGraphSettings4MAS.getCurrentCTS().get(networkComponent.getType()).getDomain();
		}
		return domain;
	}
	
	
	/**
	 * Load the network topology from a GraphML file.
	 * @param graphMlFile the GraphML file
	 * @return true, if successful
	 */
	public boolean loadGraphFile(File graphMlFile) {
		
		boolean success = false;
		GraphModelReader graphModelReader = null;
		if (graphMlFile.exists()) {
			try {
				graphModelReader = new GraphModelReader(graphMlFile);
				this.setGraph(graphModelReader.readGraph());
				success = true;
				
			} catch (GraphIOException e) {
				e.printStackTrace();
			} finally {
				if (graphModelReader!=null) {
					try {
						graphModelReader.close();
					} catch (GraphIOException gIOEx) {
						gIOEx.printStackTrace();
					}
				}
			}
		}
		return success;
	}
	
	/**
	 * Save the network topology to a GraphML file.
	 *
	 * @param graphMlFile the GraphML file
	 * @return true, if successful
	 */
	public boolean saveGraphFile(File graphMlFile) {
		
		boolean success = false;
		FileWriter fw = null;
		PrintWriter pw = null;
		try {

			fw = new FileWriter(graphMlFile);
			pw = new PrintWriter(fw);
			
			// --- Collect allowed layout ID's and write file -------
			HashSet<String> allowedLayoutIDs = new HashSet<>(this.getGeneralGraphSettings4MAS().getLayoutSettings().keySet());
			new GraphModelWriter(allowedLayoutIDs).save(this.getGraph(), pw);
			success=true;
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			pw.close();
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	
	/**
	 * Load the network component definitions from an XML file.
	 * @param xmlFile the components file
	 * @return true, if successful
	 */
	public boolean loadComponentsFile(File xmlFile) {
		
		boolean success = false;
		if (xmlFile.exists()==true) {
			
			// ------------------------------------------------------
			// --- Try to load the newer file format ----------------
			// ------------------------------------------------------
			NetworkModelFileContent fileContent = NetworkModelFileContent.load(xmlFile, false);
			if (fileContent!=null) {
				this.setLayoutID(fileContent.getLayoutID());
				this.setMapSettingsTreeMap(fileContent.getMapSettingsList());
				this.setNetworkComponents(fileContent.getNetworkComponentList());
				return true;
			}

			// ------------------------------------------------------
			// --- Not successful loaded yet, try old version -------
			// ------------------------------------------------------
			FileReader componentReader = null;
			try {
				componentReader = new FileReader(xmlFile);

				JAXBContext context = JAXBContext.newInstance(NetworkComponentList.class);
				Unmarshaller unmarsh = context.createUnmarshaller();
				NetworkComponentList compList = (NetworkComponentList) unmarsh.unmarshal(componentReader);
				this.setNetworkComponents(compList.getComponentList());
				success = true;
				
			} catch (JAXBException | IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					componentReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}
	/**
	 * Save the network component definitions to an XML file.
	 * @param xmlFile the component file
	 * @return true, if successful
	 */
	public boolean saveComponentsFile(File xmlFile) {
		
		boolean success = false;
		
		// ----------------------------------------------------------
		// --- Try to save the newer file format --------------------
		// ----------------------------------------------------------
		NetworkModelFileContent fileContent = new NetworkModelFileContent();
		fileContent.setLayoutID(this.getLayoutID());
		fileContent.setMapSettingsList(this.getMapSettingsTreeMap());
		fileContent.setNetworkComponentList(this.getNetworkComponents());
		// --- Do save -----------
		success = fileContent.save(xmlFile);
		
		if (success==false) {
			// ------------------------------------------------------
			// --- Not successful saved yet, try old version --------
			// ------------------------------------------------------
			FileWriter componentFileWriter = null;
			try {
				componentFileWriter = new FileWriter(xmlFile);
				JAXBContext context = JAXBContext.newInstance(NetworkComponentList.class);
				Marshaller marsh = context.createMarshaller();
				marsh.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marsh.marshal(new NetworkComponentList(this.getNetworkComponents()), componentFileWriter);
				success = true;
				
			} catch (IOException | JAXBException e) {
				System.err.println("Error saving network components!");
				e.printStackTrace();
			} finally {
				if (componentFileWriter!=null) {
					try {
						componentFileWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return success;
	}
	
}
