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
package agentgui.core.gui.projectwindow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.gui.components.ClassElement2Display;
import agentgui.core.gui.components.JListClassSearcher;
import agentgui.core.jade.ClassSearcher;
import agentgui.core.jade.PlatformJadeConfig;

/**
 * Represents the JPanel/Tab 'Configuration' - 'JADE-Configuration'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JadeSetup extends JPanel implements ActionListener, Observer {

	private static final long serialVersionUID = -7016775471452161527L;
	private final static String PathImage = Application.RunInfo.PathImageIntern();
	private Project currProject = null;
	
	private JLabel jLabelPort = null;
	private JLabel jLabelPortExplain = null;
	private JTextField jTextFieldDefaultPort = null;
	private JButton jButtonSetPortDefault = null;
	private JPanel jPanelPort = null;
	private JPanel jPanelServiceLists = null;
	private JLabel jLabelServicesChosen = null;
	private JLabel jLabelServicesAvailable = null;
	private JScrollPane jScrollPaneServicesChosen = null;
	private JList jListServicesChosen = null;
	private JListClassSearcher jListServicesAvailable = null;

	private JPanel jPanelServiceButtons = null;
	private JButton jButtonDefaultJadeConfig = null;
	private JButton jButtonServiceAdd = null;
	private JButton jButtonServiceRemove = null;
	private JLabel jLabelDummyServices = null;
	private JPanel jPanelServiceAvailable = null;
	private JButton jButtonSetPort = null;
	
	/**
	 * Constructor of this class
	 * @param project
	 */
	public JadeSetup(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);
		this.initialize();
		
		this.refreshDataView();
		
		// --- configure translation ------------
		jLabelPort.setText(Language.translate("Starte JADE über Port-Nr.:"));
		jLabelPortExplain.setText(Language.translate("(Falls bereits verwendet, wird versucht den nächst höheren Port zu nutzen)"));
		jButtonSetPortDefault.setToolTipText(Language.translate("Standard verwenden"));
		jButtonSetPort.setToolTipText(Language.translate("JADE-Port bearbeiten"));
		
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
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.insets = new Insets(15, 10, 0, 10);
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.weighty = 1.0;
		gridBagConstraints21.ipadx = 0;
		gridBagConstraints21.gridwidth = 3;
		gridBagConstraints21.gridy = 4;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.gridx = 1;
		gridBagConstraints5.gridy = 0;
		gridBagConstraints5.fill = GridBagConstraints.NONE;
		gridBagConstraints5.insets = new Insets(10, 10, 0, 0);
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridx = -1;
		gridBagConstraints1.gridy = 2;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.insets = new Insets(0, 15, 2, 0);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.ipady = 0;
		gridBagConstraints.insets = new Insets(10, 15, 2, 0);
		this.setLayout(new GridBagLayout());
		this.setSize(799, 439);
		
		jLabelPortExplain = new JLabel();
		jLabelPortExplain.setFont(new Font("Dialog", Font.PLAIN, 12));
		jLabelPortExplain.setText("(Falls bereits verwendet, wird versucht den nächst höheren Port zu nutzen)");
		jLabelPort = new JLabel();
		jLabelPort.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelPort.setText("Starte JADE über Port-Nr.:");

		this.add(jLabelPort, gridBagConstraints);
		this.add(jLabelPortExplain, gridBagConstraints1);
		this.add(getJPanelPort(), gridBagConstraints5);
		this.add(getJPanelServiceLists(), gridBagConstraints21);
	}

	/**
	 * This method initializes jTextFieldDefaultPort	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDefaultPort() {
		if (jTextFieldDefaultPort == null) {
			jTextFieldDefaultPort = new JTextField();
			jTextFieldDefaultPort.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldDefaultPort.setPreferredSize(new Dimension(71, 26));
			jTextFieldDefaultPort.setEditable(false);			
		}
		return jTextFieldDefaultPort;
	}
	/**
	 * This method initializes jButtonSetPort	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetPort() {
		if (jButtonSetPort == null) {
			jButtonSetPort = new JButton();
			jButtonSetPort.setIcon(new ImageIcon(getClass().getResource(PathImage + "edit.png")));
			jButtonSetPort.setPreferredSize(new Dimension(45, 26));
			jButtonSetPort.setToolTipText("JADE-Port bearbeiten");
			jButtonSetPort.setActionCommand("SetPort");
			jButtonSetPort.addActionListener(this);
		}
		return jButtonSetPort;
	}
	/**
	 * This method initializes jButtonSetPortDefault	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetPortDefault() {
		if (jButtonSetPortDefault == null) {
			jButtonSetPortDefault = new JButton();
			jButtonSetPortDefault.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBreset.png")));
			jButtonSetPortDefault.setPreferredSize(new Dimension(45, 26));
			jButtonSetPortDefault.setToolTipText("Standard verwenden");
			jButtonSetPortDefault.setActionCommand("SetPortDefault");
			jButtonSetPortDefault.addActionListener(this);
		}
		return jButtonSetPortDefault;
	}

	/**
	 * This method initializes jPanelPort	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelPort() {
		if (jPanelPort == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 1;
			gridBagConstraints15.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints15.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints3.gridy = -1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			jPanelPort = new JPanel();
			jPanelPort.setLayout(new GridBagLayout());
			jPanelPort.add(getJTextFieldDefaultPort(), gridBagConstraints2);
			jPanelPort.add(getJButtonSetPortDefault(), gridBagConstraints3);
			jPanelPort.add(getJButtonSetPort(), gridBagConstraints15);
		}
		return jPanelPort;
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
			gridBagConstraints14.gridy = 2;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(0, 15, 2, 0);
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.gridy = 1;
			jLabelServicesAvailable = new JLabel();
			jLabelServicesAvailable.setText("Verfügbare JADE-Services");
			jLabelServicesAvailable.setPreferredSize(new Dimension(156, 16));
			jLabelServicesAvailable.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(0, 5, 2, 10);
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridy = 1;
			jLabelServicesChosen = new JLabel();
			jLabelServicesChosen.setText("Ausgewählte JADE-Services");
			jLabelServicesChosen.setPreferredSize(new Dimension(156, 16));
			jLabelServicesChosen.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.weightx = 0.0;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints4.gridy = 2;
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
	private JList getJListServicesChosen() {
		if (jListServicesChosen == null) {
			jListServicesChosen = new JList();
			jListServicesChosen.setModel(currProject.JadeConfiguration.getListModelServices());
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
			jListServicesAvailable = new JListClassSearcher(ClassSearcher.CLASSES_BASESERVICE);
			//jListServicesAvailable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
			jButtonDefaultJadeConfig.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBreset.png")));
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
			jButtonServiceAdd.setIcon(new ImageIcon(getClass().getResource(PathImage + "ArrowLeft.png")));
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
			jButtonServiceRemove.setIcon(new ImageIcon(getClass().getResource(PathImage + "ArrowRight.png")));
			jButtonServiceRemove.setPreferredSize(new Dimension(45, 27));
			jButtonServiceRemove.setToolTipText("Service entfernen");
			jButtonServiceRemove.addActionListener(this);
		}
		return jButtonServiceRemove;
	}

	/**
	 * This method can be called in order to refresh the view 
	 */
	private void refreshDataView() {
		
		Integer currPort = currProject.JadeConfiguration.getLocalPort();
		if (currPort==null || currPort==0) {
			currPort = Application.RunInfo.getJadeLocalPort();
			currProject.JadeConfiguration.setLocalPort(currPort);
		}
		jTextFieldDefaultPort.setText(currPort.toString());
		this.jListServicesChosen.setModel(currProject.JadeConfiguration.getListModelServices());
	}
	
	@Override
	public void update(Observable observable, Object updateObject) {
		if (updateObject==Project.CHANGED_JadeConfiguration) {
			this.refreshDataView();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		//String actCMD = ae.getActionCommand();
		Object trigger = ae.getSource();
		
		if (trigger==jButtonSetPort) {
			
			// --- Open Dialog ------------------
			JadeSetupNewPort newPort = new JadeSetupNewPort( Application.MainWindow, currProject.getProjectName(), true, currProject, jTextFieldDefaultPort.getLocationOnScreen());
			newPort.setVisible(true);
			// === Go ahead =====================
			if ( newPort.isCanceled() == false ) {
				Integer oldLocalPort = currProject.JadeConfiguration.getLocalPort();
				Integer newLocalPort = newPort.getNewLocalPort4Jade();
				if (newLocalPort!=oldLocalPort) {
					// --- Set changes ----------
					currProject.JadeConfiguration.setLocalPort(newLocalPort);
					jTextFieldDefaultPort.setText(newPort.getNewLocalPort4Jade().toString());
				}
			}
			newPort.dispose();
			newPort = null;	
			
			
		} else if (trigger==jButtonSetPortDefault) {
			currProject.JadeConfiguration.setLocalPort(Application.RunInfo.getJadeLocalPort());
			jTextFieldDefaultPort.setText( Application.RunInfo.getJadeLocalPort().toString() );
			
		} else if (trigger==jButtonServiceAdd) {
			if (jListServicesAvailable.getSelectedValue()!=null) {
				Object[] selections = jListServicesAvailable.getSelectedValues();
				for (int i = 0; i < selections.length; i++) {
					ClassElement2Display serviceElement = (ClassElement2Display) selections[i];
					currProject.JadeConfiguration.addService(serviceElement.toString());	
				}
			}
			
		} else if (trigger==jButtonServiceRemove) {
			if (jListServicesChosen.getSelectedValue()!=null) {
				Object[] selections = jListServicesChosen.getSelectedValues();
				for (int i = 0; i < selections.length; i++) {
					String serviceReference = (String) selections[i];
					currProject.JadeConfiguration.removeService(serviceReference);
				}
			}
			
		} else if (trigger==jButtonDefaultJadeConfig) {
			
			// --- Get the default profile configuration ------------
			PlatformJadeConfig defaultConfig = Application.RunInfo.getJadeDefaultPlatformConfig();

			// --- Clean current profile configuration --------------
			PlatformJadeConfig currConfig = currProject.JadeConfiguration;
			currConfig.removeAllServices();
			
			// --- Set the current model to the default one ---------
			currConfig.setLocalPort(defaultConfig.getLocalPort());
			
			DefaultListModel delimo = defaultConfig.getListModelServices();
			for (int i = 0; i < delimo.size(); i++) {
				String serviceRef = (String) delimo.get(i);
				currConfig.addService(serviceRef);
			}
			this.refreshDataView();
		}
		
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
