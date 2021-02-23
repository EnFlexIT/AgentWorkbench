package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

public class OSMAbsoluteScalingOperator extends OSMScalingOperator {

	 ArrayList<Float> scalingFactors;
	
	/**
	 * Instantiates a new OSM scaling operator.
	 *
	 * @param landscapeDimension the landscape dimension
	 * @param visualizationDimension the visualization dimension
	 * @param centerCoord the center coord
	 * @param zoomLevel the zoom level
	 */
	public OSMAbsoluteScalingOperator(Rectangle2D landscapeDimension, Dimension visualizationDimension, WGS84LatLngCoordinate centerCoord, int zoomLevel) {
		super(landscapeDimension, visualizationDimension, centerCoord, zoomLevel);
		calcScalingFactorsByZoomLevel();
	}
	
	/**
	 * Instantiates a new OSM scaling operator.
	 *
	 * @param landscapeDimension the landscape dimension
	 * @param visualizationDimension the visualization dimension
	 * @param centerCoord the center coordinate
	 */
	public  OSMAbsoluteScalingOperator(Rectangle2D landscapeDimension, Dimension visualizationDimension, WGS84LatLngCoordinate centerCoord) {
		super(landscapeDimension, visualizationDimension, centerCoord);
		calcScalingFactorsByZoomLevel();
	}
	
	
    /**
     * Calc scaling factors by zoom level.
     */
    protected void calcScalingFactorsByZoomLevel() {
    	double scalingFactorRefined = 1f * getRefinementScalingFactor();
    	this.scalingFactors = new ArrayList<Float>();
    	for(int i = MAX_ZOOM; i <= MIN_ZOOM; i++) {
    		scalingFactors.add(i, (float) (Math.pow(2, i - getZoomLevel())));    	
    	}
    }
    
    
    public float getScalingFactor() {
    	int zoomLevel = getZoomLevel();
    	if(scalingFactors.size() > zoomLevel  && scalingFactors.get(zoomLevel) != null) {
    		return scalingFactors.get(zoomLevel);
    	}
    	return 1.0f;
    }
    
    /**
     * Gets the scaling factor.
     *
     * @param zoomLevel the zoom level
     * @return the scaling factor
     */
    public float getScalingFactor(int zoomLevel) {
    	if(scalingFactors.size() > zoomLevel  && scalingFactors.get(zoomLevel) != null) {
    		return scalingFactors.get(zoomLevel);
    	}
    	return 1.0f;
    }
    
    /* (non-Javadoc)
    * @see org.awb.env.maps.OSMScalingOperator#zoomIn()
    */
    @Override
    public boolean zoomIn() {
    	System.out.println("Try to zoom in");
		int zoomLevel = getZoomLevel();
		if(zoomLevel != MAX_ZOOM) {
			zoomLevel--;
			this.scalingFactor = getScalingFactor(zoomLevel);
			this.setZoomLevel(zoomLevel);
			System.out.println("Zooming level:"+ zoomLevel);
			return true;
		}else {
			this.scalingFactor = getScalingFactor(zoomLevel);
			return false;
		}
    } 
    
    /* (non-Javadoc)
    * @see org.awb.env.maps.OSMScalingOperator#zoomOut()
    */
    @Override
    public boolean zoomOut() {
		System.out.println("Try to zoom out");
		int zoomLevel = getZoomLevel();
		if(zoomLevel != MIN_ZOOM) {
			zoomLevel++;
			this.scalingFactor = getScalingFactor(zoomLevel);
			this.setZoomLevel(zoomLevel);
			System.out.println("Zooming level:"+ zoomLevel);
			return true;
		}else {
			this.scalingFactor = getScalingFactor(zoomLevel);
			return false;
		}
    }
	
}