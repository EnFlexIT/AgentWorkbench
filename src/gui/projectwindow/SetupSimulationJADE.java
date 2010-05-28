package gui.projectwindow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.Project;

public class SetupSimulationJADE extends JPanel implements ActionListener {

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
	/**
	 * This is the default constructor
	 */
	public SetupSimulationJADE(Project project) {
		super();
		this.currProject = project;
		initialize();
		this.refreshView();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		jLabelPortExplain = new JLabel();
		jLabelPortExplain.setText("(Falls bereits verwendet, wird versucht den nächst höheren Port zu nutzen)");
		jLabelPortExplain.setBounds(new Rectangle(40, 110, 445, 16));
		jLabelPortExplain.setFont(new Font("Dialog", Font.PLAIN, 12));
		jLabelPort = new JLabel();
		jLabelPort.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelPort.setBounds(new Rectangle(40, 94, 445, 16));
		jLabelPort.setText("Starte JADE über Port-Nr.:");
		jLabelServiceTitleAddOn = new JLabel();
		jLabelServiceTitleAddOn.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelServiceTitleAddOn.setBounds(new Rectangle(40, 368, 445, 16));
		jLabelServiceTitleAddOn.setText("Folgende Add-On-Dienste starten:");
		jLabelServiceTitle = new JLabel();
		jLabelServiceTitle.setText("Folgende Plattform-Dienste starten:");
		jLabelServiceTitle.setBounds(new Rectangle(40, 168, 445, 16));
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
	}

	/**
	 * This method initializes jCheckBoxStartJade	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxStartJade() {
		if (jCheckBoxStartJade == null) {
			jCheckBoxStartJade = new JCheckBox();
			jCheckBoxStartJade.setText("JADE zu Simulationsbeginn starten");
			jCheckBoxStartJade.setBounds(new Rectangle(20, 20, 423, 27));
			jCheckBoxStartJade.setFont(new Font("Dialog", Font.BOLD, 14));
			jCheckBoxStartJade.setActionCommand("StartJade");
			jCheckBoxStartJade.addActionListener(this);
		}
		return jCheckBoxStartJade;
	}

	/**
	 * This method initializes jCheckBoxUseDefaults	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxUseDefaults() {
		if (jCheckBoxUseDefaults == null) {
			jCheckBoxUseDefaults = new JCheckBox();
			jCheckBoxUseDefaults.setText("AgentGUI - Standardeinstellungen verwenden");
			jCheckBoxUseDefaults.setBounds(new Rectangle(20, 47, 423, 27));
			jCheckBoxUseDefaults.setFont(new Font("Dialog", Font.BOLD, 14));
			jCheckBoxUseDefaults.setActionCommand("UseDefaults");
			jCheckBoxUseDefaults.addActionListener(this);
		}
		return jCheckBoxUseDefaults;
	}

	/**
	 * This method initializes jCheckBoxTopicManagementService	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxTopicManagementService() {
		if (jCheckBoxTopicManagementService == null) {
			jCheckBoxTopicManagementService = new JCheckBox();
			jCheckBoxTopicManagementService.setText("TopicManagementService");
			jCheckBoxTopicManagementService.setBounds(new Rectangle(40, 261, 167, 24));
			jCheckBoxTopicManagementService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxTopicManagementService.setActionCommand("TopicManagementService");
			jCheckBoxTopicManagementService.addActionListener(this);
		}
		return jCheckBoxTopicManagementService;
	}

	/**
	 * This method initializes jCheckBoxPersistentDeliveryService	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxPersistentDeliveryService() {
		if (jCheckBoxPersistentDeliveryService == null) {
			jCheckBoxPersistentDeliveryService = new JCheckBox();
			jCheckBoxPersistentDeliveryService.setText("PersistentDeliveryService");
			jCheckBoxPersistentDeliveryService.setBounds(new Rectangle(40, 285, 164, 24));
			jCheckBoxPersistentDeliveryService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxPersistentDeliveryService.setActionCommand("PersistentDeliveryService");
			jCheckBoxPersistentDeliveryService.addActionListener(this);
		}
		return jCheckBoxPersistentDeliveryService;
	}

	/**
	 * This method initializes jCheckBoxMainReplicationService	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxMainReplicationService() {
		if (jCheckBoxMainReplicationService == null) {
			jCheckBoxMainReplicationService = new JCheckBox();
			jCheckBoxMainReplicationService.setText("MainReplicationService");
			jCheckBoxMainReplicationService.setBounds(new Rectangle(40, 189, 153, 24));
			jCheckBoxMainReplicationService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxMainReplicationService.setActionCommand("MainReplicationService");
			jCheckBoxMainReplicationService.addActionListener(this);			
		}
		return jCheckBoxMainReplicationService;
	}

	/**
	 * This method initializes jCheckBoxAddressNotificationService	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxAddressNotificationService() {
		if (jCheckBoxAddressNotificationService == null) {
			jCheckBoxAddressNotificationService = new JCheckBox();
			jCheckBoxAddressNotificationService.setText("AddressNotificationService");
			jCheckBoxAddressNotificationService.setBounds(new Rectangle(40, 237, 172, 24));
			jCheckBoxAddressNotificationService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxAddressNotificationService.setActionCommand("AddressNotificationService");
			jCheckBoxAddressNotificationService.addActionListener(this);
		}
		return jCheckBoxAddressNotificationService;
	}

	/**
	 * This method initializes jCheckBoxUDPNodeMonitoringServ	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxUDPNodeMonitoringServ() {
		if (jCheckBoxUDPNodeMonitoringServ == null) {
			jCheckBoxUDPNodeMonitoringServ = new JCheckBox();
			jCheckBoxUDPNodeMonitoringServ.setText("UDPNodeMonitoringService");
			jCheckBoxUDPNodeMonitoringServ.setBounds(new Rectangle(40, 309, 178, 24));
			jCheckBoxUDPNodeMonitoringServ.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxUDPNodeMonitoringServ.setActionCommand("UDPNodeMonitoringServ");
			jCheckBoxUDPNodeMonitoringServ.addActionListener(this);
		}
		return jCheckBoxUDPNodeMonitoringServ;
	}

	/**
	 * This method initializes jCheckBoxFaultRecoveryService	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxFaultRecoveryService() {
		if (jCheckBoxFaultRecoveryService == null) {
			jCheckBoxFaultRecoveryService = new JCheckBox();
			jCheckBoxFaultRecoveryService.setText("FaultRecoveryService");
			jCheckBoxFaultRecoveryService.setBounds(new Rectangle(40, 213, 142, 24));
			jCheckBoxFaultRecoveryService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxFaultRecoveryService.setActionCommand("FaultRecoveryService");
			jCheckBoxFaultRecoveryService.addActionListener(this);
		}
		return jCheckBoxFaultRecoveryService;
	}

	/**
	 * This method initializes jCheckBoxBEManagementService	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxBEManagementService() {
		if (jCheckBoxBEManagementService == null) {
			jCheckBoxBEManagementService = new JCheckBox();
			jCheckBoxBEManagementService.setText("BEManagementService");
			jCheckBoxBEManagementService.setBounds(new Rectangle(40, 333, 153, 24));
			jCheckBoxBEManagementService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxBEManagementService.setActionCommand("BEManagementService");
			jCheckBoxBEManagementService.addActionListener(this);
		}
		return jCheckBoxBEManagementService;
	}

	/**
	 * This method initializes jCheckBoxInterPlatformMobilityService	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxInterPlatformMobilityService() {
		if (jCheckBoxInterPlatformMobilityService == null) {
			jCheckBoxInterPlatformMobilityService = new JCheckBox();
			jCheckBoxInterPlatformMobilityService.setText("InterPlatformMobilityService");
			jCheckBoxInterPlatformMobilityService.setBounds(new Rectangle(40, 384, 175, 24));
			jCheckBoxInterPlatformMobilityService.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxInterPlatformMobilityService.setActionCommand("InterPlatformMobilityService");
			jCheckBoxInterPlatformMobilityService.addActionListener(this);
		}
		return jCheckBoxInterPlatformMobilityService;
	}

	/**
	 * This method initializes jTextFieldDefaultPort	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDefaultPort() {
		if (jTextFieldDefaultPort == null) {
			jTextFieldDefaultPort = new JTextField();
			jTextFieldDefaultPort.setPreferredSize(new Dimension(400, 200));
			jTextFieldDefaultPort.setLocation(new Point(40, 131));
			jTextFieldDefaultPort.setSize(new Dimension(80, 26));
			jTextFieldDefaultPort.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldDefaultPort.setActionCommand("DefaultPort");
			jTextFieldDefaultPort.addActionListener(this);
		}
		return jTextFieldDefaultPort;
	}

	
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		String ActCMD = ae.getActionCommand();
		Object Trigger = ae.getSource();
		
		if (Trigger==jCheckBoxStartJade) {
			currProject.JadeConfiguration.setStart4Simulation(jCheckBoxStartJade.isSelected());			
		} else if (Trigger==jCheckBoxUseDefaults) {
			currProject.JadeConfiguration.setUseDefaults(jCheckBoxUseDefaults.isSelected());
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
		} else if (Trigger==jCheckBoxInterPlatformMobilityService) {
			currProject.JadeConfiguration.runInterPlatformMobilityService(jCheckBoxInterPlatformMobilityService.isSelected());
		} else if (Trigger==jTextFieldDefaultPort) {
			
		} else {
			System.out.println( "Unknown Action " + ActCMD );
			System.out.println( "Source.  " + Trigger );
		}
	}

	private void refreshView() {
		
		jCheckBoxStartJade.setSelected(currProject.JadeConfiguration.isStart4Simulation());
		jCheckBoxUseDefaults.setSelected(currProject.JadeConfiguration.isUseDefaults());
		
		jCheckBoxTopicManagementService.setSelected(currProject.JadeConfiguration.isTopicManagementService());
		jCheckBoxPersistentDeliveryService.setSelected(currProject.JadeConfiguration.isPersistentDeliveryService());
		jCheckBoxMainReplicationService.setSelected(currProject.JadeConfiguration.isMainReplicationService());
		jCheckBoxAddressNotificationService.setSelected(currProject.JadeConfiguration.isAddressNotificationService());
		jCheckBoxUDPNodeMonitoringServ.setSelected(currProject.JadeConfiguration.isUDPNodeMonitoringServ());
		jCheckBoxFaultRecoveryService.setSelected(currProject.JadeConfiguration.isFaultRecoveryService());
		jCheckBoxBEManagementService.setSelected(currProject.JadeConfiguration.isBEManagementService());
		
		jCheckBoxInterPlatformMobilityService.setSelected(currProject.JadeConfiguration.isInterPlatformMobilityService());
		
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="15,13"
