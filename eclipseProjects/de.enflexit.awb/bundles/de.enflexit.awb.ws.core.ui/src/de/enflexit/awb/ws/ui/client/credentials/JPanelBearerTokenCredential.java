package de.enflexit.awb.ws.ui.client.credentials;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.awb.ws.credential.BearerTokenCredential;
import de.enflexit.awb.ws.credential.JwtToken;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;

public class JPanelBearerTokenCredential extends AbstractCredentialPanel<BearerTokenCredential> implements WsConfigurationInterface,DocumentListener{

	private static final long serialVersionUID = -3032240795807467171L;
	private JLabel jLableBearerToken;
	private JLabel jLableTokenValue;
	private JTextField jTextFieldTokenValue;
	private boolean unsavedChanges;
	
	public JPanelBearerTokenCredential() {
		initialize();
	}
	private void initialize() {
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
		add(getJLableTokenValue(), gbc_lblNewLabel);
		GridBagConstraints gbc_JTextFieldTokenValue = new GridBagConstraints();
		gbc_JTextFieldTokenValue.insets = new Insets(5, 0, 0, 5);
		gbc_JTextFieldTokenValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_JTextFieldTokenValue.gridx = 1;
		gbc_JTextFieldTokenValue.gridy = 1;
		add(getJTextFieldTokenValue(), gbc_JTextFieldTokenValue);
	}

	private JLabel getJLableBearerToken() {
		if (jLableBearerToken == null) {
			jLableBearerToken = new JLabel("Bearer-Token");
			jLableBearerToken.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableBearerToken;
	}
	private JLabel getJLableTokenValue() {
		if (jLableTokenValue == null) {
			jLableTokenValue = new JLabel("Token: ");
			jLableTokenValue.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableTokenValue;
	}
	protected JTextField getJTextFieldTokenValue() {
		if (jTextFieldTokenValue == null) {
			jTextFieldTokenValue = new JTextField();
			jTextFieldTokenValue.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldTokenValue.setColumns(10);
			fillJTextfieldTokenValue();
			jTextFieldTokenValue.getDocument().addDocumentListener(this);
		}
		return jTextFieldTokenValue;
	}
	
	//-------------------------------------------------
    //----From here methods to access the fields-------
	//-------------------------------------------------
	
	/**
	 * Fills the JTextField credential.
	 */
	public void fillJTextfieldTokenValue() {
		if(this.getCredential()!=null) {
			getJTextFieldTokenValue().setText(this.getCredential().getJwtToken().getJwtToken());
		}
	}
	
	//-------------------------------------------------
    //----From here overridden methods------------------
	//-------------------------------------------------
	
	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.ui.client.credentials.AbstractCredentialPanel#getCredential()
	*/
	@Override
	public BearerTokenCredential getCredential() {
		if (super.credential == null) {
			if (getJTextFieldTokenValue().getText() != null) {
				if (!getJTextFieldTokenValue().getText().isBlank()) {
						BearerTokenCredential bearerToken = new BearerTokenCredential();
						bearerToken.setJwtToken(new JwtToken(getJTextFieldTokenValue().getText()));
						super.credential=bearerToken;
					}
			}
		}
		return super.credential;
	}

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
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		this.unsavedChanges = true;
	}
}
