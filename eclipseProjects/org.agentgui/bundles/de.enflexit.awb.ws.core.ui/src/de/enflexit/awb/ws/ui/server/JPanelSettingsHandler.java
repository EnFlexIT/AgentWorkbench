package de.enflexit.awb.ws.ui.server;

import javax.swing.JPanel;

import de.enflexit.awb.ws.core.model.ServerTreeNodeHandler;

/**
 * The Class JPanelSettingsHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelSettingsHandler extends JPanel implements JettyConfigurationInterface<ServerTreeNodeHandler> {

	private static final long serialVersionUID = -4985161964727450005L;

	private ServerTreeNodeHandler serverTreeNodeHandler;
	
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
		// TODO
	}
	
}
