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

import edu.uci.ics.jung.graph.Graph;
import agentgui.core.application.Language;
import agentgui.envModel.graph.controller.GraphFileImporter;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;
/**
 * GraphFileImporter for GraphML files specified with yEd
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 *
 */
public class YedGraphMLFileImporter extends GraphFileImporter {
	/**
	 * The file extension used for filtering in JFileChooser selecting the file to import 
	 */
	private String fileTypeExtension = "graphml";
	/**
	 * The file type description for the JFileChooser for selecting the file to import
	 */
	private String fileTypeDescription = "yEd GraphML";
	/**
	 * Intermediate graph, after loading the GraphML file, before converting to a GridModel   
	 */
	private Graph<TempNode, Object> tempGraph;
	/**
	 * The GridModel created from the imported yEd GraphML file
	 */
	private NetworkModel gridModel;
	
	private HashMap<String, GraphElementPrototype> addedElements = null;
	
	public YedGraphMLFileImporter(HashMap<String, ComponentTypeSettings> elementSettings) {
		super(elementSettings);
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
		if (tempGraph==null) {
			return null;
		} else {
			// Find the graphs start nodes
			Iterator<TempNode> nodes = tempGraph.getVertices().iterator();
			startNodesList = new Vector<TempNode>();
			while(nodes.hasNext()){
				TempNode node = nodes.next();
				if(tempGraph.inDegree(node) == 0){
					startNodesList.add(node);
				}
			}	
		}
		
		// Build the final graph, starting from the start nodes
		if (startNodesList!=null) {
			Iterator<TempNode> startNodes = startNodesList.iterator();
			gridModel = new NetworkModel();
			while(startNodes.hasNext()){
				TempNode startNode = startNodes.next();
				addElement(startNode, null);
			}
		}		
		return gridModel;
	}
	/**
	 * This method adds a new element from the temporary graph to the final graph
	 * @param tempElement
	 * @param predecessor
	 */
	private void addElement(TempNode tempElement, GraphElementPrototype predecessor){
		// Create a NetworkComponent representing the element
		NetworkComponent newComponent = new NetworkComponent();
		newComponent.setId(tempElement.getId());
		newComponent.setType(tempElement.getType());
		newComponent.setPrototypeClassName(componentTypeSettings.get(tempElement.getType()).getGraphPrototype());
		
		// Create a GraphElementPrototype for the component
		GraphElementPrototype newElement = null;
		
		try {
			newElement = (GraphElementPrototype) Class.forName(newComponent.getPrototypeClassName()).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(newElement != null){
			newComponent.setDirected(newElement.isDirected());
			
			newElement.setId(tempElement.getId());
			newElement.setType(tempElement.getType());
			
			// Look for an existing successor
			// Not supporting multiple successors at the moment !!!
			Iterator<TempNode> successors = tempGraph.getSuccessors(tempElement).iterator();
			GraphElementPrototype successor = null;
			while(successors.hasNext()){
				TempNode succTempNode = successors.next();
				successor = addedElements.get(succTempNode.getId());
			}
			HashSet<GraphElement> graphElements = null;
			if(predecessor == null && successor == null){
				graphElements = newElement.addToGraph(gridModel.getGraph());
			}else if(predecessor != null && successor == null){
				graphElements = newElement.addAfter(gridModel.getGraph(), predecessor);
			}else if(predecessor == null && successor != null){
				graphElements = newElement.addBefore(gridModel.getGraph(), successor);
			}else if(predecessor != null && successor != null){
				graphElements = newElement.addBetween(gridModel.getGraph(), predecessor, successor);
			}
			
			if(graphElements != null){
				Iterator<GraphElement> geIter = graphElements.iterator();
				while(geIter.hasNext()){
					newComponent.getGraphElementIDs().add(geIter.next().getId());
				}
			}
			
			gridModel.addNetworkComponent(newComponent);
			
			// Call recursively for successor nodes
			// New iterator 
			successors = tempGraph.getSuccessors(tempElement).iterator();
			while(successors.hasNext()){
				TempNode succTempNode = successors.next();
				addElement(succTempNode, newElement);
			}
		}else{
			System.err.println(Language.translate("Fehler beim Instanziieren des GraphElementPrototyps für Element-Typ ")+tempElement.getType());
		}
	}

	@Override
	public String getGraphFileExtension() {
		return fileTypeExtension;
	}

	@Override
	public String getTypeString() {
		return fileTypeDescription;
	}
}
