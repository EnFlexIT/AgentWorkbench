package de.enflexit.df.core.ui.selection;

import javax.swing.JDialog;

import de.enflexit.df.core.model.DataController;

/**
 * The Class JDialogTreeNodeSelection.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JDialogTreeNodeSelection extends JDialog {

	private static final long serialVersionUID = 7802019946737954741L;

	private DataController dataController;
	
	private JPanelTreeNodeSelection jPanelTreeNodeSelection;

	
	/**
	 * Instantiates a new j dialog tree node selection.
	 * @param dataController the data controller
	 */
	public JDialogTreeNodeSelection(DataController dataController) {
		this.dataController = dataController;
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.getContentPane().add(this.getJPanelTreeNodeSelection());
		
	}
	
	/**
	 * Returns the j panel tree node selection.
	 * @return the j panel tree node selection
	 */
	private JPanelTreeNodeSelection getJPanelTreeNodeSelection() {
		if (jPanelTreeNodeSelection==null) {
			jPanelTreeNodeSelection = new JPanelTreeNodeSelection(this.dataController);
		}
		return jPanelTreeNodeSelection;
	}
	
	
}
