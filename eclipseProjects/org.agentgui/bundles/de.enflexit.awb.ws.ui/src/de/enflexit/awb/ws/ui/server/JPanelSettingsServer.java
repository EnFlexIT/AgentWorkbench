package de.enflexit.awb.ws.ui.server;

import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyConfiguration.StartOn;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;

/**
 * The Class JPanelSettingsServer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelSettingsServer extends AbstractJPanelSettings<ServerTreeNodeServer> {

	private static final long serialVersionUID = -4985161964727450005L;

	private ServerTreeNodeServer serverTreeNodeServer;
	private boolean unsaved;
	
	
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
	
	/**
	 * Instantiates a new JPanel for the server settings.
	 */
	public JPanelSettingsServer() {
		this.initialize();
	}
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.insets = new Insets(10, 10, 0, 10);
		gbc_jLabelHeader.gridwidth = 2;
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
		
		GridBagConstraints gbc_jLabelMutable = new GridBagConstraints();
		gbc_jLabelMutable.anchor = GridBagConstraints.WEST;
		gbc_jLabelMutable.insets = new Insets(7, 10, 0, 0);
		gbc_jLabelMutable.gridx = 0;
		gbc_jLabelMutable.gridy = 2;
		this.add(getJLabelMutable(), gbc_jLabelMutable);
		
		GridBagConstraints gbc_jCheckBoxMutable = new GridBagConstraints();
		gbc_jCheckBoxMutable.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxMutable.insets = new Insets(7, 5, 0, 0);
		gbc_jCheckBoxMutable.gridx = 1;
		gbc_jCheckBoxMutable.gridy = 2;
		this.add(getJCheckBoxMutable(), gbc_jCheckBoxMutable);
		
		GridBagConstraints gbc_jLabelCustomizer = new GridBagConstraints();
		gbc_jLabelCustomizer.anchor = GridBagConstraints.WEST;
		gbc_jLabelCustomizer.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelCustomizer.gridx = 0;
		gbc_jLabelCustomizer.gridy = 3;
		this.add(getJLabelCustomizer(), gbc_jLabelCustomizer);
		
		GridBagConstraints gbc_jCheckBoxCustomizer = new GridBagConstraints();
		gbc_jCheckBoxCustomizer.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxCustomizer.insets = new Insets(10, 5, 0, 0);
		gbc_jCheckBoxCustomizer.gridx = 1;
		gbc_jCheckBoxCustomizer.gridy = 3;
		this.add(getJCheckBoxCustomizer(), gbc_jCheckBoxCustomizer);
		
		GridBagConstraints gbc_jLabelJettyConfiguration = new GridBagConstraints();
		gbc_jLabelJettyConfiguration.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelJettyConfiguration.anchor = GridBagConstraints.WEST;
		gbc_jLabelJettyConfiguration.gridx = 0;
		gbc_jLabelJettyConfiguration.gridy = 4;
		this.add(getJLabelJettyConfiguration(), gbc_jLabelJettyConfiguration);
		
		GridBagConstraints gbc_jScrollPaneJettyConfiguration = new GridBagConstraints();
		gbc_jScrollPaneJettyConfiguration.insets = new Insets(5, 10, 5, 10);
		gbc_jScrollPaneJettyConfiguration.gridwidth = 2;
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
	
	private JLabel getJLabelMutable() {
		if (jLabelMutable == null) {
			jLabelMutable = new JLabel("Mutable Handler Collection");
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
	private JLabel getJLabelCustomizer() {
		if (jLabelCustomizer == null) {
			jLabelCustomizer = new JLabel("Uses Customizer");
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
	private JScrollPane getJScrollPaneJettyConfiguration() {
		if (jScrollPaneJettyConfiguration == null) {
			jScrollPaneJettyConfiguration = new JScrollPane();
		}
		return jScrollPaneJettyConfiguration;
	}
	private JLabel getJLabelJettyConfiguration() {
		if (jLabelJettyConfiguration == null) {
			jLabelJettyConfiguration = new JLabel("Jetty Configuration");
			jLabelJettyConfiguration.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelJettyConfiguration;
	}
	
	

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.AbstractJPanelSettings#setDataModel(de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject)
	 */
	@Override
	public void setDataModel(ServerTreeNodeServer dataModel) {
		
		this.serverTreeNodeServer = dataModel;
		this.unsaved = false;

		JettyConfiguration jettyConfig = this.serverTreeNodeServer.getAwbWebServerServiceWrapper().getJettyConfiguration();
		
		this.setServerName(jettyConfig.getServerName());
		
		this.getJComboBoxStartOn().setSelectedItem(jettyConfig.getStartOn());
		this.getJCheckBoxMutable().setSelected(jettyConfig.isMutableHandlerCollection());

		boolean usesCustomizer = (jettyConfig.getJettyCustomizer()!=null);
		String customizerClass = usesCustomizer==true ? "Class: " + jettyConfig.getJettyCustomizer().getClass().getName() : "";
		this.getJCheckBoxCustomizer().setSelected(usesCustomizer);
		this.getJCheckBoxCustomizer().setText(customizerClass);

		
		
		// TODO
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.AbstractJPanelSettings#getDataModel()
	 */
	@Override
	public ServerTreeNodeServer getDataModel() {
		// TODO Auto-generated method stub
		
		return this.serverTreeNodeServer;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.AbstractJPanelSettings#isUnsaved()
	 */
	@Override
	public boolean isUnsaved() {
		return unsaved;
	}
	

}
