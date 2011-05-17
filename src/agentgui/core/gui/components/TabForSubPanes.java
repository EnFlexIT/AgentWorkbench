package agentgui.core.gui.components;


import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class TabForSubPanes extends JPanel  {

	private static final long serialVersionUID = 1L;

	public JTabbedPane jTabbedPaneIntern = null;

	/**
	 * This is the default constructor
	 */
	public TabForSubPanes() {
		super();
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
		this.add(getJTabbedPaneSimSetup(), gridBagConstraints);
	}

	/**
	 * This method initializes jTabbedPaneSimSetup	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPaneSimSetup() {
		if (jTabbedPaneIntern == null) {
			jTabbedPaneIntern = new JTabbedPane();
			jTabbedPaneIntern.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jTabbedPaneIntern;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,21"
