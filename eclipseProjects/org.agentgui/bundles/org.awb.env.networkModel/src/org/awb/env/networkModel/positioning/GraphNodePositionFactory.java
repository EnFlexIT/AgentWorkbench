package org.awb.env.networkModel.positioning;

import java.awt.geom.Point2D;

import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.maps.MapSettings;
import org.awb.env.networkModel.settings.LayoutSettings;

import agentgui.core.application.Application;
import agentgui.core.project.Project;
import de.enflexit.geography.coordinates.AbstractCoordinate;
import de.enflexit.geography.coordinates.OSGBCoordinate;
import de.enflexit.geography.coordinates.UTMCoordinate;
import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

/**
 * A factory for creating GraphNodePosition objects that extend the class {@link AbstractCoordinate}.
 */
public class GraphNodePositionFactory {

	public static String POS_PREFIX_WGS84 = WGS84LatLngCoordinate.POS_PREFIX; 
	public static String POS_PREFIX_UTM   = UTMCoordinate.POS_PREFIX;
	public static String POS_PREFIX_OSGB  = OSGBCoordinate.POS_PREFIX;

	public enum CoordinateType {
		SimpleXY,
		WGS84,
		UTM,
		OSGB
	}
	
	private static NetworkModel networkModelLoading;
	
	
	// --------------------------------------------------------------------------------------------
	// --- From here some help methods to access the current project ---------- Start -------------
	// --------------------------------------------------------------------------------------------
	/**
	 * Returns the current {@link GraphEnvironmentController} if accessible in the current project.
	 * @return the graph controller
	 */
	private static GraphEnvironmentController getGraphController() {
		Project project = Application.getProjectFocused();
		if (project!=null) {
			if (project.getEnvironmentController() instanceof GraphEnvironmentController) {
				return (GraphEnvironmentController) project.getEnvironmentController();
			}
		}
		return null;
	}
	
	/**
	 * Can be used to inform the factory about the currently loading NetworkModel.
	 * @param netModelLoading the currently loading network model
	 */
	public static void setLoadingNetworkModel(NetworkModel netModelLoading) {
		networkModelLoading = netModelLoading;
	}
	/**
	 * Returns the current {@link NetworkModel} if accessible in the current project.
	 * @return the network model
	 */
	private static NetworkModel getNetworkModel() {
		GraphEnvironmentController graphController = getGraphController();
		if (graphController!=null) {
			if (networkModelLoading!=null) {
				return networkModelLoading;
			}
			return graphController.getNetworkModel();
		}
		return null;
	}
	/**
	 * Return the layout settings for the specified layoutID.
	 *
	 * @param layoutID the layout ID
	 * @return the layout settings
	 */
	private static LayoutSettings getLayoutSettings(String layoutID) {
		if (layoutID==null || layoutID.isEmpty()) return null;
		GraphEnvironmentController graphController = getGraphController();
		if (graphController!=null) {
			return graphController.getGeneralGraphSettings4MAS().getLayoutSettings().get(layoutID);
		}
		return null;
	}
	// --------------------------------------------------------------------------------------------
	// --- From here some help methods to access the current project ---------- End ---------------
	// --------------------------------------------------------------------------------------------
	
	
	
	
	
	/**
	 * Converts the specified Point2D into a coordinate that is - depending on the current {@link LayoutSettings} in the NetworkModel - a 
	 * {@link GraphNodePosition} or a geographical coordinate like a {@link WGS84LatLngCoordinate} or an {@link UTMCoordinate}.
	 *
	 * @param point2D the 2D point
	 * @return the coordinate
	 */
	public static AbstractCoordinate convertToCoordinate(Point2D point2D) {
		String layoutID = getNetworkModel().getLayoutID();
		return convertToCoordinate(layoutID, point2D);
	}
	/**
	 * Converts the specified Point2D into a coordinate for the specified layoutID and it's corresponding {@link LayoutSettings}.
	 * The instance will be of type {@link GraphNodePosition} or a geographical coordinate like a {@link WGS84LatLngCoordinate} 
	 * or an {@link UTMCoordinate}.
	 *
	 * @param layoutID the layout ID
	 * @param point2D the 2D point 
	 * @return the abstract coordinate
	 */
	public static AbstractCoordinate convertToCoordinate(String layoutID, Point2D point2D) {
		if (point2D==null) return null;
		return convertToCoordinate(layoutID, point2D.getX(), point2D.getY());
	}
	/**
	 * Converts the specified Point2D into a coordinate for the specified layoutID and it's corresponding {@link LayoutSettings}.
	 * The instance will be of type {@link GraphNodePosition} or a geographical coordinate like a {@link WGS84LatLngCoordinate} 
	 * or an {@link UTMCoordinate}.
	 *
	 * @param layoutID the layout ID
	 * @param newX the new x position
	 * @param newY the new y position
	 * @return the abstract coordinate
	 */
	public static AbstractCoordinate convertToCoordinate(String layoutID, double newX, double newY) {

		if (layoutID==null || layoutID.isEmpty()==true) return null;
		
		// --- Determine which coordinate system to use -----------------------
		NetworkModel networkModel = getNetworkModel();
		LayoutSettings ls = getLayoutSettings(layoutID);
		MapSettings ms = networkModel.getMapSettingsTreeMap().get(layoutID);

		AbstractCoordinate coord = null;
		if (ls.isGeographicalLayout()==true && ms!=null) {
			// --- Convert into UTM coordinate --------------------------------
			UTMCoordinate utmCoordinate = new UTMCoordinate();
			utmCoordinate.setLongitudeZone(ms.getUTMLongitudeZone());
			utmCoordinate.setLatitudeZone(ms.getUTMLatitudeZone());
			coord = utmCoordinate;
			
		} else {
			// --- Convert simple GraphNodePosition ---------------------------
			coord = createCoordinate(CoordinateType.SimpleXY);
			
		}
		// --- Set to location of Point2D ------------------------------------- 
		coord.setLocation(newX, newY);
		return coord;
	}
	
	
	 /**
 	 * Will convert the specified string to the specific geographical coordinate.
 	 *
 	 * @param coString the coordinate string
 	 * @return the coordinate determined from the string
 	 */
	public static AbstractCoordinate getCoordinateFromString(String coString) {
		String layoutID = getNetworkModel().getLayoutID();
		return getCoordinateFromString(layoutID, coString);
	}
	 /**
 	 * Will convert the specified string to the specific geographical coordinate.
 	 *
 	 * @param layoutID the layout ID
 	 * @param coString the coordinate string
 	 * @return the coordinate determined from the string
 	 */
    public static AbstractCoordinate getCoordinateFromString(String layoutID, String coString) {
    
    	// --- Quick exit? ------------------------------------------ 
    	if (coString==null || coString.isEmpty()==true) return null;
    	
    	AbstractCoordinate coord = null;
    	try {
    		
    		// --- Simply check the string first --------------------  
    		if (coString.startsWith(POS_PREFIX_WGS84)==true) {
    			coord = new WGS84LatLngCoordinate();
    		} else if (coString.startsWith(POS_PREFIX_UTM)==true) {
    			coord = new UTMCoordinate();
    		} else if (coString.startsWith(POS_PREFIX_OSGB)==true) {
    			coord = new OSGBCoordinate();
    		} else {
    			// --- Not sure, check the layout settings ----------
    			LayoutSettings ls = getLayoutSettings(layoutID);
    			if (ls.isGeographicalLayout()==true) {
    				// --- To convert old settings to coordinates ---
    				Point2D p2D = getPoint2DFromString(coString);
    				return convertToCoordinate(layoutID, p2D);
    			} 
    			coord = new GraphNodePosition();
    		}
			coord.deserialize(coString);
    		
		} catch (Exception ex) {
			ex.printStackTrace();
			coord = null;
		}
    	return coord;
    }
    
    /**
     * Creates a new AbstractCoordinate object.
     *
     * @param cType the c type
     * @return the abstract coordinate
     */
    public static AbstractCoordinate createCoordinate(CoordinateType cType) {
    	
    	AbstractCoordinate coord = null; 
    	
    	switch (cType) {
		case SimpleXY:
			coord = new GraphNodePosition();
			break;

		case WGS84:
			coord = new WGS84LatLngCoordinate();
			break;
			
		case UTM:
			coord = new UTMCoordinate();
			break;

		case OSGB:
			coord = new OSGBCoordinate();
			break;
		}
    	return coord;
    }
    
    
    
	// --------------------------------------------------------------------------------------------
	// --- From here, the old style conversion from string to Point2D and vice versa --- Start ----
	// --------------------------------------------------------------------------------------------	
	/**
	 * Returns a specified Point2D as string.
	 *
	 * @param position the position
	 * @return the position as string
	 */
	public static String getPoint2DAsString(Point2D position) {
		return position.getX() + ":" + position.getY();
	}
	/**
	 * Returns the Point2D from the specified string.
	 *
	 * @param positionString the position string
	 * @return the position from string
	 */
	public static Point2D getPoint2DFromString(String positionString) {
		
		if (positionString==null || positionString.isEmpty()) return null;
		
		Point2D pos = null;
		String[] coords = positionString.split(":");
		if (coords.length == 2) {
			double xPos = Double.parseDouble(coords[0]);
			double yPos = Double.parseDouble(coords[1]);
			pos = new Point2D.Double(xPos, yPos);
			
		}
		return pos;
	}
	// --------------------------------------------------------------------------------------------
	// --- From here, the old style conversion from string to Point2D and vice versa --- End ------
	// --------------------------------------------------------------------------------------------	
	
}
