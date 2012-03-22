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
package gasmas.clustering;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
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
public class AntCircleAnalyser {

	/** The coordinates map. */
	private HashMap<String, Point2D> coordinatesMap = new HashMap<String, Point2D>();

	/** The subgraphs. */
	private HashMap<Integer, ArrayList<Subgraph>> subgraphs = new HashMap<Integer, ArrayList<Subgraph>>();

	/**
	 * Instantiates a new ant circle analyser.
	 *
	 * @param ants the ants
	 * @param networkModel the network model
	 */
	public AntCircleAnalyser(ArrayList<Ant> ants, NetworkModel networkModel) {
		buildCoordinatesMap(networkModel);
		findSubgraphs(ants, networkModel);
	}

	public Subgraph getBestSubgraph() {
		ArrayList<Integer> keys = new ArrayList<Integer>(subgraphs.keySet());
		Collections.sort(keys);
		ArrayList<Subgraph> minInterfaceSubgraphs = subgraphs.get(keys.get(0));
		Subgraph subgraph = minInterfaceSubgraphs.get(0);
		for (Subgraph subgraphInList : minInterfaceSubgraphs) {
			if (subgraphInList.getNetworkComponents().size() < subgraph.getNetworkComponents().size()) {
				subgraph = subgraphInList;
			}
		}
		return subgraph;
	}

	/**
	 * Find subgraphs.
	 *
	 * @param ants the ants
	 * @param networkModel the network model
	 */
	private void findSubgraphs(ArrayList<Ant> ants, NetworkModel networkModel) {
		HashMap<Integer, ArrayList<Ant>> circles = buildCircleMap(ants);
		ArrayList<Integer> keys = new ArrayList<Integer>(circles.keySet());
		Collections.sort(keys);
		for (Integer integer : keys) {
			for (Ant ant : circles.get(integer)) {
				Subgraph subgraph = findSubgraph(networkModel, ant.getPath(), ant.getAllAlternativeComponents());
				addSubgraphToMap(subgraph);
			}
		}
	}

	/**
	 * Adds the subgraph to map.
	 *
	 * @param subgraph the subgraph
	 */
	private void addSubgraphToMap(Subgraph subgraph) {
		if (subgraphs.containsKey(subgraph.getInterfaceNetworkComponents().size())) {
			ArrayList<Subgraph> subgraphList = subgraphs.get(subgraph.getInterfaceNetworkComponents().size());
			for (Subgraph subgraphInList : subgraphList) {
				if (subgraphInList.getNetworkComponents().containsAll(subgraph.getNetworkComponents())) {
					continue;
				}
			}
			subgraphList.add(subgraph);
		} else {
			ArrayList<Subgraph> subgraphList = new ArrayList<Subgraph>();
			subgraphList.add(subgraph);
			subgraphs.put(subgraph.getInterfaceNetworkComponents().size(), subgraphList);
		}
	}

	/**
	 * adds all networkComponents which are inside the circle, based coordinates.
	 *
	 * @param networkModel the network model
	 * @param path the path
	 * @param alternatives the alternatives
	 * @return the subgraph
	 */
	private Subgraph findSubgraph(NetworkModel networkModel, ArrayList<String> path, HashSet<String> alternatives) {
		Polygon2D polygon = getPolygon2D(path);
		HashSet<String> components = new HashSet<String>(path);
		for (String networkComponentID : networkModel.getNetworkComponents().keySet()) {
			if (polygon.contains(coordinatesMap.get(networkComponentID)) && !components.contains(coordinatesMap.get(networkComponentID))) {
				components.add(networkComponentID);
			}
		}
		alternatives.removeAll(components);
		return new Subgraph(new ArrayList<String>(components), new ArrayList<String>(alternatives));
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
	 * builds a Map of Ants containing circles.
	 *
	 * @param ants the ants
	 * @return the hash map
	 */
	private HashMap<Integer, ArrayList<Ant>> buildCircleMap(ArrayList<Ant> ants) {
		HashMap<Integer, ArrayList<Ant>> circles = new HashMap<Integer, ArrayList<Ant>>();
		for (Ant ant : new ArrayList<Ant>(ants)) {
			if (ant.isCircle()) {
				addAntToMap(ant, circles);
			}
		}
		return circles;
	}

	/**
	 * Adds the ant to map.
	 *
	 * @param ant the ant
	 * @param circles the circles
	 */
	private void addAntToMap(Ant ant, HashMap<Integer, ArrayList<Ant>> circles) {
		if (circles.containsKey(ant.getPath().size())) {
			for (Ant circleAnt : circles.get(ant.getPath().size())) {
				if (circleAnt.getPath().containsAll(ant.getPath())) {
					continue;
				}
			}
			circles.get(ant.getPath().size()).add(ant);
		} else {
			ArrayList<Ant> circleAntList = new ArrayList<Ant>();
			circleAntList.add(ant);
			circles.put(ant.getPath().size(), circleAntList);
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
