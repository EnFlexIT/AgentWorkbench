package de.enflexit.db.hibernate.gui;

import java.util.Vector;

import javax.swing.ImageIcon;

import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;

/**
 * The Class StateVisualizer provides static methods to visualize  
 * the state of a session factory and thus of a database connection.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class StateVisualizer {

	private static final String imagePackage = "/de/enflexit/db/hibernate/gui/img/";

	private static Vector<HibernateStateVisualizationService> visServices;
	
	/**
	 * Sets the connection state.
	 *
	 * @param factoryID the factory ID
	 * @param sessionFactoryState the session factory state
	 */
	public static void setConnectionState(String factoryID, SessionFactoryState sessionFactoryState) {
		for (int i = 0; i < getVisualizationServices().size(); i++) {
			HibernateStateVisualizationService visService = getVisualizationServices().get(i);
			visService.setSessionFactoryState(factoryID, sessionFactoryState);
		}
	}
	
	/**
	 * Returns the registered state visualization services.
	 * @return the visualization services
	 */
	private static Vector<HibernateStateVisualizationService> getVisualizationServices() {
		if (visServices==null) {
			visServices = new Vector<>();
		}
		return visServices;
	}
	/**
	 * Register the state visualization service.
	 * @param visService the vis service
	 */
	public static void registerStateVisualizationService(HibernateStateVisualizationService visService) {
		if (visService!=null && getVisualizationServices().contains(visService)==false) {
			getVisualizationServices().addElement(visService);	
		}
	}
	/**
	 * Unregister the specified state visualization service.
	 * @param visService the vis service
	 */
	public static void unregisterStateVisualizationService(HibernateStateVisualizationService visService) {
		if (visService!=null) {
			getVisualizationServices().remove(visService);	
		}
	}

	
	/**
	 * Gets the image package location as String.
	 * @return the image package
	 */
	public static String getImagePackage() {
		return imagePackage;
	}
	/**
	 * Gets the image icon for the specified image.
	 *
	 * @param dbDescription the file name
	 * @return the image icon
	 */
	public static ImageIcon getImageIcon(String fileName) {
		String imagePackage = getImagePackage();
		ImageIcon imageIcon=null;
		try {
			imageIcon = new ImageIcon(StateVisualizer.class.getResource((imagePackage + fileName)));
		} catch (Exception err) {
			System.err.println("Error while searching for image file '" + fileName + "' in " + imagePackage);
			err.printStackTrace();
		}	
		return imageIcon;
	}
	
}
