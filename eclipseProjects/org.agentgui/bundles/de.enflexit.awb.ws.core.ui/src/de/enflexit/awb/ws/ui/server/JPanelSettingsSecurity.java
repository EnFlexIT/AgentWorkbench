package de.enflexit.awb.ws.ui.server;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import de.enflexit.awb.ws.AwbSecurityHandlerService;
import de.enflexit.awb.ws.core.JettySecuritySettings;
import de.enflexit.awb.ws.core.SecurtiyConfiguration;
import de.enflexit.awb.ws.core.model.ServerTreeNodeHandler;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServerSecurity;
import de.enflexit.awb.ws.core.security.SecurityHandlerService;

/**
 * The Class JPanelSettingsHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelSettingsSecurity extends JPanel implements JettyConfigurationInterface<ServerTreeNodeServerSecurity>, ActionListener {

	private static final long serialVersionUID = -4985161964727450005L;

	private ServerTreeNodeServer serverTreeNodeServer; 					// --- Always set and thus available --------
	private ServerTreeNodeServerSecurity serverTreeNodeServerSecurity; 	// --- Used only for server wide settings ---
	private ServerTreeNodeHandler serverTreeNodeHandler;				// --- Used only for handler settings -------
	
	private SecurtiyConfiguration securityConfiguration;
	
	private JLabel jLabelSecurityHandler;
	private DefaultComboBoxModel<String>  comboBoxModelSecurityHandler;
	private JComboBox<String> jComboBoxSecurityHandler;
	private JLabel jLabelActivated;
	private JCheckBox jCheckBoxActivated;
	
	private JLabel jLabelSecurityHandlerConfiguration;
	private JScrollPane scrollPane;
	private DefaultTableModel tableModelConfiguration;
	private JTable jTableConfiguration;

	
	/**
	 * Instantiates a new JPanel for the server settings.
	 */
	public JPanelSettingsSecurity() {
		this.initialize();
	}
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 200, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelSecurityHandler = new GridBagConstraints();
		gbc_jLabelSecurityHandler.anchor = GridBagConstraints.WEST;
		gbc_jLabelSecurityHandler.insets = new Insets(5, 10, 0, 0);
		gbc_jLabelSecurityHandler.gridx = 0;
		gbc_jLabelSecurityHandler.gridy = 0;
		this.add(getJLabelSecurityHandler(), gbc_jLabelSecurityHandler);
		
		GridBagConstraints gbc_jComboBoxSecurityHandler = new GridBagConstraints();
		gbc_jComboBoxSecurityHandler.insets = new Insets(5, 5, 0, 0);
		gbc_jComboBoxSecurityHandler.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxSecurityHandler.gridx = 1;
		gbc_jComboBoxSecurityHandler.gridy = 0;
		this.add(getJComboBoxSecurityHandler(), gbc_jComboBoxSecurityHandler);
		
		GridBagConstraints gbc_jLabelActivated = new GridBagConstraints();
		gbc_jLabelActivated.anchor = GridBagConstraints.WEST;
		gbc_jLabelActivated.insets = new Insets(5, 10, 0, 0);
		gbc_jLabelActivated.gridx = 2;
		gbc_jLabelActivated.gridy = 0;
		this.add(getJLabelActivated(), gbc_jLabelActivated);
		
		GridBagConstraints gbc_jComboBoxActivated = new GridBagConstraints();
		gbc_jComboBoxActivated.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxActivated.insets = new Insets(5, 5, 0, 10);
		gbc_jComboBoxActivated.gridx = 3;
		gbc_jComboBoxActivated.gridy = 0;
		this.add(getJCheckBoxActivated(), gbc_jComboBoxActivated);
		
		GridBagConstraints gbc_jLabelSecurityHandlerConfiguration = new GridBagConstraints();
		gbc_jLabelSecurityHandlerConfiguration.gridwidth = 2;
		gbc_jLabelSecurityHandlerConfiguration.anchor = GridBagConstraints.WEST;
		gbc_jLabelSecurityHandlerConfiguration.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelSecurityHandlerConfiguration.gridx = 0;
		gbc_jLabelSecurityHandlerConfiguration.gridy = 1;
		this.add(getJLabelSecurityHandlerConfiguration(), gbc_jLabelSecurityHandlerConfiguration);
		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(5, 10, 0, 10);
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		this.add(getScrollPane(), gbc_scrollPane);
	}
	
	
	private JLabel getJLabelSecurityHandler() {
		if (jLabelSecurityHandler == null) {
			jLabelSecurityHandler = new JLabel("Security Handler:");
			jLabelSecurityHandler.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSecurityHandler;
	}
	
	private DefaultComboBoxModel<String> getComboBoxModelSecurityHandler() {
		if (comboBoxModelSecurityHandler==null) {
			// --- Prepare data vector ------------------------------
			Vector<String> securityHandlerNameVector = new Vector<>();
			securityHandlerNameVector.add(SecurityHandlerService.NO_SECURITY_HANDLER_INDICATOR);
			SecurityHandlerService.getAwbSecurityHandlerServiceListSorted().forEach((AwbSecurityHandlerService shService) -> securityHandlerNameVector.add(shService.getSecurityHandlerName()));
			comboBoxModelSecurityHandler = new DefaultComboBoxModel<String>(securityHandlerNameVector);
			
		}
		return comboBoxModelSecurityHandler;
	}
	private JComboBox<String> getJComboBoxSecurityHandler() {
		if (jComboBoxSecurityHandler == null) {
			jComboBoxSecurityHandler = new JComboBox<>(this.getComboBoxModelSecurityHandler());
			jComboBoxSecurityHandler.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxSecurityHandler.setPreferredSize(new Dimension(200, 26));
			jComboBoxSecurityHandler.addActionListener(this);
		}
		return jComboBoxSecurityHandler;
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
	
	private JLabel getJLabelSecurityHandlerConfiguration() {
		if (jLabelSecurityHandlerConfiguration == null) {
			jLabelSecurityHandlerConfiguration = new JLabel("Security Handler Configuration:");
			jLabelSecurityHandlerConfiguration.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSecurityHandlerConfiguration;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getJTableConfiguration());
		}
		return scrollPane;
	}
	
	private DefaultTableModel getTableModelConfiguration() {
		if (tableModelConfiguration==null) {
			
			Vector<String> tableHeader = new Vector<>();
			tableHeader.add("Parameter");
			tableHeader.add("Value");
			
			tableModelConfiguration = new DefaultTableModel(null, tableHeader) {
				private static final long serialVersionUID = 8247294167382441521L;
				@Override
				public boolean isCellEditable(int row, int column) {
					if (column==0) return false;
					return true;
				}
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					switch (columnIndex) {
					case 0:
					case 1:
						return String.class;
					}
					return null;
				}
			};
		}
		return tableModelConfiguration;
	}
	private JTable getJTableConfiguration() {
		if (jTableConfiguration == null) {
			jTableConfiguration = new JTable(this.getTableModelConfiguration());
			jTableConfiguration.setFillsViewportHeight(true);
			jTableConfiguration.setShowGrid(false);
			jTableConfiguration.setRowHeight(20);
			jTableConfiguration.setFont(new Font("Dialog", Font.PLAIN, 12));
			
			jTableConfiguration.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableConfiguration.getTableHeader().setReorderingAllowed(false);
			jTableConfiguration.setAutoCreateRowSorter(true);
			
		}
		return jTableConfiguration;
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
	 * Returns the current server security settings.
	 * @return the security settings
	 */
	private JettySecuritySettings getSecuritySettings() {
		return this.serverTreeNodeServer.getJettyConfiguration().getSecuritySettings();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.AbstractJPanelSettings#setDataModel(de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject)
	 */
	@Override
	public void setDataModel(ServerTreeNodeServerSecurity dataModel) {
		// --------------------------------------------------------------------
		// --- Only used with the server wide settings !!! --------------------
		// --------------------------------------------------------------------
		this.serverTreeNodeServerSecurity = dataModel;
		this.serverTreeNodeHandler = null;
		
		this.setSecurityConfiguration(this.getSecuritySettings().getSecurityConfiguration(this.getServletHandlerID()));
	}
	/**
	 * Sets the server tree node handler.
	 * @param stnHandler the new server tree node handler
	 */
	public void setServerTreeNodeHandler(ServerTreeNodeHandler stnHandler) {
		// --------------------------------------------------------------------
		// --- Only used with a single handler !!! ----------------------------
		// --------------------------------------------------------------------
		this.serverTreeNodeHandler = stnHandler;
		this.serverTreeNodeServerSecurity = null;
		
		// --- Check if a service is defined with the current handler ---------
		if (this.serverTreeNodeHandler.getAwbWebHandlerService()==null) {
			// --- No Service, no security settings required ------------------
			this.setVisible(false);
			
		} else {
			// --- Get the current security settings -------------------------- 
			this.setVisible(true);
			this.setSecurityConfiguration(this.getSecuritySettings().getSecurityConfiguration(this.getServletHandlerID()));
		}
	}
	
	/**
	 * Returns the servlet handler ID for the security settings.
	 * @return the servlet handler ID
	 */
	private String getServletHandlerID() {
		if (this.serverTreeNodeServerSecurity!=null) {
			return JettySecuritySettings.ID_SERVER_SECURITY;
		}
		return this.serverTreeNodeHandler.getServiceClassName();
	}

	// ----------------------------------------------------------------------------------
	// --- From here, method to work on the current SecurtiyConfiguration ---------------
	// ----------------------------------------------------------------------------------
	/**
	 * Sets the security configuration to work on.
	 * @param securtiyConfiguration the security configuration
	 */
	private void setSecurityConfiguration(SecurtiyConfiguration securtiyConfiguration) {
		
		this.securityConfiguration = securtiyConfiguration;
		if (this.securityConfiguration==null) {
			this.getComboBoxModelSecurityHandler().setSelectedItem(SecurityHandlerService.NO_SECURITY_HANDLER_INDICATOR);
			this.getJCheckBoxActivated().setSelected(false);
			// --- TODO ---
			
		} else {
			this.getComboBoxModelSecurityHandler().setSelectedItem(this.securityConfiguration.getSecurityHandlerName());
			this.getJCheckBoxActivated().setSelected(this.securityConfiguration.isSecurityHandlerActivated());
			// --- TODO ---
			// TODO fill table
		}
		
	}
	/**
	 * Return the security configuration for the current node.
	 * @return the security configuration
	 */
	private SecurtiyConfiguration getSecurityConfiguration() {
		if (securityConfiguration==null) {
			securityConfiguration = new SecurtiyConfiguration();
			// --- Save to JettyConfiguration ----
			this.getSecuritySettings().setSecurityConfiguration(this.getServletHandlerID(), this.securityConfiguration);
		}
		return this.securityConfiguration;
	}

	/**
	 * Sets the security handler.
	 * @param securityHandlerName the new security handler
	 */
	private void setSecurityHandler(String securityHandlerName) {
		
		// --- Get the Security Handler Service, if available ---------------------------
		AwbSecurityHandlerService shService = SecurityHandlerService.getAwbSecurityHandlerService(securityHandlerName);
		if (shService==null) {
			// --- Remove the currently configured security handler ---------------------
			this.getSecuritySettings().removeSecurityConfiguration(this.getServletHandlerID());
			this.setSecurityConfiguration(null);
			
		} else {
			String oldSecurityHandlerName = this.getSecurityConfiguration().getSecurityHandlerName(); 
			if (oldSecurityHandlerName==null || oldSecurityHandlerName.equals(securityHandlerName)==false) {
				// --- Configure current SecurityConfiguration --------------------------
				this.getSecurityConfiguration().setSecurityHandlerName(securityHandlerName);
				this.getSecurityConfiguration().setServletHandlerID(this.getServletHandlerID());
				this.getSecurityConfiguration().getSecurityHandlerConfiguration().clear();
				for (String configKey : shService.getConfigurationKeys()) {
					this.getSecurityConfiguration().getSecurityHandlerConfiguration().put(configKey, null);
				}
				// --- Update visualization ---------------------------------------------
				this.setSecurityConfiguration(this.getSecurityConfiguration());
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJComboBoxSecurityHandler()) {
			// --- Set the selected security handler to visualization ---------
			this.setSecurityHandler((String)this.getJComboBoxSecurityHandler().getSelectedItem());
			
		} else if (ae.getSource()==this.getJCheckBoxActivated()) {
			// --- Activate / deactivate current security handler -------------
			this.getSecurityConfiguration().setSecurityHandlerActivated(this.getJCheckBoxActivated().isSelected());
		}
		
	}
	
}
