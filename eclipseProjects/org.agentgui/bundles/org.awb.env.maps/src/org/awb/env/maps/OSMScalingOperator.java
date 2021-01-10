package org.awb.env.maps;

import java.awt.Dimension;

import org.awb.env.networkModel.maps.ScalingOperator;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;
import javafx.geometry.Dimension2D;


public class OSMScalingOperator implements ScalingOperator {

	private double reqPixelPerMeters;
	
	private WGS84LatLngCoordinate center;
	
	private double scalingFactor = 1.0;
	
	private int zoomLevel = 0;
	
	
	
	/** Defines the length of the equator in meters  */
	static final float EQUATOR_LENGTH_IN_METERS = 40075016.686f; 
	
	static final int MAX_ZOOM = 19; 

	static final int MIN_ZOOM = 0; 

	
	public OSMScalingOperator(Dimension2D landscapeDimension, Dimension visualizationDimension, WGS84LatLngCoordinate centerCoord) {
		double reqPixelPerMeterWidth = landscapeDimension.getWidth() / visualizationDimension.getWidth();
		double reqPixelPerMeterHeight = landscapeDimension.getHeight() / visualizationDimension.getHeight();
		if(reqPixelPerMeterHeight >= reqPixelPerMeterWidth) {
			this.reqPixelPerMeters = reqPixelPerMeterHeight;
		}else {
			this.reqPixelPerMeters = reqPixelPerMeterWidth;
		}
		this.center = centerCoord;
		this.zoomLevel = (int) Math.floor(Math.log10(Math.toDegrees(EQUATOR_LENGTH_IN_METERS * Math.cos(center.getLatitude()))) / Math.log(2)); 
		this.scalingFactor = Math.toDegrees(EQUATOR_LENGTH_IN_METERS * Math.cos(center.getLatitude())/ Math.pow(2, zoomLevel+8));
	}

	@Override
	public double getScalingFactorToZoomIn() {
		if(this.zoomLevel == MIN_ZOOM) {
			return 1.0;
		}
		return Math.toDegrees(EQUATOR_LENGTH_IN_METERS * Math.cos(center.getLatitude())/ Math.pow(2, zoomLevel+1+8)) / this.scalingFactor;
	}

	@Override
	public double getScalingFactorToZoomOut() {
		if(this.zoomLevel == MAX_ZOOM) {
			return 1.0;
		}
		return Math.toDegrees(EQUATOR_LENGTH_IN_METERS * Math.cos(center.getLatitude())/ Math.pow(2, zoomLevel-1+8)) / this.scalingFactor;
	}

	@Override
	public double getActualScalingFactor() {
		// TODO Auto-generated method stub
		return scalingFactor;
	}
	
	

}
