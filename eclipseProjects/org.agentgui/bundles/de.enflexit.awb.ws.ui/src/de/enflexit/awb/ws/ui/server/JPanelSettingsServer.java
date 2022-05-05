package de.enflexit.awb.ws.ui.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyConfiguration.StartOn;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;

/**
 * The Class JPanelSettingsServer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelSettingsServer extends JPanel implements JettyConfigurationInterface<ServerTreeNodeServer> {

	private static final long serialVersionUID = -4985161964727450005L;

	private ServerTreeNodeServer serverTreeNodeServer;

	
	private JLabel jLabelHeader;
	
	private JLabel jLabelStartOn;
	private JComboBox<StartOn> jComboBoxStartOn;
	private DefaultComboBoxModel<StartOn> comboBoxModel;
	
	private JLabel jLabelMutable;
	private JCheckBox jCheckBoxMutable;
	
	private JLabel jLabelCustomizer;
	private JCheckBox jCheckBoxCustomizer;

	private JLabel jLabelJettyConfiguration;
	private JScrollPane jScrollPaneJettyConfiguration;
	private JTableSettingsServer jTableSettingsServer;
	
	private JLabel jLabelState;
	private JLabel jLabelStateDescription;
	private JLabel jLabelSourceBundle;
	private JLabel jLabelSourceBundleDescription;
	private JLabel jLabelServiceClass;
	private JLabel jLabelServiceClassDescription;
	
	/**
	 * Instantiates a new JPanel for the server settings.
	 */
	public JPanelSettingsServer() {
		this.initialize();
	}
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 300, 0, 300, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.insets = new Insets(10, 10, 0, 10);
		gbc_jLabelHeader.gridwidth = 4;
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		this.add(getJLabelHeader(), gbc_jLabelHeader);
		
		GridBagConstraints gbc_jLabelStartOn = new GridBagConstraints();
		gbc_jLabelStartOn.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelStartOn.anchor = GridBagConstraints.WEST;
		gbc_jLabelStartOn.gridx = 0;
		gbc_jLabelStartOn.gridy = 1;
		this.add(getJLabelStartOn(), gbc_jLabelStartOn);
		
		GridBagConstraints gbc_jComboBoxStartOn = new GridBagConstraints();
		gbc_jComboBoxStartOn.anchor = GridBagConstraints.WEST;
		gbc_jComboBoxStartOn.insets = new Insets(10, 5, 0, 10);
		gbc_jComboBoxStartOn.gridx = 1;
		gbc_jComboBoxStartOn.gridy = 1;
		this.add(getJComboBoxStartOn(), gbc_jComboBoxStartOn);
		
		GridBagConstraints gbc_jLabelState = new GridBagConstraints();
		gbc_jLabelState.anchor = GridBagConstraints.WEST;
		gbc_jLabelState.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelState.gridx = 2;
		gbc_jLabelState.gridy = 1;
		this.add(getJLabelState(), gbc_jLabelState);
		
		GridBagConstraints gbc_jLabelStateDescription = new GridBagConstraints();
		gbc_jLabelStateDescription.anchor = GridBagConstraints.WEST;
		gbc_jLabelStateDescription.insets = new Insets(10, 5, 0, 10);
		gbc_jLabelStateDescription.gridx = 3;
		gbc_jLabelStateDescription.gridy = 1;
		this.add(getJLabelStateDescription(), gbc_jLabelStateDescription);
		
		GridBagConstraints gbc_jLabelMutable = new GridBagConstraints();
		gbc_jLabelMutable.anchor = GridBagConstraints.WEST;
		gbc_jLabelMutable.insets = new Insets(7, 10, 0, 0);
		gbc_jLabelMutable.gridx = 0;
		gbc_jLabelMutable.gridy = 2;
		this.add(getJLabelMutable(), gbc_jLabelMutable);
		
		GridBagConstraints gbc_jCheckBoxMutable = new GridBagConstraints();
		gbc_jCheckBoxMutable.fill = GridBagConstraints.HORIZONTAL;
		gbc_jCheckBoxMutable.insets = new Insets(7, 5, 0, 0);
		gbc_jCheckBoxMutable.gridx = 1;
		gbc_jCheckBoxMutable.gridy = 2;
		this.add(getJCheckBoxMutable(), gbc_jCheckBoxMutable);
		
		GridBagConstraints gbc_jLabelSourceBundle = new GridBagConstraints();
		gbc_jLabelSourceBundle.insets = new Insets(7, 10, 0, 0);
		gbc_jLabelSourceBundle.anchor = GridBagConstraints.WEST;
		gbc_jLabelSourceBundle.gridx = 2;
		gbc_jLabelSourceBundle.gridy = 2;
		this.add(getJLabelSourceBundle(), gbc_jLabelSourceBundle);

		GridBagConstraints gbc_jLabelSourceBundleDescription = new GridBagConstraints();
		gbc_jLabelSourceBundleDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelSourceBundleDescription.insets = new Insets(7, 5, 0, 10);
		gbc_jLabelSourceBundleDescription.gridx = 3;
		gbc_jLabelSourceBundleDescription.gridy = 2;
		this.add(getJLabelSourceBundleDescription(), gbc_jLabelSourceBundleDescription);
		
		GridBagConstraints gbc_jLabelCustomizer = new GridBagConstraints();
		gbc_jLabelCustomizer.anchor = GridBagConstraints.WEST;
		gbc_jLabelCustomizer.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelCustomizer.gridx = 0;
		gbc_jLabelCustomizer.gridy = 3;
		this.add(getJLabelCustomizer(), gbc_jLabelCustomizer);
		
		GridBagConstraints gbc_jCheckBoxCustomizer = new GridBagConstraints();
		gbc_jCheckBoxCustomizer.fill = GridBagConstraints.HORIZONTAL;
		gbc_jCheckBoxCustomizer.insets = new Insets(10, 5, 0, 0);
		gbc_jCheckBoxCustomizer.gridx = 1;
		gbc_jCheckBoxCustomizer.gridy = 3;
		this.add(getJCheckBoxCustomizer(), gbc_jCheckBoxCustomizer);
		
		GridBagConstraints gbc_jLabelServiceClass = new GridBagConstraints();
		gbc_jLabelServiceClass.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelServiceClass.gridx = 2;
		gbc_jLabelServiceClass.gridy = 3;
		this.add(getJLabelServiceClass(), gbc_jLabelServiceClass);
		
		GridBagConstraints gbc_jLabelServiceClassDescription = new GridBagConstraints();
		gbc_jLabelServiceClassDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelServiceClassDescription.insets = new Insets(10, 5, 0, 10);
		gbc_jLabelServiceClassDescription.gridx = 3;
		gbc_jLabelServiceClassDescription.gridy = 3;
		this.add(getJLabelServiceClassDescription(), gbc_jLabelServiceClassDescription);
		
		GridBagConstraints gbc_jLabelJettyConfiguration = new GridBagConstraints();
		gbc_jLabelJettyConfiguration.insets = new Insets(12, 10, 0, 0);
		gbc_jLabelJettyConfiguration.anchor = GridBagConstraints.WEST;
		gbc_jLabelJettyConfiguration.gridx = 0;
		gbc_jLabelJettyConfiguration.gridy = 4;
		this.add(getJLabelJettyConfiguration(), gbc_jLabelJettyConfiguration);
		
		GridBagConstraints gbc_jScrollPaneJettyConfiguration = new GridBagConstraints();
		gbc_jScrollPaneJettyConfiguration.insets = new Insets(5, 10, 0, 10);
		gbc_jScrollPaneJettyConfiguration.gridwidth = 5;
		gbc_jScrollPaneJettyConfiguration.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneJettyConfiguration.gridx = 0;
		gbc_jScrollPaneJettyConfiguration.gridy = 5;
		this.add(getJScrollPaneJettyConfiguration(), gbc_jScrollPaneJettyConfiguration);
	}
	
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Settings for Server");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelHeader;
	}
	private void setServerName(String serverName) {
		this.getJLabelHeader().setText("Settings for Server '" + serverName + "'");
	}
	
	private JLabel getJLabelStartOn() {
		if (jLabelStartOn == null) {
			jLabelStartOn = new JLabel("Start On:");
			jLabelStartOn.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelStartOn;
	}
	private JComboBox<StartOn> getJComboBoxStartOn() {
		if (jComboBoxStartOn == null) {
			jComboBoxStartOn = new JComboBox<>();
			jComboBoxStartOn.setModel(this.getComboBoxModel());
			jComboBoxStartOn.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxStartOn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					StartOn startOnNew = (StartOn) JPanelSettingsServer.this.getJComboBoxStartOn().getSelectedItem();
					JPanelSettingsServer.this.getJettyConfiguration().setStartOn(startOnNew);
				}
			});
		}
		return jComboBoxStartOn;
	}
	private DefaultComboBoxModel<StartOn> getComboBoxModel() {
		if (comboBoxModel==null) {
			comboBoxModel = new DefaultComboBoxModel<>();
			// --- Fill the model ---------------
			for (StartOn startOnValue : StartOn.values()) {
				comboBoxModel.addElement(startOnValue);
			}
		}
		return comboBoxModel;
	}
	
	private JLabel getJLabelState() {
		if (jLabelState == null) {
			jLabelState = new JLabel("State:");
			jLabelState.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelState;
	}
	private JLabel getJLabelStateDescription() {
		if (jLabelStateDescription == null) {
			jLabelStateDescription = new JLabel("Stopped");
			jLabelStateDescription.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelStateDescription;
	}
	private void setStateDescription(ServerTreeNodeServer serverNodeModel) {
		String display = serverNodeModel.getRunningServerDescription();
		Color color = null; 
		if (serverNodeModel.isRunningServer()==true) {
			color = new Color(0, 153, 0);
		} else {
			color = new Color(153, 0, 0);
		}
		this.getJLabelStateDescription().setText(display);
		this.getJLabelStateDescription().setForeground(color);
	}
	
	
	private JLabel getJLabelMutable() {
		if (jLabelMutable == null) {
			jLabelMutable = new JLabel("Mutable Handler Collection:");
			jLabelMutable.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelMutable;
	}
	private JCheckBox getJCheckBoxMutable() {
		if (jCheckBoxMutable == null) {
			jCheckBoxMutable = new JCheckBox("");
			jCheckBoxMutable.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxMutable.setEnabled(true);
			jCheckBoxMutable.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					boolean selected = JPanelSettingsServer.this.getJCheckBoxMutable().isSelected();
					JPanelSettingsServer.this.getJCheckBoxMutable().setSelected(!selected);
				}
			});
		}
		return jCheckBoxMutable;
	}
	
	private JLabel getJLabelSourceBundle() {
		if (jLabelSourceBundle == null) {
			jLabelSourceBundle = new JLabel("Bundle:");
			jLabelSourceBundle.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSourceBundle;
	}
	private JLabel getJLabelSourceBundleDescription() {
		if (jLabelSourceBundleDescription == null) {
			jLabelSourceBundleDescription = new JLabel("my.bundle.name");
			jLabelSourceBundleDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelSourceBundleDescription;
	}
	
	private JLabel getJLabelCustomizer() {
		if (jLabelCustomizer == null) {
			jLabelCustomizer = new JLabel("Uses Customizer:");
			jLabelCustomizer.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelCustomizer;
	}
	private JCheckBox getJCheckBoxCustomizer() {
		if (jCheckBoxCustomizer == null) {
			jCheckBoxCustomizer = new JCheckBox("");
			jCheckBoxCustomizer.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxCustomizer.setEnabled(true);
			jCheckBoxCustomizer.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					boolean selected = JPanelSettingsServer.this.getJCheckBoxCustomizer().isSelected();
					JPanelSettingsServer.this.getJCheckBoxCustomizer().setSelected(!selected);
				}
			});
		}
		return jCheckBoxCustomizer;
	}
	
	private JLabel getJLabelServiceClass() {
		if (jLabelServiceClass == null) {
			jLabelServiceClass = new JLabel("Service:");
			jLabelServiceClass.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelServiceClass;
	}
	private JLabel getJLabelServiceClassDescription() {
		if (jLabelServiceClassDescription == null) {
			jLabelServiceClassDescription = new JLabel("Class.Name");
			jLabelServiceClassDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelServiceClassDescription;
	}
	
	private JLabel getJLabelJettyConfiguration() {
		if (jLabelJettyConfiguration == null) {
			jLabelJettyConfiguration = new JLabel("Jetty Configuration:");
			jLabelJettyConfiguration.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelJettyConfiguration;
	}
	private JScrollPane getJScrollPaneJettyConfiguration() {
		if (jScrollPaneJettyConfiguration == null) {
			jScrollPaneJettyConfiguration = new JScrollPane();
			jScrollPaneJettyConfiguration.setViewportView(this.getJTableSettingsServer());
		}
		return jScrollPaneJettyConfiguration;
	}
	
	private JTableSettingsServer getJTableSettingsServer() {
		if (jTableSettingsServer==null) {
			jTableSettingsServer = new JTableSettingsServer();
		}
		return jTableSettingsServer;
	}
	
	
	private void setServerTreeNodeServer(ServerTreeNodeServer serverTreeNodeServer) {
		this.serverTreeNodeServer = serverTreeNodeServer;
	}
	private JettyConfiguration getJettyConfiguration() {
		return this.serverTreeNodeServer.getJettyConfiguration();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.AbstractJPanelSettings#setDataModel(de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject)
	 */
	@Override
	public void setDataModel(ServerTreeNodeServer serverNodeModel) {

		// --- Remind current setting -------------------------------
		this.setServerTreeNodeServer(serverNodeModel);

		// --- Get information from ServerTreeNodeServer ------------
		this.setStateDescription(serverNodeModel);
		this.getJLabelSourceBundleDescription().setText(serverNodeModel.getSourceBundle().getSymbolicName());
		this.getJLabelServiceClassDescription().setText("Class: " + serverNodeModel.getServiceClassName());
		
		// --- Get working copy of JettyConfiguration --------------- 
		this.setServerName(this.getJettyConfiguration().getServerName());
		
		this.getJComboBoxStartOn().setSelectedItem(this.getJettyConfiguration().getStartOn());
		this.getJCheckBoxMutable().setSelected(this.getJettyConfiguration().isMutableHandlerCollection());

		boolean usesCustomizer = (this.getJettyConfiguration().getJettyCustomizer()!=null);
		String customizerClass = usesCustomizer==true ? "Class: " + this.getJettyConfiguration().getJettyCustomizer().getClass().getName() : "";
		this.getJCheckBoxCustomizer().setSelected(usesCustomizer);
		this.getJCheckBoxCustomizer().setText(customizerClass);

		this.getJTableSettingsServer().setJettyConfiguration(this.getJettyConfiguration());
	}
	
}
