package de.enflexit.awb.ws.ui.server;

import de.enflexit.awb.ws.core.model.ServerTreeNodeHandler;

/**
 * The Class JPanelSettingsHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelSettingsHandler extends AbstractJPanelSettings<ServerTreeNodeHandler> {

	private static final long serialVersionUID = -4985161964727450005L;

	private ServerTreeNodeHandler serverTreeNodeHandler;
	private boolean unsaved;
	
	/**
	 * Instantiates a new JPanel for the server settings.
	 */
	public JPanelSettingsHandler() {
		this.initialize();
	}
	private void initialize() {
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.AbstractJPanelSettings#setDataModel(de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject)
	 */
	@Override
	public void setDataModel(ServerTreeNodeHandler dataModel) {
		this.serverTreeNodeHandler = dataModel;
		this.unsaved = false;
		// TODO
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.AbstractJPanelSettings#getDataModel()
	 */
	@Override
	public ServerTreeNodeHandler getDataModel() {
		// TODO Auto-generated method stub
		return this.serverTreeNodeHandler;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.AbstractJPanelSettings#isUnsaved()
	 */
	@Override
	public boolean isUnsaved() {
		return unsaved;
	}
	
}
