package de.enflexit.db.derby.server;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import de.enflexit.awb.core.jade.NetworkAddresses;
import de.enflexit.db.hibernate.gui.HibernateStateVisualizer;

import javax.swing.JCheckBox;
import javax.swing.JButton;

/**
 * The Class CustomDerbyServerConfigurationPanel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class CustomDerbyServerConfigurationPanel extends JPanel implements ActionListener {

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
	private JButton jButtonIPedit;
	
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
		gridBagLayout.columnWidths = new int[]{130, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(10, 10, 8, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(getLblNewLabel(), gbc_lblNewLabel);
		GridBagConstraints gbc_jCheckBoxStartServer = new GridBagConstraints();
		gbc_jCheckBoxStartServer.gridwidth = 2;
		gbc_jCheckBoxStartServer.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxStartServer.insets = new Insets(10, 0, 8, 10);
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
		GridBagConstraints gbc_jButtonIPedit = new GridBagConstraints();
		gbc_jButtonIPedit.insets = new Insets(0, 5, 5, 10);
		gbc_jButtonIPedit.gridx = 2;
		gbc_jButtonIPedit.gridy = 1;
		add(getJButtonIPedit(), gbc_jButtonIPedit);
		GridBagConstraints gbc_jLabelPort = new GridBagConstraints();
		gbc_jLabelPort.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelPort.anchor = GridBagConstraints.WEST;
		gbc_jLabelPort.gridx = 0;
		gbc_jLabelPort.gridy = 2;
		add(getJLabelPort(), gbc_jLabelPort);
		GridBagConstraints gbc_jTextFieldPort = new GridBagConstraints();
		gbc_jTextFieldPort.gridwidth = 2;
		gbc_jTextFieldPort.insets = new Insets(0, 0, 5, 10);
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
		gbc_jTextFieldUserName.gridwidth = 2;
		gbc_jTextFieldUserName.insets = new Insets(0, 0, 5, 10);
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
		gbc_jTextFieldPassword.gridwidth = 2;
		gbc_jTextFieldPassword.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldPassword.gridx = 1;
		gbc_jTextFieldPassword.gridy = 4;
		add(getJTextFieldPassword(), gbc_jTextFieldPassword);
	}
	
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Start Network Server");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return lblNewLabel;
	}
	private JLabel getJLabelHostOrIP() {
		if (jLabelHostOrIP == null) {
			jLabelHostOrIP = new JLabel("Host or IP");
			jLabelHostOrIP.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHostOrIP;
	}
	private JLabel getJLabelPort() {
		if (jLabelPort == null) {
			jLabelPort = new JLabel("Port (default: 1527)");
			jLabelPort.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelPort;
	}
	private JLabel getJLabelUserName() {
		if (jLabelUserName == null) {
			jLabelUserName = new JLabel("User Name");
			jLabelUserName.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelUserName;
	}
	private JLabel getJLabelPassword() {
		if (jLabelPassword == null) {
			jLabelPassword = new JLabel("Password");
			jLabelPassword.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelPassword;
	}
	
	// --- Value fields -----------------------------------
	private JTextField getJTextFieldHostOrIP() {
		if (jTextFieldHostOrIP == null) {
			jTextFieldHostOrIP = new JTextField();
			jTextFieldHostOrIP.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldHostOrIP.setPreferredSize(this.textFieldSize);
		}
		return jTextFieldHostOrIP;
	}
	private JCheckBox getJCheckBoxStartServer() {
		if (jCheckBoxStartServer == null) {
			jCheckBoxStartServer = new JCheckBox("Start a Derby database server that is accessible via network");
			jCheckBoxStartServer.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jCheckBoxStartServer;
	}
	private JTextField getJTextFieldPort() {
		if (jTextFieldPort == null) {
			jTextFieldPort = new JTextField();
			jTextFieldPort.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldPort.setPreferredSize(this.textFieldSize);
		}
		return jTextFieldPort;
	}
	private JTextField getJTextFieldUserName() {
		if (jTextFieldUserName == null) {
			jTextFieldUserName = new JTextField();
			jTextFieldUserName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldUserName.setPreferredSize(this.textFieldSize);
		}
		return jTextFieldUserName;
	}
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
	
	private JButton getJButtonIPedit() {
		if (jButtonIPedit == null) {
			jButtonIPedit = new JButton();
			jButtonIPedit.setToolTipText("IP auswählen");
			jButtonIPedit.setPreferredSize(new Dimension(26, 26));
			jButtonIPedit.setIcon(HibernateStateVisualizer.getImageIcon("edit.png"));
			jButtonIPedit.addActionListener(this);
		}
		return jButtonIPedit;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonIPedit()) {
			NetworkAddresses netAddresses = new NetworkAddresses();
			JPopupMenu popUp = netAddresses.getJPopupMenu4NetworkAddresses(this, false, true, true, true, true);
			popUp.show(this.getJTextFieldHostOrIP(), 0, this.getJTextFieldHostOrIP().getHeight());
			
		} else if (ae.getSource() instanceof JMenuItem) {
			// --- Trigger from JPopoupMenue for the IP-Addresses ---
			JMenuItem menuItem = (JMenuItem) ae.getSource() ;
			this.getJTextFieldHostOrIP().setText(menuItem.getActionCommand());
			
		}
	}
	
}
