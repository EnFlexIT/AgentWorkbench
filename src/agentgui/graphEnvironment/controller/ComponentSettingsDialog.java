package agentgui.graphEnvironment.controller;

import javax.swing.JDialog;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.ontologies.gui.OntologyInstanceViewer;
import agentgui.graphEnvironment.networkModel.GraphNode;
import agentgui.graphEnvironment.networkModel.NetworkComponent;

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
 * GUI component for setting the properties of an ontology object representing the domain view of a grid component
 * @author Nils
 *
 */
public class ComponentSettingsDialog extends JDialog implements ActionListener{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1745551171293051322L;
	/**
	 * Content pane
	 */
	private JPanel jPanelContent = null;
	/**
	 * Apply button
	 */
	private JButton jButtonApply = null;
	/**
	 * cancel button
	 */
	private JButton jButtonAbort = null;
	/**
	 * The simulation project
	 */
	private Project project = null;
	/**
	 * The graph node containing the ontology object
	 */
	private Object element = null;
	/**
	 * The parent GUI
	 */
	private GraphEnvironmentControllerGUI parentGUI = null;
	/**
	 * The OntologyInstanceViewer instance used for editing ontology objects
	 */
	private OntologyInstanceViewer oiv = null;
	/**
	 * Constructor
	 * @param project The simulation project
	 * @param parentGUI The GraphEnvironmentControllerGUI that opened the dialog
	 * @param element The GraphElement containing the ontology object
	 */
	public ComponentSettingsDialog(Project project, GraphEnvironmentControllerGUI parentGUI, Object element){
		super(Application.MainWindow, Dialog.ModalityType.APPLICATION_MODAL);
		this.project = project;
		this.parentGUI = parentGUI;
		this.element = element;
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setContentPane(getJPanelContent());
        if(element instanceof NetworkComponent){
        	this.setTitle("NetworkComponent "+((NetworkComponent)element).getId());
        }else if(element instanceof GraphNode){
        	this.setTitle("PropagationPoint "+((GraphNode)element).getId());
        }
        this.setSize(new Dimension(450, 450));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Confirmed, apply changes
		if(e.getSource().equals(getJButtonApply())){
			oiv.save();
			if(element instanceof GraphNode){
				((GraphNode)element).setEncodedOntologyRepresentation(oiv.getConfigurationXML64()[0]);
			}else if(element instanceof NetworkComponent){
				((NetworkComponent)element).setEncodedOntologyRepresentation(oiv.getConfigurationXML64()[0]);
			}
			parentGUI.componentSettingsChanged();
			this.dispose();
		// Canceled, discard changes
		}else if(e.getSource().equals(getJButtonAbort())){
			parentGUI.componentSettingsChangeAborted();
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
			
			jPanelContent.add(getOIV(), gridBagConstraints11);
			
		}
		return jPanelContent;
	}
	
	/**
	 * Creates a new OntologyInstancViewer instance and initiates it with the currently selected node / component
	 * @return
	 */
	private OntologyInstanceViewer getOIV(){
		if(element instanceof NetworkComponent){
			NetworkComponent elemNetComp = (NetworkComponent)element;
			// Initiate a new OIV using the NetworkComponents agent class
			oiv = new OntologyInstanceViewer(project, parentGUI.getController().getComponentTypeSettings().get(((NetworkComponent) element).getType()).getAgentClass());
			// If an ontology instance is defined for this component, let the OIV decode and load it 
			if(elemNetComp.getEncodedOntologyRepresentation()!=null){
				String[] encodedOntoRepresentation = new String[1];
				encodedOntoRepresentation[0]=elemNetComp.getEncodedOntologyRepresentation();
				oiv.setConfigurationXML64(encodedOntoRepresentation);
			}
		}else if(element instanceof GraphNode){
			// Obtain the ontology class name defined for nodes
			String[] ontoClassName = new String[1];
			ontoClassName[0] = parentGUI.getController().getComponentTypeSettings().get("node").getAgentClass();
			// Initiate a new OIV with the class name
			oiv = new OntologyInstanceViewer(project, ontoClassName);
			// If an ontology instance is defined for this node, let the OIV decode and load it
			if(((GraphNode)element).getEncodedOntologyRepresentation() != null){
				String[] encodedOntoRepresentation = new String[1];
				encodedOntoRepresentation[0]=((GraphNode)element).getEncodedOntologyRepresentation();
				oiv.setConfigurationXML64(encodedOntoRepresentation);
			}
		}
		
		oiv.setAllowViewEnlargement(false);
		
		return oiv;
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
