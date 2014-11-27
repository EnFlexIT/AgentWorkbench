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
package agentgui.envModel.graph.controller.yedGraphml;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import agentgui.core.application.Language;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.controller.NetworkModelFileImporter;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;
import edu.uci.ics.jung.graph.Graph;

/**
 * GraphFileImporter for GraphML files specified with yEd
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * 
 */
public class YedGraphMLFileImporter extends NetworkModelFileImporter {
   
    /** Intermediate graph, after loading the GraphML file, before converting to a GridModel */
    private Graph<TempNode, Object> tempGraph;
    /** The GridModel created from the imported yEd GraphML file */
    private NetworkModel networkModel;

    private HashMap<String, GraphElementPrototype> addedElements = null;

    public YedGraphMLFileImporter(GraphEnvironmentController graphController, String fileTypeExtension, String fileTypeDescription) {
		super(graphController, fileTypeExtension, fileTypeDescription);
		addedElements = new HashMap<String, GraphElementPrototype>();
    }

    @Override
    public NetworkModel importGraphFromFile(File graphFile) {
		// GraphML parser instance
		YedGraphMLParser parser = new YedGraphMLParser();
		// List of the graphs start nodes
		Vector<TempNode> startNodesList = null;
	
		// Load the intermediate graph from the yEd file
		tempGraph = parser.getGraph(graphFile);
		if (tempGraph == null) {
		    return null;
		} else {
		    // Find the graphs start nodes
		    Iterator<TempNode> nodes = tempGraph.getVertices().iterator();
		    startNodesList = new Vector<TempNode>();
		    while (nodes.hasNext()) {
				TempNode node = nodes.next();
				if (tempGraph.inDegree(node) == 0) {
				    startNodesList.add(node);
				}
		    }
		}
	
		// Build the final graph, starting from the start nodes
		if (startNodesList != null) {
		    Iterator<TempNode> startNodes = startNodesList.iterator();
		    networkModel = new NetworkModel();
		    while (startNodes.hasNext()) {
			TempNode startNode = startNodes.next();
			addElement(startNode, null);
		    }
		}
		return networkModel;
    }

    /**
     * This method adds a new element from the temporary graph to the final graph
     * 
     * @param tempElement
     * @param predecessor
     */
    private void addElement(TempNode tempElement, GraphElementPrototype predecessor) {
		// Create a NetworkComponent representing the element
		String prototypeClassName = this.graphController.getGeneralGraphSettings4MAS().getCurrentCTS().get(tempElement.getType()).getGraphPrototype();
	
		// Create a GraphElementPrototype for the component
		GraphElementPrototype newElement = null;
		try {
		    newElement = (GraphElementPrototype) Class.forName(prototypeClassName).newInstance();
		} catch (InstantiationException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}
	
		if (newElement != null) {
		    newElement.setId(tempElement.getId());
		    newElement.setType(tempElement.getType());
	
		    // Look for an existing successor
		    // Not supporting multiple successors at the moment !!!
		    GraphElementPrototype successor = null;
		    for (TempNode tempNode : tempGraph.getSuccessors(tempElement)) {
		    	successor = addedElements.get(tempNode.getId());
		    }
	
		    HashSet<GraphElement> graphElements = null;
		    if (predecessor == null) {
		    	graphElements = successor == null ? newElement.addToGraph(networkModel.getGraph()) : newElement.addBefore(networkModel.getGraph(), successor);
		    } else {
		    	graphElements = successor == null ? newElement.addAfter(networkModel.getGraph(), predecessor) : newElement.addBetween(networkModel.getGraph(), predecessor, successor);
		    }
		    
		    // Get the component type settings for the component
		    ComponentTypeSettings cts = this.graphController.getGeneralGraphSettings4MAS().getCurrentCTS().get(tempElement.getType());
		    
		    // Add the new NetworkComponent
		    NetworkComponent netComp = new NetworkComponent(tempElement.getId(), tempElement.getType(), prototypeClassName, cts.getAgentClass(), graphElements, newElement.isDirected());
		    networkModel.addNetworkComponent(netComp);
	
		    // Call recursively for successor nodes
		    // New iterator
		    for (TempNode tempNode : tempGraph.getSuccessors(tempElement)) {
		    	addElement(tempNode, newElement);
		    }
		    
		} else {
		    System.err.println(Language.translate("Fehler beim Instanziieren des GraphElementPrototyps für Element-Typ ") + tempElement.getType());
		}
    }

}
