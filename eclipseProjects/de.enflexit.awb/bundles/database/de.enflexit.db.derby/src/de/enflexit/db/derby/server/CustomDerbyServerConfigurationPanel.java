package de.enflexit.db.derby.server;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

/**
 * The Class CustomDerbyServerConfigurationPanel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class CustomDerbyServerConfigurationPanel extends JPanel {

	private static final long serialVersionUID = 3414375347803741567L;
	
	private final Dimension textFieldSize = new Dimension(400, 26);
	
	private JLabel jLabelHostOrIP;
	private JLabel jLabelPort;
	private JLabel jLabelUserName;
	private JLabel jLabelPassword;
	private JLabel lblNewLabel;
	
	private JCheckBox jCheckBoxStartServer;
	private JTextField jTextFieldHostOrIP;
	private JTextField jTextFieldPort;
	private JTextField jTextFieldUserName;
	private JPasswordField jTextFieldPassword;
	
	/**
	 * Instantiates a new custom derby server configuration panel.
	 */
	public CustomDerbyServerConfigurationPanel() {
		this.initialize();
		this.setDerbyNetworkServerProperties();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{130, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(10, 10, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(getLblNewLabel(), gbc_lblNewLabel);
		GridBagConstraints gbc_jCheckBoxStartServer = new GridBagConstraints();
		gbc_jCheckBoxStartServer.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxStartServer.insets = new Insets(10, 0, 5, 0);
		gbc_jCheckBoxStartServer.gridx = 1;
		gbc_jCheckBoxStartServer.gridy = 0;
		add(getJCheckBoxStartServer(), gbc_jCheckBoxStartServer);
		GridBagConstraints gbc_jLabelHostOrIP = new GridBagConstraints();
		gbc_jLabelHostOrIP.anchor = GridBagConstraints.WEST;
		gbc_jLabelHostOrIP.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelHostOrIP.gridx = 0;
		gbc_jLabelHostOrIP.gridy = 1;
		add(getJLabelHostOrIP(), gbc_jLabelHostOrIP);
		GridBagConstraints gbc_jTextFieldHostOrIP = new GridBagConstraints();
		gbc_jTextFieldHostOrIP.insets = new Insets(0, 0, 5, 0);
		gbc_jTextFieldHostOrIP.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldHostOrIP.gridx = 1;
		gbc_jTextFieldHostOrIP.gridy = 1;
		add(getJTextFieldHostOrIP(), gbc_jTextFieldHostOrIP);
		GridBagConstraints gbc_jLabelPort = new GridBagConstraints();
		gbc_jLabelPort.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelPort.anchor = GridBagConstraints.WEST;
		gbc_jLabelPort.gridx = 0;
		gbc_jLabelPort.gridy = 2;
		add(getJLabelPort(), gbc_jLabelPort);
		GridBagConstraints gbc_jTextFieldPort = new GridBagConstraints();
		gbc_jTextFieldPort.insets = new Insets(0, 0, 5, 0);
		gbc_jTextFieldPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldPort.gridx = 1;
		gbc_jTextFieldPort.gridy = 2;
		add(getJTextFieldPort(), gbc_jTextFieldPort);
		GridBagConstraints gbc_jLabelUserName = new GridBagConstraints();
		gbc_jLabelUserName.anchor = GridBagConstraints.WEST;
		gbc_jLabelUserName.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelUserName.gridx = 0;
		gbc_jLabelUserName.gridy = 3;
		add(getJLabelUserName(), gbc_jLabelUserName);
		GridBagConstraints gbc_jTextFieldUserName = new GridBagConstraints();
		gbc_jTextFieldUserName.insets = new Insets(0, 0, 5, 0);
		gbc_jTextFieldUserName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldUserName.gridx = 1;
		gbc_jTextFieldUserName.gridy = 3;
		add(getJTextFieldUserName(), gbc_jTextFieldUserName);
		GridBagConstraints gbc_jLabelPassword = new GridBagConstraints();
		gbc_jLabelPassword.anchor = GridBagConstraints.WEST;
		gbc_jLabelPassword.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelPassword.gridx = 0;
		gbc_jLabelPassword.gridy = 4;
		add(getJLabelPassword(), gbc_jLabelPassword);
		GridBagConstraints gbc_jTextFieldPassword = new GridBagConstraints();
		gbc_jTextFieldPassword.insets = new Insets(0, 0, 5, 0);
		gbc_jTextFieldPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldPassword.gridx = 1;
		gbc_jTextFieldPassword.gridy = 4;
		add(getJTextFieldPassword(), gbc_jTextFieldPassword);
	}
	
	/**
	 * Gets the lbl new label.
	 *
	 * @return the lbl new label
	 */
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Start Network Server");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return lblNewLabel;
	}
	
	/**
	 * Gets the j check box start server.
	 *
	 * @return the j check box start server
	 */
	private JCheckBox getJCheckBoxStartServer() {
		if (jCheckBoxStartServer == null) {
			jCheckBoxStartServer = new JCheckBox("Start a Derby database server that is accessible via network");
			jCheckBoxStartServer.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jCheckBoxStartServer;
	}
	
	/**
	 * Gets the j label host or IP.
	 *
	 * @return the j label host or IP
	 */
	private JLabel getJLabelHostOrIP() {
		if (jLabelHostOrIP == null) {
			jLabelHostOrIP = new JLabel("Host or IP");
			jLabelHostOrIP.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHostOrIP;
	}
	
	/**
	 * Gets the j label port.
	 *
	 * @return the j label port
	 */
	private JLabel getJLabelPort() {
		if (jLabelPort == null) {
			jLabelPort = new JLabel("Port (default: 1527)");
			jLabelPort.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelPort;
	}
	
	/**
	 * Gets the j label user name.
	 *
	 * @return the j label user name
	 */
	private JLabel getJLabelUserName() {
		if (jLabelUserName == null) {
			jLabelUserName = new JLabel("User Name");
			jLabelUserName.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelUserName;
	}
	
	/**
	 * Gets the j label password.
	 *
	 * @return the j label password
	 */
	private JLabel getJLabelPassword() {
		if (jLabelPassword == null) {
			jLabelPassword = new JLabel("Password");
			jLabelPassword.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelPassword;
	}
	
	/**
	 * Gets the j text field host or IP.
	 *
	 * @return the j text field host or IP
	 */
	// --- Value fields -----------------------------------
	private JTextField getJTextFieldHostOrIP() {
		if (jTextFieldHostOrIP == null) {
			jTextFieldHostOrIP = new JTextField();
			jTextFieldHostOrIP.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldHostOrIP.setPreferredSize(this.textFieldSize);
		}
		return jTextFieldHostOrIP;
	}
	
	/**
	 * Gets the j text field port.
	 *
	 * @return the j text field port
	 */
	private JTextField getJTextFieldPort() {
		if (jTextFieldPort == null) {
			jTextFieldPort = new JTextField();
			jTextFieldPort.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldPort.setPreferredSize(this.textFieldSize);
		}
		return jTextFieldPort;
	}
	
	/**
	 * Gets the j text field user name.
	 *
	 * @return the j text field user name
	 */
	private JTextField getJTextFieldUserName() {
		if (jTextFieldUserName == null) {
			jTextFieldUserName = new JTextField();
			jTextFieldUserName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldUserName.setPreferredSize(this.textFieldSize);
		}
		return jTextFieldUserName;
	}
	
	/**
	 * Gets the j text field password.
	 *
	 * @return the j text field password
	 */
	private JPasswordField getJTextFieldPassword() {
		if (jTextFieldPassword == null) {
			jTextFieldPassword = new JPasswordField();
			jTextFieldPassword.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldPassword.setPreferredSize(this.textFieldSize);
		}
		return jTextFieldPassword;
	}

	/**
	 * Sets the DerbyNetworkServerProperties to this panel.
	 */
	private void setDerbyNetworkServerProperties() {
		DerbyNetworkServerProperties dnsProperties = new DerbyNetworkServerProperties();
		this.getJCheckBoxStartServer().setSelected(dnsProperties.isStartDerbyNetworkServer());
		this.getJTextFieldHostOrIP().setText(dnsProperties.getHost());
		this.getJTextFieldPort().setText(dnsProperties.getPort() + "");
		this.getJTextFieldUserName().setText(dnsProperties.getUserName());
		this.getJTextFieldPassword().setText(dnsProperties.getPassword());
	}
	/**
	 * Gets the derby network server properties.
	 * @return the derby network server properties
	 */
	public DerbyNetworkServerProperties getDerbyNetworkServerProperties() {
		DerbyNetworkServerProperties dnsProperties = new DerbyNetworkServerProperties();
		dnsProperties.setStartDerbyNetworkServer(this.getJCheckBoxStartServer().isSelected());
		dnsProperties.setHost(this.getJTextFieldHostOrIP().getText());
		dnsProperties.setPort(Integer.parseInt(this.getJTextFieldPort().getText()));
		dnsProperties.setUserName(this.getJTextFieldUserName().getText());
		dnsProperties.setPassword(new String(this.getJTextFieldPassword().getPassword()));
		return dnsProperties;
	}
	
}
