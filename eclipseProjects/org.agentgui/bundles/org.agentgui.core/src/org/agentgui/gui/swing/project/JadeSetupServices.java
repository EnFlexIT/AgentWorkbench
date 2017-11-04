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
package org.agentgui.gui.swing.project;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.project.PlatformJadeConfig;
import agentgui.core.project.Project;
import de.enflexit.common.classSelection.ClassElement2Display;
import de.enflexit.common.classSelection.JListClassSearcher;
import de.enflexit.common.classSelection.JListClassSearcherListener;
import jade.core.BaseService;

/**
 * Represents the JPanel/Tab 'Configuration' - 'JADE-Services'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JadeSetupServices extends JPanel implements ActionListener, Observer {

	private static final long serialVersionUID = -7016775471452161527L;
	
	private Project currProject = null;

	private JPanel jPanelServiceLists = null;
	private JLabel jLabelServicesChosen = null;
	private JLabel jLabelServicesAvailable = null;
	private JScrollPane jScrollPaneServicesChosen = null;
	private JList<String> jListServicesChosen = null;
	private JListClassSearcher jListServicesAvailable = null;

	private JPanel jPanelServiceButtons = null;
	private JPanel jPanelServiceAvailable = null;
	private JButton jButtonDefaultJadeConfig = null;
	private JButton jButtonServiceAdd = null;
	private JButton jButtonServiceRemove = null;
	private JLabel jLabelDummyServices = null;
	
	/**
	 * Constructor of this class
	 * @param project
	 */
	public JadeSetupServices(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);
		this.initialize();
		
		jLabelServicesChosen.setText(Language.translate("Ausgewählte JADE-Services"));
		jLabelServicesAvailable.setText(Language.translate("Verfügbare JADE-Services"));
		
		jButtonDefaultJadeConfig.setToolTipText(Language.translate("Standardkonfiguration verwenden"));
		jButtonServiceAdd.setToolTipText(Language.translate("Service hinzufügen"));
		jButtonServiceRemove.setToolTipText(Language.translate("Service entfernen"));
		
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		this.setLayout(gridBagLayout);
		this.setSize(950, 500);

		GridBagConstraints gbc_ServiceLists = new GridBagConstraints();
		gbc_ServiceLists.gridx = 0;
		gbc_ServiceLists.fill = GridBagConstraints.BOTH;
		gbc_ServiceLists.insets = new Insets(10, 10, 5, 10);
		gbc_ServiceLists.weightx = 1.0;
		gbc_ServiceLists.weighty = 1.0;
		gbc_ServiceLists.ipadx = 0;
		gbc_ServiceLists.gridy = 0;
		this.add(this.getJPanelServiceLists(), gbc_ServiceLists);
		
	}
	
	/**
	 * This method initializes jPanelServiceLists	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelServiceLists() {
		if (jPanelServiceLists == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 2;
			gridBagConstraints14.fill = GridBagConstraints.BOTH;
			gridBagConstraints14.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints14.weightx = 0.0;
			gridBagConstraints14.weighty = 1.0;
			gridBagConstraints14.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(0, 15, 5, 0);
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.gridy = 0;
			jLabelServicesAvailable = new JLabel();
			jLabelServicesAvailable.setText("Verfügbare JADE-Services");
			jLabelServicesAvailable.setPreferredSize(new Dimension(156, 16));
			jLabelServicesAvailable.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(0, 5, 5, 10);
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridy = 0;
			jLabelServicesChosen = new JLabel();
			jLabelServicesChosen.setText("Ausgewählte JADE-Services");
			jLabelServicesChosen.setPreferredSize(new Dimension(156, 16));
			jLabelServicesChosen.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.weightx = 0.0;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 0.0;
			jPanelServiceLists = new JPanel();
			jPanelServiceLists.setLayout(new GridBagLayout());
			jPanelServiceLists.add(jLabelServicesChosen, gridBagConstraints11);
			jPanelServiceLists.add(getJScrollPaneServicesChosen(), gridBagConstraints4);
			jPanelServiceLists.add(getJPanelServiceButtons(), gridBagConstraints7);
			jPanelServiceLists.add(jLabelServicesAvailable, gridBagConstraints12);
			jPanelServiceLists.add(getJPanelServiceAvailable(), gridBagConstraints14);
			
		}
		return jPanelServiceLists;
	}

	/**
	 * This method initializes jScrollPaneServicesChosen	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneServicesChosen() {
		if (jScrollPaneServicesChosen == null) {
			jScrollPaneServicesChosen = new JScrollPane();
			jScrollPaneServicesChosen.setPreferredSize(new Dimension(60, 150));
			jScrollPaneServicesChosen.setViewportView(getJListServicesChosen());
		}
		return jScrollPaneServicesChosen;
	}


	/**
	 * This method initializes jListServicesChosen	
	 * @return javax.swing.JList	
	 */
	private JList<String> getJListServicesChosen() {
		if (jListServicesChosen == null) {
			jListServicesChosen = new JList<String>();
			jListServicesChosen.setModel(currProject.getJadeConfiguration().getListModelServices());
		}
		return jListServicesChosen;
	}

	/**
	 * This method initializes jPanelServiceAvailable	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelServiceAvailable() {
		if (jPanelServiceAvailable == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.insets = new Insets(0, 0, 0, 0);
			jPanelServiceAvailable = new JPanel();
			jPanelServiceAvailable.setLayout(new GridBagLayout());
			jPanelServiceAvailable.setPreferredSize(new Dimension(60, 150));
			jPanelServiceAvailable.add(getJListServicesAvailable(), gridBagConstraints6);
		}
		return jPanelServiceAvailable;
	}
	
	/**
	 * This method initializes jListServicesAvailable	
	 * @return javax.swing.JList	
	 */
	private JListClassSearcher getJListServicesAvailable() {
		if (jListServicesAvailable == null) {
			jListServicesAvailable = new JListClassSearcher(BaseService.class);
			jListServicesAvailable.addClassSearcherListListener(new JListClassSearcherListener() {
				@Override
				public void addClassFound(ClassElement2Display ce2d) {
					if (PlatformJadeConfig.isAutoService(ce2d.getClassElement())==true) {
						ce2d.setAdditionalText(PlatformJadeConfig.getAutoServiceTextAddition());
					}
				}
				@Override
				public void removeClassFound(ClassElement2Display ce2d) {
				}
			});
		}
		return jListServicesAvailable;
	}

	/**
	 * This method initializes jPanelServiceButtons	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelServiceButtons() {
		if (jPanelServiceButtons == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.anchor = GridBagConstraints.CENTER;
			gridBagConstraints13.insets = new Insets(0, 0, 30, 0);
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints8.weighty = 1.0;
			gridBagConstraints8.gridy = 3;
			jLabelDummyServices = new JLabel();
			jLabelDummyServices.setText(" ");
			jLabelDummyServices.setPreferredSize(new Dimension(16, 16));
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.insets = new Insets(0, 0, 10, 0);
			gridBagConstraints10.gridy = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.insets = new Insets(0, 0, 10, 0);
			gridBagConstraints9.gridy = 1;
			jPanelServiceButtons = new JPanel();
			jPanelServiceButtons.setLayout(new GridBagLayout());
			jPanelServiceButtons.add(getJButtonAdd(), gridBagConstraints9);
			jPanelServiceButtons.add(getJButtonRemove(), gridBagConstraints10);
			jPanelServiceButtons.add(jLabelDummyServices, gridBagConstraints8);
			jPanelServiceButtons.add(getJButtonDefaultJadeConfig(), gridBagConstraints13);
		}
		return jPanelServiceButtons;
	}

	/**
	 * This method initializes jButtonDefaultJadeConfig	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultJadeConfig() {
		if (jButtonDefaultJadeConfig == null) {
			jButtonDefaultJadeConfig = new JButton();
			jButtonDefaultJadeConfig.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonDefaultJadeConfig.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultJadeConfig.setToolTipText("Standardkonfiguration verwenden");
			jButtonDefaultJadeConfig.addActionListener(this);
		}
		return jButtonDefaultJadeConfig;
	}
	/**
	 * This method initializes jButtonAdd	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAdd() {
		if (jButtonServiceAdd == null) {
			jButtonServiceAdd = new JButton();
			jButtonServiceAdd.setIcon(GlobalInfo.getInternalImageIcon("ArrowLeft.png"));
			jButtonServiceAdd.setPreferredSize(new Dimension(45, 27));
			jButtonServiceAdd.setToolTipText("Service hinzufügen");
			jButtonServiceAdd.addActionListener(this);
		}
		return jButtonServiceAdd;
	}
	/**
	 * This method initializes jButtonRemove	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemove() {
		if (jButtonServiceRemove == null) {
			jButtonServiceRemove = new JButton();
			jButtonServiceRemove.setIcon(GlobalInfo.getInternalImageIcon("ArrowRight.png"));
			jButtonServiceRemove.setPreferredSize(new Dimension(45, 27));
			jButtonServiceRemove.setToolTipText("Service entfernen");
			jButtonServiceRemove.addActionListener(this);
		}
		return jButtonServiceRemove;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		// --- Nothing to do here yet -----------
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object trigger = ae.getSource();
		if (trigger==jButtonServiceAdd) {
			if (jListServicesAvailable.getSelectedValue()!=null) {
				Object[] selections = jListServicesAvailable.getSelectedValuesList().toArray();
				for (int i = 0; i < selections.length; i++) {
					ClassElement2Display serviceElement = (ClassElement2Display) selections[i];
					currProject.getJadeConfiguration().addService(serviceElement.getClassElement());	
				}
			}
			
		} else if (trigger==jButtonServiceRemove) {
			if (jListServicesChosen.getSelectedValue()!=null) {
				List<String> selections = jListServicesChosen.getSelectedValuesList();
				for (int i = 0; i < selections.size(); i++) {
					String serviceReference = (String) selections.get(i);
					currProject.getJadeConfiguration().removeService(serviceReference);
				}
			}
			
		} else if (trigger==jButtonDefaultJadeConfig) {
			
			// --- Get the default profile configuration ------------
			PlatformJadeConfig defaultConfig = Application.getGlobalInfo().getJadeDefaultPlatformConfig();

			// --- Clean current profile configuration --------------
			PlatformJadeConfig currConfig = currProject.getJadeConfiguration();
			
			// --- Set the current model to the default one ---------
			currConfig.setLocalPort(defaultConfig.getLocalPort());
			currConfig.setMtpCreation(defaultConfig.getMtpCreation());
			currConfig.setMtpIpAddress(defaultConfig.getMtpIpAddress());
			currConfig.setLocalPortMTP(defaultConfig.getLocalPortMTP());
			
			// --- Reset services -----------------------------------
			currConfig.removeAllServices();
			DefaultListModel<String> delimo = defaultConfig.getListModelServices();
			for (int i = 0; i < delimo.size(); i++) {
				String serviceRef = (String) delimo.get(i);
				currConfig.addService(serviceRef);
			}
		
		} 
		
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
