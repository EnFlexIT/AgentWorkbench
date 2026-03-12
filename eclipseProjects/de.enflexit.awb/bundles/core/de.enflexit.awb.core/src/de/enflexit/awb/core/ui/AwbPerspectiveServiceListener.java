package de.enflexit.awb.core.ui;


public interface AwbPerspectiveServiceListener {

	/**
	 * Adds the perspective service.
	 *
	 * @param service the service
	 */
	public void addedPerspectiveService(AwbPerspectiveService service);

	/**
	 * Removes the perspective service.
	 *
	 * @param service the service
	 */
	public void removedPerspectiveService(AwbPerspectiveService service);
}
