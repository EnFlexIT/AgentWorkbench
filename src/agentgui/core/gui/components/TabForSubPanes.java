package agentgui.core.gui.components;


import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import agentgui.core.application.Project;

public class TabForSubPanes extends JPanel  {

	private static final long serialVersionUID = 1L;

	private Project currProject = null;
	private JTabbedPane jTabbedPaneIntern = null;

	/**
	 * This is the default constructor
	 */
	public TabForSubPanes(Project project) {
		super();
		this.currProject = project;
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.ipady = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(421, 239);
		this.add(getJTabbedPane(), gridBagConstraints);
	}

	/**
	 * This method initializes jTabbedPaneSimSetup	
	 * @return javax.swing.JTabbedPane	
	 */
	public JTabbedPane getJTabbedPane() {
		if (jTabbedPaneIntern == null) {
			jTabbedPaneIntern = new JTabbedPane();
			jTabbedPaneIntern.setFont(new Font("Dialog", Font.BOLD, 12));
			jTabbedPaneIntern.addMouseListener(currProject.projectWindow.getTabMouseListener());
			jTabbedPaneIntern.addChangeListener(currProject.projectWindow.getTabSelectionListener());
		}
		return jTabbedPaneIntern;
	}
	
	
	
}  //  @jve:decl-index=0:visual-constraint="10,21"
