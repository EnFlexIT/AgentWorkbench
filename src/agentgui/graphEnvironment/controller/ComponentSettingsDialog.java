package agentgui.graphEnvironment.controller;

import javax.swing.JDialog;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.ontologies.gui.OntologyInstanceViewer;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
/**
 * GUI component for setting the properties of an ontology object representing a grid component
 * @author Nils
 *
 */
public class ComponentSettingsDialog extends JDialog implements ActionListener{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1745551171293051322L;
	private JPanel jPanelContent = null;
	private JButton jButtonApply = null;
	private JButton jButtonAbort = null;
	/**
	 * The simulation project
	 */
	private Project project = null;
	/**
	 * The graph node containing the ontology object
	 */
	private GridComponent component = null;
	/**
	 * Constructor
	 * @param project The simulation project
	 * @param component The graph node containing the ontology object
	 */
	public ComponentSettingsDialog(Project project, GridComponent component){
		super(Application.MainWindow, Dialog.ModalityType.APPLICATION_MODAL);
		this.project = project;
		this.component = component;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setContentPane(getJPanelContent());
        this.setTitle(Language.translate("Netzkomponente")+" "+component.getAgentID());
        this.setSize(new Dimension(450, 450));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(getJButtonApply())){
			this.dispose();
		}else if(e.getSource().equals(getJButtonAbort())){
			this.dispose();
		}
	}

	/**
	 * This method initializes jPanelContent	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelContent() {
		if (jPanelContent == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			gridBagConstraints.gridy = 1;
			jPanelContent = new JPanel();
			jPanelContent.setLayout(new GridBagLayout());
			jPanelContent.add(getJButtonApply(), gridBagConstraints);
			jPanelContent.add(getJButtonAbort(), gridBagConstraints1);
			
			String agentClassName = project.simSetups.getCurrSimSetup().getAgentClassesHash().get(component.getType());
			jPanelContent.add(new OntologyInstanceViewer(project, agentClassName), gridBagConstraints11);
			
		}
		return jPanelContent;
	}

	/**
	 * This method initializes jButtonApply	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton();
			jButtonApply.setText(Language.translate("Übernehmen"));
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}

	/**
	 * This method initializes jButtonAbort	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAbort() {
		if (jButtonAbort == null) {
			jButtonAbort = new JButton();
			jButtonAbort.setText(Language.translate("Abbrechen"));
			jButtonAbort.addActionListener(this);
		}
		return jButtonAbort;
	}

}
