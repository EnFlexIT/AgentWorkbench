package de.enflexit.awb.ws.core.util;

/**
 * The listener interface for receiving webApplicationUpdateProcess events.
 * The class that is interested in processing a webApplicationUpdateProcess
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addWebApplicationUpdateProcessListener<code> method. When
 * the webApplicationUpdateProcess event occurs, that object's appropriate
 * method is invoked.
 */
public interface WebApplicationUpdateProcessListener {

	/**
	 * On update process finalized.
	 */
	public void onUpdateProcessFinalized();
	
}
