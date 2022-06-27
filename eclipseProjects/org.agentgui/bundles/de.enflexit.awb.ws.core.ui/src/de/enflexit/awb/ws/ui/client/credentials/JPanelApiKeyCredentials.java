package de.enflexit.awb.ws.ui.client.credentials;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import de.enflexit.awb.ws.credential.ApiKeyCredential;

public class JPanelApiKeyCredentials extends AbstractCredentialPanel<ApiKeyCredential> {

	private static final long serialVersionUID = -4236093133804754420L;
	private JLabel jLableApiKeyName;
	private JTextField textField;
	private JLabel jLableApiKeyValues;
	private JLabel jLableKey;
	private JTextField jTextField_KeyValue;
	
	public JPanelApiKeyCredentials() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLableApiKeyValues = new GridBagConstraints();
		gbc_jLableApiKeyValues.insets = new Insets(5, 0, 5, 5);
		gbc_jLableApiKeyValues.gridx = 1;
		gbc_jLableApiKeyValues.gridy = 0;
		add(getJLableApiKeyValues(), gbc_jLableApiKeyValues);
		GridBagConstraints gbc_jLableApiKeyName = new GridBagConstraints();
		gbc_jLableApiKeyName.insets = new Insets(5, 5, 5, 5);
		gbc_jLableApiKeyName.anchor = GridBagConstraints.EAST;
		gbc_jLableApiKeyName.gridx = 0;
		gbc_jLableApiKeyName.gridy = 1;
		add(getJLableApiKeyName(), gbc_jLableApiKeyName);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(5, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		add(getTextField(), gbc_textField);
		GridBagConstraints gbc_jLableKey = new GridBagConstraints();
		gbc_jLableKey.anchor = GridBagConstraints.EAST;
		gbc_jLableKey.insets = new Insets(5, 5, 0, 5);
		gbc_jLableKey.gridx = 0;
		gbc_jLableKey.gridy = 2;
		add(getJLableKey(), gbc_jLableKey);
		GridBagConstraints gbc_jTextField_KeyValue = new GridBagConstraints();
		gbc_jTextField_KeyValue.insets = new Insets(5, 0, 0, 5);
		gbc_jTextField_KeyValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextField_KeyValue.gridx = 1;
		gbc_jTextField_KeyValue.gridy = 2;
		add(getJTextField_KeyValue(), gbc_jTextField_KeyValue);
	}
	
	private JLabel getJLableApiKeyName() {
		if (jLableApiKeyName == null) {
			jLableApiKeyName = new JLabel("Name :");
			jLableApiKeyName.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableApiKeyName;
	}
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setFont(new Font("Dialog", Font.BOLD, 12));
			textField.setColumns(10);
		}
		return textField;
	}
	private JLabel getJLableApiKeyValues() {
		if (jLableApiKeyValues == null) {
			jLableApiKeyValues = new JLabel("API-Key Values");
			jLableApiKeyValues.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableApiKeyValues;
	}
	private JLabel getJLableKey() {
		if (jLableKey == null) {
			jLableKey = new JLabel("Key :");
			jLableKey.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableKey;
	}
	private JTextField getJTextField_KeyValue() {
		if (jTextField_KeyValue == null) {
			jTextField_KeyValue = new JTextField();
			jTextField_KeyValue.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextField_KeyValue.setColumns(10);
		}
		return jTextField_KeyValue;
	}
}
