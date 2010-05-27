package gui.projectwindow;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import application.Project;

public class SetupSimulationJADE extends JPanel {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private Project currProject = null;
	
	private JCheckBox jCheckBoxStartJade = null;
	private JCheckBox jCheckBoxUseDefaults = null;
	private JLabel jLabelServiceTitle = null;
	private JCheckBox jCheckBoxTopicManagementService = null;
	private JCheckBox jCheckBoxPersistentDeliveryService = null;
	private JCheckBox jCheckBoxMainReplicationService = null;
	private JCheckBox jCheckBoxAddressNotificationService = null;
	private JCheckBox jCheckBoxUDPNodeMonitoringServ = null;
	private JCheckBox jCheckBoxFaultRecoveryService = null;
	private JCheckBox jCheckBoxBEManagementService = null;
	private JCheckBox jCheckBoxInterPlatformMobilityService = null;
	private JLabel jLabelDummy = null;
	private JLabel jLabelServiceTitleAddOn = null;
	/**
	 * This is the default constructor
	 */
	public SetupSimulationJADE(Project project) {
		super();
		this.currProject = project;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
		gridBagConstraints111.gridx = 0;
		gridBagConstraints111.anchor = GridBagConstraints.WEST;
		gridBagConstraints111.insets = new Insets(20, 30, 0, 0);
		gridBagConstraints111.gridy = 10;
		jLabelServiceTitleAddOn = new JLabel();
		jLabelServiceTitleAddOn.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelServiceTitleAddOn.setText("Folgende Add-On-Dienste starten:");
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.gridx = 0;
		gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints10.weighty = 1.0;
		gridBagConstraints10.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints10.gridy = 12;
		jLabelDummy = new JLabel();
		jLabelDummy.setText(" ");
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 0;
		gridBagConstraints9.anchor = GridBagConstraints.WEST;
		gridBagConstraints9.insets = new Insets(0, 30, 0, 0);
		gridBagConstraints9.gridy = 11;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.anchor = GridBagConstraints.WEST;
		gridBagConstraints7.insets = new Insets(0, 30, 0, 0);
		gridBagConstraints7.gridy = 9;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.insets = new Insets(0, 30, 0, 0);
		gridBagConstraints6.gridy = 4;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.insets = new Insets(0, 30, 0, 0);
		gridBagConstraints5.gridy = 8;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.insets = new Insets(0, 30, 0, 0);
		gridBagConstraints4.gridy = 5;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.insets = new Insets(0, 30, 0, 0);
		gridBagConstraints3.gridy = 3;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.insets = new Insets(0, 30, 0, 0);
		gridBagConstraints21.gridy = 7;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.fill = GridBagConstraints.NONE;
		gridBagConstraints11.weightx = 0.1;
		gridBagConstraints11.weighty = 0.0;
		gridBagConstraints11.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints11.ipadx = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(0, 30, 0, 0);
		gridBagConstraints2.gridy = 6;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.insets = new Insets(20, 30, 5, 0);
		gridBagConstraints1.gridy = 2;
		jLabelServiceTitle = new JLabel();
		jLabelServiceTitle.setText("Folgende Plattform-Dienste starten:");
		jLabelServiceTitle.setFont(new Font("Dialog", Font.BOLD, 12));
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints.gridy = 1;
		this.setSize(579, 420);
		this.setLayout(new GridBagLayout());
		this.add(getJCheckBoxStartJade(), gridBagConstraints11);
		this.add(getJCheckBoxUseDefaults(), gridBagConstraints);
		this.add(jLabelServiceTitle, gridBagConstraints1);
		this.add(getJCheckBoxTopicManagementService(), gridBagConstraints2);
		this.add(getJCheckBoxPersistentDeliveryService(), gridBagConstraints21);
		this.add(getJCheckBoxMainReplicationService(), gridBagConstraints3);
		this.add(getJCheckBoxAddressNotificationService(), gridBagConstraints4);
		this.add(getJCheckBoxUDPNodeMonitoringServ(), gridBagConstraints5);
		this.add(getJCheckBoxFaultRecoveryService(), gridBagConstraints6);
		this.add(getJCheckBoxBEManagementService(), gridBagConstraints7);
		this.add(getJCheckBoxInterPlatformMobilityService(), gridBagConstraints9);
		this.add(jLabelDummy, gridBagConstraints10);
		this.add(jLabelServiceTitleAddOn, gridBagConstraints111);
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
			jCheckBoxStartJade.setFont(new Font("Dialog", Font.BOLD, 14));
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
			jCheckBoxUseDefaults.setFont(new Font("Dialog", Font.BOLD, 14));
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
			jCheckBoxTopicManagementService.setFont(new Font("Dialog", Font.PLAIN, 12));
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
			jCheckBoxPersistentDeliveryService.setFont(new Font("Dialog", Font.PLAIN, 12));
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
			jCheckBoxMainReplicationService.setFont(new Font("Dialog", Font.PLAIN, 12));
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
			jCheckBoxAddressNotificationService.setFont(new Font("Dialog", Font.PLAIN, 12));
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
			jCheckBoxUDPNodeMonitoringServ.setFont(new Font("Dialog", Font.PLAIN, 12));
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
			jCheckBoxFaultRecoveryService.setFont(new Font("Dialog", Font.PLAIN, 12));
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
			jCheckBoxBEManagementService.setFont(new Font("Dialog", Font.PLAIN, 12));
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
			jCheckBoxInterPlatformMobilityService.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jCheckBoxInterPlatformMobilityService;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
