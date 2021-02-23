package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;


// TODO: Auto-generated Javadoc
/**
 * The Class OSMScalingOperator.
 */
public class OSMScalingOperator implements ScalingOperator {

	/** The req pixel per meters. */
	protected double reqMetersPerPixel;
	
	/** The center. */
	protected WGS84LatLngCoordinate center;
	
	/** The scaling factor. */
	protected float scalingFactor = 1.0f;
	
	/** The zoom level. */
	protected int zoomLevel = 0;
	

	protected double refinementScalingFactor = 1.0;
	
	
	
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
	public int calcReqZoomLevelToFitVisualization(Rectangle2D landscapeDimension, Dimension visualizationDimension, WGS84LatLngCoordinate centerCoord) {
		reqMetersPerPixel = calcMetersPerPixel(landscapeDimension, visualizationDimension);
		System.out.println("Required resolution:"+reqMetersPerPixel+ "m/px");
		double metersPerPixelAtLatitude = EQUATOR_LENGTH_IN_METERS * Math.cos(Math.toRadians(centerCoord.getLatitude())) /TILE_SIZE / reqMetersPerPixel;
		return (int) Math.floor((Math.log(metersPerPixelAtLatitude ) / Math.log(2)));
	}
	
	protected double calcRefinementScalingFactor(Rectangle2D landscapeDimension, Dimension visualizationDimension, WGS84LatLngCoordinate centerCoord) {
		reqMetersPerPixel = calcMetersPerPixel(landscapeDimension, visualizationDimension);
		System.out.println("Required resolution:"+reqMetersPerPixel+ "m/px");
		int reqZoomLevel = calcReqZoomLevelToFitVisualization(landscapeDimension, visualizationDimension, centerCoord);
		double metersPerPixelByZoomLevel = calcMetersPerPixelByZoomLevel(reqZoomLevel, center.getLatitude());
		System.out.println("Given resolution:"+metersPerPixelByZoomLevel+ "m/px");
		System.out.println("Refinement factor:"+ metersPerPixelByZoomLevel / reqMetersPerPixel);
		return  metersPerPixelByZoomLevel / reqMetersPerPixel;		
	}
	
	protected double calcMetersPerPixel(Rectangle2D landscapeDimension, Dimension visualizationDimension) {
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
	protected float calcMetersPerPixelByZoomLevel(int zoomLevel, double latitude) {
		double metersPerPixel = (EQUATOR_LENGTH_IN_METERS * Math.cos(Math.toRadians(latitude))/ Math.pow(2, zoomLevel))/TILE_SIZE ;
		System.out.println("PixelsPerMeter raw:"+metersPerPixel);
		return (float) metersPerPixel;
	}
	
	
	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.ScalingOperator#getScalingFactorToZoomIn()
	*/
	@Override
	public float getScalingFactorToZoomIn() {
		if(this.zoomLevel == MIN_ZOOM) {
			return 1.0f;
		}
		System.out.println("Old scaling factor: "+ scalingFactor);
		double scalingFactor =  calcMetersPerPixelByZoomLevel(this.zoomLevel+1, this.center.getLatitude()) / calcMetersPerPixelByZoomLevel(this.zoomLevel, this.center.getLatitude()) * getRefinementScalingFactor() ;
		System.out.println("Zoom in scaling factor:"+ scalingFactor);
		return (float) scalingFactor;

	}

	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.ScalingOperator#getScalingFactorToZoomOut()
	*/
	@Override
	public float getScalingFactorToZoomOut() {
		if(this.zoomLevel == MAX_ZOOM) {
			return 1.0f;
		}
		System.out.println("Old scaling factor: "+ scalingFactor);
		double scalingFactor =  calcMetersPerPixelByZoomLevel(this.zoomLevel-1, this.center.getLatitude()) / calcMetersPerPixelByZoomLevel(this.zoomLevel, this.center.getLatitude()) *getRefinementScalingFactor();
		System.out.println("Zoom out scaling factor:"+ scalingFactor);
		return (float) scalingFactor;
//		return Math.toDegrees(EQUATOR_LENGTH_IN_METERS * Math.cos(center.getLatitude())/ Math.pow(2, zoomLevel-1+8)) / this.scalingFactor;
	}

	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.ScalingOperator#getActualScalingFactor()
	*/
	@Override
	public float getScalingFactor() {
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
	* @see org.awb.env.maps.ScalingOperator#zoomIn()
	*/
	public boolean zoomIn() {
		System.out.println("Try to zoom in");
		if(this.zoomLevel != MAX_ZOOM) {
			this.scalingFactor = getScalingFactorToZoomOut();
			this.zoomLevel--;
			System.out.println("Zooming level:"+ zoomLevel);
			return true;
		}else {
			this.scalingFactor = 1.0f;
			return false;
		}
	}
	
	
	/* (non-Javadoc)
	* @see org.awb.env.maps.ScalingOperator#zoomOut()
	*/
	public boolean zoomOut() {
		System.out.println("Try to zoom out");
		if(this.zoomLevel != MIN_ZOOM) {
			this.scalingFactor = getScalingFactorToZoomIn();
			this.zoomLevel++;
			System.out.println("Zooming level:"+ zoomLevel);
			return true;
		}else {
			this.scalingFactor = 1.0f;
			return false;
		}
		
	}

	public double getRefinementScalingFactor() {
		return refinementScalingFactor;
	}

	
	/**
	 * @param zoomLevel the zoomLevel to set
	 */
	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	
	

}
