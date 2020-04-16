package org.awb.env.networkModel.persistence;

import java.awt.geom.Point2D;

// TODO: Auto-generated Javadoc
/**
 * This class provides mapping functionality from geo coordinates to display coordinates (0:0-<upperBOund>) 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class GeoCoordinatesMapper {
	// --- The range of geo coordinates that should be mapped -------
	private double geoCoordinateMinX;
	private double geoCoordinateMaxX;
	private double geoCoordinateMinY;
	private double geoCoordinateMaxY;
	
	// --- The target range of display coordinates (upper bounds, the actual range depends on the ration of the original data range)
	private int targetRangeMaxX;
	private int targetRangeMaxY;
	
	// --- These offsets can be used to shift the mapped coordinates, if the smallest coordinates should not start at 0:0   
	private int offsetX = 0;
	private int offsetY = 0;
	
	// --- Set to true if the coordinates for this direction should be inverted 
	private boolean invertXAxis = false;
	private boolean invertYAxis = false;

	// --- The conversion factor will be calculated, based on the ration between original and target range
	private double conversionFactor;
	private int displayCoordinateMaxX;
	private int displayCoordinateMaxY;
	
	/**
	 * Instantiates a new GeoCoordinatesMapper, with all mandatory fields set.
	 * @param geoCoordinateMinX the smallest x coordinate of the original range
	 * @param geoCoordinateMaxX the largest x coordinate of the original range
	 * @param geoCoordinateMinY the smallest y coordinate of the original range
	 * @param geoCoordinateMaxY the largest y coordinate of the original range
	 * @param targetRangeMaxX the largest x coordinate of the target range
	 * @param targetRangeMaxY the largest y coordinate of the target range
	 */
	public GeoCoordinatesMapper(double geoCoordinateMinX, double geoCoordinateMaxX, double geoCoordinateMinY, double geoCoordinateMaxY, int targetRangeMaxX, int targetRangeMaxY) {
		this.geoCoordinateMinX = geoCoordinateMinX;
		this.geoCoordinateMaxX = geoCoordinateMaxX;
		this.geoCoordinateMinY = geoCoordinateMinY;
		this.geoCoordinateMaxY = geoCoordinateMaxY;
		this.targetRangeMaxX = targetRangeMaxX;
		this.targetRangeMaxY = targetRangeMaxY;
	}

	/**
	 * Gets the current smallest x coordinate of the original range.
	 * @return the current smallest x coordinate of the original range
	 */
	public double getGeoCoordinateMinX() {
		return geoCoordinateMinX;
	}
	/**
	 * Sets the smallest x coordinate of the original range.
	 * @param geoCoordinateMinX the smallest x coordinate of the original range
	 */
	public void setGeoCoordinateMinX(double geoCoordinateMinX) {
		this.geoCoordinateMinX = geoCoordinateMinX;
	}
	/**
	 * Gets the current largest x coordinate of the original range.
	 * @return the current largest x coordinate of the original range
	 */
	public double getGeoCoordinateMaxX() {
		return geoCoordinateMaxX;
	}
	/**
	 * Sets the largest x coordinate of the original range.
	 * @param geoCoordinateMaxX the largest x coordinate of the original range
	 */
	public void setGeoCoordinateMaxX(double geoCoordinateMaxX) {
		this.geoCoordinateMaxX = geoCoordinateMaxX;
	}
	/**
	 * Gets the current smallest y coordinate of the original range.
	 * @return the current smallest y coordinate of the original range
	 */
	public double getGeoCoordinateMinY() {
		return geoCoordinateMinY;
	}
	/**
	 * Sets the smallest y coordinate of the original range.
	 * @param geoCoordinateMinY the smallest y coordinate of the original range
	 */
	public void setGeoCoordinateMinY(double geoCoordinateMinY) {
		this.geoCoordinateMinY = geoCoordinateMinY;
	}
	/**
	 * Gets the current largest y coordinate of the original range.
	 * @return the current largest y coordinate of the original range
	 */
	public double getGeoCoordinateMaxY() {
		return geoCoordinateMaxY;
	}
	/**
	 * Sets the largest y coordinate of the original range.
	 * @param geoCoordinateMaxY the largest y coordinate of the original range
	 */
	public void setGeoCoordinateMaxY(double geoCoordinateMaxY) {
		this.geoCoordinateMaxY = geoCoordinateMaxY;
	}
	
	

	/**
	 * Gets the current upper bound for the target range's x coordinate
	 * @return the current upper bound for the target range's x coordinate
	 */
	public int getTargetRangeMaxX() {
		return targetRangeMaxX;
	}
	/**
	 * Sets the upper bound for the target range's x coordinate
	 * @param targetRangeMaxX the new upper bound for the target range's x coordinate
	 */
	public void setTargetRangeMaxX(int targetRangeMaxX) {
		this.targetRangeMaxX = targetRangeMaxX;
	}
	/**
	 * Gets the current upper bound for the target range's y coordinate
	 * @return the current upper bound for the target range's y coordinate
	 */
	public int getTargetRangeMaxY() {
		return targetRangeMaxY;
	}
	/**
	 * Sets the upper bound for the target range's y coordinate
	 * @param targetRangeMaxY the new upper bound for the target range's y coordinate
	 */
	public void setTargetRangeMaxY(int targetRangeMaxY) {
		this.targetRangeMaxY = targetRangeMaxY;
	}

	/**
	 * Gets the current offset for x coordinates.
	 * @return the current offset for x coordinates
	 */
	public int getOffsetX() {
		return offsetX;
	}
	/**
	 * Sets the offset for x coordinates.
	 * @param offsetX the offset for x coordinates
	 */
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}
	/**
	 * Gets the current offset for y coordinates.
	 * @return the current offset for y coordinates
	 */
	public int getOffsetY() {
		return offsetY;
	}
	/**
	 * Sets the offset for y coordinates.
	 * @param offsetY the offset for y coordinates
	 */
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	/**
	 * Sets the same offset for both directions.
	 * @param offset the new offset
	 */
	public void setOffset(int offset) {
		this.setOffsetX(offset);
		this.setOffsetY(offset);
	}

	
	/**
	 * Checks if x coordinates will be inverted.
	 * @return true, if x coordinates will be inverted.
	 */
	public boolean isInvertXAxis() {
		return invertXAxis;
	}
	/**
	 * Specifies if x coordinates should be inverted.
	 * @param invertXAxis if true, x coordinates will be inverted.
	 */
	public void setInvertXAxis(boolean invertXAxis) {
		this.invertXAxis = invertXAxis;
	}
	/**
	 * Checks if y coordinates will be inverted.
	 * @return true, if y coordinates will be inverted.
	 */
	public boolean isInvertYAxis() {
		return invertYAxis;
	}
	/**
	 * Specifies if y coordinates should be inverted.
	 * @param invertYAxis if true, y coordinates will be inverted.
	 */
	public void setInvertYAxis(boolean invertYAxis) {
		this.invertYAxis = invertYAxis;
	}
	
	private double getConversionFactor() {
		if (conversionFactor==0) {
			conversionFactor = this.calculateConversionFactorAndTargetRange();
		}
		return conversionFactor;
	}
	
	/**
	 * Calculates conversion factor for mapping coordinates, and the actual target range, that is
	 * defined by the user-specified upper bounds and the ration of the original data range.
	 * @return the conversion factor
	 */
	private double calculateConversionFactorAndTargetRange() {
		
		double conversionFactor;

		// --- Determine the range of the original geo data -------------------
		double geoCoordinatesRangeX = this.geoCoordinateMaxX-this.geoCoordinateMinX;
		double geoCoordinatesRangeY = this.geoCoordinateMaxY-this.geoCoordinateMinY;
		
		// --- Determine the actual target range, considering the offset ------
		int targetRangeX = this.targetRangeMaxX-this.offsetX;
		int targetRangeY = this.targetRangeMaxY-this.offsetY;
		
		// --- Determine the conversion factor and the actual target range ----
		if ((geoCoordinatesRangeX/geoCoordinatesRangeY)<=(targetRangeX/targetRangeY)) {
			conversionFactor = targetRangeY / geoCoordinatesRangeY;
			this.displayCoordinateMaxX = (int) Math.round(targetRangeX * (geoCoordinatesRangeX/geoCoordinatesRangeY));
			this.displayCoordinateMaxY = targetRangeY;
		} else {
			conversionFactor = targetRangeX / geoCoordinatesRangeX;
			this.displayCoordinateMaxX = targetRangeX;
			this.displayCoordinateMaxY = (int) Math.round(targetRangeY * (geoCoordinatesRangeX/geoCoordinatesRangeY));
		}
		
		return conversionFactor;
	}
	
	/**
	 * Maps a pair of geo coordinates the specified range of display coordinates.
	 * @param geoCoordinates the geo coordinates
	 * @return the display coordinates
	 */
	public Point2D mapGeoCoordinatesToDisplay(Point2D geoCoordinates) {
		return this.mapGeoCoordinatesToDisplay(geoCoordinates.getX(), geoCoordinates.getY());
	}
	
	/**
	 * Maps a pair of geo coordinates the specified range of display coordinates.
	 * @param geoCoordinateX the geo coordinate X
	 * @param geoCoordinateY the geo coordinate Y
	 * @return the display coordinates
	 */
	public Point2D mapGeoCoordinatesToDisplay(double geoCoordinateX, double geoCoordinateY) {
		// --- Convert to the target range ----------------
		double displayCoordinateX = (geoCoordinateX-this.geoCoordinateMinX) * this.getConversionFactor() + this.getOffsetX();
		double displayCoordinateY = (geoCoordinateY-this.geoCoordinateMinY) * this.getConversionFactor() + this.getOffsetY();
		// --- Invert, if specified
		if (this.isInvertXAxis()==true) {
			displayCoordinateX = this.displayCoordinateMaxX - displayCoordinateX;
		}
		if (this.isInvertYAxis()==true) {
			displayCoordinateY = this.displayCoordinateMaxY - displayCoordinateY;
		}
		return new Point2D.Double(displayCoordinateX, displayCoordinateY);
	}
}	
