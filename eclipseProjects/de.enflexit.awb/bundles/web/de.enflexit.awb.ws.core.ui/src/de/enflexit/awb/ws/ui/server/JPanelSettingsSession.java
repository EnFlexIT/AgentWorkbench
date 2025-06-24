package de.enflexit.awb.ws.ui.server;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.enflexit.awb.ws.core.JettySessionSettings;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServerSession;

/**
 * The Class JPanelSettingsSession.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelSettingsSession extends JPanel implements JettyConfigurationInterface<ServerTreeNodeServerSession>, ActionListener {

	private static final long serialVersionUID = -4985161964727450005L;

	private ServerTreeNodeServer serverTreeNodeServer; 					// --- Always set and thus available --------
	
	private boolean isPauseActionListener;
	private JLabel jLabelActivated;
	private JCheckBox jCheckBoxActivated;
	
	private JLabel jLabelSessionHandlerConfiguration;
	private JScrollPane jScrollPaneConfiguration;
	private JTableSettingsServer jTableSettingsServer;
	private JPanel jPanelLeft;
	private JPanel jPanelRight;

	
	/**
	 * Instantiates a new JPanel for the server settings.
	 */
	public JPanelSettingsSession() {
		this.initialize();
	}
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jPanelLeft = new GridBagConstraints();
		gbc_jPanelLeft.insets = new Insets(5, 10, 0, 5);
		gbc_jPanelLeft.fill = GridBagConstraints.BOTH;
		gbc_jPanelLeft.gridx = 0;
		gbc_jPanelLeft.gridy = 0;
		this.add(this.getJPanelLeft(), gbc_jPanelLeft);
		
		GridBagConstraints gbc_jPanelRight = new GridBagConstraints();
		gbc_jPanelRight.insets = new Insets(5, 5, 0, 10);
		gbc_jPanelRight.fill = GridBagConstraints.BOTH;
		gbc_jPanelRight.gridx = 1;
		gbc_jPanelRight.gridy = 0;
		this.add(this.getJPanelRight(), gbc_jPanelRight);
		
		GridBagConstraints gbc_jScrollPaneConfiguration = new GridBagConstraints();
		gbc_jScrollPaneConfiguration.insets = new Insets(5, 10, 0, 10);
		gbc_jScrollPaneConfiguration.gridwidth = 2;
		gbc_jScrollPaneConfiguration.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneConfiguration.gridx = 0;
		gbc_jScrollPaneConfiguration.gridy = 1;
		this.add(this.getJScrollPaneConfiguration(), gbc_jScrollPaneConfiguration);
	}
	
	private JPanel getJPanelLeft() {
		if (jPanelLeft == null) {
			jPanelLeft = new JPanel();
			jPanelLeft.setPreferredSize(new Dimension(200, 22));
			GridBagLayout gbl_jPanelLeft = new GridBagLayout();
			gbl_jPanelLeft.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelLeft.rowHeights = new int[]{0, 0};
			gbl_jPanelLeft.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelLeft.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelLeft.setLayout(gbl_jPanelLeft);
			GridBagConstraints gbc_jLabelSessionHandlerConfiguration = new GridBagConstraints();
			gbc_jLabelSessionHandlerConfiguration.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelSessionHandlerConfiguration.gridwidth = 2;
			gbc_jLabelSessionHandlerConfiguration.gridx = 0;
			gbc_jLabelSessionHandlerConfiguration.gridy = 0;
			jPanelLeft.add(getJLabelSessionHandlerConfiguration(), gbc_jLabelSessionHandlerConfiguration);
		}
		return jPanelLeft;
	}
	
	private JPanel getJPanelRight() {
		if (jPanelRight == null) {
			jPanelRight = new JPanel();
			jPanelRight.setPreferredSize(new Dimension(200, 22));
			
			GridBagLayout gbl_jPanelRight = new GridBagLayout();
			gbl_jPanelRight.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelRight.rowHeights = new int[]{0, 0};
			gbl_jPanelRight.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelRight.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelRight.setLayout(gbl_jPanelRight);
			GridBagConstraints gbc_jLabelActivated = new GridBagConstraints();
			gbc_jLabelActivated.gridx = 0;
			gbc_jLabelActivated.gridy = 0;
			jPanelRight.add(getJLabelActivated(), gbc_jLabelActivated);
			GridBagConstraints gbc_jCheckBoxActivated = new GridBagConstraints();
			gbc_jCheckBoxActivated.fill = GridBagConstraints.HORIZONTAL;
			gbc_jCheckBoxActivated.insets = new Insets(0, 5, 0, 0);
			gbc_jCheckBoxActivated.gridx = 1;
			gbc_jCheckBoxActivated.gridy = 0;
			jPanelRight.add(getJCheckBoxActivated(), gbc_jCheckBoxActivated);
		}
		return jPanelRight;
	}
	private JLabel getJLabelActivated() {
		if (jLabelActivated == null) {
			jLabelActivated = new JLabel("Activated:");
			jLabelActivated.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelActivated;
	}
	private JCheckBox getJCheckBoxActivated() {
		if (jCheckBoxActivated == null) {
			jCheckBoxActivated = new JCheckBox("");
			jCheckBoxActivated.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxActivated.addActionListener(this);
		}
		return jCheckBoxActivated;
	}
	
	private JLabel getJLabelSessionHandlerConfiguration() {
		if (jLabelSessionHandlerConfiguration == null) {
			jLabelSessionHandlerConfiguration = new JLabel("Session Handler Configuration:");
			jLabelSessionHandlerConfiguration.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSessionHandlerConfiguration;
	}
	private JScrollPane getJScrollPaneConfiguration() {
		if (jScrollPaneConfiguration == null) {
			jScrollPaneConfiguration = new JScrollPane();
			jScrollPaneConfiguration.setViewportView(this.getJTableSettingsServer());
		}
		return jScrollPaneConfiguration;
	}
	
	private JTableSettingsServer getJTableSettingsServer() {
		if (jTableSettingsServer==null) {
			jTableSettingsServer = new JTableSettingsServer();
		}
		return jTableSettingsServer;
	}
	
	
	// ----------------------------------------------------------------------------------
	// --- From here some context variables to answer the question where we are ---------
	// ----------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.JettyConfigurationInterface#setServerTreeNodeServer(de.enflexit.awb.ws.core.model.ServerTreeNodeServer)
	 */
	@Override
	public void setServerTreeNodeServer(ServerTreeNodeServer serverTreeNodeServer) {
		// --------------------------------------------------------------------
		// --- Always set with an visualization -------------------------------
		// --------------------------------------------------------------------
		this.serverTreeNodeServer = serverTreeNodeServer;
	}
	/**
	 * Returns the current server session settings.
	 * @return the security settings
	 */
	private JettySessionSettings getSessionSettings() {
		return this.serverTreeNodeServer.getJettyConfiguration().getSessionSettings();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.AbstractJPanelSettings#setDataModel(de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject)
	 */
	@Override
	public void setDataModel(ServerTreeNodeServerSession dataModel) {
		// --------------------------------------------------------------------
		// --- Only used with the server wide settings !!! --------------------
		// --------------------------------------------------------------------
		JettySessionSettings sSettings = this.getSessionSettings();
		this.getJCheckBoxActivated().setSelected(sSettings.isUseIndividualSettings());
		this.getJTableSettingsServer().setJettySettings(sSettings.getSettings());
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (this.isPauseActionListener==true) return;
		
		if (ae.getSource()==this.getJCheckBoxActivated()) {
			// --- Activated Session settings -----------------------
			this.getSessionSettings().setUseIndividualSettings(this.getJCheckBoxActivated().isSelected());
		} 
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.JettyConfigurationInterface#stopEditing()
	 */
	@Override
	public void stopEditing() {
		if (this.getJTableSettingsServer().isEditing()==true) {
			this.getJTableSettingsServer().getCellEditor().stopCellEditing();
		}
	}
	
}
