package de.enflexit.awb.ws.ui.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.enflexit.awb.ws.ui.client.credentials.JPanelApiKeyCredentials;

public class JPanelCredentials extends JPanel {
	
	private static final long serialVersionUID = -1252151357398930115L;
	
	private JLabel jLabelCredentialList;
	private JScrollPane jScrollPaneCredentialList;
	private JList jListCredentials;
	private JPanelApiKeyCredentials jPanelApiKeyCredentials;
	private GridBagConstraints gbc_jPanelApiKeyCredentials;
	
	
	public JPanelCredentials() {
		initialize();
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelCredentialList = new GridBagConstraints();
		gbc_jLabelCredentialList.insets = new Insets(0, 0, 5, 0);
		gbc_jLabelCredentialList.anchor = GridBagConstraints.WEST;
		gbc_jLabelCredentialList.gridx = 0;
		gbc_jLabelCredentialList.gridy = 0;
		add(getJLabelCredentialList(), gbc_jLabelCredentialList);
		GridBagConstraints gbc_jScrollPaneCredentialList = new GridBagConstraints();
		gbc_jScrollPaneCredentialList.insets = new Insets(5, 0, 5, 0);
		gbc_jScrollPaneCredentialList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneCredentialList.gridx = 0;
		gbc_jScrollPaneCredentialList.gridy = 1;
		add(getJScrollPaneCredentialList(), gbc_jScrollPaneCredentialList);
		gbc_jPanelApiKeyCredentials = new GridBagConstraints();
		gbc_jPanelApiKeyCredentials.fill = GridBagConstraints.BOTH;
		gbc_jPanelApiKeyCredentials.gridx = 0;
		gbc_jPanelApiKeyCredentials.gridy = 2;
		add(getJPanelApiKeyCredentials(), gbc_jPanelApiKeyCredentials);
	}

	private JLabel getJLabelCredentialList() {
		if (jLabelCredentialList == null) {
			jLabelCredentialList = new JLabel("Credentials");
			jLabelCredentialList.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelCredentialList;
	}
	private JScrollPane getJScrollPaneCredentialList() {
		if (jScrollPaneCredentialList == null) {
			jScrollPaneCredentialList = new JScrollPane();
			jScrollPaneCredentialList.setViewportView(getJListCredentials());
		}
		return jScrollPaneCredentialList;
	}
	private JList getJListCredentials() {
		if (jListCredentials == null) {
			jListCredentials = new JList();
		}
		return jListCredentials;
	}
	private JPanelApiKeyCredentials getJPanelApiKeyCredentials() {
		if (jPanelApiKeyCredentials == null) {
			jPanelApiKeyCredentials = new JPanelApiKeyCredentials();
		}
		return jPanelApiKeyCredentials;
	}
}
