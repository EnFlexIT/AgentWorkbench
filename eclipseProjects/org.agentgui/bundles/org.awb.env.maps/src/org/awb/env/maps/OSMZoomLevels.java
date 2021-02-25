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
	private OSMZoomLevels() {
		this.initialize();
	}
	/**
	 * Initializes the local vector of know ZoomLevel.
	 */
	private void initialize() {
		
		// --------------------------------------------------------------------
		// --- Fill the local list of the know 20 Zoom Level in m / pixel -----
		// --- From: https://wiki.openstreetmap.org/wiki/DE:Zoom_levels -------
		// --- $zoomstufen = array(156412, 78206, 39103, 19551, 9776, 4888, 2444, 1222, 610.984, 305.492, 152.746, 76.373, 38.187, 19.093, 9.547, 4.773, 2.387, 1.193, 0.596, 0.298)
		// --------------------------------------------------------------------		
		
		this.getZoomLevelList().add(new ZoomLevel(0, 156412));
		this.getZoomLevelList().add(new ZoomLevel(1, 78206));
		this.getZoomLevelList().add(new ZoomLevel(2, 39103));
		this.getZoomLevelList().add(new ZoomLevel(3, 19551));
		this.getZoomLevelList().add(new ZoomLevel(4, 9776));
		this.getZoomLevelList().add(new ZoomLevel(5, 4888));
		this.getZoomLevelList().add(new ZoomLevel(6, 2444));
		this.getZoomLevelList().add(new ZoomLevel(7, 1222));
		this.getZoomLevelList().add(new ZoomLevel(8, 610.984));
		this.getZoomLevelList().add(new ZoomLevel(9, 305.492));
		
		this.getZoomLevelList().add(new ZoomLevel(10, 152.746));
		this.getZoomLevelList().add(new ZoomLevel(11, 76.373));
		this.getZoomLevelList().add(new ZoomLevel(12, 38.187));
		this.getZoomLevelList().add(new ZoomLevel(13, 19.093));
		this.getZoomLevelList().add(new ZoomLevel(14, 9.547));
		this.getZoomLevelList().add(new ZoomLevel(15, 4.773));
		this.getZoomLevelList().add(new ZoomLevel(16, 2.387));
		this.getZoomLevelList().add(new ZoomLevel(17, 1.193));
		this.getZoomLevelList().add(new ZoomLevel(18, 0.596));
		this.getZoomLevelList().add(new ZoomLevel(19, 0.298));
	}

	/**
	 * Returns the zoom level list.
	 * @return the zoom level list
	 */
	public List<ZoomLevel> getZoomLevelList() {
		if (zoomLevelList==null) {
			zoomLevelList = new ArrayList<>();
		}
		return zoomLevelList;
	}
	
	
	/**
	 * Returns the closest {@link ZoomLevel} for the specified Jung scaling.
	 *
	 * @param scaling the scaling
	 * @return the closest zoom level of jung scaling
	 */
	public ZoomLevel getClosestZoomLevelOfJungScaling(double scaling) {
		return this.getClosestZoomLevelOfResolution((1.0 / scaling));
	}
	
	/**
	 * Returns the closest {@link ZoomLevel} for the specified map resolution in m/pixel.
	 *
	 * @param resolution the resolution
	 * @return the closest zoom level of resolution
	 */
	public ZoomLevel getClosestZoomLevelOfResolution(double resolution) {
		
		ZoomLevel zlFound = null;
		
		// --- Check first and last Zoom Level first ----------------
		ZoomLevel zlFirst = this.getZoomLevelList().get(0);
		ZoomLevel zlLast = this.getZoomLevelList().get(this.getZoomLevelList().size()-1);

		if (Double.isNaN(resolution)==true) return zlFirst;
		if (resolution >= zlFirst.getResolution()) return zlFirst;
		if (resolution <= zlLast.getResolution())  return zlLast;
		
		// --- Find the range in which we are located ---------------
		for (int i = 0; i < this.getZoomLevelList().size()-1; i++) {
			ZoomLevel zlUpper = this.getZoomLevelList().get(i);
			ZoomLevel zlLower = this.getZoomLevelList().get(i+1);
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
	 * @return the next zoom level
	 */
	public ZoomLevel getNextZoomLevel(ZoomLevel zlCurrent, int zoomIn) {

		// --- Set default return value -----------------------------
		ZoomLevel zlNext = zlCurrent;
		if (zoomIn==0) return zlNext;
		
		// --- Run through list and find the current ZoomLevel ------
		for (int i = 0; i < this.getZoomLevelList().size(); i++) {
			ZoomLevel zlCheck = this.getZoomLevelList().get(i);
			if (zlCheck==zlCurrent) {
				// --- Check search direction -----------------------
				int iSelection = i;
				if (zoomIn > 0) {
					// --- Zoom-in action ---------------------------
					iSelection = i+1;
					if (iSelection <= this.getZoomLevelList().size()-1) {
						zlNext = this.getZoomLevelList().get(iSelection);
					}
					
				} else {
					// --- Zoom-out action --------------------------
					iSelection = i-1;
					if (iSelection >= 0) {
						zlNext = this.getZoomLevelList().get(iSelection);
					}
				}
				break;
			}
		}
		return zlNext;
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
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "OSM-Zoom level: " + this.getOSMZoomLevel() + ", JMapViewer-Zoom level: " + this.getJXMapViewerZoomLevel() + ", Map-Resolution: " + this.getResolution() + " m/pixel, Jung-Scaling: " + this.getJungScaling();
		}
	}
	
}
