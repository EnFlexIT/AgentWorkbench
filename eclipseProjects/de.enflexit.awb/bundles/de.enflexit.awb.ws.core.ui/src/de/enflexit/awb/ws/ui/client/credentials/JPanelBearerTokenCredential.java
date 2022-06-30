package de.enflexit.awb.ws.ui.client.credentials;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import de.enflexit.awb.ws.credential.BearerTokenCredential;

public class JPanelBearerTokenCredential extends AbstractCredentialPanel<BearerTokenCredential> {
	public JPanelBearerTokenCredential() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLableBearerToken = new GridBagConstraints();
		gbc_jLableBearerToken.insets = new Insets(5, 0, 5, 0);
		gbc_jLableBearerToken.gridx = 1;
		gbc_jLableBearerToken.gridy = 0;
		add(getJLableBearerToken(), gbc_jLableBearerToken);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(5, 5, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(getLblNewLabel(), gbc_lblNewLabel);
		GridBagConstraints gbc_JTextFieldTokenValue = new GridBagConstraints();
		gbc_JTextFieldTokenValue.insets = new Insets(5, 0, 0, 5);
		gbc_JTextFieldTokenValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_JTextFieldTokenValue.gridx = 1;
		gbc_JTextFieldTokenValue.gridy = 1;
		add(getJTextFieldTokenValue(), gbc_JTextFieldTokenValue);
	}

	private static final long serialVersionUID = -3032240795807467171L;
	private JLabel jLableBearerToken;
	private JLabel lblNewLabel;
	private JTextField JTextFieldTokenValue;


	private JLabel getJLableBearerToken() {
		if (jLableBearerToken == null) {
			jLableBearerToken = new JLabel("Bearer-Token");
			jLableBearerToken.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableBearerToken;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Token: ");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return lblNewLabel;
	}
	private JTextField getJTextFieldTokenValue() {
		if (JTextFieldTokenValue == null) {
			JTextFieldTokenValue = new JTextField();
			JTextFieldTokenValue.setFont(new Font("Dialog", Font.BOLD, 12));
			JTextFieldTokenValue.setColumns(10);
		}
		return JTextFieldTokenValue;
	}
}
