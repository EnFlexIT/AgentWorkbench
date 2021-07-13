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
import org.awb.env.networkModel.settings.LayoutSettings;
import org.awb.env.networkModel.settings.LayoutSettings.CoordinateSystemXDirection;
import org.awb.env.networkModel.settings.LayoutSettings.CoordinateSystemYDirection;

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
		return this.transform(graphNode.getPosition());
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition#transform(java.awt.geom.Point2D)
	 */
	@Override
	public Point2D transform(Point2D posGraphNodeUTM) {

		// --- The default case ---------------------------------------------------------
		Point2D posGeoLayout = super.transform(posGraphNodeUTM);
		
		// --- Transform to correct geographical location -------------------------------
		try {
			
			// --- Get WGS84-coordinate from current UTM coordinate ---------------------
			int utmLngZone = this.getMapSettings().getUTMLongitudeZone();
			String utmLatZone = this.getMapSettings().getUTMLatitudeZone();
			
			UTMCoordinate utmCoordinate = new UTMCoordinate(utmLngZone, utmLatZone, posGraphNodeUTM.getX(), posGraphNodeUTM.getY());
			WGS84LatLngCoordinate wgsCoordinate = utmCoordinate.getWGS84LatLngCoordinate();
			
			// --- Get the xy-Position on Screen from the current MapService ------------ 
			Point2D posOnScreen = this.getMapSettings().getMapService().getMapRenderer().getPositionOnScreen(wgsCoordinate);
			
			// --- Get Jung coordinates -------------------------------------------------
			AffineTransform at = this.getBasicGraphGuiVisViewer().getOverallAffineTransform();
			Point2D posInvers = at.inverseTransform(posOnScreen, null);
			posGeoLayout = posInvers;
					
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return posGeoLayout;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition#inverseTransform(java.awt.geom.Point2D)
	 */
	@Override
	public Point2D inverseTransform(Point2D posGeoLayout) {
		 
		// --- The default case ---------------------------------------------------------
		Point2D posGraphNodeUTM = super.inverseTransform(posGeoLayout);
		
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
			
			LatLon latLonCoord = UTMCoord.locationFromUTMCoord(utmCoord.getZone(), utmCoord.getHemisphere(), utmCoord.getEasting(), utmCoord.getNorthing());
			WGS84LatLngCoordinate wgsCoordinateReverted = new WGS84LatLngCoordinate(latLonCoord.getLatitude().getDegrees(), latLonCoord.getLongitude().getDegrees());
//			wgsCoordinateReverted = utmCoordinate.getWGS84LatLngCoordinate();
			System.out.println("WGS84 org:      " + wgsCoordinate);
			System.out.println("WGS84 reverted: " + wgsCoordinateReverted);
			
			
			// --- Transform to destination longitude zone ------------------------------
			int utmLngZone = this.getMapSettings().getUTMLongitudeZone();
//			utmCoordinate.transformZone(utmLngZone);
			
			posGraphNodeUTM = new Point2D.Double(utmCoordinate.getEasting(), utmCoordinate.getNorthing());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return posGraphNodeUTM;
	}
	
	
}
