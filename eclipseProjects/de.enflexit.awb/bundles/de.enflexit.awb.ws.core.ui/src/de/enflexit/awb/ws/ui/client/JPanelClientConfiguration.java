package de.enflexit.awb.ws.ui.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.enflexit.awb.ws.client.ApiRegistration;
import de.enflexit.awb.ws.client.CredentialAssignment;
import de.enflexit.awb.ws.client.ServerURL;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;

// TODO: Auto-generated Javadoc
/**
 * The Class JPanelClientConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelClientConfiguration extends JPanel implements ActionListener,ListSelectionListener,WsConfigurationInterface {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7987858783733542296L;
	
	/** The button size. */
	public static Dimension BUTTON_SIZE = new Dimension(26, 26);
	
	/** The j panel client bundle. */
	private JPanelClientBundle jPanelClientBundle;
	
	/** The j panel credentials. */
	private JPanelCredentials jPanelCredentials;
	
	/** The j split pane left. */
	private JSplitPane jSplitPaneLeft;
	
	/** The j split pane middle right. */
	private JSplitPane jSplitPaneMiddleRight;
	
	/** The j split pane middle. */
	private JSplitPane jSplitPaneMiddle;
	
	/** The j panel server URL. */
	private JPanelServerURL jPanelServerURL;
	
	/** The j panel assigned credentials. */
	private JPanelAssignedCredentials jPanelAssignedCredentials;
	
	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelClientConfiguration() {
		this.initialize();
	}
	
	/**
	 * Instantiates a new j panel client configuration.
	 *
	 * @param apiReg the api reg
	 */
	public JPanelClientConfiguration(ApiRegistration apiReg) {
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jSplitPaneLeft = new GridBagConstraints();
		gbc_jSplitPaneLeft.insets = new Insets(10, 10, 10, 10);
		gbc_jSplitPaneLeft.fill = GridBagConstraints.BOTH;
		gbc_jSplitPaneLeft.gridx = 0;
		gbc_jSplitPaneLeft.gridy = 0;
		this.add(this.getJSplitPaneLeft(), gbc_jSplitPaneLeft);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible==true) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					JPanelClientConfiguration.this.getJSplitPaneLeft().setDividerLocation(0.333);
					JPanelClientConfiguration.this.validate();
				}
			});
		}
	}
	
	
	/**
	 * Gets the j panel client bundle.
	 *
	 * @return the j panel client bundle
	 */
	public JPanelClientBundle getJPanelClientBundle() {
		if (jPanelClientBundle == null) {
			jPanelClientBundle = new JPanelClientBundle();
			jPanelClientBundle.getJListApiRegistration().addListSelectionListener(this);
		}
		return jPanelClientBundle;
	}

	/**
	 * Gets the j panel credentials.
	 *
	 * @return the j panel credentials
	 */
	public JPanelCredentials getJPanelCredentials() {
		if (jPanelCredentials == null) {
			jPanelCredentials = new JPanelCredentials();
			jPanelCredentials.getJListCredentials().addListSelectionListener(this);
			this.repaint();
			this.revalidate();
		}		
		return jPanelCredentials;
	}

	/**
	 * Gets the j split pane middle right.
	 *
	 * @return the j split pane middle right
	 */
	private JSplitPane getJSplitPaneMiddleRight() {
		if (jSplitPaneMiddleRight == null) {
			jSplitPaneMiddleRight = new JSplitPane();
			jSplitPaneMiddleRight.setResizeWeight(0.5);
			jSplitPaneMiddleRight.setDividerSize(5);
			jSplitPaneMiddleRight.setBorder(BorderFactory.createEmptyBorder());
			jSplitPaneMiddleRight.setRightComponent(this.getJPanelCredentials());
			jSplitPaneMiddleRight.setLeftComponent(this.getJSplitPaneMiddle());
		}
		return jSplitPaneMiddleRight;
	}
	
	/**
	 * Gets the j split pane middle.
	 *
	 * @return the j split pane middle
	 */
	private JSplitPane getJSplitPaneMiddle() {
		if (jSplitPaneMiddle == null) {
			jSplitPaneMiddle = new JSplitPane();
			jSplitPaneMiddle.setDividerSize(8);
			jSplitPaneMiddle.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPaneMiddle.setBorder(BorderFactory.createEmptyBorder());
			jSplitPaneMiddle.setRightComponent(this.getJPanelServerURL());
			jSplitPaneMiddle.setLeftComponent(this.getJPanelAssignedCredentials());
		}
		return jSplitPaneMiddle;
	}
	
	/**
	 * Gets the j panel server URL.
	 *
	 * @return the j panel server URL
	 */
	private JPanelServerURL getJPanelServerURL() {
		if (jPanelServerURL == null) {
			jPanelServerURL = new JPanelServerURL();
			jPanelServerURL.getJListServerUrl().addListSelectionListener(this);
		}
		return jPanelServerURL;
	}
	
	/**
	 * Gets the j panel assigned credentials.
	 *
	 * @return the j panel assigned credentials
	 */
	private JPanelAssignedCredentials getJPanelAssignedCredentials() {
		if (jPanelAssignedCredentials == null) {
			jPanelAssignedCredentials = new JPanelAssignedCredentials();
			jPanelAssignedCredentials.getJButtonCreateACredentialAssignment().addActionListener(this);
			jPanelAssignedCredentials.getJButtonDeleteCredentialAssignment().addActionListener(this);
			jPanelAssignedCredentials.getJListAssignedCredentials().addListSelectionListener(this);
		}
		return jPanelAssignedCredentials;
	}
	
	
	//-------------------------------------------------------------------------------------
	//--------------Overriden Methods and Non GUI Getter and Setters-----------------------
	//-------------------------------------------------------------------------------------
	
	/**
	 * Deletes a {@link CredentialAssignment}.
	 */
	private void deleteCredentialAssignment() {
		if (getJPanelClientBundle().getJListApiRegistration().getSelectedValue() != null) {
				if (getJPanelAssignedCredentials().getJListAssignedCredentials().getSelectedValue() != null) {
					ApiRegistration apiReg = getJPanelClientBundle().getJListApiRegistration().getSelectedValue();
					AbstractCredential cred = this.getJPanelAssignedCredentials().getJListAssignedCredentials().getSelectedValue();
					int option = JOptionPane.showConfirmDialog(this,"Do you want to delete the Assignment of the credential with the name " + cred.getName()+ " of the type " + cred.getCredentialType() + "?","Deletion of a credential", JOptionPane.YES_NO_CANCEL_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						List<CredentialAssignment> credAssgn = WsCredentialStore.getInstance().getCredentialAssignmentsWithOneCredential(apiReg, cred);
		                for (CredentialAssignment credentialAssignment : credAssgn) {
							credAssgn.remove(credentialAssignment);
							this.getJPanelAssignedCredentials().getListCacheDeletedCredAssignment().add(credentialAssignment);
						}
						this.getJPanelAssignedCredentials().fillAssignedCredentialJList(apiReg);
						this.getJPanelAssignedCredentials().getJListAssignedCredentials().revalidate();
						this.getJPanelAssignedCredentials().getJListAssignedCredentials().repaint();
						this.revalidate();
						this.repaint();
					}
				}else {
					JOptionPane.showMessageDialog(this,"Please select a Credential in order to delete a Credential Assignment");
				}
		}else {
			JOptionPane.showMessageDialog(this,"Please select a Client-Bundle in order to delete a Credential Assignment");
		}
	}
	
	/**
	 * Creates a {@link CredentialAssignment}.
	 *
	 * @param apiReg the api reg
	 * @param serverURL the server URL
	 * @param credential the credential
	 */
	private void createCredAssgn(ApiRegistration apiReg, ServerURL serverURL, AbstractCredential credential) {
		if(WsCredentialStore.getInstance().getCredentialAssignment(apiReg, credential, serverURL)==null) {
			CredentialAssignment credAssgn = new CredentialAssignment();
			credAssgn.setIdApiRegistrationDefaultBundleName(apiReg.getClientBundleName());
			credAssgn.setIdCredential(credential.getID());
			credAssgn.setIdServerURL(serverURL.getID());
			if(WsCredentialStore.getInstance().putCredAssgnInCredAssgnList(credAssgn)) {
				this.getJPanelAssignedCredentials().getListCacheCredAssignment().add(credAssgn);
				this.getJPanelAssignedCredentials().fillAssignedCredentialJList(this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue());
				this.getJPanelAssignedCredentials().getJListAssignedCredentials().revalidate();
				this.getJPanelAssignedCredentials().getJListAssignedCredentials().repaint();
				this.revalidate();
				this.repaint();
			} else{
				JOptionPane.showMessageDialog(this,"This credential is already assigned to the selected Server and Client! Please select another credential!");
			}
		}else {
			JOptionPane.showMessageDialog(this,"This credential is already assigned to the selected Server and Client! Please select another credential!");
		}
	}
	
	//-------------------------------------------------------------------------------------
	//--------------Overriden Methods and Non GUI Getter and Setters-----------------------
	//-------------------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		if(this.getJPanelAssignedCredentials().hasUnsavedChanges()) {
			return true;
		}
		if(this.getJPanelClientBundle().hasUnsavedChanges()) {
			return true;
		}
		if(this.getJPanelCredentials().hasUnsavedChanges()) {
			return true;
		}
		if(this.getJPanelServerURL().hasUnsavedChanges()) {
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	 */
	@Override
	public boolean userConfirmedToChangeView() {
		return false;
	}
	
	/**
	 * Gets the j split pane left.
	 *
	 * @return the j split pane left
	 */
	private JSplitPane getJSplitPaneLeft() {
		if (jSplitPaneLeft == null) {
			jSplitPaneLeft = new JSplitPane();
			jSplitPaneLeft.setResizeWeight(0.3);
			jSplitPaneLeft.setDividerSize(5);
			jSplitPaneLeft.setBorder(BorderFactory.createEmptyBorder());
			jSplitPaneLeft.setLeftComponent(this.getJPanelClientBundle());
			jSplitPaneLeft.setRightComponent(this.getJSplitPaneMiddleRight());
		}
		return jSplitPaneLeft;
	}
	
	/* (non-Javadoc)
	* @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getJPanelAssignedCredentials().getJButtonCreateACredentialAssignment())) {
			if (getJPanelClientBundle().getJListApiRegistration().getSelectedValue() != null) {
				if (getJPanelServerURL().getJListServerUrl().getSelectedValue() != null) {
					if (getJPanelCredentials().getJListCredentials().getSelectedValue() != null) {
						ApiRegistration apiReg = getJPanelClientBundle().getJListApiRegistration().getSelectedValue();
						ServerURL serverURL = getJPanelServerURL().getJListServerUrl().getSelectedValue();
						AbstractCredential credential = getJPanelCredentials().getJListCredentials().getSelectedValue();
						createCredAssgn(apiReg, serverURL, credential);
					} else {
						JOptionPane.showMessageDialog(this,"Please select a Credential in order to create a Credential Assignment");
					}
				} else {
					JOptionPane.showMessageDialog(this,"Please select a Server-URL in order to create a Credential Assignment");
				}
			} else {
				JOptionPane.showMessageDialog(this,"Please select a Client-Bundle in order to create a Credential Assignment");
			}
		} else if (e.getSource().equals(getJPanelAssignedCredentials().getJButtonDeleteCredentialAssignment())) {
			deleteCredentialAssignment();

		}
	}
	
	/* (non-Javadoc)
	* @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	*/
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(this.getJPanelServerURL().getJListServerUrl())) {

		} else if (e.getSource().equals(getJPanelClientBundle().getJListApiRegistration())) {
			if (this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue() != null) {
				this.getJPanelAssignedCredentials().fillAssignedCredentialJList(this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue());
			}
		} else if (e.getSource().equals(getJPanelCredentials().getJListCredentials())) {

		}else if(e.getSource().equals(getJPanelAssignedCredentials().getJListAssignedCredentials())) {
			if (this.getJPanelAssignedCredentials().getJListAssignedCredentials().getSelectedValue() != null) {
				
				AbstractCredential cred=this.getJPanelAssignedCredentials().getJListAssignedCredentials().getSelectedValue();
				this.getJPanelCredentials().getJListCredentials().setSelectedValue(cred, true);
				
				if (this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue() != null) {
					
					//Can not be ==null because then the assignment list is empty but still checking
					ApiRegistration apiReg=this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue();
					List<CredentialAssignment> credAssgn=WsCredentialStore.getInstance().getCredentialAssignmentsWithOneCredential(apiReg, cred);
					
					if(credAssgn.size()==1) {
						UUID serverID=credAssgn.get(0).getIdServerURL();
						for(int i = 0; i< this.getJPanelServerURL().getJListServerUrl().getModel().getSize();i++){
						    ServerURL server=this.getJPanelServerURL().getJListServerUrl().getModel().getElementAt(i);
						    if(server.getID().equals(serverID)){
						    	this.getJPanelServerURL().getJListServerUrl().setSelectedValue(server, true);
						    	break;
						    }
						}
					}else {
						String serverID=apiReg.getDefaultURL();
						for(int i = 0; i< this.getJPanelServerURL().getJListServerUrl().getModel().getSize();i++){
						    ServerURL server=this.getJPanelServerURL().getJListServerUrl().getModel().getElementAt(i);
						    if(server.getServerURL().equals(serverID)){
						    	this.getJPanelServerURL().getJListServerUrl().setSelectedValue(server, true);
						    	break;
						    }
						}
					}
				}
			}	
		}
	}
}
