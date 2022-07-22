package de.enflexit.awb.ws.ui.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JPanelAssignCredential extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel jLableClientBundle;
	private JTextField jtextFieldClientBundle;
	private JLabel jLableCredential;
	private JTextField jtextFieldCredential;
	private JLabel jLableCredentialsOfClient;
	private JButton btnAssignTo;

	public JPanelAssignCredential() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLableCredentialsOfClient = new GridBagConstraints();
		gbc_jLableCredentialsOfClient.insets = new Insets(5, 0, 5, 5);
		gbc_jLableCredentialsOfClient.gridx = 1;
		gbc_jLableCredentialsOfClient.gridy = 0;
		add(getJLableCredentialsOfClient(), gbc_jLableCredentialsOfClient);
		GridBagConstraints gbc_jLableClientBundle = new GridBagConstraints();
		gbc_jLableClientBundle.insets = new Insets(5, 5, 5, 5);
		gbc_jLableClientBundle.anchor = GridBagConstraints.EAST;
		gbc_jLableClientBundle.gridx = 0;
		gbc_jLableClientBundle.gridy = 1;
		add(getJLableClientBundle(), gbc_jLableClientBundle);
		GridBagConstraints gbc_jtextFieldClientBundle = new GridBagConstraints();
		gbc_jtextFieldClientBundle.insets = new Insets(2, 0, 5, 5);
		gbc_jtextFieldClientBundle.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtextFieldClientBundle.gridx = 1;
		gbc_jtextFieldClientBundle.gridy = 1;
		add(getJtextFieldClientBundle(), gbc_jtextFieldClientBundle);
		GridBagConstraints gbc_btnAssignTo = new GridBagConstraints();
		gbc_btnAssignTo.insets = new Insets(2, 5, 5, 5);
		gbc_btnAssignTo.gridx = 1;
		gbc_btnAssignTo.gridy = 2;
		add(getBtnAssignToButton(), gbc_btnAssignTo);
		GridBagConstraints gbc_jLableCredential = new GridBagConstraints();
		gbc_jLableCredential.anchor = GridBagConstraints.EAST;
		gbc_jLableCredential.insets = new Insets(0, 5, 5, 5);
		gbc_jLableCredential.gridx = 0;
		gbc_jLableCredential.gridy = 3;
		add(getJLableCredential(), gbc_jLableCredential);
		GridBagConstraints gbc_jtextFieldCredential = new GridBagConstraints();
		gbc_jtextFieldCredential.insets = new Insets(2, 0, 5, 5);
		gbc_jtextFieldCredential.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtextFieldCredential.gridx = 1;
		gbc_jtextFieldCredential.gridy = 3;
		add(getJtextFieldCredential(), gbc_jtextFieldCredential);
	}

	private JLabel getJLableClientBundle() {
		if (jLableClientBundle == null) {
			jLableClientBundle = new JLabel("Client:");
			jLableClientBundle.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableClientBundle;
	}

	public JTextField getJtextFieldClientBundle() {
		if (jtextFieldClientBundle == null) {
			jtextFieldClientBundle = new JTextField();
			jtextFieldClientBundle.setFont(new Font("Dialog", Font.BOLD, 12));
			jtextFieldClientBundle.setColumns(10);
		}
		return jtextFieldClientBundle;
	}

	private JLabel getJLableCredential() {
		if (jLableCredential == null) {
			jLableCredential = new JLabel("Credential:");
			jLableCredential.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableCredential;
	}

	public JTextField getJtextFieldCredential() {
		if (jtextFieldCredential == null) {
			jtextFieldCredential = new JTextField();
			jtextFieldCredential.setFont(new Font("Dialog", Font.BOLD, 12));
			jtextFieldCredential.setColumns(10);
		}
		return jtextFieldCredential;
	}

	private JLabel getJLableCredentialsOfClient() {
		if (jLableCredentialsOfClient == null) {
			jLableCredentialsOfClient = new JLabel("Client and Credentials");
			jLableCredentialsOfClient.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableCredentialsOfClient;
	}

	public JButton getBtnAssignToButton() {
		if (btnAssignTo == null) {
			btnAssignTo = new JButton("Assign to");
			btnAssignTo.setFont(new Font("Dialog", Font.BOLD, 12));
			btnAssignTo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
		}
		return btnAssignTo;
	}
}
