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
package org.awb.env.networkModel.settings;

import de.enflexit.language.Language;
import jade.util.leap.Serializable;

/**
 * The Class LayoutSettings describes how a NetworkModel should be displayed and handled.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class LayoutSettings implements Serializable, Cloneable {

	private static final long serialVersionUID = 7769992668898387964L;

	/** The enumeration for CoordinateSystemXDirection */
	public enum CoordinateSystemXDirection {
		North("North"),
		West("West"),
		South("South"),
		East("East");
		
		private final String displayText;
		private CoordinateSystemXDirection(String displayText) {
			this.displayText = displayText;
		}
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return Language.translate(displayText, Language.EN);
		}
	}
	
	/** The enumeration for CoordinateSystemYDirection */
	public enum CoordinateSystemYDirection {
		ClockwiseToX("Clockwise to X"),
		CounterclockwiseToX("Counterclockwise to X");
		
		private final String displayText;
		private CoordinateSystemYDirection(String displayText) {
			this.displayText = displayText;
		}
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return Language.translate(displayText, Language.EN);
		}
	}
	
	/** The list of possible edge shapes */
	public static enum EdgeShape {
		BentLine,
		Box,
		ConfigurableLine,
		CubicCurve,
		Line,
		Loop,
		Orthogonal,
		QuadCurve,
		SimpleLoop,
		Wedge
	}
	/** Default raster size for guide grid. */
	public static final double DEFAULT_RASTER_SIZE = 5.0; 
	
	private String layoutName;
	
	private boolean geographicalLayout;
	
	private CoordinateSystemXDirection coordinateSystemXDirection = CoordinateSystemXDirection.East;
	private CoordinateSystemYDirection coordinateSystemYDirection = CoordinateSystemYDirection.ClockwiseToX; 
	
	private EdgeShape edgeShape = EdgeShape.Line;
	private boolean snap2Grid = true;
	private double snapRaster = DEFAULT_RASTER_SIZE;

	
	/**
	 * Instantiates new layout settings.
	 */
	public LayoutSettings() { }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		if (compareObject==null) return false;
		if (! (compareObject instanceof LayoutSettings)) return false;
		
		LayoutSettings ls2Compare = (LayoutSettings) compareObject;
		 
		boolean isEqual = true;
		if (isEqual==true) isEqual = (this.getLayoutName().equals(ls2Compare.getLayoutName()));
		
		if (isEqual==true) isEqual = (this.isGeographicalLayout()==ls2Compare.isGeographicalLayout());
		if (isEqual==true) isEqual = (this.getCoordinateSystemXDirection()==ls2Compare.getCoordinateSystemXDirection());
		if (isEqual==true) isEqual = (this.getCoordinateSystemYDirection()==ls2Compare.getCoordinateSystemYDirection());
		
		if (isEqual==true) isEqual = (this.getEdgeShape()==ls2Compare.getEdgeShape());
		if (isEqual==true) isEqual = (this.getSnapRaster()==ls2Compare.getSnapRaster());
		if (isEqual==true) isEqual = (this.isSnap2Grid()==ls2Compare.isSnap2Grid());
		
		return isEqual;
	}

	/**
	 * Returns a copy of the current instance.
	 * @return the copy
	 */
	public LayoutSettings getCopy() {
		
		LayoutSettings copy = new LayoutSettings();

		copy.setLayoutName(new String(this.getLayoutName()));
		
		copy.setGeographicalLayout(this.isGeographicalLayout());
		copy.setCoordinateSystemXDirection(this.getCoordinateSystemXDirection());
		copy.setCoordinateSystemYDirection(this.getCoordinateSystemYDirection());
		
		copy.setEdgeShape(this.getEdgeShape());
		copy.setSnapRaster(this.getSnapRaster());
		copy.setSnap2Grid(this.isSnap2Grid());
		
		return copy;
	}

	
	/**
	 * Sets the layout name.
	 * @param layoutName the new layout name
	 */
	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}
	/**
	 * Returns the layout name.
	 * @return the layout name
	 */
	public String getLayoutName() {
		return layoutName;
	}
	
	/**
	 * Sets that the current layout is a geographical layout.
	 * @param geographicalLayout the new geographical layout
	 */
	public void setGeographicalLayout(boolean geographicalLayout) {
		this.geographicalLayout = geographicalLayout;
	}
	/**
	 * Checks if the current layout is geographical layout.
	 * @return true, if is geographical layout
	 */
	public boolean isGeographicalLayout() {
		return geographicalLayout;
	}
	
	/**
	 * Sets the coordinate systems X direction.
	 * @param coordinateSystemXDirection the new coordinate system X direction
	 */
	public void setCoordinateSystemXDirection(CoordinateSystemXDirection coordinateSystemXDirection) {
		this.coordinateSystemXDirection = coordinateSystemXDirection;
	}
	/**
	 * Returns the coordinate systems X direction.
	 * @return the coordinate system X direction
	 */
	public CoordinateSystemXDirection getCoordinateSystemXDirection() {
		return coordinateSystemXDirection;
	}
	
	/**
	 * Sets the coordinate systems Y direction (relative to the x-axis - 90°).
	 * @param coordinateSystemYDirection the new coordinate system Y direction
	 */
	public void setCoordinateSystemYDirection(CoordinateSystemYDirection coordinateSystemYDirection) {
		this.coordinateSystemYDirection = coordinateSystemYDirection;
	}
	/**
	 * Returns the coordinate systems Y direction (relative to the x-axis - 90°).
	 * @return the coordinate system Y direction
	 */
	public CoordinateSystemYDirection getCoordinateSystemYDirection() {
		return coordinateSystemYDirection;
	}
	
	
	/**
	 * Sets the edge shape.
	 * @param edgeShape the new edge shape
	 */
	public void setEdgeShape(EdgeShape edgeShape) {
		this.edgeShape = edgeShape;
	}
	/**
	 * Gets the edge shape.
	 * @return the edge shape
	 */
	public EdgeShape getEdgeShape() {
		return edgeShape;
	}
	
	/**
	 * Sets the snap2 grid.
	 * @param snap2Grid the new snap2 grid
	 */
	public void setSnap2Grid(boolean snap2Grid) {
		this.snap2Grid = snap2Grid;
	}
	/**
	 * Checks if is snap2 grid.
	 * @return true, if is snap2grid is true
	 */
	public boolean isSnap2Grid() {
		return snap2Grid;
	}

	/**
	 * Sets the snap raster.
	 * @param snapRaster the new snap raster
	 */
	public void setSnapRaster(double snapRaster) {
		this.snapRaster = snapRaster;
	}
	/**
	 * Gets the snap raster.
	 * @return the snap raster
	 */
	public double getSnapRaster() {
		return snapRaster;
	}
	
}
