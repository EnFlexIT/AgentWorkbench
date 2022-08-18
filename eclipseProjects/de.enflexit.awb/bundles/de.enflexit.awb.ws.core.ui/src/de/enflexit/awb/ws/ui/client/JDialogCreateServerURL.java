package de.enflexit.awb.ws.ui.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.enflexit.awb.ws.client.ServerURL;

/**
 * The Class JDialogCreateServerURL enables the creation of a {@link ServerURL}.
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
public class JDialogCreateServerURL extends JDialog {
	public JDialogCreateServerURL() {
		getContentPane().add(getJPanelServerURL(), BorderLayout.CENTER);
	}

	private static final long serialVersionUID = -4595711046276221996L;
	private JPanel jPanelServerURL;
	private JLabel jLableServerURL;
	private JLabel jLableURLofServer;
	private JTextField textField;

	private JPanel getJPanelServerURL() {
		if (jPanelServerURL == null) {
			jPanelServerURL = new JPanel();
			GridBagLayout gbl_jPanelServerURL = new GridBagLayout();
			gbl_jPanelServerURL.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelServerURL.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelServerURL.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelServerURL.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			jPanelServerURL.setLayout(gbl_jPanelServerURL);
			GridBagConstraints gbc_jLableServerURL = new GridBagConstraints();
			gbc_jLableServerURL.insets = new Insets(0, 0, 5, 5);
			gbc_jLableServerURL.gridx = 1;
			gbc_jLableServerURL.gridy = 0;
			jPanelServerURL.add(getJLableServerURL(), gbc_jLableServerURL);
			GridBagConstraints gbc_jLableURLofServer = new GridBagConstraints();
			gbc_jLableURLofServer.anchor = GridBagConstraints.EAST;
			gbc_jLableURLofServer.insets = new Insets(0, 0, 0, 5);
			gbc_jLableURLofServer.gridx = 0;
			gbc_jLableURLofServer.gridy = 1;
			jPanelServerURL.add(getJLableURLofServer(), gbc_jLableURLofServer);
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 0, 5);
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 1;
			jPanelServerURL.add(getTextField(), gbc_textField);
		}
		return jPanelServerURL;
	}
	private JLabel getJLableServerURL() {
		if (jLableServerURL == null) {
			jLableServerURL = new JLabel("Server-URL");
			jLableServerURL.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableServerURL;
	}
	private JLabel getJLableURLofServer() {
		if (jLableURLofServer == null) {
			jLableURLofServer = new JLabel("URL of the Server");
			jLableURLofServer.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableURLofServer;
	}
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setFont(new Font("Dialog", Font.BOLD, 12));
			textField.setColumns(10);
		}
		return textField;
	}
}
