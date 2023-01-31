package de.enflexit.awb.ws.ui.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.awb.ws.client.ServerURL;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;
import de.enflexit.common.swing.JDialogSizeAndPostionController;
import de.enflexit.common.swing.JDialogSizeAndPostionController.JDialogPosition;

/**
 * The Class JDialogCreateServerURL enables the creation of a {@link ServerURL}.
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
public class JDialogCreateServerURL extends JDialog implements ActionListener,DocumentListener,WsConfigurationInterface {
	
	/**
	 * Instantiates a new JDialog to create a {@link ServerURL}.
	 *
	 * @param owner the owner
	 */
	public JDialogCreateServerURL(Window owner, boolean isVisible) {
		super(owner);
		this.getContentPane().add(getJPanelServerURL(), BorderLayout.CENTER);
		JDialogSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
		this.setSize(300, 120);
		this.setModal(true);
		
		if(isVisible) {
	     this.setVisible(true);
		}
	}
	
	/**
	 * Instantiates a new {@link JDialog} for editing an existing {@link ServerURL}.
	 *
	 * @param owner the owner
	 * @param serverURL the server URL
	 */
	public JDialogCreateServerURL(Window owner,boolean isVisible,ServerURL serverURL) {
		super(owner);
		if(serverURL!=null) {
			this.setModifiedServerURL(serverURL);
		}
		getContentPane().add(getJPanelServerURL(), BorderLayout.CENTER);
		JDialogSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
		this.getJButtonCreateServerURL().setText("Save changes");
		this.setSize(300, 120);
		this.setModal(true);
		if(isVisible) {
		     this.setVisible(true);
		}
	}

	private static final long serialVersionUID = -4595711046276221996L;
	private JPanel jPanelServerURL;
	private JLabel jLableServerURL;
	private JLabel jLableURLofServer;
	private JTextField jTextFieldServerUrl;
	private JButton jButtonCreateServerURL;
	
	private ServerURL createdServerURL=null;
	private ServerURL modifiedServerURL=null;

	private boolean unsavedChanges=false;
	
	

	private JPanel getJPanelServerURL() {
		if (jPanelServerURL == null) {
			jPanelServerURL = new JPanel();
			GridBagLayout gbl_jPanelServerURL = new GridBagLayout();
			gbl_jPanelServerURL.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelServerURL.rowHeights = new int[]{0, 0, 0, 0};
			gbl_jPanelServerURL.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelServerURL.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			jPanelServerURL.setLayout(gbl_jPanelServerURL);
			GridBagConstraints gbc_jLableServerURL = new GridBagConstraints();
			gbc_jLableServerURL.insets = new Insets(0, 0, 5, 5);
			gbc_jLableServerURL.gridx = 1;
			gbc_jLableServerURL.gridy = 0;
			jPanelServerURL.add(getJLableServerURL(), gbc_jLableServerURL);
			GridBagConstraints gbc_jLableURLofServer = new GridBagConstraints();
			gbc_jLableURLofServer.anchor = GridBagConstraints.EAST;
			gbc_jLableURLofServer.insets = new Insets(0, 0, 5, 5);
			gbc_jLableURLofServer.gridx = 0;
			gbc_jLableURLofServer.gridy = 1;
			jPanelServerURL.add(getJLableURLofServer(), gbc_jLableURLofServer);
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 5, 5);
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 1;
			jPanelServerURL.add(getJTextFieldServerUrl(), gbc_textField);
			GridBagConstraints gbc_jButtonCreateServerURL = new GridBagConstraints();
			gbc_jButtonCreateServerURL.insets = new Insets(0, 5, 5, 5);
			gbc_jButtonCreateServerURL.gridx = 0;
			gbc_jButtonCreateServerURL.gridy = 2;
			jPanelServerURL.add(getJButtonCreateServerURL(), gbc_jButtonCreateServerURL);
		}
		return jPanelServerURL;
	}
	
	//----------------------------------------------------------
	//--------------------Getter and Setter---------------------
	//----------------------------------------------------------
	
	private JLabel getJLableServerURL() {
		if (jLableServerURL == null) {
			jLableServerURL = new JLabel("Server-URL");
			jLableServerURL.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableServerURL;
	}
	private JLabel getJLableURLofServer() {
		if (jLableURLofServer == null) {
			jLableURLofServer = new JLabel("URL of the Server");
			jLableURLofServer.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableURLofServer;
	}
	private JTextField getJTextFieldServerUrl() {
		if (jTextFieldServerUrl == null) {
			jTextFieldServerUrl = new JTextField();
			jTextFieldServerUrl.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldServerUrl.setColumns(10);
			
			//Set modifiedServerURl as text
			if(this.getModifiedServerURL()!=null && !this.getModifiedServerURL().getServerURL().isEmpty()) {
				jTextFieldServerUrl.setText(this.getModifiedServerURL().getServerURL());
			}
			jTextFieldServerUrl.getDocument().addDocumentListener(this);
		}
		return jTextFieldServerUrl;
	}
	private JButton getJButtonCreateServerURL() {
		if (jButtonCreateServerURL == null) {
			jButtonCreateServerURL = new JButton("Create & Save");
			jButtonCreateServerURL.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCreateServerURL.addActionListener(this);
		}
		return jButtonCreateServerURL;
	}
	
	public ServerURL getServerURL() {
		return createdServerURL;
	}
	
	public void setServerURL(ServerURL serverUrl) {
		createdServerURL=serverUrl;
		getJTextFieldServerUrl().setText(serverUrl.getServerURL());
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Gets the modified server URL.
	 *
	 * @return the modified server URL
	 */
	public ServerURL getModifiedServerURL() {
		return modifiedServerURL;
	}

	/**
	 * Sets the modified server URL.
	 *
	 * @param modifiedServerURL the new modified server URL
	 */
	public void setModifiedServerURL(ServerURL modifiedServerURL) {
		this.modifiedServerURL = modifiedServerURL;
		if(modifiedServerURL!=null) {
		this.getJTextFieldServerUrl().setText(modifiedServerURL.getServerURL());
		}
	}
	
	
	//----------------------------------------------------------
	//--------------------Overridden Methods---------------------
	//----------------------------------------------------------
	
	/* (non-Javadoc)
	* @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getJButtonCreateServerURL())) {
			if (!getJTextFieldServerUrl().getText().isBlank()) {
				//Modified URL
				if (this.getModifiedServerURL() != null) {
					if (this.hasUnsavedChanges()) {
						
						// Save changes runtime
						this.getModifiedServerURL().setServerURL(this.getJTextFieldServerUrl().getText());
						if (WsCredentialStore.getInstance().getServerURLList().contains(this.getModifiedServerURL())) {
							WsCredentialStore.getInstance().getServerURLList().remove(this.getModifiedServerURL());
							WsCredentialStore.getInstance().getServerURLList().add(this.getModifiedServerURL());
						}else {
						   WsCredentialStore.getInstance().getServerURLList().add(this.getModifiedServerURL());
						}
						unsavedChanges = true;
						this.dispose();
					}
				} else {
					createdServerURL = new ServerURL(getJTextFieldServerUrl().getText());
					if (!WsCredentialStore.getInstance().getServerURLList().contains(createdServerURL)) {
						WsCredentialStore.getInstance().getServerURLList().add(createdServerURL);
					}
					unsavedChanges = true;
					this.dispose();
				}

			}
		}
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
		unsavedChanges=true;
	}

	/* (non-Javadoc)
	* @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	*/
	@Override
	public void removeUpdate(DocumentEvent e) {
		unsavedChanges=true;
	}

	/* (non-Javadoc)
	* @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	*/
	@Override
	public void changedUpdate(DocumentEvent e) {
		unsavedChanges=true;		
	}
}
