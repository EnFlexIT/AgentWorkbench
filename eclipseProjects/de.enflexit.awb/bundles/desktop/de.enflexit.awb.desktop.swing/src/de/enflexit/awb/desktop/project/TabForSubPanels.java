package de.enflexit.awb.desktop.project;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.enflexit.awb.core.project.Project;


/**
 * This class can be used in order to create a tab that can hold further sub tabs 
 * (like the project tabs 'configuration' or 'Setup').
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TabForSubPanels extends JPanel  {

	private static final long serialVersionUID = 1L;

	private Project currProject = null;
	private JTabbedPane jTabbedPaneIntern = null;

	/**
	 * This is the default constructor.
	 * @param project the project
	 */
	public TabForSubPanels(Project project) {
		super();
		this.currProject = project;
		this.initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(400, 250);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder());
		this.add(this.getJTabbedPane(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jTabbedPaneSimSetup	
	 * @return javax.swing.JTabbedPane	
	 */
	public JTabbedPane getJTabbedPane() {
		if (jTabbedPaneIntern == null) {
			jTabbedPaneIntern = new JTabbedPane();
			jTabbedPaneIntern.setTabPlacement(JTabbedPane.TOP);
			jTabbedPaneIntern.setFont(new Font("Dialog", Font.BOLD, 13));
			jTabbedPaneIntern.setBorder(BorderFactory.createEmptyBorder());
			
			ProjectWindow projectWindow = (ProjectWindow) currProject.getProjectEditorWindow();
			jTabbedPaneIntern.addMouseListener(projectWindow.getTabMouseListener());
			jTabbedPaneIntern.addChangeListener(projectWindow.getTabSelectionListener());
		}
		return jTabbedPaneIntern;
	}
	
} 
