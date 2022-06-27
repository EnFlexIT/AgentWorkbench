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
package org.awb.env.networkModel.helper;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.prototypes.AbstractGraphElementPrototype;
import org.awb.env.networkModel.prototypes.StarGraphElement;
import org.awb.env.networkModel.settings.ComponentTypeSettings;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import agentgui.core.classLoadService.ClassLoadServiceUtility;


/**
 * A factory for creating {@link NetworkComponent} objects in an own {@link NetworkModel}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkComponentFactory {

	private static String factoryName = "[NetworkComponentFactory] ";
	private static AbstractGraphElementPrototype abstractGraphElementPrototype;
	

	/**
	 * Returns the last {@link AbstractGraphElementPrototype} that was created with the last {@link NetworkComponent}.
	 * @return the graph element prototype of the last network component
	 */
	public static AbstractGraphElementPrototype getGraphElementPrototypeOfLastNetworkComponent() {
		return abstractGraphElementPrototype;
	}
	
	
	/**
	 * This method can be used in order to produce new {@link NetworkComponent} objects.
	 * Just specify the component by the name given in the ComponentTypeSettings dialog. 
	 *
	 * @param sourceNetworkModel the source network model
	 * @param networkComponentTypeName the network component type name
	 * @return the NetworkModel for the component
	 */
	public static NetworkModel getNetworkModel4NetworkComponent(NetworkModel sourceNetworkModel, String networkComponentTypeName) {
		return getNetworkModel4NetworkComponent(sourceNetworkModel, networkComponentTypeName, null);
	}
	/**
	 * This method can be used in order to produce new {@link NetworkComponent} objects.
	 * Just specify the component by the name given in the ComponentTypeSettings dialog. 
	 *
	 * @param sourceNetworkModel the source network model
	 * @param networkComponentTypeName the network component type name
	 * @param viewerDimension the viewer dimension
	 * @return the NetworkModel for the component
	 */
    public static NetworkModel getNetworkModel4NetworkComponent(NetworkModel sourceNetworkModel, String networkComponentTypeName, Dimension viewerDimension) {

    	// ----------------------------------------------------------
    	// --- Set last create abstractGraphElementPrototype to null -------- 
    	abstractGraphElementPrototype = null;

    	// ----------------------------------------------------------
    	// --- Collect the needed base information ------------------
    	if (sourceNetworkModel==null) {
    		System.err.println(factoryName + "Missing required source network model!");
    		return null;
    	}
    	// ----------------------------------------------------------
    	// --- Everything fine so far => Create NetworkModel --------
		return getNetworkModel4NetworkComponent(sourceNetworkModel.getGeneralGraphSettings4MAS(), networkComponentTypeName, viewerDimension);
	}
    
    
    /**
     * This method can be used in order to produce new {@link NetworkComponent} objects.
     * Just specify the component by the name given in the ComponentTypeSettings dialog. 
     *
     * @param ggs4MAS the {@link GeneralGraphSettings4MAS}
     * @param networkComponentTypeName the network component type name
     * @return the NetworkModel for the newly create {@link NetworkComponent}
     */
    public static NetworkModel getNetworkModel4NetworkComponent(GeneralGraphSettings4MAS ggs4MAS, String networkComponentTypeName) {
    	return getNetworkModel4NetworkComponent(ggs4MAS, networkComponentTypeName, null);
    }
    
    /**
	 * This method can be used in order to produce new {@link NetworkComponent} objects.
	 * Just specify the component by the name given in the ComponentTypeSettings dialog. 
     *
     * @param ggs4MAS the {@link GeneralGraphSettings4MAS}
     * @param networkComponentTypeName the network component type name
     * @param viewerDimension the viewer dimension
     * @return the NetworkModel for the newly create {@link NetworkComponent}
     */
    public static NetworkModel getNetworkModel4NetworkComponent(GeneralGraphSettings4MAS ggs4MAS, String networkComponentTypeName, Dimension viewerDimension) {

    	// ----------------------------------------------------------
    	// --- Set last create abstractGraphElementPrototype to null -------- 
    	abstractGraphElementPrototype = null;
    	
    	// ----------------------------------------------------------
    	// --- Collect the needed base information ------------------
    	if (ggs4MAS==null) {
    		System.err.println(factoryName + "General graph settings were not found!");
    		return null;
    	}
    	// --- Get the needed ComponentTypeSettings ----------------- 
    	ComponentTypeSettings cts = getComponentTypeSettings(ggs4MAS, networkComponentTypeName);
    	if (cts==null) {
    		System.err.println(factoryName + "Component type named '" + networkComponentTypeName + "' could not be found!");
    		return null;
    	}
    	// --- Get the class name of the of the component type ------
    	String graphPrototypeClass = cts.getGraphPrototype();
    	if (graphPrototypeClass==null) {
    		System.err.println(factoryName + "Missing definition of the graph prototype for component type '" + networkComponentTypeName + "'!");
    		return null;
    	}
    	
    	// --- Create the graph of the NetworkComponent -------------
    	try {
		    abstractGraphElementPrototype = (AbstractGraphElementPrototype) ClassLoadServiceUtility.newInstance(graphPrototypeClass);
		    
		} catch (ClassNotFoundException ex) {
		    System.err.println(factoryName + "AbstractGraphElementPrototype class must be in class path.\n" + ex);
		    return null;
		} catch (InstantiationException ex) {
		    System.err.println(factoryName + "AbstractGraphElementPrototype class must be concrete.\n" + ex);
		    return null;
		} catch (IllegalAccessException ex) {
		    System.err.println(factoryName + "AbstractGraphElementPrototype class must have a no-arg constructor.\n" + ex);
		    return null;
		} catch (IllegalArgumentException ex) {
			System.err.println(ex + "AbstractGraphElementPrototype has been passed an illegal or inappropriate argument.");
		} catch (InvocationTargetException ex) {
			System.err.println(ex + "Loading of AbstractGraphElementPrototype led to InovcationTargetException");
		} catch (NoSuchMethodException ex) {
			System.err.println(ex + "A method of AbstractGraphElementPrototype can not be found");
		} catch (SecurityException ex) {
			System.err.println(ex + "Loading of AbstractGraphElementPrototype led to security violation");
		} catch (NoClassDefFoundError ex) {
		    System.err.println(ex + "Class AbstractGraphElementPrototype could not be found at runtime");
		}
		    
		// --- Create a new local NetworkModel ----------------------
		NetworkModel compNetworkModel = new NetworkModel();
		compNetworkModel.setGeneralGraphSettings4MAS(ggs4MAS.getCopy());

		// --- Configure and add the prototype to the graph --------- 
		abstractGraphElementPrototype.setId(compNetworkModel.nextNetworkComponentID());
		abstractGraphElementPrototype.setType(networkComponentTypeName);
		abstractGraphElementPrototype.addToGraph(compNetworkModel);
	    
	    // --- Create the needed NetworkComponent ------------------- 
		HashSet<GraphElement> graphElements = new HashSet<GraphElement>();

    	// --- Get graph elements -----------------------------------
    	Graph<GraphNode, GraphEdge> currGraph = compNetworkModel.getGraph();    	
		for (GraphNode vertex : currGraph.getVertices()) {
			graphElements.add(vertex);
		}
		for (GraphEdge edge : currGraph.getEdges()) {
		    graphElements.add(edge);
		}
    	
		// --- Create a NetworkComponent ----------------------------
		NetworkComponent newNetComp = new NetworkComponent(abstractGraphElementPrototype.getId(), networkComponentTypeName, graphElements);
		compNetworkModel.addNetworkComponent(newNetComp);
	    
		// --- Set position of the GraphNoes ------------------------
	    setGraphNodePositions(compNetworkModel, abstractGraphElementPrototype, viewerDimension);

	    // --- Return resulting NetworkModel ------------------------
	    return compNetworkModel;
    }
    
    /**
     * Repaints/Refreshes the visualisation viewer, with the given graph.
     *
     * @param networkModel the network model
     * @param gep the {@link AbstractGraphElementPrototype} that was created
     */
    private static void setGraphNodePositions(NetworkModel compNetworkModel, AbstractGraphElementPrototype gep, Dimension viewerDimension) {
		
    	boolean graphNodePositionsSet = false;
    	int rasterSize = 50;

    	double centerX = 70;
    	double centerY = 70;
    	if (viewerDimension!=null) {
    		centerX = viewerDimension.getWidth() / 2;
        	centerY = viewerDimension.getHeight() / 2;
    	}
    	
    	Graph<GraphNode, GraphEdge> graph = compNetworkModel.getGraph();
		// ------------------------------------------------
		// --- Some special cases -------------------------
		// ------------------------------------------------
		if (graph.getEdgeCount()==0 && graph.getVertexCount()==1) {
			// --------------------------------------------
			// --- Case DistributionNodes -----------------
			// --------------------------------------------
			GraphNode node = graph.getVertices().iterator().next();
			node.setPosition(new Point2D.Double(centerX, centerY));
			graphNodePositionsSet = true;
			
		} else if (graph.getEdgeCount()==1) {
			// --------------------------------------------
			// --- Case directed Graph --------------------
			// --------------------------------------------
			GraphEdge edge = graph.getEdges().iterator().next();
			EdgeType edgeType = graph.getEdgeType(edge);
			if (edgeType==EdgeType.DIRECTED) {
			
				GraphNode nodeSource = graph.getSource(edge);
				GraphNode nodeDestin = graph.getDest(edge);
				nodeSource.setPosition(new Point2D.Double(centerX-rasterSize, centerY));
				nodeDestin.setPosition(new Point2D.Double(centerX+rasterSize, centerY));
				graphNodePositionsSet = true;
			}
			
		} 
		
		if (gep instanceof StarGraphElement) {
			// ----------------------------------
			// --- Case StarGraphElement --------
			// ----------------------------------
			int nNodes = graph.getVertices().size();
			double angle = 0;
			double angleStep = 0;
			if ((nNodes-1)<=4) {
				angleStep = 2 * Math.PI / 4;
			} else {
				angleStep = 2 * Math.PI / (nNodes-1);
			}
			
			for (GraphNode node : graph.getVertices()) {
				int nEdges = graph.getIncidentEdges(node).size();
				
				if (nEdges==nNodes-1) {
					node.setPosition(new Point2D.Double(centerX, centerY));
				} else {
					// --- outer Node found -----
					double x = centerX + rasterSize * Math.cos(angle);
					double y = centerY + rasterSize * Math.sin(angle);
					node.setPosition(new Point2D.Double(x, y));
					angle += angleStep;
				}
			}
			graphNodePositionsSet = true;
		}
		
		if (graphNodePositionsSet==false) {
			// --- Distribute the GraphNodes in a cycle ---
			double angle = 0;
			double angleStep = 2 * Math.PI / graph.getVertices().size();
			for (GraphNode graphNode : graph.getVertices()) {
			
				double x = centerX + rasterSize * Math.cos(angle); 
				double y = centerY + rasterSize * Math.sin(angle);
				
				Point2D position = new Point2D.Double(x, y);
				graphNode.setPosition(position);
				angle+=angleStep;
			}
			graphNodePositionsSet = true;
		}
		
    }
    
    /**
     * Returns the {@link ComponentTypeSettings} for the specified NetworkComponent type.
     *
     * @param networkComponentTypeName the network component type name
     * @param ggs4MAS the {@link GeneralGraphSettings4MAS}
     * @return the component type settings
     */
    private static ComponentTypeSettings getComponentTypeSettings(GeneralGraphSettings4MAS ggs4MAS, String networkComponentTypeName) {
    	ComponentTypeSettings ctsFound = null;
    	if (ggs4MAS!=null) {
    		ctsFound = ggs4MAS.getCurrentCTS().get(networkComponentTypeName);
    	}
    	return ctsFound;
    }
    
}
