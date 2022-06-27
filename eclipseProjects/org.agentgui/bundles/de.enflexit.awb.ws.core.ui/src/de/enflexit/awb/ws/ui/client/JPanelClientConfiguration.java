package de.enflexit.awb.ws.ui.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JSeparator;

import de.enflexit.awb.ws.ui.WsConfigurationInterface;
import de.enflexit.awb.ws.ui.client.credentials.JPanelAssignCredential;

/**
 * The Class JPanelClientConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelClientConfiguration extends JPanel implements WsConfigurationInterface {
	
	private static final long serialVersionUID = 7987858783733542296L;
	
	private JPanelClientBundle panelClientBundle;
	private JPanelCredentials panelCredentials;
	private JPanelAssignCredential panelAssignCredential;
	private JSeparator jSeparatorClientAssign;
	private JPanel panel;
	private JSeparator jSeparatorAssignCredential;
	
	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelClientConfiguration() {
		this.initialize();
	}
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{230, 0, 281, 0, 0, 230, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_panelClientBundle = new GridBagConstraints();
		gbc_panelClientBundle.insets = new Insets(10, 10, 10, 5);
		gbc_panelClientBundle.fill = GridBagConstraints.BOTH;
		gbc_panelClientBundle.gridx = 0;
		gbc_panelClientBundle.gridy = 0;
		add(getPanelClientBundle(), gbc_panelClientBundle);
		GridBagConstraints gbc_jSeparatorClientAssign = new GridBagConstraints();
		gbc_jSeparatorClientAssign.insets = new Insets(0, 0, 0, 5);
		gbc_jSeparatorClientAssign.gridx = 1;
		gbc_jSeparatorClientAssign.gridy = 0;
		add(getJSeparatorClientAssign(), gbc_jSeparatorClientAssign);
		GridBagConstraints gbc_panelAssignCredential = new GridBagConstraints();
		gbc_panelAssignCredential.insets = new Insets(10, 5, 10, 5);
		gbc_panelAssignCredential.fill = GridBagConstraints.BOTH;
		gbc_panelAssignCredential.gridx = 2;
		gbc_panelAssignCredential.gridy = 0;
		add(getPanelAssignCredential(), gbc_panelAssignCredential);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 4;
		gbc_panel.gridy = 0;
		add(getPanel(), gbc_panel);
		GridBagConstraints gbc_panelCredentials = new GridBagConstraints();
		gbc_panelCredentials.insets = new Insets(10, 0, 10, 0);
		gbc_panelCredentials.fill = GridBagConstraints.BOTH;
		gbc_panelCredentials.gridx = 5;
		gbc_panelCredentials.gridy = 0;
		add(getPanelCredentials(), gbc_panelCredentials);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	 */
	@Override
	public boolean userConfirmedToChangeView() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private JPanelClientBundle getPanelClientBundle() {
		if (panelClientBundle == null) {
			panelClientBundle = new JPanelClientBundle();
		}
		return panelClientBundle;
	}
	private JPanelCredentials getPanelCredentials() {
		if (panelCredentials == null) {
			panelCredentials = new JPanelCredentials();
		}
		return panelCredentials;
	}
	private JPanelAssignCredential getPanelAssignCredential() {
		if (panelAssignCredential == null) {
			panelAssignCredential = new JPanelAssignCredential();
		}
		return panelAssignCredential;
	}
	private JSeparator getJSeparatorClientAssign() {
		if (jSeparatorClientAssign == null) {
			jSeparatorClientAssign = new JSeparator();
		}
		return jSeparatorClientAssign;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout(0, 0));
			panel.add(getJSeparatorAssignCredential(), BorderLayout.NORTH);
		}
		return panel;
	}
	private JSeparator getJSeparatorAssignCredential() {
		if (jSeparatorAssignCredential == null) {
			jSeparatorAssignCredential = new JSeparator();
			jSeparatorAssignCredential.setPreferredSize(new Dimension(5, 1));
		}
		return jSeparatorAssignCredential;
	}
}
