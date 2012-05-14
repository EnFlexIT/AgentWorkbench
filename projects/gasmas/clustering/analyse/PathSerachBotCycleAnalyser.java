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
package gasmas.clustering.analyse;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import org.apache.batik.ext.awt.geom.Polygon2D;

import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class AntCircleAnalyser.
 */
public class PathSerachBotCycleAnalyser {

	private NetworkModel networkModel;

	/** The coordinates map. */
	private HashMap<String, Point2D> coordinatesMap = new HashMap<String, Point2D>();

	
	/** The subgraphs. */
	private ArrayList<Subgraph> subgraphs;

	private int minConnections = 1000;

	/**
	 * Instantiates a new ant circle analyser.
	 *
	 * @param ants the ants
	 * @param networkModel the network model
	 */
	public PathSerachBotCycleAnalyser(NetworkModel networkModel) {
		this.networkModel = networkModel;
		buildCoordinatesMap(networkModel);
	}

	public Subgraph getBestSubgraph() {
		Subgraph subgraph = subgraphs.get(0);
		for (Subgraph subgraphInList : subgraphs) {
			if (subgraphInList.getNetworkComponents().size() < subgraph.getNetworkComponents().size()) {
				subgraph = subgraphInList;
			}
		}
		return subgraph;
	}

	/**
	 * adds all networkComponents which are inside the circle, based coordinates.
	 *
	 * @param networkModel the network model
	 * @param path the path
	 * @param alternatives the alternatives
	 * @return the subgraph
	 */
	private Subgraph findSubgraph(NetworkModel networkModel, ArrayList<String> path, ArrayList<String> alternatives) {
		Polygon2D polygon = getPolygon2D(path);
		HashSet<String> components = new HashSet<String>(path);
		ArrayList<String> otherComponents = new ArrayList<String>(networkModel.getNetworkComponents().keySet());
		otherComponents.removeAll(components);
		for (String networkComponentID : otherComponents) {
			if (polygon.contains(coordinatesMap.get(networkComponentID))) {
				components.add(networkComponentID);
			}
		}
		alternatives.removeAll(components);
		Subgraph subgraph = new Subgraph(new ArrayList<String>(components), new ArrayList<String>(alternatives));
		subgraph.setPath(path);
		return subgraph;
	}


	/**
	 * Gets the polygon2 d.
	 *
	 * @param path the path
	 * @return the polygon2 d
	 */
	private Polygon2D getPolygon2D(ArrayList<String> path) {
		Polygon2D polygon = new Polygon2D();
		for (String componentID : path) {
			polygon.addPoint(coordinatesMap.get(componentID));
		}
		return polygon;
	}

	/**
	 * Calculates Subgraph for Circle and it's interfaces and adds it to the List if it's good
	 *
	 * @param ant the ant
	 * @param circles the circles
	 */
	public void addPathSearchBotToSubgraphs(PathSearchBot ant) {
		Subgraph subgraph = findSubgraph(networkModel, ant.getPath(), ant.getAllAlternativeComponents());
		if (subgraph.getInterfaceNetworkComponents().size() < minConnections) {
			subgraphs = new ArrayList<Subgraph>();
			subgraphs.add(subgraph);
			minConnections = subgraph.getInterfaceNetworkComponents().size();
		} else if (subgraph.getInterfaceNetworkComponents().size() == minConnections) {
			subgraphs.add(subgraph);
		}
	}

	/**
	 * translates components into Position2D.
	 *
	 * @param networkModel the network model
	 */
	private void buildCoordinatesMap(NetworkModel networkModel) {
		for (NetworkComponent networkComponent : networkModel.getNetworkComponents().values()) {
			Vector<GraphNode> graphNodes = networkModel.getNodesFromNetworkComponent(networkComponent);
			if (networkModel.isStarGraphElement(networkComponent)) {
				for (GraphNode graphNode : graphNodes) {
					if (networkModel.isCenterNodeOfStar(graphNode, networkComponent)) {
						coordinatesMap.put(networkComponent.getId(), graphNode.getPosition());
					}
				}
			} else {
				coordinatesMap.put(networkComponent.getId(), calculateCenter(graphNodes));
			}
		}
	}

	/**
	 * calculates the center for a list of graphNodes.
	 *
	 * @param graphNodes the graph nodes
	 * @return the point2 d
	 */
	private Point2D calculateCenter(Vector<GraphNode> graphNodes) {
		if (graphNodes.size() == 1) {
			return graphNodes.get(0).getPosition();
		}
		if (graphNodes.size() < 4) {
			return center(graphNodes);
		}
		return polygonCentroid(graphNodes);
	}

	/**
	 * calculates center of lines and triangle, 3 point circles.
	 *
	 * @param graphNodes the graph nodes
	 * @return the point2 d
	 */
	private Point2D center(Vector<GraphNode> graphNodes) {
		double x = 0;
		double y = 0;
		for (GraphNode graphNode : graphNodes) {
			x += graphNode.getPosition().getX();
			y += graphNode.getPosition().getY();
		}
		return new Point2D.Double(x / graphNodes.size(), y / graphNodes.size());
	}

	/**
	 * calculates Centroid.
	 *
	 * @param graphNodes the graph nodes
	 * @return the point2 d
	 */
	private Point2D polygonCentroid(Vector<GraphNode> graphNodes) {
		double A = 0;
		double xS = 0;
		double yS = 0;
		for (int i = 0; i < graphNodes.size() - 1; i++) {
			Point2D first = graphNodes.get(i).getPosition();
			Point2D second = graphNodes.get(i + 1).getPosition();
			double formulaPart = first.getX() * second.getY() - second.getX() * first.getY();
			A += formulaPart;
			xS += first.getX() * second.getX() * formulaPart;
			yS += first.getY() * second.getY() * formulaPart;
		}
		A /= 12;
		return new Point2D.Double(xS *= A, yS *= A);
	}
}
