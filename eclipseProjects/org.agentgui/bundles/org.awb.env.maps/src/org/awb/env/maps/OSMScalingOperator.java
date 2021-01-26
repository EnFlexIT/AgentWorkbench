package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import org.awb.env.networkModel.maps.ScalingOperator;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;


// TODO: Auto-generated Javadoc
/**
 * The Class OSMScalingOperator.
 */
public class OSMScalingOperator implements ScalingOperator {

	/** The req pixel per meters. */
	private double reqMetersPerPixel;
	
	/** The center. */
	private WGS84LatLngCoordinate center;
	
	/** The scaling factor. */
	private double scalingFactor = 1.0;
	
	/** The zoom level. */
	private int zoomLevel = 0;
	
	private double refinementScalingFactor = 1.0;
	
	
	
	/**   The length of the equator in meters  . */
	static final float EQUATOR_LENGTH_IN_METERS = 40075016.686f; 
	
	/** The size of map tiles in pixels. */
	static final int TILE_SIZE = 256;
	
	/** The Constant MAX_ZOOM. */
	static final int MAX_ZOOM = 0; 

	/** The Constant MIN_ZOOM. */
	static final int MIN_ZOOM = 19; 

	
	/**
	 * Instantiates a new OSM scaling operator.
	 *
	 * @param landscapeDimension the landscape dimension
	 * @param visualizationDimension the visualization dimension
	 * @param centerCoord the center coord
	 * @param zoomLevel the zoom level
	 */
	public OSMScalingOperator(Rectangle2D landscapeDimension, Dimension visualizationDimension, WGS84LatLngCoordinate centerCoord, int zoomLevel) {
		this.center = centerCoord;
		this.zoomLevel = MIN_ZOOM - zoomLevel;
		this.scalingFactor = calcMetersPerPixelByZoomLevel(zoomLevel, centerCoord.getLatitude());
		this.refinementScalingFactor = calcRefinementScalingFactor(landscapeDimension, visualizationDimension, centerCoord);
	}
	
	/**
	 * Instantiates a new OSM scaling operator.
	 *
	 * @param landscapeDimension the landscape dimension
	 * @param visualizationDimension the visualization dimension
	 * @param centerCoord the center coordinate
	 */
	public  OSMScalingOperator(Rectangle2D landscapeDimension, Dimension visualizationDimension, WGS84LatLngCoordinate centerCoord) {
		this.center = centerCoord;
		this.zoomLevel = MIN_ZOOM - this.calcReqZoomLevelToFitVisualization(landscapeDimension, visualizationDimension, centerCoord);
		this.scalingFactor = calcMetersPerPixelByZoomLevel(this.zoomLevel, center.getLatitude());
		this.refinementScalingFactor = calcRefinementScalingFactor(landscapeDimension, visualizationDimension, centerCoord);
	}

	/**
	 * Calculate required zoom level to fit visualization rectangle.
	 *
	 * @param landscapeDimension the landscape dimension
	 * @param visualizationDimension the visualization dimension
	 * @param centerCoord the center coordinate
	 * @return the zoom level
	 */
	protected int calcReqZoomLevelToFitVisualization(Rectangle2D landscapeDimension, Dimension visualizationDimension, WGS84LatLngCoordinate centerCoord) {
		reqMetersPerPixel = calcMetersPerPixel(landscapeDimension, visualizationDimension);
		double metersPerPixelAtLatitude = EQUATOR_LENGTH_IN_METERS * Math.cos(Math.toRadians(centerCoord.getLatitude())) /TILE_SIZE / reqMetersPerPixel;
		return (int) Math.floor((Math.log(metersPerPixelAtLatitude ) / Math.log(2)));
	}
	
	protected double calcRefinementScalingFactor(Rectangle2D landscapeDimension, Dimension visualizationDimension, WGS84LatLngCoordinate centerCoord) {
		reqMetersPerPixel = calcMetersPerPixel(landscapeDimension, visualizationDimension);
		int reqZoomLevel = calcReqZoomLevelToFitVisualization(landscapeDimension, visualizationDimension, centerCoord);
		double metersPerPixelByZoomLevel = calcMetersPerPixelByZoomLevel(reqZoomLevel, center.getLatitude());
		return  metersPerPixelByZoomLevel / reqMetersPerPixel;		
	}
	
	protected double calcMetersPerPixel(Rectangle2D landscapeDimension, Dimension visualizationDimension)
	{
		double reqMetersPerPixelWidth = landscapeDimension.getWidth() / visualizationDimension.getWidth();
		double reqMetersPerPixelHeight = landscapeDimension.getHeight() / visualizationDimension.getHeight();
		return (reqMetersPerPixelHeight >= reqMetersPerPixelWidth) ? reqMetersPerPixelHeight : reqMetersPerPixelWidth;
	}
	
	/**
	 * Calculate required pixels per meter by a given zoom level and latitude
	 * 
	 * @param zoomLevel the zoom level
	 * @param latitude the latitude
	 * @return the scaling factor
	 */
	protected double calcMetersPerPixelByZoomLevel(int zoomLevel, double latitude) {
		double metersPerPixel = (EQUATOR_LENGTH_IN_METERS * Math.cos(Math.toRadians(latitude))/ Math.pow(2, zoomLevel))/TILE_SIZE ;
		System.out.println("PixelsPerMeter raw:"+metersPerPixel);
		return metersPerPixel;
	}
	
	
	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.ScalingOperator#getScalingFactorToZoomIn()
	*/
	@Override
	public double getScalingFactorToZoomIn() {
		if(this.zoomLevel == MIN_ZOOM) {
			return 1.0;
		}
		System.out.println("Old scaling factor: "+ scalingFactor);
		double scalingFactor =  calcMetersPerPixelByZoomLevel(this.zoomLevel+1, this.center.getLatitude()) / calcMetersPerPixelByZoomLevel(this.zoomLevel, this.center.getLatitude()) ;
		System.out.println("Zoom in scaling factor:"+ scalingFactor);
		return scalingFactor;

	}

	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.ScalingOperator#getScalingFactorToZoomOut()
	*/
	@Override
	public double getScalingFactorToZoomOut() {
		if(this.zoomLevel == MAX_ZOOM) {
			return 1.0;
		}
		System.out.println("Old scaling factor: "+ scalingFactor);
		double scalingFactor =  calcMetersPerPixelByZoomLevel(this.zoomLevel-1, this.center.getLatitude()) / calcMetersPerPixelByZoomLevel(this.zoomLevel, this.center.getLatitude()) ;
		System.out.println("Zoom out scaling factor:"+ scalingFactor);
		return scalingFactor;
//		return Math.toDegrees(EQUATOR_LENGTH_IN_METERS * Math.cos(center.getLatitude())/ Math.pow(2, zoomLevel-1+8)) / this.scalingFactor;
	}

	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.ScalingOperator#getActualScalingFactor()
	*/
	@Override
	public double getActualScalingFactor() {
		return scalingFactor;
	}

	/**
	 * Gets the required pixels per meter.
	 *
	 * @return required pixels per meters
	 */
	public double getReqPixelsPerMeter() {
		return reqMetersPerPixel;
	}

	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.ScalingOperator#getZoomLevel()
	*/
	@Override
	public int getZoomLevel() {
		return this.zoomLevel;
	}
	
	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.ScalingOperator#increaseZoomLevel()
	*/
	public boolean increaseZoomLevel() {
		System.out.println("Try to increase zoom level");
		if(this.zoomLevel != MAX_ZOOM) {
			this.scalingFactor = getScalingFactorToZoomOut();
			this.zoomLevel--;
			System.out.println("Zooming level:"+ zoomLevel);
			return true;
		}else {
			this.scalingFactor = 1.0;
			return false;
		}
	}
	
	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.ScalingOperator#decreaseZoomLevel()
	*/
	public boolean decreaseZoomLevel() {
		System.out.println("Try to decrease zoom level");
		if(this.zoomLevel != MIN_ZOOM) {
			this.scalingFactor = getScalingFactorToZoomIn();
			this.zoomLevel++;
			System.out.println("Zooming level:"+ zoomLevel);
			return true;
		}else {
			this.scalingFactor = 1.0;
			return false;
		}
		
	}

	public double getRefinementScalingFactor() {
		return refinementScalingFactor;
	}


	
	

}
