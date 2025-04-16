package de.enflexit.awb.ws.ui.client.credentials;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.awb.ws.credential.UserPasswordCredential;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;

/**
 * The Class JPanelPasswordAuthenticationCredentials to create a UserPasswordCredential
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
public class JPanelPasswordAuthenticationCredentials extends AbstractCredentialPanel<UserPasswordCredential> implements WsConfigurationInterface,DocumentListener{

	private static final long serialVersionUID = -7913504913322598414L;
	private JLabel jLablePasswordAuthenticationValues;
	private JLabel jLableUsernameValue;
	private JLabel jLablePasswordValue;
	private JTextField jTextFieldUsername;
	private JTextField jTextFieldPassword;
	private boolean unsavedChanges=false;
	
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
		add(getjLableUserPasswordAuth(), gbc_jLablePasswordAuthenticationValues);
		GridBagConstraints gbc_jLableUsernameValue = new GridBagConstraints();
		gbc_jLableUsernameValue.anchor = GridBagConstraints.EAST;
		gbc_jLableUsernameValue.insets = new Insets(5, 5, 5, 5);
		gbc_jLableUsernameValue.gridx = 0;
		gbc_jLableUsernameValue.gridy = 1;
		add(getJLableUsername(), gbc_jLableUsernameValue);
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
		add(getJTextFieldPassword(), gbc_textField_1);
	}

	private JLabel getjLableUserPasswordAuth() {
		if (jLablePasswordAuthenticationValues == null) {
			jLablePasswordAuthenticationValues = new JLabel("Username/Password-Authentication");
			jLablePasswordAuthenticationValues.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLablePasswordAuthenticationValues;
	}
	
	private JLabel getJLableUsername() {
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
	
	protected JTextField getJTextFieldUsername() {
		if (jTextFieldUsername == null) {
			jTextFieldUsername = new JTextField();
			jTextFieldUsername.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldUsername.setColumns(10);
			fillJTextfieldUsername();
			jTextFieldUsername.getDocument().addDocumentListener(this);
		}
		
		return jTextFieldUsername;
	}

	private JTextField getJTextFieldPassword() {
		if (jTextFieldPassword == null) {
			jTextFieldPassword = new JTextField();
			jTextFieldPassword.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldPassword.setColumns(10);
			fillJTextFieldPassword();
			jTextFieldPassword.getDocument().addDocumentListener(this);
		}
		return jTextFieldPassword;
	}
	
	//-------------------------------------------------
    //----From here methods to fill/access the fields-------
	//-------------------------------------------------
	
	/**
	 * Fills the Password related JTextfields. If the credential is create or was set before. 
	 */
	public void fillPasswordTextfields() {
		fillJTextfieldUsername();
		fillJTextFieldPassword(); 
	}
	
	/**
	 * Fill JTextfield username.
	 */
	private void fillJTextfieldUsername() {
		if(this.getCredential()!=null) {
			getJTextFieldUsername().setText(this.getCredential().getUserName());
		}
	}
	
	/**
	 * Fill JTextfield password.
	 */
	private void fillJTextFieldPassword() {
		if(this.getCredential()!=null) {
			getJTextFieldPassword().setText(this.getCredential().getPassword());
		}
	}
	//-------------------------------------------------
    //----From here overridden methods-------
	//-------------------------------------------------
	
	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	*/
	@Override
	public boolean hasUnsavedChanges() {
		return unsavedChanges;
	}

	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	*/
	@Override
	public boolean userConfirmedToChangeView() {
		return hasUnsavedChanges();
	}
	
	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.ui.client.credentials.AbstractCredentialPanel#getCredential()
	*/
	@Override
	public UserPasswordCredential getCredential() {
		if (super.credential == null) {
			if (getJTextFieldUsername().getText() != null && getJTextFieldPassword().getText() != null) {
				if (!getJTextFieldUsername().getText().isBlank()) {
					if (!getJTextFieldPassword().getText().isBlank()) {
						UserPasswordCredential userPasswordCred = new UserPasswordCredential();
						userPasswordCred.setPassword(getJTextFieldPassword().getText());
						userPasswordCred.setUserName(getJTextFieldUsername().getText());
						super.credential=userPasswordCred;
					}
				}
			}
		}
		return super.credential;
	}

	/* (non-Javadoc)
	* @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	*/
	@Override
	public void insertUpdate(DocumentEvent e) {
		this.unsavedChanges=true;
	}

	/* (non-Javadoc)
	* @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	*/
	@Override
	public void removeUpdate(DocumentEvent e) {
		this.unsavedChanges=true;		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.
	 * DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		this.unsavedChanges = true;
	}
	
}
