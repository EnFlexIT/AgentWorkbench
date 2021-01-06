package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import org.awb.env.networkModel.maps.MapRenderer;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseMapRenderer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class BaseMapRenderer extends JComponent implements MapRenderer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6308436046951210047L;
	
	/** The graphics. */
	private Graphics graphics;
	
	/** The mouse input listener. */
	protected MouseInputListener mouseInputListener;
	
	/** The mouse wheel listener. */
	protected MouseWheelListener mouseWheelListener;
	
	
	/**
	 * Paint map.
	 *
	 * @param graphics the graphics
	 * @param geoCoord the geo coord
	 * @param dimension the dimension
	 */
	@Override
	public void paintMap(Graphics graphics, WGS84LatLngCoordinate geoCoord, Rectangle dimension) {
		this.paintMap(graphics, geoCoord, dimension, new ArrayList<WGS84LatLngCoordinate>());
	}
	
	/**
	 * Paint map.
	 *
	 * @param graphics the graphics
	 * @param geoCoord the geo coord
	 * @param dimension the dimension
	 * @param positions the positions
	 */
	@Override
	public void paintMap(Graphics graphics, WGS84LatLngCoordinate geoCoord, Rectangle dimension, List<WGS84LatLngCoordinate> positions) {
		this.graphics = graphics;
	}

	/**
	 * Sets the mouse input listener.
	 *
	 * @param mouseListener the new mouse input listener
	 */
	@Override
	public void setMouseInputListener(MouseInputListener mouseListener) {
		this.mouseInputListener = mouseListener;
	}

	/**
	 * Sets the mouse wheel listener.
	 *
	 * @param wheelListener the new mouse wheel listener
	 */
	@Override
	public void setMouseWheelListener(MouseWheelListener wheelListener) {
		this.mouseWheelListener = wheelListener;		
	}
	





}
