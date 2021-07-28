package org.awb.env.maps;

import java.util.ArrayList;
import java.util.List;

/**
 * The singleton class OSMZoomLevels holds all available {@link ZoomLevel} that correspond
 * to the zoom level of OpenStreetMap.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OSMZoomLevels {

	private List<ZoomLevel> zoomLevelList;
	private Double latitude;
	
	private static OSMZoomLevels thisInstance;
	/**
	 * Returns the singleton of the OSMZoomLevels.
	 * @return single instance of OSMZoomLevels
	 */
	public static OSMZoomLevels getInstance() {
		if (thisInstance==null) {
			thisInstance = new OSMZoomLevels();
		}
		return thisInstance;
	}
	/**
	 * private constructor for the OSMZoomLevels.
	 */
	private OSMZoomLevels() { }
	
	/**
	 * Initializes the local vector of know ZoomLevel.
	 */
	private void recalcResolutions() {
		
		// --------------------------------------------------------------------
		// --- Fill the local list of the know 20 Zoom Level in m / pixel -----
		// --- From: https://wiki.openstreetmap.org/wiki/DE:Zoom_levels -------
		// --- $zoomstufen = array(156412, 78206, 39103, 19551, 9776, 4888, 2444, 1222, 610.984, 305.492, 152.746, 76.373, 38.187, 19.093, 9.547, 4.773, 2.387, 1.193, 0.596, 0.298)
		// --------------------------------------------------------------------		
//		this.getZoomLevelList().add(new ZoomLevel(0, 156412));
//		this.getZoomLevelList().add(new ZoomLevel(1, 78206));
//		this.getZoomLevelList().add(new ZoomLevel(2, 39103));
//		this.getZoomLevelList().add(new ZoomLevel(3, 19551));
//		this.getZoomLevelList().add(new ZoomLevel(4, 9776));
//		this.getZoomLevelList().add(new ZoomLevel(5, 4888));
//		this.getZoomLevelList().add(new ZoomLevel(6, 2444));
//		this.getZoomLevelList().add(new ZoomLevel(7, 1222));
//		this.getZoomLevelList().add(new ZoomLevel(8, 610.984));
//		this.getZoomLevelList().add(new ZoomLevel(9, 305.492));
//		
//		this.getZoomLevelList().add(new ZoomLevel(10, 152.746));
//		this.getZoomLevelList().add(new ZoomLevel(11, 76.373));
//		this.getZoomLevelList().add(new ZoomLevel(12, 38.187));
//		this.getZoomLevelList().add(new ZoomLevel(13, 19.093));
//		this.getZoomLevelList().add(new ZoomLevel(14, 9.547));
//		this.getZoomLevelList().add(new ZoomLevel(15, 4.773));
//		this.getZoomLevelList().add(new ZoomLevel(16, 2.387));
//		this.getZoomLevelList().add(new ZoomLevel(17, 1.193));
//		this.getZoomLevelList().add(new ZoomLevel(18, 0.596));
//		this.getZoomLevelList().add(new ZoomLevel(19, 0.298));
		
		// --- Clear the list first -------------------------------------------
		this.getZoomLevelList(this.latitude).clear();
		
		// --- Refill the list by newly calculated values ---------------------
		double circumference = 40075016.686; // in m 
		for (int zoomLevel = 0; zoomLevel < 20; zoomLevel++) {
			// --- Formula from above web-site --------------------------------
			double resolution = circumference * Math.cos(Math.toRadians(this.latitude)) / Math.pow(2, (zoomLevel+8));
			this.getZoomLevelList(this.latitude).add(new ZoomLevel(zoomLevel, resolution));
		}
	}

	/**
	 * Returns the zoom level list.
	 *
	 * @param newLatitude the latitude coordinate in degree
	 * @return the zoom level list
	 */
	public List<ZoomLevel> getZoomLevelList(double newLatitude) {
		// --- Create zoom level list -------------------------------
		if (zoomLevelList==null) {
			zoomLevelList = new ArrayList<>();
		}
		// --- Recalculate resolutions? -----------------------------
		if (this.isRecalculateResolutions(newLatitude)==true) {
			this.latitude = newLatitude;
			this.recalcResolutions();
		}
		return zoomLevelList;
	}
	/**
	 * Checks if the current resolution needs to be recalculated based on the new latitude coordinate.
	 *
	 * @param newLatitude the new latitude
	 * @return true, if is recalculate resolutions
	 */
	private boolean isRecalculateResolutions(double newLatitude) {
		
		if (this.latitude==null) return true;
		if (newLatitude==this.latitude) return false;
		
		int roundBoundary = 2;
		double latLocalRounded = this.round(this.latitude, roundBoundary);
		double latNewRounded   = this.round(newLatitude, roundBoundary);
		return latNewRounded != latLocalRounded;
	}
	
	/**
	 * Returns the {@link ZoomLevel} for the specified Open Street Map zoom level.
	 *
	 * @param osmZoomLevel the osm zoom level
	 * @param latitude the latitude
	 * @return the zoom level
	 */
	public ZoomLevel getZoomLevel(int osmZoomLevel, double latitude) {
		return this.getZoomLevelList(latitude).get(osmZoomLevel);
	}
	
	
	/**
	 * Returns the closest {@link ZoomLevel} for the specified Jung scaling.
	 *
	 * @param scaling the scaling
	 * @@param latitude the current latitude area
	 * @return the closest zoom level of jung scaling
	 */
	public ZoomLevel getClosestZoomLevelOfJungScaling(double scaling, double latitude) {
		return this.getClosestZoomLevelOfResolution((1.0 / scaling), latitude);
	}
	
	/**
	 * Returns the closest {@link ZoomLevel} for the specified map resolution in m/pixel.
	 *
	 * @param resolution the resolution
	 * @param latitude the current latitude area
	 * @return the closest zoom level of resolution
	 */
	public ZoomLevel getClosestZoomLevelOfResolution(double resolution, double latitude) {
		
		ZoomLevel zlFound = null;
		List<ZoomLevel> zoomLevelList = this.getZoomLevelList(latitude);
		
		// --- Check first and last Zoom Level first ----------------
		ZoomLevel zlFirst = zoomLevelList.get(0);
		ZoomLevel zlLast = zoomLevelList.get(zoomLevelList.size()-1);

		if (Double.isNaN(resolution)==true) return zlFirst;
		if (resolution >= zlFirst.getResolution()) return zlFirst;
		if (resolution <= zlLast.getResolution())  return zlLast;
		
		// --- Find the range in which we are located ---------------
		for (int i = 0; i < zoomLevelList.size()-1; i++) {
			ZoomLevel zlUpper = zoomLevelList.get(i);
			ZoomLevel zlLower = zoomLevelList.get(i+1);
			// --- In range? ----------------------------------------
			if (resolution < zlUpper.getResolution() & resolution >= zlLower.getResolution()) {
				// --- Found the right range, select ZoomLevel ------
				double upperDist = zlUpper.getResolution() - resolution;
				double lowerDist = resolution - zlLower.getResolution();
				if (upperDist >= lowerDist) {
					zlFound = zlLower;
				} else {
					zlFound = zlUpper;
				}
				break;
			}
		}
		return zlFound;
	}
	
	/**
	 * Return the next / Neighbor {@link ZoomLevel} of the specified ZoomLevel. 
	 *
	 * @param zlCurrent the current ZoomLevel
	 * @param zoomIn a positive number to zoom in, a negative number to zoom out
	 * @param latitude the current latitude area
	 * @return the next zoom level
	 */
	public ZoomLevel getNextZoomLevel(ZoomLevel zlCurrent, int zoomIn, double latitude) {

		// --- Set default return value -----------------------------
		ZoomLevel zlNext = zlCurrent;
		if (zoomIn==0) return zlNext;
		
		List<ZoomLevel> zoomLevelList = this.getZoomLevelList(latitude);
		
		// --- Run through list and find the current ZoomLevel ------
		for (int i = 0; i < zoomLevelList.size(); i++) {
			ZoomLevel zlCheck = zoomLevelList.get(i);
			if (zlCheck.getOSMZoomLevel()==zlCurrent.getOSMZoomLevel()) {
				// --- Check search direction -----------------------
				int iSelection = i;
				if (zoomIn > 0) {
					// --- Zoom-in action ---------------------------
					iSelection = i+1;
					if (iSelection <= zoomLevelList.size()-1) {
						zlNext = zoomLevelList.get(iSelection);
					}
					
				} else {
					// --- Zoom-out action --------------------------
					iSelection = i-1;
					if (iSelection >= 0) {
						zlNext = zoomLevelList.get(iSelection);
					}
				}
				break;
			}
		}
		return zlNext;
	}
	
	/**
	 * Rounds a double value to the given precision.
	 * For example: <br> 
	 * <code> round(3.1415926535, 2)</code> will deliver 3.14
	 *
	 * @param doubleValue the double value
	 * @param precision the precision
	 * @return the double
	 */
	private double round(double doubleValue, double precision) {
		return Math.round(doubleValue * Math.pow(10.0, precision)) / Math.pow(10.0, precision);
	}
	
	/**
	 * The Class ZoomLevel describes the zoom level relationship between OSM zoom level
	 * resolution and the scaling of the Jung graph.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class ZoomLevel {
		
		private static final int OSM_MAX_ZOOM_LEVEL = 19;
		
		private int osmZoomLevel;
		private double resolution;
		
		public ZoomLevel(int osmZoomLevel, double resolution) {
			this.setOSMZoomLevel(osmZoomLevel);
			this.setResolution(resolution);
		}
		
		/**
		 * Returns the OSM zoom level.
		 * @return the OSM zoom level
		 */
		public int getOSMZoomLevel() {
			return osmZoomLevel;
		}
		/**
		 * Sets the OSM zoom level.
		 * @param osmZoomLevel the new OSM zoom level
		 */
		private void setOSMZoomLevel(int osmZoomLevel) {
			this.osmZoomLevel = osmZoomLevel;
		}
		
		/**
		 * Returns the zoom level of the JXMapViewer .
		 * @return the JX map viewer zoom level
		 */
		public int getJXMapViewerZoomLevel() {
			return -this.getOSMZoomLevel() + OSM_MAX_ZOOM_LEVEL;
		}
		
		/**
		 * Returns the resolution of the current ZoomLevel in m / pixel.
		 * @return the resolution
		 */
		public double getResolution() {
			return resolution;
		}
		/**
		 * Sets the resolution.
		 * @param resolution the new resolution
		 */
		private void setResolution(double resolution) {
			this.resolution = resolution;
		}
		
		/**
		 * Returns the absolute Jung scaling factor for zooming.
		 * @return the jung scaling
		 */
		public float getJungScaling() {
			return (float) (1.0 / this.getResolution());
		}
		
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object compObj) {
			if (!(compObj instanceof ZoomLevel)) return false;
			if (compObj==this) return true;
			ZoomLevel zlComp = (ZoomLevel) compObj;
			if (zlComp.getOSMZoomLevel()!= this.getOSMZoomLevel()) return false;
			if (zlComp.getResolution()!=this.getResolution()) return false;
			return true;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "OSM-Zoom level: " + this.getOSMZoomLevel() + ", JXMapViewer-Zoom level: " + this.getJXMapViewerZoomLevel() + ", Map-Resolution: " + this.getResolution() + " m/pixel, Jung-Scaling: " + this.getJungScaling();
		}
	}
	
}
