package de.enflexit.awb.ws.ui.client.credentials;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class JPanelAssignCredential extends JPanel{
	private JLabel jLableClientBundle;
	private JTextField jtextFieldClientBundle;
	private JLabel lblNewLabel;
	private JLabel jLableCredential;
	private JTextField jtextFieldCredential;
	private JLabel jLableCredentialsOfClient;
	private JButton btnNewButton;
	private JScrollPane jScrollPaneAssignedCredentials;
	private JList list;
	public JPanelAssignCredential() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
		gbc_jtextFieldClientBundle.insets = new Insets(2, 0, 5, 0);
		gbc_jtextFieldClientBundle.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtextFieldClientBundle.gridx = 1;
		gbc_jtextFieldClientBundle.gridy = 1;
		add(getJtextFieldClientBundle(), gbc_jtextFieldClientBundle);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(2, 5, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 2;
		add(getBtnNewButton(), gbc_btnNewButton);
		GridBagConstraints gbc_jLableCredential = new GridBagConstraints();
		gbc_jLableCredential.anchor = GridBagConstraints.EAST;
		gbc_jLableCredential.insets = new Insets(0, 5, 5, 5);
		gbc_jLableCredential.gridx = 0;
		gbc_jLableCredential.gridy = 3;
		add(getJLableCredential(), gbc_jLableCredential);
		GridBagConstraints gbc_jtextFieldCredential = new GridBagConstraints();
		gbc_jtextFieldCredential.insets = new Insets(2, 0, 5, 0);
		gbc_jtextFieldCredential.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtextFieldCredential.gridx = 1;
		gbc_jtextFieldCredential.gridy = 3;
		add(getJtextFieldCredential(), gbc_jtextFieldCredential);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 4;
		add(getLblNewLabel(), gbc_lblNewLabel);
		GridBagConstraints gbc_jScrollPaneAssignedCredentials = new GridBagConstraints();
		gbc_jScrollPaneAssignedCredentials.insets = new Insets(0, 0, 5, 0);
		gbc_jScrollPaneAssignedCredentials.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneAssignedCredentials.gridx = 1;
		gbc_jScrollPaneAssignedCredentials.gridy = 5;
		add(getJScrollPaneAssignedCredentials(), gbc_jScrollPaneAssignedCredentials);
	}

	private JLabel getJLableClientBundle() {
		if (jLableClientBundle == null) {
			jLableClientBundle = new JLabel("Client:");
			jLableClientBundle.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableClientBundle;
	}
	private JTextField getJtextFieldClientBundle() {
		if (jtextFieldClientBundle == null) {
			jtextFieldClientBundle = new JTextField();
			jtextFieldClientBundle.setFont(new Font("Dialog", Font.BOLD, 12));
			jtextFieldClientBundle.setColumns(10);
		}
		return jtextFieldClientBundle;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Assigned Credentials");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return lblNewLabel;
	}
	private JLabel getJLableCredential() {
		if (jLableCredential == null) {
			jLableCredential = new JLabel("Credential:");
			jLableCredential.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableCredential;
	}
	private JTextField getJtextFieldCredential() {
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
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Assign to");
			btnNewButton.setFont(new Font("Dialog", Font.BOLD, 12));
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
		}
		return btnNewButton;
	}
	private JScrollPane getJScrollPaneAssignedCredentials() {
		if (jScrollPaneAssignedCredentials == null) {
			jScrollPaneAssignedCredentials = new JScrollPane();
			jScrollPaneAssignedCredentials.setViewportView(getList_1());
		}
		return jScrollPaneAssignedCredentials;
	}
	private JList getList_1() {
		if (list == null) {
			list = new JList();
			list.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return list;
	}
}
