package de.enflexit.awb.ws.ui.server;

import de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;

/**
 * The Interface JettyConfigurationInterface.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface JettyConfigurationInterface<T extends AbstractServerTreeNodeObject> {

	/**
	 * Will be used to set the data model as specified with the type of this class.
	 * @param dataModel the new data model
	 */
	public void setDataModel(T dataModel);
	
	/**
	 * Will be used to set the current {@link ServerTreeNodeServer} to which this node belongs.
	 * @param serverTreeNodeServer the instance of the corresponding {@link ServerTreeNodeServer}
	 */
	public void setServerTreeNodeServer(ServerTreeNodeServer serverTreeNodeServer);
	
	/**
	 * Will be invoked to stop the current edit action (e.g. in table cells).
	 */
	public void stopEditing();
	
}
