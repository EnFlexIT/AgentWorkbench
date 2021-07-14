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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.maps.MapService;
import org.awb.env.networkModel.positioning.GraphNodePositionFactory;
import org.awb.env.networkModel.settings.LayoutSettings;
import org.awb.env.networkModel.settings.LayoutSettings.CoordinateSystemXDirection;
import org.awb.env.networkModel.settings.LayoutSettings.CoordinateSystemYDirection;

import de.enflexit.common.SerialClone;
import de.enflexit.geography.coordinates.UTMCoordinate;
import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.coords.UTMCoord;

/**
 * The Class TransformerForGraphNodePosition justifies a {@link GraphNode} position in the visualization viewer 
 * according to the {@link LayoutSettings}, where the position of the used coordinate system is specified 
 * by a {@link CoordinateSystemXDirection} and a {@link CoordinateSystemYDirection}.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TransformerForGraphNodeGeoPosition extends TransformerForGraphNodePosition {

	/**
	 * Instantiates a new transformer for node position that dynamically 'asks' the graph controller 
	 * about the current and possibly changed settings in the {@link NetworkModel} (dynamic approach).
	 * 
	 * @param graphController the current graph controller
	 */
	public TransformerForGraphNodeGeoPosition(GraphEnvironmentController graphController) {
		super(graphController);
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition#transform(org.awb.env.networkModel.GraphNode)
	 */
	@Override
	public Point2D transform(GraphNode graphNode) {
		if (graphNode==null) return null;
		return this.transform(graphNode.getCoordinate());
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition#transform(java.awt.geom.Point2D)
	 */
	@Override
	public Point2D transform(Point2D coordGraphNode) {

		// --- The default case ---------------------------------------------------------
		Point2D layoutPosition = null;
		
		// --- Transform to visualization location --------------------------------------
		MapService ms = this.getMapSettings().getMapService();
		if (ms==null || ms.getMapRenderer()==null) {
			// --------------------------------------------------------------------------
			// --- Use UTM coordinates for the visualization ----------------------------
			// --------------------------------------------------------------------------
			try {
				// --- Define the destination UTM zones ---------------------------------
				int utmLongZone   = this.getMapSettings().getUTMLongitudeZone();
				String utmLatZone = this.getMapSettings().getUTMLatitudeZone();
				
				// --- Get a local instance for the layout visualization ----------------
				UTMCoordinate utmCoordinate = null;
				if (coordGraphNode instanceof UTMCoordinate) {
					utmCoordinate = SerialClone.clone((UTMCoordinate) coordGraphNode); 
				} else if (coordGraphNode instanceof WGS84LatLngCoordinate) {
					WGS84LatLngCoordinate wgs84 = (WGS84LatLngCoordinate) coordGraphNode;
					utmCoordinate = wgs84.getUTMCoordinate();
				} else {
					utmCoordinate = (UTMCoordinate) GraphNodePositionFactory.convertToCoordinate(coordGraphNode);
				}
				
				// --- Correct longitude zone for the visualization? --------------------
				if (utmCoordinate.getLongitudeZone()!=utmLongZone) {
					// --- Move to the correct longitude zone ---------------------------
					utmCoordinate.transformZone(utmLongZone);
				}
				layoutPosition = super.transform(utmCoordinate);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		} else {
			// --------------------------------------------------------------------------
			// --- Use WGS84 coordinates and the specific MapRenderer -------------------
			// --------------------------------------------------------------------------
			try {
				// --- Get WGS84 coordinate ---------------------------------------------
				WGS84LatLngCoordinate wgs84Coord = null;
				if (coordGraphNode instanceof WGS84LatLngCoordinate) {
					wgs84Coord = SerialClone.clone((WGS84LatLngCoordinate) coordGraphNode);
				} else if (coordGraphNode instanceof UTMCoordinate) {
					UTMCoordinate utmCoord = (UTMCoordinate) coordGraphNode;
					wgs84Coord = utmCoord.getWGS84LatLngCoordinate();
				} else {
					UTMCoordinate utmCoord = (UTMCoordinate) GraphNodePositionFactory.convertToCoordinate(coordGraphNode);
					wgs84Coord = utmCoord.getWGS84LatLngCoordinate();
				}
				
				// --- Get the xy-Position on Screen from current MapService ------------ 
				Point2D posOnScreen = this.getMapSettings().getMapService().getMapRenderer().getPositionOnScreen(wgs84Coord);

				// --- Use Jung transformer to calculate virtual graph position ---------
				AffineTransform at = this.getBasicGraphGuiVisViewer().getOverallAffineTransform();
				layoutPosition = at.inverseTransform(posOnScreen, null);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return layoutPosition;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition#inverseTransform(java.awt.geom.Point2D)
	 */
	@Override
	public Point2D inverseTransform(Point2D posGeoLayout) {
		 
		// --- The default case ---------------------------------------------------------
		Point2D posGraphNodeUTM = super.inverseTransform(posGeoLayout);
		if (true) return posGraphNodeUTM;
		
		// --- Do the inverse things to the above ---------------------------------------
		try {
			
			// --- Get Jung coordinates -------------------------------------------------
			AffineTransform at = this.getBasicGraphGuiVisViewer().getOverallAffineTransform();
			Point2D posOnScreen = at.transform(posGeoLayout, null);
			
			// --- Get WGS84 coordinates ------------------------------------------------
			WGS84LatLngCoordinate wgsCoordinate = this.getMapSettings().getMapService().getMapRenderer().getGeoCoordinate(posOnScreen);
			UTMCoordinate utmCoordinate = wgsCoordinate.getUTMCoordinate();
			
			UTMCoord utmCoord = UTMCoord.fromLatLon(Angle.fromDegrees(wgsCoordinate.getLatitude()), Angle.fromDegrees(wgsCoordinate.getLongitude()));
			System.out.println("UTM oww:  " + utmCoordinate);
			System.out.println("UTM NASA: " + utmCoord);
			
			// --- testwise, re convert to WGS84 using NASA system ----------------------  
			LatLon latLonCoord = UTMCoord.locationFromUTMCoord(utmCoord.getZone(), utmCoord.getHemisphere(), utmCoord.getEasting(), utmCoord.getNorthing());
			WGS84LatLngCoordinate wgsCoordinateReverted = new WGS84LatLngCoordinate(latLonCoord.getLatitude().getDegrees(), latLonCoord.getLongitude().getDegrees());
			System.out.println("WGS84 org:      " + wgsCoordinate);
			System.out.println("WGS84 reverted: " + wgsCoordinateReverted);
			
			
			// --- Transform to destination longitude zone ------------------------------
			int utmLngZone = this.getMapSettings().getUTMLongitudeZone();
			utmCoordinate.transformZone(utmLngZone);
			
			posGraphNodeUTM = new Point2D.Double(utmCoordinate.getEasting(), utmCoordinate.getNorthing());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return posGraphNodeUTM;
	}
	
	
}
