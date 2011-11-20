/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.envModel.graph.controller;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;

import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.ontologies.gui.OntologyInstanceViewer;
import agentgui.core.project.Project;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
/**
 * GUI component for setting the properties of an ontology object representing the domain view of a grid component
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 *
 */
public class OntologySettingsDialog extends JDialog implements ActionListener{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1745551171293051322L;

	private Project project = null;

	private Object element = null;
	
	private JPanel jPanelContent = null;
	private JButton jButtonApply = null;
	private JButton jButtonAbort = null;

	/**
	 * The parent GUI
	 */
	private GraphEnvironmentControllerGUI graphEnvContGUI = null;
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
	public OntologySettingsDialog(Project project, GraphEnvironmentControllerGUI graphEnvContGUI, Object element) {
		super(Application.MainWindow, Dialog.ModalityType.APPLICATION_MODAL);
		this.project = project;
		this.graphEnvContGUI = graphEnvContGUI;
		this.element = element;
		initialize();
	}
	
	/**
	 * This method initializes this
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
		
		if(e.getSource().equals(getJButtonApply())){
			// --- Confirmed, apply changes -----
			oiv.save();
			if (element instanceof GraphNode) {
				((GraphNode)element).setEncodedOntologyRepresentation(oiv.getConfigurationXML64()[0]);
			} else if(element instanceof NetworkComponent) {
				((NetworkComponent)element).setEncodedOntologyRepresentation(oiv.getConfigurationXML64()[0]);
				DefaultListModel agents2Start = graphEnvContGUI.getController().getAgents2Start();
				
				//Setting the start arguments of the ontology instance in the agent start list of the environment.
				for(int i=0;i< agents2Start.size() ; i++){
					AgentClassElement4SimStart ac4s = (AgentClassElement4SimStart) agents2Start.get(i);
					if(ac4s.getStartAsName().equals(((NetworkComponent)element).getId())){
						ac4s.setStartArguments(oiv.getConfigurationXML());
						break;
					}
				}
			}
			graphEnvContGUI.componentSettingsChanged();
			this.dispose();
		
		} else if(e.getSource().equals(getJButtonAbort())) {
			// --- Canceled, discard changes ----
			graphEnvContGUI.componentSettingsChangeAborted();
			this.dispose();
		}
	}

	/**
	 * This method initializes jPanelContent	
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
			
			OntologyInstanceViewer oiv = getOIV();
			if (oiv==null) {
				jPanelContent.remove(this.getJButtonApply());
			} else {
				jPanelContent.add(oiv, gridBagConstraints11);
			}
			
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
			oiv = new OntologyInstanceViewer(project, graphEnvContGUI.getController().getComponentTypeSettings().get(((NetworkComponent) element).getType()).getAgentClass());
			// If an ontology instance is defined for this component, let the OIV decode and load it 
			if(elemNetComp.getEncodedOntologyRepresentation()!=null){
				String[] encodedOntoRepresentation = new String[1];
				encodedOntoRepresentation[0]=elemNetComp.getEncodedOntologyRepresentation();
				oiv.setConfigurationXML64(encodedOntoRepresentation);
			}
			
		} else if(element instanceof GraphNode) {
			// Obtain the ontology class name defined for nodes
			String[] ontoClassName = new String[1];
			ontoClassName[0] = graphEnvContGUI.getController().getComponentTypeSettings().get("node").getAgentClass();
			if (ontoClassName[0]==null || ontoClassName[0].equals("") ) {
				return null;
			}
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
			jButtonApply.setText("Übernehmen");
			jButtonApply.setText(Language.translate(jButtonApply.getText()));
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setForeground(new Color(0,153,0));			
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
			
			jButtonAbort.setText("Abbrechen");
			jButtonAbort.setText(Language.translate(jButtonAbort.getText()));
			jButtonAbort.setForeground(new Color(153,0,0));
			jButtonAbort.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonAbort.addActionListener(this);
		}
		return jButtonAbort;
	}

}
