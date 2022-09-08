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
	private boolean hasUnsavedChanges=false;
	private JPanel jPanelNameOfACredential;
	private JLabel jLableNameOfTheCredential;
	private JTextField jTextFieldNameOfTheCredential;
	
	public JDialogCredentialCreation(Window owner) {
		super(owner);
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

	/**
	 * Sets the created credential.
	 *
	 * @param createdCredential the new created credential
	 */
	public void setCreatedCredential(AbstractCredential createdCredential) {
		this.createdCredential = createdCredential;
		fillTextfieldsWithCredentialValues(createdCredential);
        getJComboBox().setEnabled(false);
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
		putCredentialinWsCredentialStore(type, cred);
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
		boolean success=false;
		if (cred != null) {
			if (!getJTextFieldNameOfTheCredential().getText().isBlank()) {
				if (!WsCredentialStore.getInstance().getCredentialList().contains(cred)) {
					
					// Case of creating a new credential
					
					AbstractCredential abstrCred = WsCredentialStore.getInstance().getCredentialWithName(getJTextFieldNameOfTheCredential().getText()+"["+type+"]");
					if (abstrCred == null) {
						cred.setName("["+type+"]"+getJTextFieldNameOfTheCredential().getText());
						WsCredentialStore.getInstance().getCredentialList().add(cred);
						setCreatedCredential(cred);
						success=true;
					}else {
						throw new Exception("The credential name was used before, please change it. It must be unique!");
						
					}
				} else {
					
                  // Case of updating an already created credential
					
					AbstractCredential abstrCred = WsCredentialStore.getInstance().getCredentialWithName(getJTextFieldNameOfTheCredential().getText());
					if (abstrCred == null) {
						cred.setName("["+type+"]"+getJTextFieldNameOfTheCredential().getText());
						WsCredentialStore.getInstance().updateCredentialInCredentialList(abstrCred);
						setCreatedCredential(cred);
						success=true;
						
					}else {
						throw new Exception("The credential name was used before, please change it. It must be unique!");
						
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
			comboBox.addItem(credentialType);
		}
		comboBox.setSelectedItem(CredentialType.API_KEY);
	}
	
	/**
	 * Fill specific Jtextfields with credential values.
	 *
	 * @param createdCredential the created credential
	 */
	private void fillTextfieldsWithCredentialValues(AbstractCredential createdCredential) {
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
				getJPanelApiKeyCredentials().setCredential((ApiKeyCredential) createdCredential);
				getJPanelApiKeyCredentials().fillApiKeyTextfields();
			} else if (credType.equals(CredentialType.BEARER_TOKEN)) {
				getJPanelBearerTokenCredential().setCredential((BearerTokenCredential) createdCredential);
				getJPanelBearerTokenCredential().fillJTextfieldTokenValue();
			} else if (credType.equals(CredentialType.USERNAME_PASSWORD)) {
				getJPanelPasswordAuthenticationCredentials().setCredential((UserPasswordCredential) createdCredential);
				getJPanelPasswordAuthenticationCredentials().fillPasswordTextfields();
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
		if(e.getSource().equals(getJButtonCreateAndSaveCredential())) {
			try {
				getCreatedCredential();
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
			cred = getCreatedCredential();
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
		return hasUnsavedChanges();
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
