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

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;

/**
 * GUI component for setting the properties of an ontology object representing the domain view of a grid component
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 *
 */
public class NetworkComponentDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1745551171293051322L;

	private GraphEnvironmentController graphController = null;
	private Object graphObject = null;

	private NetworkComponent networkComponent = null;
	private NetworkComponentAdapter networkComponentAdapter = null;
	private NetworkComponentAdapter4DataModel adapterView = null;
	
	private JPanel jPanelContent = null;
	private JButton jButtonApply = null;
	private JButton jButtonAbort = null;

	/**
	 * Constructor.
	 * @param project The simulation project
	 * @param controller the GraphEnvironmentController
	 * @param element The GraphElement containing the ontology object
	 */
	public NetworkComponentDialog(GraphEnvironmentController controller, Object graphObject) {
		super(Application.getMainWindow(), Dialog.ModalityType.APPLICATION_MODAL);
		this.graphController = controller;
		this.graphObject = graphObject;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		
		if (graphObject instanceof GraphNode) {
			// --- Is the GraphNode a DistributionNode ? ------------
			this.networkComponent = this.graphController.getNetworkModel().isDistributionNode((GraphNode) this.graphObject);
			if (this.networkComponent==null) {
				// --- Check for central GraphNode element ----------
				// --- or single outer GraphNodes		   ----------
				HashSet<NetworkComponent> netComps = this.graphController.getNetworkModel().getNetworkComponents((GraphNode) this.graphObject);
				if (netComps.size()==1) {
					this.networkComponent = netComps.iterator().next();
				}
			}
			
		} else if (graphObject instanceof GraphEdge) {
			// --- Just get the corresponding NetworkComponent ------ 
			this.networkComponent = this.graphController.getNetworkModel().getNetworkComponent((GraphEdge) this.graphObject);
			
		} else if (graphObject instanceof NetworkComponent) {
			// --- Cast to NetworkComponent -------------------------
			this.networkComponent = this.graphController.getNetworkModel().getNetworkComponent(((NetworkComponent) this.graphObject).getId());
			
		}
		
		// --- NetworkComponent selected ? -------------------------- 
		if (this.networkComponent==null) {
			String msgHead = Language.translate("Nicht eindeutige Komponentenauswahl !");
			String msgText = Language.translate("Bitte wählen Sie eine einzelne Netzwerkkomponente !");			
			JOptionPane.showMessageDialog(null, msgText, msgHead, JOptionPane.WARNING_MESSAGE);
			this.dispose();
			return;
		}
		
		// --- Get the corresponding NetworkComponentAdapter -------- 
		this.networkComponentAdapter = this.graphController.getNetworkModel().getNetworkComponentAdapter(this.networkComponent);

		// --- Mark / Select NetworkComponent for user --------------
		NetworkModelNotification nmn = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Select);
		nmn.setInfoObject(this.networkComponent);
		this.graphController.notifyObservers(nmn);

		// --- Some layout stuff ------------------------------------ 
		this.setTitle("Network component: " + this.networkComponent.getId() + " (" +  this.networkComponent.getType() + ")");
		this.setSize(new Dimension(450, 450));
		this.setContentPane(getJPanelContent());
		this.setVisible(true);
		
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
			
			if (this.networkComponentAdapter== null) {
				this.jPanelContent.remove(this.getJButtonApply());
			} else {
				adapterView = this.networkComponentAdapter.invokeGetDataModelAdapter();
				if (adapterView == null) {
					this.jPanelContent.remove(this.getJButtonApply());
				} else {
					adapterView.setDataModel(this.networkComponent.getDataModel());
					JComponent visualisation = adapterView.getVisualisationComponent();
					visualisation.validate();
					jPanelContent.add(visualisation, gridBagConstraints11);
				}
			}
			
		}
		return jPanelContent;
	}

	/**
	 * This method initializes jButtonApply	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton();
			jButtonApply.setText("Übernehmen");
			jButtonApply.setText(Language.translate(jButtonApply.getText()));
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setForeground(new Color(0, 153, 0));
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}
	/**
	 * This method initializes jButtonAbort	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAbort() {
		if (jButtonAbort == null) {
			jButtonAbort = new JButton();
			jButtonAbort.setText("Abbrechen");
			jButtonAbort.setText(Language.translate(jButtonAbort.getText()));
			jButtonAbort.setForeground(new Color(153, 0, 0));
			jButtonAbort.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonAbort.addActionListener(this);
		}
		return jButtonAbort;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(this.getJButtonApply())) {
			// --- Confirmed, apply changes -----
			this.adapterView.save();
			Object dataModel = this.adapterView.getDataModel();
			this.networkComponent.setDataModel(dataModel);			
			this.graphController.getNetworkModel().getNetworkComponent(this.networkComponent.getId()).setDataModel(dataModel);
			this.dispose();

		} else if (e.getSource().equals(getJButtonAbort())) {
			// --- Cancelled, discard changes ----
			this.dispose();
		}
	}
	
	
}
