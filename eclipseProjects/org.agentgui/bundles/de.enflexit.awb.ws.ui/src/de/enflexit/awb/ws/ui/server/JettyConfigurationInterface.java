package de.enflexit.awb.ws.ui.server;

import de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject;

/**
 * The Interface JettyConfigurationInterface.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface JettyConfigurationInterface<T extends AbstractServerTreeNodeObject> {

	public abstract void setDataModel(T dataModel);
	
}
