package org.awb.env.maps;


public interface ScalingOperator {

	/**
	 * Gets the scaling factor to zoom in.
	 *
	 * @return the scaling factor to zoom in
	 */
	public float getScalingFactorToZoomIn();

	/**
	 * Gets the scaling factor to zoom out.
	 *
	 * @return the scaling factor to zoom out
	 */
	public float getScalingFactorToZoomOut();
	
	/**
	 * Gets the actual scaling factor.
	 *
	 * @return the actual scaling factor
	 */
	public float getScalingFactor();
	
	/**
	 * 
	 * @return the actual zoom level
	 */
	public int getZoomLevel();

	
	/**
	 * Zoom in if it is possible.
	 *
	 * @return true, if successful
	 */
	public boolean zoomIn();
	
	/**
	 * Zoom out if it is possible.
	 *
	 * @return true, if successful
	 */
	public boolean zoomOut();
	
}
