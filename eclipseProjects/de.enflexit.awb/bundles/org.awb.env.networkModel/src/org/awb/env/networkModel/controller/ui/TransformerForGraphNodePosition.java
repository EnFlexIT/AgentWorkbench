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
package org.awb.env.networkModel.controller.ui;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.maps.MapSettings;
import org.awb.env.networkModel.settings.LayoutSettings;
import org.awb.env.networkModel.settings.LayoutSettings.CoordinateSystemXDirection;
import org.awb.env.networkModel.settings.LayoutSettings.CoordinateSystemYDirection;

import com.google.common.base.Function;

/**
 * The Class TransformerForGraphNodePosition justifies a {@link GraphNode} position in the visualization viewer 
 * according to the {@link LayoutSettings}, where the position of the used coordinate system is specified 
 * by a {@link CoordinateSystemXDirection} and a {@link CoordinateSystemYDirection}.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TransformerForGraphNodePosition implements Function<GraphNode, Point2D> {

	private GraphEnvironmentController graphController;
	
	private LayoutSettings layoutSettings;
	private MapSettings mapSettings;
	
	private BasicGraphGuiVisViewer<?, ?> basicGraphGuiVisViewer;
	
	/**
	 * Instantiates a new transformer for node position that dynamically 'asks' the graph controller 
	 * about the current and possibly changed settings in the {@link NetworkModel} (dynamic approach).
	 * 
	 * @param graphController the current graph controller
	 */
	public TransformerForGraphNodePosition(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	/**
	 * Returns the current or initially specified layout settings.
	 * @return the layout settings
	 */
	public LayoutSettings getLayoutSettings() {
		// --- Try to get current settings from GraphEnvironmentController ---- 
		if (this.graphController!=null) {
			return this.graphController.getNetworkModel().getLayoutSettings();
		}
		// --- Return the specified LayoutSettings (not dynamic) --------------
		return layoutSettings;
	}
	/**
	 * Returns the current MapSettings.
	 * @return the map settings
	 */
	public MapSettings getMapSettings() {
		// --- Try to get current settings from GraphEnvironmentController ----
		if (this.graphController!=null) {
			return this.graphController.getNetworkModel().getMapSettings();
		}
		// --- Return the specified MapSettings (not dynamic) -----------------
		return mapSettings;
	}

	
	/**
	 * Sets the current instance of the {@link BasicGraphGuiVisViewer} to this static layout.
	 * @param basicGraphGuiVisViewer the basic graph gui vis viewer
	 */
	public void setBasicGraphGuiVisViewer(BasicGraphGuiVisViewer<?, ?> basicGraphGuiVisViewer) {
		this.basicGraphGuiVisViewer = basicGraphGuiVisViewer;
	}
	/**
	 * Returns the current {@link BasicGraphGuiVisViewer} that is used to visualize the current Graph.
	 * @return the current BasicGraphGuiVisViewer
	 */
	public BasicGraphGuiVisViewer<?, ?> getBasicGraphGuiVisViewer() {
		return basicGraphGuiVisViewer;
	}
	
	/**
	 * Return the list of JUNG coordinates from the list of GraphNode positions.
	 *
	 * @param graphPosList the GraphNode position list
	 * @return the position list in JUNG coordinates
	 */
	public List<Point2D> getJungPositionList(List<Point2D> graphPosList) {
		ArrayList<Point2D> jungPosList = new ArrayList<>();
		for (int i = 0; i < graphPosList.size(); i++) {
			Point2D graphPos = graphPosList.get(i);
			jungPosList.add(this.apply(graphPos));
		}
		return jungPosList;
	}
	
	
	/* (non-Javadoc)
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public Point2D apply(GraphNode graphNode) {
		if (graphNode==null) return null;
		return this.apply(graphNode.getPosition());
	}
	/**
	 * Transforms the specified position into the position for the JUNG visualization.
	 *
	 * @param npGraphNode the node position of a graph node
	 * @return the point 2 D
	 */
	public Point2D apply(Point2D npGraphNode) {
		
		if (npGraphNode==null) return null;
		
		Point2D visualNodePosition = new Point2D.Double();
		
		LayoutSettings layoutSettings = this.getLayoutSettings();
		switch (layoutSettings.getCoordinateSystemYDirection()) {
		case ClockwiseToX:
			switch (layoutSettings.getCoordinateSystemXDirection()) {
			case East:
				visualNodePosition = npGraphNode;
				break;
			case North:
				visualNodePosition.setLocation(npGraphNode.getY(), npGraphNode.getX() * (-1));
				break;
			case West:
				visualNodePosition.setLocation(npGraphNode.getX() * (-1), npGraphNode.getY() * (-1));
				break;
			case South:
				visualNodePosition.setLocation(npGraphNode.getY() * (-1), npGraphNode.getX());
				break;
			}
			break;
			
		case CounterclockwiseToX:
			switch (layoutSettings.getCoordinateSystemXDirection()) {
			case East:
				visualNodePosition.setLocation(npGraphNode.getX(), npGraphNode.getY() * (-1));
				break;
			case North:
				visualNodePosition.setLocation(npGraphNode.getY() * (-1), npGraphNode.getX() * (-1));
				break;
			case West:
				visualNodePosition.setLocation(npGraphNode.getX() * (-1), npGraphNode.getY());
				break;
			case South:
				visualNodePosition.setLocation(npGraphNode.getY(), npGraphNode.getX());
				break;
			}
			break;
		}
		return visualNodePosition;
	}
	
	/**
	 * Inverse transforms the specified position into the position for the GraphNode.
	 *
	 * @param visualNodePosition the visual node position
	 * @return the point 2 D
	 */
	public Point2D inverseTransform(Point2D visualNodePosition) {
		
		Point2D npGraphNode = new Point2D.Double();
		
		LayoutSettings layoutSettings = this.getLayoutSettings();
		switch (layoutSettings.getCoordinateSystemYDirection()) {
		case ClockwiseToX:
			switch (layoutSettings.getCoordinateSystemXDirection()) {
			case East:
				npGraphNode = visualNodePosition;
				break;
			case North:
				npGraphNode.setLocation(visualNodePosition.getY() * (-1), visualNodePosition.getX());
				break;
			case West:
				npGraphNode.setLocation(visualNodePosition.getX() * (-1), visualNodePosition.getY() * (-1));
				break;
			case South:
				npGraphNode.setLocation(visualNodePosition.getY(), visualNodePosition.getX() * (-1));
				break;
			}
			break;
			
		case CounterclockwiseToX:
			switch (layoutSettings.getCoordinateSystemXDirection()) {
			case East:
				npGraphNode.setLocation(visualNodePosition.getX(), visualNodePosition.getY()* (-1));
				break;
			case North:
				npGraphNode.setLocation(visualNodePosition.getY() * (-1), visualNodePosition.getX() * (-1));
				break;
			case West:
				npGraphNode.setLocation(visualNodePosition.getX() * (-1), visualNodePosition.getY());
				break;
			case South:
				npGraphNode.setLocation(visualNodePosition.getY(), visualNodePosition.getX());
				break;
			}
			break;
		}
		return npGraphNode;
	}
	
}
