package de.enflexit.awb.ws.ui.client.credentials;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.awb.ws.client.CredentialType;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.credential.ApiKeyCredential;
import de.enflexit.awb.ws.credential.BearerTokenCredential;
import de.enflexit.awb.ws.credential.JwtToken;
import de.enflexit.awb.ws.credential.UserPasswordCredential;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;
import de.enflexit.common.swing.JDialogSizeAndPostionController;
import de.enflexit.common.swing.JDialogSizeAndPostionController.JDialogPosition;

/**
 * The Class JDialogCredentialCreation for the creation of a credential.
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
public class JDialogCredentialCreation extends JDialog implements ActionListener,ItemListener,DocumentListener,WsConfigurationInterface{
	
	private static final long serialVersionUID = 671154533090271312L;
	private JComboBox<CredentialType> comboBox;
	private JPanelApiKeyCredentials panelApiKeyCredentials;
	private JPanelBearerTokenCredential jPanelBearerTokenCredential;
	private JPanelPasswordAuthenticationCredentials jPanelPasswordAuthenticationCredentials;
	private JButton jButtonCreateAndSaveCredential;
	
	private AbstractCredential createdCredential=null;
	private AbstractCredential modifiedCredential=null;
	private boolean hasUnsavedChanges=false;
	private JPanel jPanelNameOfACredential;
	private JLabel jLableNameOfTheCredential;
	private JTextField jTextFieldNameOfTheCredential;
	
	/**
	 * Instantiates a new JDialog to create a credential.
	 *
	 * @param owner the owner
	 */
	public JDialogCredentialCreation(Window owner) {
		super(owner);
		this.initialize();
	}

	/**
	 * Instantiates a new JDialog .
	 *
	 * @param owner the owner
	 * @param cred the cred to modify
	 */
	public JDialogCredentialCreation(Window owner, AbstractCredential cred) {
		super(owner);
		this.setCreatedCredential(cred);
		this.initialize();
	}
	/**
	 * Initialize the JDialog.
	 */
	private void initialize() {
		this.setTitle("Create a Credential");
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(500, 300);
		JDialogSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{351, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(5, 5, 10, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		getContentPane().add(getJComboBox(), gbc_comboBox);
		addCredentialPanel(getJPanelApiKeyCredentials());
		
		GridBagConstraints gbc_jButtonCreateAndSaveCredential = new GridBagConstraints();
		gbc_jButtonCreateAndSaveCredential.insets = new Insets(0, 5, 0, 0);
		gbc_jButtonCreateAndSaveCredential.anchor = GridBagConstraints.WEST;
		gbc_jButtonCreateAndSaveCredential.gridx = 0;
		gbc_jButtonCreateAndSaveCredential.gridy = 3;
		getContentPane().add(getJButtonCreateAndSaveCredential(), gbc_jButtonCreateAndSaveCredential);
	}

	//---------------------------------------------------------
	//------------- From here getter and setter ---------------
	//---------------------------------------------------------
	
	private JComboBox<CredentialType> getJComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox<CredentialType>();
			fillJComboBox();
			comboBox.addItemListener(this);
		}
		return comboBox;
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
			jButtonCreateAndSaveCredential.addActionListener(this);
		}
		return jButtonCreateAndSaveCredential;
	}
	
	private JLabel getJLableNameOfTheCredential() {
		if (jLableNameOfTheCredential == null) {
			jLableNameOfTheCredential = new JLabel("Name of the credential");
			jLableNameOfTheCredential.setToolTipText("The name of the credential, which will be shown in the credential-list");
			jLableNameOfTheCredential.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableNameOfTheCredential;
	}
	private JTextField getJTextFieldNameOfTheCredential() {
		if (jTextFieldNameOfTheCredential == null) {
			jTextFieldNameOfTheCredential = new JTextField();
			jTextFieldNameOfTheCredential.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldNameOfTheCredential.setColumns(10);
			jTextFieldNameOfTheCredential.getDocument().addDocumentListener(this);
		}
		return jTextFieldNameOfTheCredential;
	}
	
	private JPanel getJPanelNameOfACredential() {
		if (jPanelNameOfACredential == null) {
			jPanelNameOfACredential = new JPanel();
			GridBagLayout gbl_jPanelNameOfACredential = new GridBagLayout();
			gbl_jPanelNameOfACredential.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelNameOfACredential.rowHeights = new int[]{0, 0};
			gbl_jPanelNameOfACredential.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelNameOfACredential.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelNameOfACredential.setLayout(gbl_jPanelNameOfACredential);
			GridBagConstraints gbc_jLableNameOfTheCredential = new GridBagConstraints();
			gbc_jLableNameOfTheCredential.insets = new Insets(0, 5, 0, 5);
			gbc_jLableNameOfTheCredential.anchor = GridBagConstraints.EAST;
			gbc_jLableNameOfTheCredential.gridx = 0;
			gbc_jLableNameOfTheCredential.gridy = 0;
			jPanelNameOfACredential.add(getJLableNameOfTheCredential(), gbc_jLableNameOfTheCredential);
			GridBagConstraints gbc_jTextFieldNameOfTheCredential = new GridBagConstraints();
			gbc_jTextFieldNameOfTheCredential.insets = new Insets(0, 0, 0, 5);
			gbc_jTextFieldNameOfTheCredential.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldNameOfTheCredential.gridx = 1;
			gbc_jTextFieldNameOfTheCredential.gridy = 0;
			jPanelNameOfACredential.add(getJTextFieldNameOfTheCredential(), gbc_jTextFieldNameOfTheCredential);
		}
		return jPanelNameOfACredential;
	}
	
	//---------------------------------------------------------
	//-------From here methods for the credential creation ----
	//---------------------------------------------------------
	
	/**
	 * Gets the created credential.
	 *
	 * @return the created credential
	 * @throws Exception the exception
	 */
	public AbstractCredential getCreatedCredential() throws Exception {
		if (createdCredential == null){
			createdCredential=createCredential();
		}
		return createdCredential;
	}


	private void setCreatedCredential(AbstractCredential createdCredential) {
		this.createdCredential = createdCredential;
		this.fillTextfieldsWithCredentialValues(createdCredential);
        getJComboBox().setEnabled(false);
 }
	
	
	/**
	 * Gets the modified credential.
	 *
	 * @return the modified credential
	 */
	public AbstractCredential getModifiedCredential() {
		return modifiedCredential;
	}

	/**
	 * Sets the modified credential.
	 *
	 * @param modifiedCredential the new modified credential
	 */
	public void setModifiedCredential(AbstractCredential modifiedCredential) {
		this.modifiedCredential = modifiedCredential;
	}
	

	/**
	 * Creates the credentials from JTextfields of the different JPanels for the credential creation.
	 * @throws Exception 
	 */
	private AbstractCredential createCredential() throws Exception {
		CredentialType type=(CredentialType) getJComboBox().getSelectedItem();
		AbstractCredential cred=null;
		if(type.equals(CredentialType.API_KEY)) {
			if(getJPanelApiKeyCredentials().hasUnsavedChanges()) {
				ApiKeyCredential apiCred=getJPanelApiKeyCredentials().getCredential();
				if(apiCred!=null) {
					cred=apiCred;
				}
			}
		}else if(type.equals(CredentialType.BEARER_TOKEN)) {
			if(getJPanelBearerTokenCredential().hasUnsavedChanges()) {
				BearerTokenCredential token=getJPanelBearerTokenCredential().getCredential();
				if(token!=null) {
					cred=token;
				}
			}
		}else if(type.equals(CredentialType.USERNAME_PASSWORD)) {
			if(getJPanelPasswordAuthenticationCredentials().hasUnsavedChanges()) {
				UserPasswordCredential passwordCred=getJPanelPasswordAuthenticationCredentials().getCredential();
				cred=passwordCred;
			}
		}
		
		this.putCredentialinWsCredentialStore(type, cred);
		return cred;
	}

	/**
	 * Put credential in {@link WsCredentialStore}.
	 *
	 * @param type the type
	 * @param cred the cred
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	private boolean putCredentialinWsCredentialStore(CredentialType type, AbstractCredential cred) throws Exception {
		boolean success = false;
		if (cred != null) {
			if (!getJTextFieldNameOfTheCredential().getText().isBlank()) {
				if (!WsCredentialStore.getInstance().getCredentialList().contains(cred)) {

					// Case of creating a new credential
					String name = "[" + type + "]" + getJTextFieldNameOfTheCredential().getText();
					if (!WsCredentialStore.getInstance().isCredentialnameAlreadyUsed(name)) {
						cred.setName(name);
						WsCredentialStore.getInstance().getCredentialList().add(cred);
						this.setCreatedCredential(cred);
						success = true;

					} else {
						throw new Exception("The credential name was used before, please change it. It must be unique!");
					}
				} else {

					// Case of updating an already created credential
					AbstractCredential oldCred;

					// Init old cred
					if (this.getCreatedCredential() != null) {
						oldCred = this.getCreatedCredential();
					} else {
						oldCred = WsCredentialStore.getInstance().getCredentialWithName(this.getJTextFieldNameOfTheCredential().getText());
					}

					// update old cred
					if (oldCred != null) {
						oldCred.setName("[" + type + "]" + this.getJTextFieldNameOfTheCredential().getText());
						CredentialType credType = oldCred.getCredentialType();
						String newName = "[" + type + "]" + this.getJTextFieldNameOfTheCredential().getText();
						if (WsCredentialStore.getInstance().isCredentialnameAlreadyUsed(newName)) {
							if (credType.equals(CredentialType.API_KEY)) {

								ApiKeyCredential apiKey = (ApiKeyCredential) oldCred;
								apiKey.setName(newName);
								apiKey.setApiKeyPrefix(
										this.getJPanelApiKeyCredentials().getJTextFieldKeyName().getText());
								apiKey.setApiKeyValue(
										this.getJPanelApiKeyCredentials().getJTextFieldKeyValue().getText());
							} else if (credType.equals(CredentialType.BEARER_TOKEN)) {

								BearerTokenCredential token = (BearerTokenCredential) oldCred;
								token.setName(newName);
								token.setJwtToken(new JwtToken(
										this.getJPanelBearerTokenCredential().getJTextFieldTokenValue().getText()));
							} else if (credType.equals(CredentialType.USERNAME_PASSWORD)) {

								UserPasswordCredential password = (UserPasswordCredential) oldCred;
								password.setUserName(this.getJPanelPasswordAuthenticationCredentials().getJTextFieldUsername().getText());
								password.setPassword(this.getJPanelPasswordAuthenticationCredentials().getJTextFieldUsername().getText());
								password.setName(newName);
							}
							this.setModifiedCredential(cred);
							success = true;
						}else {
							if(newName.equals(oldCred.getName())) {
								//Case nothing has changed
								return success;
							}else {
								throw new Exception("The credential name is already used, please create another one. It must be unique!");
							}
						}
					} else {
						throw new Exception("The credential name was not used before, please create another one. It must be unique!");
					}
				}
			} else {
				throw new Exception("The credential name must be specified");

			}
		}
		return success;
	}
	
	
	/**
	 * Adds a credential panel.
	 *
	 * @param credPanel the cred panel
	 */
	private void addCredentialPanel(JPanel credPanel) {
		GridBagConstraints gbc_jPanelNameOfACredential = new GridBagConstraints();
		gbc_jPanelNameOfACredential.insets = new Insets(0, 5, 5, 0);
		gbc_jPanelNameOfACredential.fill = GridBagConstraints.BOTH;
		gbc_jPanelNameOfACredential.gridx = 0;
		gbc_jPanelNameOfACredential.gridy = 1;
		getContentPane().add(getJPanelNameOfACredential(), gbc_jPanelNameOfACredential);
		GridBagConstraints gbc_panelApiKeyCredentials = new GridBagConstraints();
		gbc_panelApiKeyCredentials.insets = new Insets(0, 5, 5, 0);
		gbc_panelApiKeyCredentials.fill = GridBagConstraints.BOTH;
		gbc_panelApiKeyCredentials.gridx = 0;
		gbc_panelApiKeyCredentials.gridy = 2;
		getContentPane().add(credPanel, gbc_panelApiKeyCredentials);
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Fill the JComboBox.
	 */
	private void fillJComboBox() {
		CredentialType[] credentialTypes=CredentialType.values();
		for (CredentialType credentialType : credentialTypes) {
			this.getJComboBox().addItem(credentialType);
		}
		this.getJComboBox().setSelectedItem(CredentialType.API_KEY);
	}
	
	/**
	 * Fill specific JTextfields with credential values.
	 *
	 * @param createdCredential the created credential
	 */
	public void fillTextfieldsWithCredentialValues(AbstractCredential createdCredential) {
		if (createdCredential != null) {
			String credName=createdCredential.getName();
			if(credName.contains("]")) {
				String [] name=createdCredential.getName().split("\\]");
				this.getJTextFieldNameOfTheCredential().setText(name[1]);
			}else {
				this.getJTextFieldNameOfTheCredential().setText(createdCredential.getName());
			}
			CredentialType credType = createdCredential.getCredentialType();
			this.getJComboBox().setSelectedItem(createdCredential.getCredentialType());
			if (credType.equals(CredentialType.API_KEY)) {
				this.getJPanelApiKeyCredentials().setCredential((ApiKeyCredential) createdCredential);
				this.getJPanelApiKeyCredentials().fillApiKeyTextfields();
			} else if (credType.equals(CredentialType.BEARER_TOKEN)) {
				this.getJPanelBearerTokenCredential().setCredential((BearerTokenCredential) createdCredential);
				this.getJPanelBearerTokenCredential().fillJTextfieldTokenValue();
			} else if (credType.equals(CredentialType.USERNAME_PASSWORD)) {
				this.getJPanelPasswordAuthenticationCredentials().setCredential((UserPasswordCredential) createdCredential);
				this.getJPanelPasswordAuthenticationCredentials().fillPasswordTextfields();
			}
		}
	}
	
	//---------------------------------------------------------
	//-------From here overridden methods-----------------------
	//---------------------------------------------------------

	/* (non-Javadoc)
	* @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getJButtonCreateAndSaveCredential())) {
			try {
				//Check which Credential is selected
				if (this.getJComboBox().getSelectedItem() != null) {
					
					CredentialType type = (CredentialType) getJComboBox().getSelectedItem();
					
					//Check if modified credential is not empty, then updated it
					if (this.getModifiedCredential() != null) {
						this.putCredentialinWsCredentialStore(type, this.getModifiedCredential());
					} else {
						this.createCredential();
						this.putCredentialinWsCredentialStore(type, this.getCreatedCredential());
					}
				}

				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource().equals(getJComboBox())) {
			CredentialType type = (CredentialType) e.getItem();
			if (ItemEvent.SELECTED == e.getStateChange()) {
				if (type.equals(CredentialType.API_KEY)) {
					JPanel credPanel = getJPanelApiKeyCredentials();
					addCredentialPanel(credPanel);
				} else if (type.equals(CredentialType.BEARER_TOKEN)) {
					JPanel credPanel = getJPanelBearerTokenCredential();
					addCredentialPanel(credPanel);
				} else if (type.equals(CredentialType.USERNAME_PASSWORD)) {
					JPanel credPanel = getJPanelPasswordAuthenticationCredentials();
					addCredentialPanel(credPanel);
				}
			} else if (ItemEvent.DESELECTED == e.getStateChange()) {
				if (type.equals(CredentialType.API_KEY)) {
					remove(getJPanelApiKeyCredentials());
				} else if (type.equals(CredentialType.BEARER_TOKEN)) {
					remove(getJPanelBearerTokenCredential());
				} else if (type.equals(CredentialType.USERNAME_PASSWORD)) {
					remove(getJPanelPasswordAuthenticationCredentials());
				}
			}
		}
	}

	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	*/
	@Override
	public boolean hasUnsavedChanges() {
		AbstractCredential cred=null;
		try {
			cred = this.getCreatedCredential();
			if(cred==null) {
				cred = this.getModifiedCredential();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(cred!=null) {
			return true;
		}else {
			return hasUnsavedChanges;
		}		
	}

	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	*/
	@Override
	public boolean userConfirmedToChangeView() {
		return true;
	}

	/* (non-Javadoc)
	* @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	*/
	@Override
	public void insertUpdate(DocumentEvent e) {
		hasUnsavedChanges=true;		
	}

	/* (non-Javadoc)
	* @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	*/
	@Override
	public void removeUpdate(DocumentEvent e) {
		hasUnsavedChanges=true;
	}
	/* (non-Javadoc)
	* @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	*/
	@Override
	public void changedUpdate(DocumentEvent e) {
		hasUnsavedChanges=true;
	}
}
