package de.enflexit.awb.ws.ui.server;

import java.awt.Color;
import java.awt.Dimension;
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
	private JPanel jPanelLeft;
	private JPanel jPanelRight;
	
	/**
	 * Instantiates a new JPanel for the server settings.
	 */
	public JPanelSettingsServer() {
		this.initialize();
	}
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jPanelLeft = new GridBagConstraints();
		gbc_jPanelLeft.insets = new Insets(0, 10, 0, 5);
		gbc_jPanelLeft.fill = GridBagConstraints.BOTH;
		gbc_jPanelLeft.gridx = 0;
		gbc_jPanelLeft.gridy = 0;
		this.add(this.getJPanelLeft(), gbc_jPanelLeft);
		
		GridBagConstraints gbc_jPanelRight = new GridBagConstraints();
		gbc_jPanelRight.insets = new Insets(0, 5, 0, 10);
		gbc_jPanelRight.fill = GridBagConstraints.BOTH;
		gbc_jPanelRight.gridx = 1;
		gbc_jPanelRight.gridy = 0;
		this.add(this.getJPanelRight(), gbc_jPanelRight);
		
		GridBagConstraints gbc_jLabelJettyConfiguration = new GridBagConstraints();
		gbc_jLabelJettyConfiguration.insets = new Insets(12, 10, 0, 0);
		gbc_jLabelJettyConfiguration.anchor = GridBagConstraints.WEST;
		gbc_jLabelJettyConfiguration.gridx = 0;
		gbc_jLabelJettyConfiguration.gridy = 1;
		this.add(this.getJLabelJettyConfiguration(), gbc_jLabelJettyConfiguration);
		
		GridBagConstraints gbc_jScrollPaneJettyConfiguration = new GridBagConstraints();
		gbc_jScrollPaneJettyConfiguration.insets = new Insets(5, 10, 0, 10);
		gbc_jScrollPaneJettyConfiguration.gridwidth = 2;
		gbc_jScrollPaneJettyConfiguration.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneJettyConfiguration.gridx = 0;
		gbc_jScrollPaneJettyConfiguration.gridy = 2;
		this.add(this.getJScrollPaneJettyConfiguration(), gbc_jScrollPaneJettyConfiguration);
	}
	
	private JPanel getJPanelLeft() {
		if (jPanelLeft == null) {
			jPanelLeft = new JPanel();
			jPanelLeft.setPreferredSize(new Dimension(200, 80));
			
			GridBagLayout gbl_jPanelLeft = new GridBagLayout();
			gbl_jPanelLeft.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelLeft.rowHeights = new int[]{0, 0, 0, 0};
			gbl_jPanelLeft.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelLeft.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			jPanelLeft.setLayout(gbl_jPanelLeft);
			GridBagConstraints gbc_jLabelStartOn = new GridBagConstraints();
			gbc_jLabelStartOn.anchor = GridBagConstraints.WEST;
			gbc_jLabelStartOn.gridx = 0;
			gbc_jLabelStartOn.gridy = 0;
			jPanelLeft.add(getJLabelStartOn(), gbc_jLabelStartOn);
			GridBagConstraints gbc_jComboBoxStartOn = new GridBagConstraints();
			gbc_jComboBoxStartOn.insets = new Insets(0, 5, 0, 0);
			gbc_jComboBoxStartOn.anchor = GridBagConstraints.WEST;
			gbc_jComboBoxStartOn.gridx = 1;
			gbc_jComboBoxStartOn.gridy = 0;
			jPanelLeft.add(getJComboBoxStartOn(), gbc_jComboBoxStartOn);
			GridBagConstraints gbc_jLabelMutable = new GridBagConstraints();
			gbc_jLabelMutable.insets = new Insets(6, 0, 0, 0);
			gbc_jLabelMutable.anchor = GridBagConstraints.WEST;
			gbc_jLabelMutable.gridx = 0;
			gbc_jLabelMutable.gridy = 1;
			jPanelLeft.add(getJLabelMutable(), gbc_jLabelMutable);
			GridBagConstraints gbc_jCheckBoxMutable = new GridBagConstraints();
			gbc_jCheckBoxMutable.insets = new Insets(6, 5, 0, 0);
			gbc_jCheckBoxMutable.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxMutable.gridx = 1;
			gbc_jCheckBoxMutable.gridy = 1;
			jPanelLeft.add(getJCheckBoxMutable(), gbc_jCheckBoxMutable);
			GridBagConstraints gbc_jLabelCustomizer = new GridBagConstraints();
			gbc_jLabelCustomizer.insets = new Insets(9, 0, 0, 0);
			gbc_jLabelCustomizer.anchor = GridBagConstraints.WEST;
			gbc_jLabelCustomizer.gridx = 0;
			gbc_jLabelCustomizer.gridy = 2;
			jPanelLeft.add(getJLabelCustomizer(), gbc_jLabelCustomizer);
			GridBagConstraints gbc_jCheckBoxCustomizer = new GridBagConstraints();
			gbc_jCheckBoxCustomizer.insets = new Insets(9, 5, 0, 0);
			gbc_jCheckBoxCustomizer.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxCustomizer.gridx = 1;
			gbc_jCheckBoxCustomizer.gridy = 2;
			jPanelLeft.add(getJCheckBoxCustomizer(), gbc_jCheckBoxCustomizer);
		}
		return jPanelLeft;
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
	
	private JPanel getJPanelRight() {
		if (jPanelRight == null) {
			jPanelRight = new JPanel();
			jPanelRight.setPreferredSize(new Dimension(200, 80));
			
			GridBagLayout gbl_jPanelRight = new GridBagLayout();
			gbl_jPanelRight.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelRight.rowHeights = new int[]{0, 0, 0, 0};
			gbl_jPanelRight.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelRight.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			jPanelRight.setLayout(gbl_jPanelRight);
			GridBagConstraints gbc_jLabelState = new GridBagConstraints();
			gbc_jLabelState.insets = new Insets(3, 0, 0, 0);
			gbc_jLabelState.anchor = GridBagConstraints.WEST;
			gbc_jLabelState.gridx = 0;
			gbc_jLabelState.gridy = 0;
			jPanelRight.add(getJLabelState(), gbc_jLabelState);
			GridBagConstraints gbc_jLabelStateDescription = new GridBagConstraints();
			gbc_jLabelStateDescription.insets = new Insets(3, 0, 0, 0);
			gbc_jLabelStateDescription.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelStateDescription.gridx = 1;
			gbc_jLabelStateDescription.gridy = 0;
			jPanelRight.add(getJLabelStateDescription(), gbc_jLabelStateDescription);
			GridBagConstraints gbc_jLabelSourceBundle = new GridBagConstraints();
			gbc_jLabelSourceBundle.insets = new Insets(12, 0, 0, 0);
			gbc_jLabelSourceBundle.anchor = GridBagConstraints.WEST;
			gbc_jLabelSourceBundle.gridx = 0;
			gbc_jLabelSourceBundle.gridy = 1;
			jPanelRight.add(getJLabelSourceBundle(), gbc_jLabelSourceBundle);
			GridBagConstraints gbc_jLabelSourceBundleDescription = new GridBagConstraints();
			gbc_jLabelSourceBundleDescription.anchor = GridBagConstraints.WEST;
			gbc_jLabelSourceBundleDescription.insets = new Insets(12, 5, 0, 0);
			gbc_jLabelSourceBundleDescription.gridx = 1;
			gbc_jLabelSourceBundleDescription.gridy = 1;
			jPanelRight.add(getJLabelSourceBundleDescription(), gbc_jLabelSourceBundleDescription);
			GridBagConstraints gbc_jLabelServiceClass = new GridBagConstraints();
			gbc_jLabelServiceClass.insets = new Insets(12, 0, 0, 0);
			gbc_jLabelServiceClass.anchor = GridBagConstraints.WEST;
			gbc_jLabelServiceClass.gridx = 0;
			gbc_jLabelServiceClass.gridy = 2;
			jPanelRight.add(getJLabelServiceClass(), gbc_jLabelServiceClass);
			GridBagConstraints gbc_jLabelServiceClassDescription = new GridBagConstraints();
			gbc_jLabelServiceClassDescription.anchor = GridBagConstraints.WEST;
			gbc_jLabelServiceClassDescription.insets = new Insets(12, 5, 0, 0);
			gbc_jLabelServiceClassDescription.gridx = 1;
			gbc_jLabelServiceClassDescription.gridy = 2;
			jPanelRight.add(getJLabelServiceClassDescription(), gbc_jLabelServiceClassDescription);
		}
		return jPanelRight;
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
			jLabelMutable = new JLabel("Handler Collection:");
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
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.JettyConfigurationInterface#setServerTreeNodeServer(de.enflexit.awb.ws.core.model.ServerTreeNodeServer)
	 */
	@Override
	public void setServerTreeNodeServer(ServerTreeNodeServer serverTreeNodeServer) {
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
		this.getJComboBoxStartOn().setSelectedItem(this.getJettyConfiguration().getStartOn());
		this.getJCheckBoxMutable().setSelected(this.getJettyConfiguration().isMutableHandlerCollection());

		boolean usesCustomizer = (this.getJettyConfiguration().getJettyCustomizer()!=null);
		String customizerClass = usesCustomizer==true ? "Class: " + this.getJettyConfiguration().getJettyCustomizer().getClass().getName() : "";
		this.getJCheckBoxCustomizer().setSelected(usesCustomizer);
		this.getJCheckBoxCustomizer().setText(customizerClass);

		this.getJTableSettingsServer().setJettyConfiguration(this.getJettyConfiguration());
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
