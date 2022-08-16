package de.enflexit.awb.ws.ui.client.credentials;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.awb.ws.credential.ApiKeyCredential;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;

public class JPanelApiKeyCredentials extends AbstractCredentialPanel<ApiKeyCredential> implements WsConfigurationInterface,DocumentListener {

	private static final long serialVersionUID = -4236093133804754420L;
	private JLabel jLableApiKeyName;
	private JTextField jTextField_KeyName;
	private JLabel jLableApiKeyValues;
	private JLabel jLableKey;
	private JTextField jTextField_KeyValue;
	private boolean unsavedChanges=false;
	
	//----------------------------------------------------
	//--------From here fields for temporary savings------
	//----------------------------------------------------
	
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
		add(getJTextFieldKeyName(), gbc_textField);
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
		add(getJTextFieldKeyValue(), gbc_jTextField_KeyValue);
	}
	
	private JLabel getJLableApiKeyName() {
		if (jLableApiKeyName == null) {
			jLableApiKeyName = new JLabel("Name :");
			jLableApiKeyName.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableApiKeyName;
	}
	
	private JTextField getJTextFieldKeyName() {
		if (jTextField_KeyName == null) {
			jTextField_KeyName = new JTextField();
			jTextField_KeyName.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextField_KeyName.setColumns(10);
			jTextField_KeyName.setEditable(true);
			fillJTextfieldCredentialKeyName();
			jTextField_KeyName.getDocument().addDocumentListener(this);
		}
		return jTextField_KeyName;
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
	private JTextField getJTextFieldKeyValue() {
		if (jTextField_KeyValue == null) {
			jTextField_KeyValue = new JTextField();
			jTextField_KeyValue.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextField_KeyValue.setColumns(10);
			jTextField_KeyValue.setEditable(true);
			fillJTextFieldKeyValue();
			jTextField_KeyValue.getDocument().addDocumentListener(this);
		}
		return jTextField_KeyValue;
	}


	
	//-------------------------------------------------
    //----From here methods to fill/access the fields-------
	//-------------------------------------------------
	
	/**
	 * Fill J textfield credential key name.
	 */
	private void fillJTextfieldCredentialKeyName() {
		if(this.getCredential()!=null) {
			getJTextFieldKeyName().setText(this.getCredential().getApiKeyName());
		}
	}
	
	/**
	 * Fill J text field key value.
	 */
	private void fillJTextFieldKeyValue() {
		if(this.getCredential()!=null) {
			getJTextFieldKeyValue().setText(this.getCredential().getApiKeyValue());
		}
	}
	
	/**
	 * Fills api key textfields. If the credential is create or was set before. This methods fills the API-Key related JTextfields
	 */
	public void fillApiKeyTextfields() {
		fillJTextfieldCredentialKeyName();
		fillJTextFieldKeyValue(); 
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
	public ApiKeyCredential getCredential() {
		if (super.credential == null) {
			if (getJTextFieldKeyName().getText() != null && getJTextFieldKeyValue().getText() != null) {
				if (!getJTextFieldKeyName().getText().isBlank()) {
					if (!getJTextFieldKeyValue().getText().isBlank()) {
						ApiKeyCredential apiKeyCred = new ApiKeyCredential();
						apiKeyCred.setApiKeyName(getJTextFieldKeyName().getText());
						apiKeyCred.setApiKeyValue(getJTextFieldKeyValue().getText());
						super.credential=apiKeyCred;
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
