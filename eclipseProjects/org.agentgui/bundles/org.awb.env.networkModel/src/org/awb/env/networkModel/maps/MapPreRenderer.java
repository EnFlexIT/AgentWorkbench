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
package org.awb.env.networkModel.maps;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.GraphRectangle2D;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition;
import org.awb.env.networkModel.settings.LayoutSettings;

import de.enflexit.geography.coordinates.UTMCoordinate;
import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * The Class MapPreRenderer is used to prepare the painting of the graph by
 * downloading maps.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class MapPreRenderer implements VisualizationViewer.Paintable {

    private BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer;

    private TransformerForGraphNodePosition coordianteSOurceTransformer;

    private MapSettings lastMapSettings;
    private AffineTransform lastAffineTransform;
    private Dimension lastVisViewerDimension;

    private WGS84LatLngCoordinate lastWGS84LatLngCoordinate;

    /**
     * Instantiates a new MapPreRendererr.
     * 
     * @param basicGraphGuiVisViewer the BasicGraphGuiVisViewer
     */
    public MapPreRenderer(BasicGraphGuiVisViewer<GraphNode, GraphEdge> basicGraphGuiVisViewer) {
	this.visViewer = basicGraphGuiVisViewer;
	this.initializeMapRenderer();
    }

    /**
     * Initialize map renderer.
     */
    private void initializeMapRenderer() {

	MapRenderer mr = this.getMapRenderer();
	if (mr != null) {

	    // --- Get the center of the graph to show --------------
	    GraphRectangle2D graphRectUTM = GraphGlobals
		    .getGraphSpreadDimension(this.visViewer.getGraphLayout().getGraph());

	    // --- Get the center UTM / WGS84 coordinate ------------
	    MapSettings mapSet = this.visViewer.getCoordinateSystemPositionTransformer().getMapSettings();
	    UTMCoordinate centerUTMCoordinate = new UTMCoordinate(mapSet.getUTMLongitudeZone(),
		    mapSet.getUTMLatitudeZone(), graphRectUTM.getCenterX(), graphRectUTM.getCenterY());
	    WGS84LatLngCoordinate centerWGSCoordinate = centerUTMCoordinate.getWGS84LatLngCoordinate();

	    // --- Set center coordinate to renderer ----------------
	    mr.initialize(this.visViewer, centerWGSCoordinate);
	}
    }

    /**
     * Returns the current {@link MapRenderer}.
     * 
     * @return the map renderer
     */
    private MapRenderer getMapRenderer() {
	MapService ms = this.visViewer.getMapService();
	if (ms != null) {
	    MapRenderer mr = ms.getMapRenderer();
	    if (mr != null) {
		return mr;
	    }
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.uci.ics.jung.visualization.VisualizationServer.Paintable#paint(java.awt.
     * Graphics)
     */
    @Override
    public void paint(Graphics graphics) {

	// --- Call the current MapService ------------------------------------
	MapService ms = this.visViewer.getMapService();
	if (ms != null) {
	    MapRenderer mr = ms.getMapRenderer();
	    if (mr != null) {

		// --- Cast to Graphics2D instance ----------------------------
		Graphics2D g2d = (Graphics2D) graphics;
		Composite compReminder = g2d.getComposite();

		try {
		    // --------------------------------------------------------
		    // --- Update geographical location of the map ------------
		    this.updateCenterGeoLocation();

		    // --------------------------------------------------------
		    // --- Set the clip area to paint in ----------------------
		    g2d.setClip(0, 0, this.lastVisViewerDimension.width, this.lastVisViewerDimension.height);

		    // --------------------------------------------------------
		    // --- Prepare transparency of the map --------------------
		    float transparency = 1.0f - (float) (this.lastMapSettings.getMapTileTransparency() / 100.0);
		    AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency);
		    g2d.setComposite(alcom);

		    // --------------------------------------------------------
		    // --- Invoke map tile integration / painting -------------
		    mr.paintMap(g2d);

		} catch (Exception ex) {
		    ex.printStackTrace();
		} finally {
		    // --------------------------------------------------------
		    // --- Reset to non-transparency for graph painting -------
		    g2d.setComposite(compReminder);
		}

	    } else {
		System.err.println("[" + this.getClass().getSimpleName() + "] MapService '" + ms.getMapServiceName()
			+ "' does not implement MapRenderer!");
	    }
	}
    }

    /**
     * Will update the current geographical center location of the map
     * visualization.
     */
    public void updateCenterGeoLocation() {

	try {
	    MapRenderer mr = this.getMapRenderer();
	    if (mr != null && this.isChangedMapVisualization() == true) {
		this.lastAffineTransform = this.visViewer.getOverallAffineTransform();
		this.lastVisViewerDimension = this.visViewer.getSize();
		this.lastMapSettings = this.visViewer.getCoordinateSystemPositionTransformer().getMapSettings().clone();
		this.lastWGS84LatLngCoordinate = this.getCenterWgs84Coordinate();
		mr.setCenterGeoCoordinate(this.lastWGS84LatLngCoordinate);
	    }

	} catch (CloneNotSupportedException ex) {
	    ex.printStackTrace();
	}
    }

    /**
     * Checks if the map visualization settings have changed.
     *
     * @param graphics the graphics
     * @return true, if is changed overall affine transform
     */
    private boolean isChangedMapVisualization() {

	if (this.lastWGS84LatLngCoordinate != null && this.lastAffineTransform != null && this.lastMapSettings != null
		&& this.lastVisViewerDimension != null) {
	    // --- Check AffineTransform ------------------
	    boolean isChangedAffineTransform = !this.visViewer.getOverallAffineTransform()
		    .equals(this.lastAffineTransform);
	    if (isChangedAffineTransform == true)
		return true;

	    // --- Check VisViewer dimension --------------
	    boolean isChangedVisViewerDimension = !this.visViewer.getSize().equals(this.lastVisViewerDimension);
	    if (isChangedVisViewerDimension == true)
		return true;

	    // --- Check MapSettings ----------------------
	    MapSettings currMapSettings = this.visViewer.getCoordinateSystemPositionTransformer().getMapSettings();
	    boolean isChangedMapSetting = !currMapSettings.equals(this.lastMapSettings);

	    return isChangedMapSetting;
	}
	return true;
    }

    /**
     * Returns the current center WGS84-coordinate.
     * 
     * @return the center WGS84 coordinate
     */
    public WGS84LatLngCoordinate getCenterWgs84Coordinate() {

	boolean isPrintTransformation = false;
	WGS84LatLngCoordinate wgs84 = null;
	try {

	    Point2D centerVisViewer = new Point2D.Double(this.visViewer.getSize().getWidth() / 2.0,
		    this.visViewer.getSize().getHeight() / 2.0);

	    AffineTransform at = this.visViewer.getOverallAffineTransform();
	    Point2D pointJung = at.inverseTransform(centerVisViewer, null);

	    Point2D pointCoSy = this.getCoordinateSourceTransformer().inverseTransform(pointJung);
	    MapSettings ms = this.getCoordinateSourceTransformer().getMapSettings();
	    UTMCoordinate utm = new UTMCoordinate(ms.getUTMLongitudeZone(), ms.getUTMLatitudeZone(), pointCoSy.getX(),
		    pointCoSy.getY());
	    wgs84 = utm.getWGS84LatLngCoordinate();

	    if (isPrintTransformation == true) {
		System.out.println("Pos. Screen   " + centerVisViewer.getX() + ", " + centerVisViewer.getY() + "");
		System.out.println("Pos. Jung     " + pointJung.getX() + ", " + pointJung.getY() + "");
		System.out.println("Pos. CoordSys " + pointCoSy.getX() + ", " + pointCoSy.getY() + "");
		System.out.println("=> UTM        " + utm.toString());
		System.out.println("=> WGS84      " + wgs84.toString());
		System.out.println();
	    }

	} catch (Exception ex) {
	    System.err.println("[" + this.getClass().getSimpleName()
		    + "] Error while transforming visual coordinate into WGS84 coordinate:");
	    ex.printStackTrace();
	}
	return wgs84;

    }

    /**
     * Private coordinate source transformer that uses the current transformer to
     * get {@link LayoutSettings} and {@link MapSettings}.
     * 
     * @return the coordinate source transformer
     */
    private TransformerForGraphNodePosition getCoordinateSourceTransformer() {
	if (coordianteSOurceTransformer == null) {
	    coordianteSOurceTransformer = new TransformerForGraphNodePosition(null) {
		@Override
		public LayoutSettings getLayoutSettings() {
		    return MapPreRenderer.this.visViewer.getCoordinateSystemPositionTransformer().getLayoutSettings();
		}

		@Override
		public MapSettings getMapSettings() {
		    return MapPreRenderer.this.visViewer.getCoordinateSystemPositionTransformer().getMapSettings();
		}
	    };
	}
	return coordianteSOurceTransformer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.uci.ics.jung.visualization.VisualizationServer.Paintable#useTransform()
     */
    @Override
    public boolean useTransform() {
	return false;
    }

}
