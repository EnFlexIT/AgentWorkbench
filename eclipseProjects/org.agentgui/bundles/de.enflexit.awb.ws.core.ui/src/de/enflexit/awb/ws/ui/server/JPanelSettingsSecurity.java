package de.enflexit.awb.ws.ui.server;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
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

import org.apache.commons.lang3.StringUtils;

import de.enflexit.awb.ws.AwbSecurityHandlerService;
import de.enflexit.awb.ws.core.JettySecuritySettings;
import de.enflexit.awb.ws.core.ServletSecurityConfiguration;
import de.enflexit.awb.ws.core.model.ServerTreeNodeHandler;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServerSecurity;
import de.enflexit.awb.ws.core.security.SecurityHandlerService;
import de.enflexit.common.swing.TableCellListener;

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
	
	private ServletSecurityConfiguration servletSecurityConfiguration;
	private boolean isPauseActionListener;
	
	private JLabel jLabelSecurityHandler;
	private DefaultComboBoxModel<String>  comboBoxModelSecurityHandler;
	private JComboBox<String> jComboBoxSecurityHandler;
	private JLabel jLabelActivated;
	private JCheckBox jCheckBoxActivated;
	
	private JLabel jLabelSecurityHandlerConfiguration;
	private JScrollPane jScrollPaneConfiguration;
	private DefaultTableModel tableModelConfiguration;
	private JTable jTableConfiguration;
	private JPanel jPanelLeft;
	private JPanel jPanelRight;

	
	/**
	 * Instantiates a new JPanel for the server settings.
	 */
	public JPanelSettingsSecurity() {
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
			jPanelLeft.setPreferredSize(new Dimension(200, 50));
			GridBagLayout gbl_jPanelLeft = new GridBagLayout();
			gbl_jPanelLeft.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelLeft.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelLeft.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelLeft.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			jPanelLeft.setLayout(gbl_jPanelLeft);
			GridBagConstraints gbc_jLabelSecurityHandler = new GridBagConstraints();
			gbc_jLabelSecurityHandler.anchor = GridBagConstraints.WEST;
			gbc_jLabelSecurityHandler.gridx = 0;
			gbc_jLabelSecurityHandler.gridy = 0;
			jPanelLeft.add(getJLabelSecurityHandler(), gbc_jLabelSecurityHandler);
			GridBagConstraints gbc_jComboBoxSecurityHandler = new GridBagConstraints();
			gbc_jComboBoxSecurityHandler.fill = GridBagConstraints.HORIZONTAL;
			gbc_jComboBoxSecurityHandler.insets = new Insets(0, 5, 0, 0);
			gbc_jComboBoxSecurityHandler.gridx = 1;
			gbc_jComboBoxSecurityHandler.gridy = 0;
			jPanelLeft.add(getJComboBoxSecurityHandler(), gbc_jComboBoxSecurityHandler);
			GridBagConstraints gbc_jLabelSecurityHandlerConfiguration = new GridBagConstraints();
			gbc_jLabelSecurityHandlerConfiguration.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelSecurityHandlerConfiguration.gridwidth = 2;
			gbc_jLabelSecurityHandlerConfiguration.insets = new Insets(10, 0, 0, 0);
			gbc_jLabelSecurityHandlerConfiguration.gridx = 0;
			gbc_jLabelSecurityHandlerConfiguration.gridy = 1;
			jPanelLeft.add(getJLabelSecurityHandlerConfiguration(), gbc_jLabelSecurityHandlerConfiguration);
		}
		return jPanelLeft;
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
			securityHandlerNameVector.add(JettySecuritySettings.ID_NO_SECURITY_HANDLER);
			SecurityHandlerService.getAwbSecurityHandlerServiceListSorted().forEach((AwbSecurityHandlerService shService) -> securityHandlerNameVector.add(shService.getSecurityHandlerName()));
			comboBoxModelSecurityHandler = new DefaultComboBoxModel<String>(securityHandlerNameVector);
			
		}
		return comboBoxModelSecurityHandler;
	}
	private JComboBox<String> getJComboBoxSecurityHandler() {
		if (jComboBoxSecurityHandler == null) {
			jComboBoxSecurityHandler = new JComboBox<>(this.getComboBoxModelSecurityHandler());
			jComboBoxSecurityHandler.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxSecurityHandler.addActionListener(this);
		}
		return jComboBoxSecurityHandler;
	}
	
	private JPanel getJPanelRight() {
		if (jPanelRight == null) {
			jPanelRight = new JPanel();
			jPanelRight.setPreferredSize(new Dimension(200, 50));
			
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
	
	private JLabel getJLabelSecurityHandlerConfiguration() {
		if (jLabelSecurityHandlerConfiguration == null) {
			jLabelSecurityHandlerConfiguration = new JLabel("Security Handler Configuration:");
			jLabelSecurityHandlerConfiguration.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSecurityHandlerConfiguration;
	}
	private JScrollPane getJScrollPaneConfiguration() {
		if (jScrollPaneConfiguration == null) {
			jScrollPaneConfiguration = new JScrollPane();
			jScrollPaneConfiguration.setViewportView(getJTableConfiguration());
		}
		return jScrollPaneConfiguration;
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
			// --- Create TableCellListener ---------------
			new TableCellListener(jTableConfiguration, this);
		}
		return jTableConfiguration;
	}
	
	/**
	 * Fills the table model.
	 */
	private void fillTableModel(TreeMap<String, String> secHandlerConfig) {

		// --- Clear table model --------------------------
		this.stopEditing();
		this.getTableModelConfiguration().setRowCount(0);
		
		// --- Early exit ? -------------------------------
		if (secHandlerConfig==null || secHandlerConfig.size()==0) return;
		
		// --- Fill each list element into table model ----
		List<String> configKeys = new ArrayList<>(secHandlerConfig.keySet());
		for (String configKey : configKeys) {
			// --- Add data model row ---------------------
			Vector<Object> row = new Vector<>();
			row.add(configKey);
			row.add(secHandlerConfig.get(configKey));
			this.getTableModelConfiguration().addRow(row);
		}
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
		
		this.setSecurityConfiguration(this.getSecuritySettings().getSecurityConfiguration(this.getServletContextPath()));
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
			this.setSecurityConfiguration(null);
			
		} else {
			// --- Get the current security settings -------------------------- 
			this.setVisible(true);
			this.setSecurityConfiguration(this.getSecuritySettings().getSecurityConfiguration(this.getServletContextPath()));
		}
	}
	
	/**
	 * Returns the servlet context path for the security settings.
	 * @return the servlet context path
	 */
	private String getServletContextPath() {
		if (this.serverTreeNodeServerSecurity!=null) {
			return JettySecuritySettings.ID_SERVER_SECURITY;
		}
		return this.serverTreeNodeHandler.getContextPath();
	}

	// ----------------------------------------------------------------------------------
	// --- From here, method to work on the current ServletSecurityConfiguration ---------------
	// ----------------------------------------------------------------------------------
	/**
	 * Sets the security configuration to work on.
	 * @param servletSecurityConfiguration the security configuration
	 */
	private void setSecurityConfiguration(ServletSecurityConfiguration servletSecurityConfiguration) {
		
		this.servletSecurityConfiguration = servletSecurityConfiguration;
		this.isPauseActionListener = true;
		if (this.servletSecurityConfiguration==null) {
			this.getComboBoxModelSecurityHandler().setSelectedItem(JettySecuritySettings.ID_NO_SECURITY_HANDLER);
			this.getJCheckBoxActivated().setSelected(false);
			this.fillTableModel(null);
		} else {
			this.getComboBoxModelSecurityHandler().setSelectedItem(this.servletSecurityConfiguration.getSecurityHandlerName());
			this.getJCheckBoxActivated().setSelected(this.servletSecurityConfiguration.isSecurityHandlerActivated());
			this.fillTableModel(this.servletSecurityConfiguration.getSecurityHandlerConfiguration());
		}
		this.isPauseActionListener = false;
	}
	/**
	 * Return the security configuration for the current node.
	 * @return the security configuration
	 */
	private ServletSecurityConfiguration getSecurityConfiguration() {
		if (servletSecurityConfiguration==null) {
			servletSecurityConfiguration = new ServletSecurityConfiguration();
			// --- Save to JettyConfiguration ----
			this.getSecuritySettings().setSecurityConfiguration(this.getServletContextPath(), this.servletSecurityConfiguration);
		}
		return this.servletSecurityConfiguration;
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
			this.getSecuritySettings().removeSecurityConfiguration(this.getServletContextPath());
			this.setSecurityConfiguration(null);
			
		} else {
			String oldSecurityHandlerName = this.getSecurityConfiguration().getSecurityHandlerName(); 
			if (oldSecurityHandlerName==null || oldSecurityHandlerName.equals(securityHandlerName)==false) {
				// --- Configure current ServletSecurityConfiguration --------------------------
				this.getSecurityConfiguration().setContextPath(this.getServletContextPath());
				this.getSecurityConfiguration().setSecurityHandlerName(securityHandlerName);
				this.getSecurityConfiguration().setSecurityHandlerActivated(true);
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
		
		if (this.isPauseActionListener==true) return;
		
		if (ae.getSource()==this.getJComboBoxSecurityHandler()) {
			// --- Set the selected security handler to visualization ---------
			this.setSecurityHandler((String)this.getJComboBoxSecurityHandler().getSelectedItem());
			
		} else if (ae.getSource()==this.getJCheckBoxActivated()) {
			// --- Activate / deactivate current security handler -------------
			this.getSecurityConfiguration().setSecurityHandlerActivated(this.getJCheckBoxActivated().isSelected());
		
		} else if (ae.getSource() instanceof TableCellListener) {
			// --- React on table cell changes --------------------------------
			TableCellListener tcl = (TableCellListener) ae.getSource();
			int row = tcl.getRow();
			String oldValue = (String) tcl.getOldValue();
			String newValue = (String) tcl.getNewValue();
			if (newValue!=null && newValue.isBlank()==true) newValue = null;
			
			if (StringUtils.equals(newValue, oldValue)==false) {
				// --- Adjust in security configuration -----------------------
				String configKey = (String) this.getJTableConfiguration().getValueAt(row, 0);
				this.getSecurityConfiguration().getSecurityHandlerConfiguration().put(configKey, newValue);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.JettyConfigurationInterface#stopEditing()
	 */
	@Override
	public void stopEditing() {
		if (this.getJTableConfiguration().isEditing()==true) {
			this.getJTableConfiguration().getCellEditor().stopCellEditing();
		}
	}
	
}
