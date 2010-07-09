package gui.projectwindow.simsetup;


import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.Application;
import application.Language;
import application.Project;

public class JadeSetup extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Project currProject = null;
	
	private JCheckBox jCheckBoxStartJade = null;
	private JCheckBox jCheckBoxUseDefaults = null;
	private JCheckBox jCheckBoxTopicManagementService = null;
	private JCheckBox jCheckBoxPersistentDeliveryService = null;
	private JCheckBox jCheckBoxMainReplicationService = null;
	private JCheckBox jCheckBoxAddressNotificationService = null;
	private JCheckBox jCheckBoxUDPNodeMonitoringServ = null;
	private JCheckBox jCheckBoxFaultRecoveryService = null;
	private JCheckBox jCheckBoxBEManagementService = null;
	private JCheckBox jCheckBoxInterPlatformMobilityService = null;
	private JTextField jTextFieldDefaultPort = null;
	private JLabel jLabelServiceTitle = null;
	private JLabel jLabelServiceTitleAddOn = null;
	private JLabel jLabelPort = null;
	private JLabel jLabelPortExplain = null;

	private JCheckBox jCheckBoxAgentMobilityService = null;

	private JCheckBox jCheckBoxNotificationService = null;

	private JButton jButtonSetPortDefault = null;

	/**
	 * This is the default constructor
	 */
	public JadeSetup(Project project) {
		super();
		this.currProject = project;
		initialize();
		
		// --- Daten aus Projekt übernehmen ---
		this.refreshDataView();
		
		// --- Übersetzung konfigurieren ------
		jCheckBoxStartJade.setText(Language.translate("JADE zu Simulationsbeginn starten"));
		jCheckBoxUseDefaults.setText(Language.translate("AgentGUI - Standardeinstellungen verwenden"));
		jLabelPort.setText(Language.translate("Starte JADE über Port-Nr.:"));
		jLabelPortExplain.setText(Language.translate("(Falls bereits verwendet, wird versucht den nächst höheren Port zu nutzen)"));
		jLabelServiceTitle.setText(Language.translate("Folgende Plattform-Dienste starten:"));
		jLabelServiceTitleAddOn.setText(Language.translate("Folgende Add-On-Dienste starten:"));
		
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		jLabelPortExplain = new JLabel();
		jLabelPortExplain.setText("(Falls bereits verwendet, wird versucht den nächst höheren Port zu nutzen)");
		jLabelPortExplain.setBounds(new Rectangle(40, 104, 445, 16));
		jLabelPortExplain.setFont(new Font("Dialog", Font.PLAIN, 12));
		jLabelPort = new JLabel();
		jLabelPort.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelPort.setBounds(new Rectangle(40, 88, 445, 16));
		jLabelPort.setText("Starte JADE über Port-Nr.:");
		jLabelServiceTitleAddOn = new JLabel();
		jLabelServiceTitleAddOn.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelServiceTitleAddOn.setBounds(new Rectangle(40, 410, 445, 16));
		jLabelServiceTitleAddOn.setText("Folgende Add-On-Dienste starten:");
		jLabelServiceTitle = new JLabel();
		jLabelServiceTitle.setText("Folgende Plattform-Dienste starten:");
		jLabelServiceTitle.setBounds(new Rectangle(40, 160, 445, 16));
		jLabelServiceTitle.setFont(new Font("Dialog", Font.BOLD, 12));
		this.setSize(591, 485);
		this.setLayout(null);
		this.add(getJCheckBoxStartJade(), null);
		this.add(getJCheckBoxUseDefaults(), null);
		this.add(jLabelServiceTitle, null);
		this.add(getJCheckBoxTopicManagementService(), null);
		this.add(getJCheckBoxPersistentDeliveryService(), null);
		this.add(getJCheckBoxMainReplicationService(), null);
		this.add(getJCheckBoxAddressNotificationService(), null);
		this.add(getJCheckBoxUDPNodeMonitoringServ(), null);
		this.add(getJCheckBoxFaultRecoveryService(), null);
		this.add(getJCheckBoxBEManagementService(), null);
		this.add(getJCheckBoxInterPlatformMobilityService(), null);
		this.add(jLabelServiceTitleAddOn, null);
		this.add(getJTextFieldDefaultPort(), null);
		this.add(jLabelPort, null);
		this.add(jLabelPortExplain, null);
		this.add(getJCheckBoxAgentMobilityService(), null);
		this.add(getJCheckBoxNotificationService(), null);
		this.add(getJButtonSetPortDefault(), null);
	}
	/**
	 * This method initializes jCheckBoxStartJade	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxStartJade() {
		if (jCheckBoxStartJade == null) {
			jCheckBoxStartJade = new JCheckBox();
			jCheckBoxStartJade.setText("JADE zu Simulationsbeginn starten");
			jCheckBoxStartJade.setFont(new Font("Dialog", Font.BOLD, 14));
			jCheckBoxStartJade.setBounds(new Rectangle(20, 20, 423, 27));
			jCheckBoxStartJade.setActionCommand("StartJade");
			jCheckBoxStartJade.addActionListener(this);
		}
		return jCheckBoxStartJade;
	}
	/**
	 * This method initializes jCheckBoxUseDefaults	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxUseDefaults() {
		if (jCheckBoxUseDefaults == null) {
			jCheckBoxUseDefaults = new JCheckBox();
			jCheckBoxUseDefaults.setText("AgentGUI - Standardeinstellungen verwenden");
			jCheckBoxUseDefaults.setFont(new Font("Dialog", Font.BOLD, 14));
			jCheckBoxUseDefaults.setBounds(new Rectangle(20, 47, 423, 27));
			jCheckBoxUseDefaults.setActionCommand("UseDefaults");
			jCheckBoxUseDefaults.addActionListener(this);
		}
		return jCheckBoxUseDefaults;
	}
	/**
	 * This method initializes jCheckBoxTopicManagementService	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxTopicManagementService() {
		if (jCheckBoxTopicManagementService == null) {
			jCheckBoxTopicManagementService = new JCheckBox();
			jCheckBoxTopicManagementService.setText("TopicManagementService");
			jCheckBoxTopicManagementService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxTopicManagementService.setBounds(new Rectangle(40, 202, 167, 24));
			jCheckBoxTopicManagementService.setActionCommand("TopicManagementService");
			jCheckBoxTopicManagementService.addActionListener(this);
		}
		return jCheckBoxTopicManagementService;
	}
	/**
	 * This method initializes jCheckBoxPersistentDeliveryService	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxPersistentDeliveryService() {
		if (jCheckBoxPersistentDeliveryService == null) {
			jCheckBoxPersistentDeliveryService = new JCheckBox();
			jCheckBoxPersistentDeliveryService.setText("PersistentDeliveryService");
			jCheckBoxPersistentDeliveryService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxPersistentDeliveryService.setBounds(new Rectangle(40, 298, 164, 24));
			jCheckBoxPersistentDeliveryService.setActionCommand("PersistentDeliveryService");
			jCheckBoxPersistentDeliveryService.addActionListener(this);
		}
		return jCheckBoxPersistentDeliveryService;
	}
	/**
	 * This method initializes jCheckBoxMainReplicationService	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxMainReplicationService() {
		if (jCheckBoxMainReplicationService == null) {
			jCheckBoxMainReplicationService = new JCheckBox();
			jCheckBoxMainReplicationService.setText("MainReplicationService");
			jCheckBoxMainReplicationService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxMainReplicationService.setBounds(new Rectangle(40, 226, 153, 24));
			jCheckBoxMainReplicationService.setActionCommand("MainReplicationService");
			jCheckBoxMainReplicationService.addActionListener(this);			
		}
		return jCheckBoxMainReplicationService;
	}
	/**
	 * This method initializes jCheckBoxAddressNotificationService	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxAddressNotificationService() {
		if (jCheckBoxAddressNotificationService == null) {
			jCheckBoxAddressNotificationService = new JCheckBox();
			jCheckBoxAddressNotificationService.setText("AddressNotificationService");
			jCheckBoxAddressNotificationService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxAddressNotificationService.setBounds(new Rectangle(40, 274, 172, 24));
			jCheckBoxAddressNotificationService.setActionCommand("AddressNotificationService");
			jCheckBoxAddressNotificationService.addActionListener(this);
		}
		return jCheckBoxAddressNotificationService;
	}
	/**
	 * This method initializes jCheckBoxUDPNodeMonitoringServ	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxUDPNodeMonitoringServ() {
		if (jCheckBoxUDPNodeMonitoringServ == null) {
			jCheckBoxUDPNodeMonitoringServ = new JCheckBox();
			jCheckBoxUDPNodeMonitoringServ.setText("UDPNodeMonitoringService");
			jCheckBoxUDPNodeMonitoringServ.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxUDPNodeMonitoringServ.setBounds(new Rectangle(40, 322, 178, 24));
			jCheckBoxUDPNodeMonitoringServ.setActionCommand("UDPNodeMonitoringServ");
			jCheckBoxUDPNodeMonitoringServ.addActionListener(this);
		}
		return jCheckBoxUDPNodeMonitoringServ;
	}
	/**
	 * This method initializes jCheckBoxFaultRecoveryService	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxFaultRecoveryService() {
		if (jCheckBoxFaultRecoveryService == null) {
			jCheckBoxFaultRecoveryService = new JCheckBox();
			jCheckBoxFaultRecoveryService.setText("FaultRecoveryService");
			jCheckBoxFaultRecoveryService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxFaultRecoveryService.setBounds(new Rectangle(40, 250, 142, 24));
			jCheckBoxFaultRecoveryService.setActionCommand("FaultRecoveryService");
			jCheckBoxFaultRecoveryService.addActionListener(this);
		}
		return jCheckBoxFaultRecoveryService;
	}
	/**
	 * This method initializes jCheckBoxBEManagementService	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxBEManagementService() {
		if (jCheckBoxBEManagementService == null) {
			jCheckBoxBEManagementService = new JCheckBox();
			jCheckBoxBEManagementService.setText("BEManagementService");
			jCheckBoxBEManagementService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxBEManagementService.setBounds(new Rectangle(40, 346, 153, 24));
			jCheckBoxBEManagementService.setActionCommand("BEManagementService");
			jCheckBoxBEManagementService.addActionListener(this);
		}
		return jCheckBoxBEManagementService;
	}
	/**
	 * This method initializes jCheckBoxAgentMobilityService	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxAgentMobilityService() {
		if (jCheckBoxAgentMobilityService == null) {
			jCheckBoxAgentMobilityService = new JCheckBox();
			jCheckBoxAgentMobilityService.setText("AgentMobilityService");
			jCheckBoxAgentMobilityService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxAgentMobilityService.setBounds(new Rectangle(40, 370, 136, 24));
			jCheckBoxAgentMobilityService.setActionCommand("AgentMobilityService.");
			jCheckBoxAgentMobilityService.addActionListener(this);
		}
		return jCheckBoxAgentMobilityService;
	}
	/**
	 * This method initializes jCheckBoxNotificationService	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxNotificationService() {
		if (jCheckBoxNotificationService == null) {
			jCheckBoxNotificationService = new JCheckBox();
			jCheckBoxNotificationService.setText("NotificationService");
			jCheckBoxNotificationService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxNotificationService.setBounds(new Rectangle(40, 178, 126, 24));
			jCheckBoxNotificationService.setActionCommand("NotificationService");
			jCheckBoxNotificationService.addActionListener(this);

		}
		return jCheckBoxNotificationService;
	}
	/**
	 * This method initializes jCheckBoxInterPlatformMobilityService	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxInterPlatformMobilityService() {
		if (jCheckBoxInterPlatformMobilityService == null) {
			jCheckBoxInterPlatformMobilityService = new JCheckBox();
			jCheckBoxInterPlatformMobilityService.setText("InterPlatformMobilityService");
			jCheckBoxInterPlatformMobilityService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxInterPlatformMobilityService.setBounds(new Rectangle(40, 426, 175, 24));
			jCheckBoxInterPlatformMobilityService.setActionCommand("InterPlatformMobilityService");
			jCheckBoxInterPlatformMobilityService.addActionListener(this);
		}
		return jCheckBoxInterPlatformMobilityService;
	}

	/**
	 * This method initializes jTextFieldDefaultPort	
	 * @return javax.swing.JTextField	
	 * @throws ParseException 
	 */
	private JTextField getJTextFieldDefaultPort() {
		if (jTextFieldDefaultPort == null) {
			jTextFieldDefaultPort = new JTextField();
			jTextFieldDefaultPort.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldDefaultPort.setBounds(new Rectangle(40, 121, 71, 26));
			jTextFieldDefaultPort.setEditable(false);
			jTextFieldDefaultPort.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusGained(java.awt.event.FocusEvent e) {
					// --- Focus auf den Default-Button -------------
					jButtonSetPortDefault.requestFocusInWindow();
					// --- Dialog zum einstellen des Ports öffnen ---
					JadeSetupNewPort newPort = new JadeSetupNewPort( Application.MainWindow, currProject.getProjectName(), true, currProject, jTextFieldDefaultPort.getLocationOnScreen() );
					newPort.setVisible(true);
					// === Hier geht's weiter, wenn der Dialog wieder geschlossen ist ===
					if ( newPort.isCanceled() == false ) {
						Integer oldLocalPort = currProject.JadeConfiguration.getLocalPort();
						Integer newLocalPort = newPort.getNewLocalPort4Jade();
						if (newLocalPort!=oldLocalPort) {
							// --- Änderung übernehmen --------------
							currProject.JadeConfiguration.setLocalPort(newLocalPort);
							jTextFieldDefaultPort.setText(newPort.getNewLocalPort4Jade().toString());
							if ( currProject.JadeConfiguration.isEqual(Application.RunInfo.getJadeDefaultPlatformConfig()) == true ) {
								jCheckBoxUseDefaults.setSelected(true);
							} else {
								jCheckBoxUseDefaults.setSelected(false);
							}
							currProject.JadeConfiguration.setUseDefaults(jCheckBoxUseDefaults.isSelected());
							currProject.ProjectUnsaved = true;
						}
					}
					newPort.dispose();
					newPort = null;	
				}
			});
		}
		return jTextFieldDefaultPort;
	}

	/**
	 * This method initializes jButtonSetPortDefault	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetPortDefault() {
		if (jButtonSetPortDefault == null) {
			jButtonSetPortDefault = new JButton();
			jButtonSetPortDefault.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonSetPortDefault.setText("Default");
			jButtonSetPortDefault.setActionCommand("SetPortDefault");
			jButtonSetPortDefault.addActionListener(this);
		}
		return jButtonSetPortDefault;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {

		String ActCMD = ae.getActionCommand();
		Object Trigger = ae.getSource();
		
		if (Trigger==jCheckBoxStartJade) {
			currProject.JadeConfiguration.setStart4Simulation(jCheckBoxStartJade.isSelected());			
		} else if (Trigger==jCheckBoxUseDefaults) {
			// --- Default-Configuration ? --- START ----------------
			if ( jCheckBoxUseDefaults.isSelected() == true ) {
				// --- take and show the default-configuration ------
				currProject.JadeConfiguration = Application.RunInfo.getJadeDefaultPlatformConfig();
				this.refreshDataView();
			}
			// --- Default-Configuration ? --- END ------------------		
			
		} else if (Trigger==jButtonSetPortDefault) {
			currProject.JadeConfiguration.setLocalPort(Application.RunInfo.getJadeLocalPort());
			jTextFieldDefaultPort.setText( Application.RunInfo.getJadeLocalPort().toString() );
	
		} else if (Trigger==jCheckBoxNotificationService) {
			currProject.JadeConfiguration.runNotificationService(jCheckBoxNotificationService.isSelected());
		} else if (Trigger==jCheckBoxTopicManagementService) {
			currProject.JadeConfiguration.runTopicManagementService(jCheckBoxTopicManagementService.isSelected());
		} else if (Trigger==jCheckBoxPersistentDeliveryService) {
			currProject.JadeConfiguration.runPersistentDeliveryService(jCheckBoxPersistentDeliveryService.isSelected());
		} else if (Trigger==jCheckBoxMainReplicationService) {
			currProject.JadeConfiguration.runMainReplicationService(jCheckBoxMainReplicationService.isSelected());
		} else if (Trigger==jCheckBoxAddressNotificationService) {
			currProject.JadeConfiguration.runAddressNotificationService(jCheckBoxAddressNotificationService.isSelected());
		} else if (Trigger==jCheckBoxUDPNodeMonitoringServ) {
			currProject.JadeConfiguration.runUDPNodeMonitoringServ(jCheckBoxUDPNodeMonitoringServ.isSelected());
		} else if (Trigger==jCheckBoxFaultRecoveryService) {
			currProject.JadeConfiguration.runFaultRecoveryService(jCheckBoxFaultRecoveryService.isSelected());
		} else if (Trigger==jCheckBoxBEManagementService) {
			currProject.JadeConfiguration.runBEManagementService(jCheckBoxBEManagementService.isSelected());
		} else if (Trigger==jCheckBoxAgentMobilityService) {
			currProject.JadeConfiguration.runAgentMobilityService(jCheckBoxAgentMobilityService.isSelected());
		} else if (Trigger==jCheckBoxInterPlatformMobilityService) {
			currProject.JadeConfiguration.runInterPlatformMobilityService(jCheckBoxInterPlatformMobilityService.isSelected());
		
		} else {
			System.out.println( "Unknown Action " + ActCMD );
			System.out.println( "Source.  " + Trigger );
		}
		
		// --- Keep the 'jCheckBoxUseDefaults' selected ? -----------
		if (Trigger!=jCheckBoxUseDefaults || Trigger==jCheckBoxUseDefaults && jCheckBoxUseDefaults.isSelected() == true) {
			if ( currProject.JadeConfiguration.isEqual(Application.RunInfo.getJadeDefaultPlatformConfig()) == true ) {
				jCheckBoxUseDefaults.setSelected(true);
			} else {
				jCheckBoxUseDefaults.setSelected(false);
			}
		}
		currProject.JadeConfiguration.setUseDefaults(jCheckBoxUseDefaults.isSelected());
		
		// --- Set currProject unsaved ------------------------------
		currProject.ProjectUnsaved = true;
	}

	private void refreshDataView() {
		
		jCheckBoxStartJade.setSelected(currProject.JadeConfiguration.isStart4Simulation());
		jCheckBoxUseDefaults.setSelected(currProject.JadeConfiguration.isUseDefaults());
		
		jTextFieldDefaultPort.setText(currProject.JadeConfiguration.getLocalPort().toString());
		
		jCheckBoxNotificationService.setSelected(currProject.JadeConfiguration.isNotificationService());
		jCheckBoxTopicManagementService.setSelected(currProject.JadeConfiguration.isTopicManagementService());
		jCheckBoxPersistentDeliveryService.setSelected(currProject.JadeConfiguration.isPersistentDeliveryService());
		jCheckBoxMainReplicationService.setSelected(currProject.JadeConfiguration.isMainReplicationService());
		jCheckBoxAddressNotificationService.setSelected(currProject.JadeConfiguration.isAddressNotificationService());
		jCheckBoxUDPNodeMonitoringServ.setSelected(currProject.JadeConfiguration.isUDPNodeMonitoringServ());
		jCheckBoxFaultRecoveryService.setSelected(currProject.JadeConfiguration.isFaultRecoveryService());
		jCheckBoxBEManagementService.setSelected(currProject.JadeConfiguration.isBEManagementService());
		jCheckBoxAgentMobilityService.setSelected(currProject.JadeConfiguration.isAgentMobilityService());
		
		jCheckBoxInterPlatformMobilityService.setSelected(currProject.JadeConfiguration.isInterPlatformMobilityService());
		
	}

}  //  @jve:decl-index=0:visual-constraint="19,15"
