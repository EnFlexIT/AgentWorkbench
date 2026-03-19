package de.enflexit.awb.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.FrameworkUtil;

import de.enflexit.common.ServiceFinder;

/**
 * The Class AwbPerspective.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class AwbPerspective {

	private static List<AwbPerspectiveServiceListener> perspectiveListenerList;
	private static AwbPerspectiveServiceTracker pstTracker;
	/**
	 * Returns the perspective listener list.
	 * @return the perspective listener list
	 */
	private static List<AwbPerspectiveServiceListener> getPerspectiveListenerList() {
		if (perspectiveListenerList == null) {
			perspectiveListenerList = new ArrayList<>();
			// --- Start AwbPerspectiveServiceTracker ---------------
			AwbPerspective.startPerspectiveServiceTracker();
		}
		return perspectiveListenerList;
	}
	
	/**
	 * Starts the local PropertyBusServiceTracker.
	 */
	private static void startPerspectiveServiceTracker() {
		if (pstTracker==null) {
			pstTracker = new AwbPerspectiveServiceTracker(FrameworkUtil.getBundle(AwbPerspective.class).getBundleContext());
			pstTracker.open();
		}
	}

	/**
	 * Adds the perspective listener.
	 * @param listener2add the listener 2 add
	 */
	public static void addPerspectiveServiceListener(AwbPerspectiveServiceListener listener2add) {
		AwbPerspective.getPerspectiveListenerList().add(listener2add);
	}
	/**
	 * Removes the perspective listener.
	 * @param listener2remove the listener 2 remove
	 */
	public static void removePerspectiveServiceListener(AwbPerspectiveServiceListener listener2remove) {
		AwbPerspective.getPerspectiveListenerList().remove(listener2remove);
	}
	
	/**
	 * Notify perspective service listener.
	 *
	 * @param service the service
	 * @param serviceWasAdded the service was added
	 */
	public static void notifyPerspectiveServiceListener(AwbPerspectiveService service, boolean serviceWasAdded) {
		if (serviceWasAdded == true) {
			AwbPerspective.getPerspectiveListenerList().forEach(listener -> listener.addedPerspectiveService(service));
		} else {
			AwbPerspective.getPerspectiveListenerList().forEach(listener -> listener.removedPerspectiveService(service));
		}
	}

	
	/**
	 * Returns the list of AwbPerspectiveServices.
	 * @return the list of OSGI registered AwbPerspectiveServices
	 */
	public static List<AwbPerspectiveService> getAwbPerspectiveServiceList() {
		return ServiceFinder.findServices(AwbPerspectiveService.class);
	}
	
	/**
	 * Returns the AwbPerspectiveService that matches the specified class name.
	 *
	 * @param perspectiveClassName the perspective class name
	 * @return the awb perspective service
	 */
	public static AwbPerspectiveService getAwbPerspectiveService(String perspectiveClassName) {
		
		List<AwbPerspectiveService> servicesList = ServiceFinder.findServices(AwbPerspectiveService.class);
		if (servicesList!=null) {
			for (AwbPerspectiveService pService : servicesList) {
				if (pService.getClass().getName().equals(perspectiveClassName)) return pService;
			}
		}
		return null;
	}
	
	
}
