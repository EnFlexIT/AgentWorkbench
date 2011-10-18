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
package agentgui.envModel.graph.prototypes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import agentgui.core.application.Language;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * A simple graph / network element with two connection points,
 * represented by two nodes and an edge. 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 *
 */
public class SimpleGraphElement extends GraphElementPrototype {
	/**
	 * The GraphElementPrototype's connection points
	 */
	private Vector<GraphNode> nodes;
	
	public SimpleGraphElement(){
		nodes = new Vector<GraphNode>();
	}

	@Override
	public HashSet<GraphElement> addToGraph(Graph<GraphNode, GraphEdge> graph) {
		this.graph = graph;
		
		// Create nodes and edge
		GraphNode entry = new GraphNode();
		entry.setId("PP"+(nodeCounter++));
		GraphNode exit = new GraphNode();
		exit.setId("PP"+(nodeCounter++));
		GraphEdge e = new GraphEdge(getId(), getType());
		
		// Add them to the graph
		graph.addVertex(entry);
		graph.addVertex(exit);
		graph.addEdge(e, entry, exit, EdgeType.UNDIRECTED);
		
		// Add the nodes to this GraphElementPrototypes node list
		nodes.add(entry);
		nodes.add(exit);
		
		// Create a HashSet containing the nodes and edge ant return it
		HashSet<GraphElement> elements = new HashSet<GraphElement>();
		elements.add(e);
		elements.add(entry);
		elements.add(exit);
		return elements;
	}

	@Override
	public HashSet<GraphElement> addAfter(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor) {
		this.graph = graph;
		// Get the predecessor node
		GraphNode entry = predecessor.getFreeExit();
		if(entry != null){
			// Create successor node and edge
			GraphNode exit = new GraphNode();
			exit.setId("PP"+(nodeCounter++));
			GraphEdge e = new GraphEdge(getId(), getType());
			
			// Add them to the graph
			graph.addVertex(exit);
			graph.addEdge(e, entry, exit, EdgeType.UNDIRECTED);
			
			// Add the nodes to this GraphElementPrototypes node list
			nodes.add(entry);
			nodes.add(exit);
			
			// Create a HashSet containing the nodes and edge ant return it
			HashSet<GraphElement> elements = new HashSet<GraphElement>();
			elements.add(e);
			elements.add(entry);
			elements.add(exit);
			return elements;
			
		}else{
			System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Vorgänger ")+predecessor.getId());
			return null;
		}
	}

	@Override
	public HashSet<GraphElement> addBefore(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype successor) {
		this.graph = graph;
		// Get the successor node
		GraphNode exit = successor.getFreeEntry();
		if(exit != null){
			
			// Create predecessor node and edge
			GraphNode entry = new GraphNode();
			entry.setId("PP"+(nodeCounter++));
			GraphEdge e = new GraphEdge(getId(), getType());
			
			// Add them to the graph
			graph.addVertex(entry);
			graph.addEdge(e, entry, exit, EdgeType.UNDIRECTED);
			
			// Add the nodes to this GraphElementPrototypes node list
			nodes.add(entry);
			nodes.add(exit);
			
			// Create a HashSet containing the nodes and edge and return it
			HashSet<GraphElement> elements = new HashSet<GraphElement>();
			elements.add(e);
			elements.add(entry);
			elements.add(exit);
			return elements;
			
		}else{
			System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Nachfolger")+successor.getId());
			return null;
		}
	}

	@Override
	public HashSet<GraphElement> addBetween(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor, GraphElementPrototype successor) {
		
		this.graph = graph;
		// Find predecessor and successor node
		GraphNode entry = predecessor.getFreeExit();
		GraphNode exit = successor.getFreeEntry();
		if(entry != null && exit != null){
			
			// Create the new edge
			GraphEdge e = new GraphEdge(getId(), getType());
			
			// Add it to the graph
			graph.addEdge(e, entry, exit, EdgeType.UNDIRECTED);
			
			// Add the nodes to this GraphElementPrototypes node list
			nodes.add(entry);
			nodes.add(exit);
			
			// Create a HashSet containing the nodes and edge and return it
			HashSet<GraphElement> elements = new HashSet<GraphElement>();
			elements.add(e);
			elements.add(entry);
			elements.add(exit);
			return elements;
			
		}else{
			if(entry == null){
				System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Vorgänger ")+predecessor.getId());
			}
			if(exit == null){
				System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Nachfolger")+successor.getId());
			}
			return null;
		}
	}

	@Override
	public GraphNode getFreeEntry() {
		Iterator<GraphNode> iter = nodes.iterator();
		while(iter.hasNext()){
			GraphNode node = iter.next();
			if(graph.getNeighborCount(node) < 2){
				return node;
			}
		}
		return null;
	}

	@Override
	public GraphNode getFreeExit() {
		// Undirected GraphElement => no distinction between entry and exit necessary
		return getFreeEntry();
	}

	@Override
	public boolean isDirected() {
		return false;
	}

}
