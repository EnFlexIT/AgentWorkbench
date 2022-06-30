package de.enflexit.awb.ws.ui.client.credentials;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import de.enflexit.awb.ws.credential.UserPasswordCredential;

public class JPanelPasswordAuthenticationCredentials extends AbstractCredentialPanel<UserPasswordCredential> {

	private static final long serialVersionUID = -7913504913322598414L;
	private JLabel jLablePasswordAuthenticationValues;
	private JLabel jLableUsernameValue;
	private JLabel jLablePasswordValue;
	private JTextField jTextFieldUsername;
	private JTextField textField_1;
	
	public JPanelPasswordAuthenticationCredentials() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLablePasswordAuthenticationValues = new GridBagConstraints();
		gbc_jLablePasswordAuthenticationValues.insets = new Insets(5, 0, 5, 5);
		gbc_jLablePasswordAuthenticationValues.gridx = 1;
		gbc_jLablePasswordAuthenticationValues.gridy = 0;
		add(getLabel_1(), gbc_jLablePasswordAuthenticationValues);
		GridBagConstraints gbc_jLableUsernameValue = new GridBagConstraints();
		gbc_jLableUsernameValue.anchor = GridBagConstraints.EAST;
		gbc_jLableUsernameValue.insets = new Insets(5, 5, 5, 5);
		gbc_jLableUsernameValue.gridx = 0;
		gbc_jLableUsernameValue.gridy = 1;
		add(getLabel_2(), gbc_jLableUsernameValue);
		GridBagConstraints gbc_jTextFieldUsername = new GridBagConstraints();
		gbc_jTextFieldUsername.insets = new Insets(5, 0, 5, 5);
		gbc_jTextFieldUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldUsername.gridx = 1;
		gbc_jTextFieldUsername.gridy = 1;
		add(getJTextFieldUsername(), gbc_jTextFieldUsername);
		GridBagConstraints gbc_jLablePasswordValue = new GridBagConstraints();
		gbc_jLablePasswordValue.anchor = GridBagConstraints.EAST;
		gbc_jLablePasswordValue.insets = new Insets(5, 5, 0, 5);
		gbc_jLablePasswordValue.gridx = 0;
		gbc_jLablePasswordValue.gridy = 2;
		add(getJLablePasswordValue(), gbc_jLablePasswordValue);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(5, 0, 0, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		add(getTextField_1(), gbc_textField_1);
	}

	private JLabel getLabel_1() {
		if (jLablePasswordAuthenticationValues == null) {
			jLablePasswordAuthenticationValues = new JLabel("Username/Password-Authentication");
			jLablePasswordAuthenticationValues.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLablePasswordAuthenticationValues;
	}
	private JLabel getLabel_2() {
		if (jLableUsernameValue == null) {
			jLableUsernameValue = new JLabel("Username:");
			jLableUsernameValue.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableUsernameValue;
	}
	private JLabel getJLablePasswordValue() {
		if (jLablePasswordValue == null) {
			jLablePasswordValue = new JLabel("Password:");
			jLablePasswordValue.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLablePasswordValue;
	}
	private JTextField getJTextFieldUsername() {
		if (jTextFieldUsername == null) {
			jTextFieldUsername = new JTextField();
			jTextFieldUsername.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldUsername.setColumns(10);
		}
		return jTextFieldUsername;
	}
	private JTextField getTextField_1() {
		if (textField_1 == null) {
			textField_1 = new JTextField();
			textField_1.setFont(new Font("Dialog", Font.BOLD, 12));
			textField_1.setColumns(10);
		}
		return textField_1;
	}
	
}
