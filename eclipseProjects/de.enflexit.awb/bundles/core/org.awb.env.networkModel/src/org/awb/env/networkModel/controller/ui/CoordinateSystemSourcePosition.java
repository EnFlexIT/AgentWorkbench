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
import java.awt.geom.Rectangle2D;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.settings.LayoutSettings;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * The Class CoordinateSystemSourcePosition describes the source position of the coordinate system for the 
 * visualization viewer within the {@link BasicGraphGui}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class CoordinateSystemSourcePosition {

	/**
	 * The enumeration for the vertical alignment.
	 */
	public enum VerticalAlignment {
		Top,
		Bottom
	}
	
	/**
	 * The enumeration for the horizontal alignment.
	 */
	public enum HorizontalAlignment {
		Left,
		Right
	}
	
	private VerticalAlignment verticalAlignment;
	private HorizontalAlignment horizontalAlignment;
	
	/**
	 * Instantiates a new coordinate source position.
	 *
	 * @param verticalAlignment the vertical alignment
	 * @param horizontalAlignment the horizontal alignment
	 */
	public CoordinateSystemSourcePosition(VerticalAlignment verticalAlignment, HorizontalAlignment horizontalAlignment) {
		this.setVerticalAlignment(verticalAlignment);
		this.setHorizontalAlignment(horizontalAlignment);
	}

	/**
	 * Gets the vertical alignment.
	 * @return the vertical alignment
	 */
	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}
	/**
	 * Sets the vertical alignment.
	 * @param verticalAlignment the new vertical alignment
	 */
	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	/**
	 * Gets the horizontal alignment.
	 * @return the horizontal alignment
	 */
	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}
	/**
	 * Sets the horizontal alignment.
	 * @param horizontalAlignment the new horizontal alignment
	 */
	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}
	
	@Override
	public String toString() {
		return "Coordinate System Source: " + this.getVerticalAlignment().name() + "-" + this.getHorizontalAlignment();
	}
	

	/**
	 * Returns the coordinate system source position as qualitative description in the form 'Top|Bottom-Left|Right.
	 *
	 * @param layoutSettings the layout settings
	 * @return the coordinate system source
	 */
	public static CoordinateSystemSourcePosition getCoordinateSystemSource(LayoutSettings layoutSettings) {
		
		VerticalAlignment vertical = null;
		HorizontalAlignment horizontal= null;
		
		switch (layoutSettings.getCoordinateSystemYDirection()) {
		case ClockwiseToX:
			switch (layoutSettings.getCoordinateSystemXDirection()) {
			case East:
				vertical = VerticalAlignment.Top;
				horizontal = HorizontalAlignment.Left;
				break;
			case North:
				vertical = VerticalAlignment.Bottom;
				horizontal = HorizontalAlignment.Left;
				break;
			case West:
				vertical = VerticalAlignment.Bottom;
				horizontal = HorizontalAlignment.Right;
				break;
			case South:
				vertical = VerticalAlignment.Top;
				horizontal = HorizontalAlignment.Right;
				break;
			}
			break;
			
		case CounterclockwiseToX:
			switch (layoutSettings.getCoordinateSystemXDirection()) {
			case East:
				vertical = VerticalAlignment.Bottom;
				horizontal = HorizontalAlignment.Left;
				break;
			case North:
				vertical = VerticalAlignment.Bottom;
				horizontal = HorizontalAlignment.Right;
				break;
			case West:
				vertical = VerticalAlignment.Top;
				horizontal = HorizontalAlignment.Right;
				break;
			case South:
				vertical = VerticalAlignment.Top;
				horizontal = HorizontalAlignment.Left;
				break;
			}
			break;
		}
		return new CoordinateSystemSourcePosition(vertical, horizontal);
	}

	
	/**
	 * Returns the coordinate system source point with respect to the specified visualization viewer.
	 *
	 * @param visViewer the vis viewer
	 * @param layoutSettings the layout settings
	 * @return the coordinate system source point in visualization viewer
	 */
	public static Point2D getCoordinateSystemSourcePointInVisualizationViewer(VisualizationViewer<GraphNode, GraphEdge> visViewer, LayoutSettings layoutSettings) {
		return getCoordinateSystemSourcePointInVisualizationViewer(visViewer, getCoordinateSystemSource(layoutSettings));
	}
	/**
	 * Returns the coordinate system source point with respect to the specified visualization viewer.
	 *
	 * @param visViewer the vis viewer
	 * @param sourcePosition the source position
	 * @return the coordinate system source point in visualization viewer
	 */
	public static Point2D getCoordinateSystemSourcePointInVisualizationViewer(VisualizationViewer<GraphNode, GraphEdge> visViewer, CoordinateSystemSourcePosition sourcePosition) {
		
		Rectangle2D visViewerRect = visViewer.getVisibleRect();
		
		double xPos = 0.0;
		double yPos = 0.0;
		switch (sourcePosition.getVerticalAlignment()) {
		case Top:
			switch (sourcePosition.getHorizontalAlignment()) {
			case Left:
				// --- Nothing to do here ---
				break;
			case Right:
				xPos = visViewerRect.getWidth();
				break;
			}
			break;

		case Bottom:
			switch (sourcePosition.getHorizontalAlignment()) {
			case Left:
				yPos = visViewerRect.getHeight();
				break;
			case Right:
				xPos = visViewerRect.getWidth();
				yPos = visViewerRect.getHeight();
				break;
			}
			break;
		}
		return new Point2D.Double(xPos, yPos);
	}
	
}
