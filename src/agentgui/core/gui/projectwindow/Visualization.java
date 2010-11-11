package agentgui.core.gui.projectwindow;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import agentgui.core.application.Project;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

/**
 * @author: Christian Derksen
 */
public class Visualization extends JPanel  {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private Project currProject;
	private JPanel jPanel = null;

	/**
	 * This is the default constructor
	 */
	public Visualization(Project project) {
		super();
		this.currProject = project;
		initialize();	
	}

	/**
	 * This method initialises this
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.ipadx = 0;
		gridBagConstraints1.ipady = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridx = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(600, 400);
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		this.setVisible(true);	
		this.add(getJPanel(), gridBagConstraints1);
	}

	/**
	 * This method initializes jPanel	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(10);
			borderLayout.setVgap(10);
			jPanel = new JPanel();
			jPanel.setLayout(borderLayout);
			jPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}
		return jPanel;
	}
	
	public JPanel getJPanel4Visualization() {
		return this.jPanel;
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
