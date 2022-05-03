package de.enflexit.awb.ws.ui.server;

import javax.swing.JPanel;

import de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject;

/**
 * The Class AbstractJPanelSettings serves as a base class for the server/handler setting panel.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractJPanelSettings<T extends AbstractServerTreeNodeObject> extends JPanel {

	private static final long serialVersionUID = -8173153780380927590L;

	/**
	 * Will be invoked to set the data model.
	 * @param dataModel the new data model
	 */
	public abstract void setDataModel(T dataModel);

	/**
	 * has to return the current data model to be saved.
	 * @return the data model
	 */
	public abstract T getDataModel();
	
	/**
	 * Has to return, if the current settings are unsaved.
	 * @return true, if the settings are unsaved
	 */
	public abstract boolean isUnsaved();
	
}
