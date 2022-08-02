package de.enflexit.awb.ws.ui.client.credentials;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import de.enflexit.awb.ws.client.CredentialType;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.credential.ApiKeyCredential;
import de.enflexit.awb.ws.credential.BearerTokenCredential;
import de.enflexit.awb.ws.credential.JwtToken;
import de.enflexit.awb.ws.credential.UserPasswordCredential;

public class JDialogCredentialCreation extends JDialog {
	
	private static final long serialVersionUID = 671154533090271312L;
	private JComboBox<CredentialType> comboBox;
	private JPanelApiKeyCredentials panelApiKeyCredentials;
	private JPanelBearerTokenCredential jPanelBearerTokenCredential;
	private JPanelPasswordAuthenticationCredentials jPanelPasswordAuthenticationCredentials;
	private JButton jButtonCreateAndSaveCredential;
	
	public JDialogCredentialCreation() {
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{351, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(5, 5, 10, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		add(getComboBox(), gbc_comboBox);
		addCredentialPanel(getJPanelApiKeyCredentials());
		GridBagConstraints gbc_jButtonCreateAndSaveCredential = new GridBagConstraints();
		gbc_jButtonCreateAndSaveCredential.insets = new Insets(0, 5, 5, 5);
		gbc_jButtonCreateAndSaveCredential.anchor = GridBagConstraints.WEST;
		gbc_jButtonCreateAndSaveCredential.gridx = 0;
		gbc_jButtonCreateAndSaveCredential.gridy = 2;
		add(getJButtonCreateAndSaveCredential(), gbc_jButtonCreateAndSaveCredential);
	}

	private void addCredentialPanel(JPanel credPanel) {
		GridBagConstraints gbc_panelApiKeyCredentials = new GridBagConstraints();
		gbc_panelApiKeyCredentials.insets = new Insets(0, 5, 5, 5);
		gbc_panelApiKeyCredentials.fill = GridBagConstraints.BOTH;
		gbc_panelApiKeyCredentials.gridx = 0;
		gbc_panelApiKeyCredentials.gridy = 1;
		add(credPanel, gbc_panelApiKeyCredentials);
		this.revalidate();
		this.repaint();
	}

	private JComboBox<CredentialType> getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox<CredentialType>();
		}
		fillJComboBox();
		changeCredPanelAfterSelection();
		return comboBox;
	}

	private void changeCredPanelAfterSelection() {
		comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				CredentialType type=(CredentialType) e.getItem();
				if(ItemEvent.SELECTED==e.getStateChange()) {
					if(type.equals(CredentialType.API_KEY)) {
						JPanel credPanel=getJPanelApiKeyCredentials();
						addCredentialPanel(credPanel);
					}else if(type.equals(CredentialType.BEARER_TOKEN)) {
						JPanel credPanel=getJPanelBearerTokenCredential();
						addCredentialPanel(credPanel);
					}else if(type.equals(CredentialType.USERNAME_PASSWORD)) {
						JPanel credPanel=getJPanelPasswordAuthenticationCredentials();
						addCredentialPanel(credPanel);
					}
				}else if(ItemEvent.DESELECTED==e.getStateChange()) {
					if(type.equals(CredentialType.API_KEY)) {
						remove(getJPanelApiKeyCredentials());
					}else if(type.equals(CredentialType.BEARER_TOKEN)) {
						remove(getJPanelBearerTokenCredential());
					}else if(type.equals(CredentialType.USERNAME_PASSWORD)) {
						remove(getJPanelPasswordAuthenticationCredentials());
					}
				}	
			}
		});
	}

	private void fillJComboBox() {
		CredentialType[] credentialTypes=CredentialType.values();
		for (CredentialType credentialType : credentialTypes) {
			comboBox.addItem(credentialType);
		}
		comboBox.setSelectedItem(CredentialType.API_KEY);
	}
	
	private JPanelApiKeyCredentials getJPanelApiKeyCredentials() {
		if (panelApiKeyCredentials == null) {
			panelApiKeyCredentials = new JPanelApiKeyCredentials();
		}
		return panelApiKeyCredentials;
	}
	
	private JPanelBearerTokenCredential getJPanelBearerTokenCredential() {
		if (jPanelBearerTokenCredential == null) {
			jPanelBearerTokenCredential = new JPanelBearerTokenCredential();
		}
		return jPanelBearerTokenCredential;
	}
	
	private JPanelPasswordAuthenticationCredentials getJPanelPasswordAuthenticationCredentials() {
		if (jPanelPasswordAuthenticationCredentials == null) {
			jPanelPasswordAuthenticationCredentials = new JPanelPasswordAuthenticationCredentials();
		}
		return jPanelPasswordAuthenticationCredentials;
	}
	
	private JButton getJButtonCreateAndSaveCredential() {
		if (jButtonCreateAndSaveCredential == null) {
			jButtonCreateAndSaveCredential = new JButton("Create & Save");
			jButtonCreateAndSaveCredential.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCreateAndSaveCredential.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					createCredentialsFromTextfield();
				}
				
			});
		}
		return jButtonCreateAndSaveCredential;
	}
	
	private void createCredentialsFromTextfield() {
		CredentialType type=(CredentialType) getComboBox().getSelectedItem();
		AbstractCredential cred=null;
		if(type.equals(CredentialType.API_KEY)) {
			if(getJPanelApiKeyCredentials().hasUnsavedChanges()) {
				String keyValue=getJPanelApiKeyCredentials().getJTextFieldKeyValue().getText();
				String keyName=getJPanelApiKeyCredentials().getJTextFieldKeyName().getText();
				ApiKeyCredential apiCreds=new ApiKeyCredential();
				apiCreds.setApiKeyName(keyName);
				apiCreds.setApiKeyValue(keyValue);
				cred=apiCreds;
			}
		}else if(type.equals(CredentialType.BEARER_TOKEN)) {
			if(getJPanelBearerTokenCredential().hasUnsavedChanges()) {
				String bearerTokenValue=getJPanelBearerTokenCredential().getJTextFieldTokenValue().getText();
				BearerTokenCredential token=new BearerTokenCredential();
				token.setJwtToken(new JwtToken(bearerTokenValue));
				cred=token;
			}
		}else if(type.equals(CredentialType.USERNAME_PASSWORD)) {
			if(getJPanelPasswordAuthenticationCredentials().hasUnsavedChanges()) {
				String username=getJPanelPasswordAuthenticationCredentials().getJTextFieldUsername().getText();
				String password=getJPanelPasswordAuthenticationCredentials().getJTextFieldPassword().getText();
				UserPasswordCredential passwordCred=new UserPasswordCredential();
				passwordCred.setPassword(password);
				passwordCred.setUserName(username);
				cred=passwordCred;
			}
		}
		if(cred!=null) {
			WsCredentialStore.getInstance().getCredentialList().add(cred);
		}
		this.dispose();
	}
	
}
