package org.awb.env.networkModel.maps;


public interface ScalingOperator {

	/**
	 * Gets the scaling factor to zoom in.
	 *
	 * @return the scaling factor to zoom in
	 */
	public double getScalingFactorToZoomIn();

	/**
	 * Gets the scaling factor to zoom out.
	 *
	 * @return the scaling factor to zoom out
	 */
	public double getScalingFactorToZoomOut();
	
	/**
	 * Gets the actual scaling factor.
	 *
	 * @return the actual scaling factor
	 */
	public double getActualScalingFactor();
	
	/**
	 * 
	 * @return the actual zoom level
	 */
	public int getZoomLevel();

	
	/**
	 * Increase zoom level.
	 *
	 * @return true, if successful
	 */
	public boolean increaseZoomLevel();
	
	/**
	 * Decrease zoom level.
	 *
	 * @return true, if successful
	 */
	public boolean decreaseZoomLevel();
	
}
