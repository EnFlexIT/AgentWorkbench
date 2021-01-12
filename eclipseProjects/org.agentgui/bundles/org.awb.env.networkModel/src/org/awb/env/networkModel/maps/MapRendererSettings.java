package org.awb.env.networkModel.maps;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition;

import de.enflexit.geography.coordinates.UTMCoordinate;
import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

/**
 * The Class MapRendererSettings provides information for the rendering of maps.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MapRendererSettings implements Serializable {

	private static final long serialVersionUID = -5126554545457917787L;

	private Dimension visualizationDimension;
	private Rectangle2D landscapeDimension;

	private WGS84LatLngCoordinate topLeftPosition;
	private WGS84LatLngCoordinate topRightPosition;
	
	private WGS84LatLngCoordinate centerPostion;
	
	private WGS84LatLngCoordinate bottomLeftPosition;
	private WGS84LatLngCoordinate bottomRightPosition;
	
	private int mapTileTransparency = 0;
	
	
	/**
	 * Instantiates new visual map print settings.
	 * @param visViewer the vis viewer
	 * @param at the AffineTransform to transform to Jung coordinates
	 */
	public MapRendererSettings(BasicGraphGuiVisViewer<?,?> visViewer, AffineTransform at) {
		this.initialize(visViewer, at);
	}
	/**
	 * Initialize all fields derived from the current visualization viewer and its affine transformer to 
	 * map coordinates from layout to visualization.
	 *
	 * @param visViewer the vis viewer
	 * @param at the AffineTransform to transform to Jung coordinates
	 */
	private void initialize(BasicGraphGuiVisViewer<?,?> visViewer, AffineTransform at) {

		boolean isPrintTransformation = false;
		
		TransformerForGraphNodePosition<GraphNode, GraphEdge> cspt = visViewer.getCoordinateSystemPositionTransformer();
		Dimension vvDim = visViewer.getSize();

		UTMCoordinate topLeftCoordinate = this.getUTMCoordinate(cspt, at, new Point2D.Double(0, 0), isPrintTransformation);
        UTMCoordinate topRightCoordinate = this.getUTMCoordinate(cspt, at, new Point2D.Double(vvDim.getWidth(), 0), isPrintTransformation);
        UTMCoordinate bottomLeftCoordinate = this.getUTMCoordinate(cspt, at, new Point2D.Double(0, vvDim.getHeight()), isPrintTransformation);
        UTMCoordinate bottomRightCoordinate = this.getUTMCoordinate(cspt, at, new Point2D.Double(vvDim.getWidth(), vvDim.getWidth()), isPrintTransformation);

        this.setTopLeftPosition(topLeftCoordinate.getWGS84LatLngCoordinate());
		this.setTopRightPosition(topRightCoordinate.getWGS84LatLngCoordinate());
		this.setBottomLeftPosition(bottomLeftCoordinate.getWGS84LatLngCoordinate());
		this.setBottomRightPosition(bottomRightCoordinate.getWGS84LatLngCoordinate());
		
		double centerX = vvDim.getWidth()  / 2.0;
		double centerY = vvDim.getHeight() / 2.0;
		this.setCenterPostion(this.getWGS84LatLngCoordinate(cspt, at, new Point2D.Double(centerX, centerY), isPrintTransformation));
		
		this.setMapTileTransparency(cspt.getMapSettings().getMapTileTransparency());

		this.setVisualizationDimension(vvDim);
		this.calcLandscapeDimension(topLeftCoordinate, bottomRightCoordinate);
	}
	
	/**
	 * Returns a WGS 84 coordinate from the specified visual point in the application.
	 *
	 * @param cspTransformer the coordinate system position transformer
	 * @param at the AffineTransform to transform to Jung coordinates
	 * @param point2D the point 2 D
	 * @param isPrintTransformation the indicator to print the transformation of the coordinates
	 * @return the WGS84LatLngCoordinate
	 */
	private WGS84LatLngCoordinate getWGS84LatLngCoordinate(TransformerForGraphNodePosition<GraphNode, GraphEdge> cspTransformer, AffineTransform at, Point2D point2D, boolean isPrintTransformation) {
		
		WGS84LatLngCoordinate wgs84 = null;
		try {
			
			MapSettings ms = cspTransformer.getMapSettings();
			
			Point2D pointJung = at.inverseTransform(point2D, null);
			Point2D pointCoSy = cspTransformer.inverseTransform(pointJung);
			UTMCoordinate utm = new UTMCoordinate(ms.getUTMLongitudeZone(), ms.getUTMLatitudeZone(), pointCoSy.getX(), pointCoSy.getY());
			wgs84 = utm.getWGS84LatLngCoordinate(); 

			if (isPrintTransformation==true) {
				System.out.println("Pos. Screen   " + point2D.getX() + ", " + point2D.getY() + "");
				System.out.println("Pos. Jung     " + pointJung.getX() + ", " + pointJung.getY() + "");
				System.out.println("Pos. CoordSys " + pointCoSy.getX() + ", " + pointCoSy.getY() + "");
				System.out.println("=> UTM        " + utm.toString());
				System.out.println("=> WGS84      " + wgs84.toString());
				System.out.println();
			}
			
		} catch (NoninvertibleTransformException nitEx) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error while transforming visual coordinate into WGS84 coordinate:");
			nitEx.printStackTrace();
		}
		return wgs84;
	}
	
	/**
	 * Gets the UTM coordinate.
	 *
	 * @param cspTransformer the csp transformer
	 * @param at the at
	 * @param point2D the point 2 D
	 * @param isPrintTransformation the is print transformation
	 * @return the UTM coordinate
	 */
	private UTMCoordinate getUTMCoordinate(TransformerForGraphNodePosition<GraphNode, GraphEdge> cspTransformer, AffineTransform at, Point2D point2D, boolean isPrintTransformation) {
		
		UTMCoordinate utm = null;
		try {
			
			MapSettings ms = cspTransformer.getMapSettings();
			
			Point2D pointJung = at.inverseTransform(point2D, null);
			Point2D pointCoSy = cspTransformer.inverseTransform(pointJung);
			utm = new UTMCoordinate(ms.getUTMLongitudeZone(), ms.getUTMLatitudeZone(), pointCoSy.getX(), pointCoSy.getY());
			if (isPrintTransformation==true) {
				System.out.println("Pos. Screen   " + point2D.getX() + ", " + point2D.getY() + "");
				System.out.println("Pos. Jung     " + pointJung.getX() + ", " + pointJung.getY() + "");
				System.out.println("Pos. CoordSys " + pointCoSy.getX() + ", " + pointCoSy.getY() + "");
				System.out.println("=> UTM        " + utm.toString());
				System.out.println();
			}
			
		} catch (NoninvertibleTransformException nitEx) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error while transforming visual coordinate into WGS84 coordinate:");
			nitEx.printStackTrace();
		}
		return utm;
	}
	
	
	public WGS84LatLngCoordinate getTopLeftPosition() {
		return topLeftPosition;
	}
	public void setTopLeftPosition(WGS84LatLngCoordinate topLeftPosition) {
		this.topLeftPosition = topLeftPosition;
	}
	
	
	public WGS84LatLngCoordinate getTopRightPosition() {
		return topRightPosition;
	}
	public void setTopRightPosition(WGS84LatLngCoordinate topRightPosition) {
		this.topRightPosition = topRightPosition;
	}
	
	
	public WGS84LatLngCoordinate getCenterPostion() {
		return centerPostion;
	}
	public void setCenterPostion(WGS84LatLngCoordinate centerPostion) {
		this.centerPostion = centerPostion;
	}
	
	
	public WGS84LatLngCoordinate getBottomLeftPosition() {
		return bottomLeftPosition;
	}
	public void setBottomLeftPosition(WGS84LatLngCoordinate bottomLeftPosition) {
		this.bottomLeftPosition = bottomLeftPosition;
	}
	
	
	public WGS84LatLngCoordinate getBottomRightPosition() {
		return bottomRightPosition;
	}
	public void setBottomRightPosition(WGS84LatLngCoordinate bottomRightPosition) {
		this.bottomRightPosition = bottomRightPosition;
	}
	
	
	public int getMapTileTransparency() {
		return mapTileTransparency;
	}
	public void setMapTileTransparency(int mapTileTransparency) {
		this.mapTileTransparency = mapTileTransparency;
	}

	
	public Dimension getVisualizationDimension() {
		return visualizationDimension;
	}
	public void setVisualizationDimension(Dimension visualizationDimension) {
		this.visualizationDimension = visualizationDimension;
	}

	
	public Rectangle2D getLandscapeDimension() {
		return landscapeDimension;
	}
	public void setLandscapeDimension(Rectangle2D landscapeDimension) {
		this.landscapeDimension = landscapeDimension;
	}
	/**
	 * Calculate the landscape dimensions in meter.
	 *
	 * @param topLeftCoordinate the top left  UTM coordinate
	 * @param bottomRightCoordinate the bottom right UTM coordinate
	 * @return the dimension 2 D
	 */
	private void calcLandscapeDimension(UTMCoordinate topLeftCoordinate, UTMCoordinate bottomRightCoordinate) {
		double widthInMeter = Math.abs(topLeftCoordinate.getEasting()- bottomRightCoordinate.getEasting());
		double heightInMeter = Math.abs(topLeftCoordinate.getNorthing() - bottomRightCoordinate.getNorthing());
		this.landscapeDimension = new Rectangle2D.Double(0, 0, widthInMeter, heightInMeter);
	}
	
}
