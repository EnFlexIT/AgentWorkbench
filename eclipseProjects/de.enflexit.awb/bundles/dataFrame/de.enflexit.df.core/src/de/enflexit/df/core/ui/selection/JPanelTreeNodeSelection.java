package de.enflexit.df.core.ui.selection;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.JPanelNavigation;

/**
 * The Class JPanelTreeNodeSelection reuses the {@link JPanelNavigation} to
 * enable user to search and select an specific tree node (e.g. for a table selection).
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelTreeNodeSelection extends JPanel {

	private static final long serialVersionUID = -6641745184460956169L;

	private DataController dataController;
	
	private JPanelNavigation jPanelNavigation;
	
	/**
	 * Instantiates a new j panel tree node selection.
	 * @param dataController the data controller
	 */
	public JPanelTreeNodeSelection(DataController dataController) {
		this.dataController = dataController;
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(this.getjPanelNavigation(), BorderLayout.CENTER);
	}
	public JPanelNavigation getjPanelNavigation() {
		if (jPanelNavigation==null) {
			jPanelNavigation = new JPanelNavigation(this.dataController);
		}
		return jPanelNavigation;
	}
	
}
