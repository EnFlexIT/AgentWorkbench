package de.enflexit.awb.ws.ui.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.enflexit.awb.ws.client.ApiRegistration;
import de.enflexit.awb.ws.client.AwbApiRegistrationService;
import de.enflexit.awb.ws.client.CredentialAssignment;
import de.enflexit.awb.ws.client.CredentialType;
import de.enflexit.awb.ws.client.ServerURL;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.credential.ApiKeyCredential;
import de.enflexit.awb.ws.credential.BearerTokenCredential;
import de.enflexit.awb.ws.credential.UserPasswordCredential;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;

/**
 * The Class JPanelClientConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelClientConfiguration extends JPanel implements ActionListener,ListSelectionListener,WsConfigurationInterface {
	
	private static final long serialVersionUID = 7987858783733542296L;
	
	public static Dimension BUTTON_SIZE = new Dimension(26, 26);
	
	private JPanelClientBundle jPanelClientBundle;
	
	private JPanelCredentials jPanelCredentials;
	
	private JSplitPane jSplitPaneLeft;
	
	private JSplitPane jSplitPaneMiddleRight;
	
	private JSplitPane jSplitPaneMiddle;
	
	private JPanelServerURL jPanelServerURL;
	
	private JPanelAssignedCredentials jPanelAssignedCredentials;
	
	private JPanelCachedClientBundles jPanelCachedClientBundles;
	private JPanelCachedCredentials jPanelCachedCredentials;
	
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
	 * Gets the {@link JPanelClientBundle}
	 *
	 * @return the {@link JPanelClientBundle}
	 */
	public JPanelClientBundle getJPanelClientBundle() {
		if (jPanelClientBundle == null) {
			jPanelClientBundle = new JPanelClientBundle();
		    jPanelClientBundle.getJButtonCachedCredentialAssignmentsView().addActionListener(this);
			jPanelClientBundle.getJListApiRegistration().addListSelectionListener(this);
		}
		return jPanelClientBundle;
	}
	
	public JPanelCachedClientBundles getJPanelCachedClientBundles() {
		if (jPanelCachedClientBundles== null) {
			jPanelCachedClientBundles = new JPanelCachedClientBundles();
			jPanelCachedClientBundles.getJButtonBackToCredAssgnView().addActionListener(this);
			jPanelCachedClientBundles.getJButtonDeleteCredentialAssignment().addActionListener(this);
			jPanelCachedClientBundles.getJListCachedApiRegistration().addListSelectionListener(this);
		}
		return jPanelCachedClientBundles;
	}

	/**
	 * Gets the {@link JPanelCredentials}
	 *
	 * @return the {@link JPanelCredentials}
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
	
	public JPanelCachedCredentials getJPanelCachedCredentials() {
		if (jPanelCachedCredentials == null) {
			jPanelCachedCredentials = new JPanelCachedCredentials(null);
			jPanelCachedCredentials.getJListAssignedCredentials().addListSelectionListener(this);
			jPanelCachedCredentials.getJButtonDeleteCredentialAssignment().addActionListener(this);
			this.repaint();
			this.revalidate();
		}		
		return 	jPanelCachedCredentials;
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
			jPanelServerURL.getJButtonDeleteServerUrl().addActionListener(this);
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
	
	
	//-------------------------------------------------------------------------------------
	//--------------Helper Methods to configure and handle all events and components-----------------------
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
	 * Deletes a {@link CredentialAssignment}.
	 */
	private void deleteCachedCredentialAssignment() {
		if (this.getJPanelCachedClientBundles().getJListCachedApiRegistration().getSelectedValue()!=null) {
				if (this.getJPanelCachedCredentials().getJListAssignedCredentials().getSelectedValue() != null) {
					CredentialAssignment credAssgn = this.getJPanelCachedCredentials().getJListAssignedCredentials().getSelectedValue();
					AbstractCredential cred =WsCredentialStore.getInstance().getCredential(credAssgn.getIdCredential());
					int option = JOptionPane.showConfirmDialog(this,"Do you want to delete the Assignment of the credential with the name " + cred.getName()+ " of the type " + cred.getCredentialType() + "?","Deletion of a credential", JOptionPane.YES_NO_CANCEL_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						
						//Delete credAssignment
					    this.getJPanelCachedCredentials().getListCacheCredAssignment().add(credAssgn);
						WsCredentialStore.getInstance().getCacheCredentialAssignmentList().remove(credAssgn);
						String bundleName=this.getJPanelCachedClientBundles().getJListCachedApiRegistration().getSelectedValue();
						WsCredentialStore.getInstance().getBundleCredAssgnsMap().get(bundleName).remove(credAssgn);
						if(WsCredentialStore.getInstance().getBundleCredAssgnsMap().get(bundleName).size()==0) {
							WsCredentialStore.getInstance().getBundleCredAssgnsMap().remove(bundleName);
						}
						
						//Refresh panel
     					this.getJPanelCachedCredentials().fillCachedAssignedCredentialJList(bundleName);
						this.getJPanelCachedCredentials().getJListAssignedCredentials().revalidate();
						this.getJPanelCachedCredentials().getJListAssignedCredentials().repaint();
						//Refresh CredentialPanel because empty credential of an assignment will be deleted
						this.getJPanelCachedCredentials().fillCachedAssignedCredentialJList(bundleName);
						this.getJPanelCachedCredentials().getJListAssignedCredentials().revalidate();
						this.getJPanelCachedCredentials().getJListAssignedCredentials().repaint();
						//Refresh the client panel
						this.revalidate();
						this.repaint();
					}
				}else {
					JOptionPane.showMessageDialog(this,"Please select a assigned Credential in order to delete a Credential Assignment");
				}
		}else {
			JOptionPane.showMessageDialog(this,"Please select a Client-Bundle in order to delete a Credential Assignment");
		}
	}

	
	/**
	 * Delete all cached credential assignments.
	 *
	 * @param clientBundleName the client bundle name
	 * @return the deleted CredentialAssignments as a list
	 */
	public List<CredentialAssignment> deleteAllCachedCredentialAssignments() {
		if (this.getJPanelCachedClientBundles().getJListCachedApiRegistration().isSelectionEmpty()) {
			
			JOptionPane.showConfirmDialog(this, "Please select a credential assignment!","Select a credential assignment", JOptionPane.YES_OPTION);
			return new ArrayList<CredentialAssignment>();
			
		} else {
			
			int ret = JOptionPane.showConfirmDialog(this,"You want to delete all selected cached client bundles and their credential assignments?","Delete client bundles and credential assignments", JOptionPane.YES_OPTION);
			
			// Delete all credential assignments of the bundle
			List<CredentialAssignment> deletedClientBundles = new ArrayList<CredentialAssignment>();
			if (ret == JOptionPane.YES_OPTION) {
				
				List<String> clientBundleNames = this.getJPanelCachedClientBundles().getJListCachedApiRegistration().getSelectedValuesList();
				
				for (Iterator<String> iterator = clientBundleNames.iterator(); iterator.hasNext();) {
					String clientBundleName = (String) iterator.next();
					
					//Delete empty assigned credentials to avoid redundancy
					deleteEmptyCredentials(clientBundleName);
					
					//Remove the cached credential assignments
					WsCredentialStore.getInstance().getCacheCredentialAssignmentList().removeAll(WsCredentialStore.getInstance().getBundleCredAssgnsMap().get(clientBundleName));
					deletedClientBundles.addAll(WsCredentialStore.getInstance().getBundleCredAssgnsMap().remove(clientBundleName));
	               
					//Cache changes
					this.getJPanelCachedClientBundles().getDeletedApiRegistrations().add(clientBundleName);
				}
			}
			
			// Refill lists
			this.getJPanelCachedCredentials().refreshPanel();
			this.getJPanelCachedClientBundles().refillListModelCachedRegisteredApis();
			this.getJPanelCredentials().fillCredentialJListAndRepaint();
			
			// Refresh this panel
			this.revalidate();
			this.repaint();
			if (this.getJPanelCachedClientBundles().getJListCachedApiRegistration().getModel().getSize() == 0) {
				int opt = JOptionPane.showConfirmDialog(this,"You want to switch back to the non-cached Credential Assignments, because all cached Credential Assignments are deleted?","Switch to Credential Assignment View", JOptionPane.YES_OPTION);
				
			    //Switch back
				if (opt == JOptionPane.YES_OPTION) {
				this.getJSplitPaneLeft().setLeftComponent(this.getJPanelClientBundle());
				this.getJSplitPaneMiddle().setLeftComponent(this.getJPanelAssignedCredentials());
				this.revalidate();
				this.repaint();
				}
			}
			return deletedClientBundles;
		}
	}

	/**
	 * Delete empty credentials.
	 *
	 * @param clientBundleName the client bundle name
	 */
	private void deleteEmptyCredentials(String clientBundleName) {
		List<CredentialAssignment> credAssgns = WsCredentialStore.getInstance().getBundleCredAssgnsMap()
				.get(clientBundleName);
		for (Iterator<CredentialAssignment> credAssignments = credAssgns.iterator(); credAssignments.hasNext();) {
			CredentialAssignment credentialAssignment = (CredentialAssignment) credAssignments.next();
			AbstractCredential credential = WsCredentialStore.getInstance()
					.getCredential(credentialAssignment.getIdCredential());
			if (credential != null) {
				if (credential.getCredentialType() == CredentialType.API_KEY) {
					ApiKeyCredential apiKey = (ApiKeyCredential) credential;
					if (apiKey.isEmpty()) {
						WsCredentialStore.getInstance().getCredentialList().remove(credential);
						this.getJPanelCachedClientBundles().getDeletedCredentials().add(credential);
					}
				} else if (credential.getCredentialType() == CredentialType.BEARER_TOKEN) {
					BearerTokenCredential bearerToken = (BearerTokenCredential) credential;
					if (bearerToken.isEmpty()) {
						WsCredentialStore.getInstance().getCredentialList().remove(credential);
						this.getJPanelCachedClientBundles().getDeletedCredentials().add(credential);
					}
				} else if (credential.getCredentialType() == CredentialType.USERNAME_PASSWORD) {
					UserPasswordCredential password = (UserPasswordCredential) credential;
					if (password.isEmpty()) {
						WsCredentialStore.getInstance().getCredentialList().remove(credential);
						this.getJPanelCachedClientBundles().getDeletedCredentials().add(credential);
					}
				}
			}
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
	
	/**
	 * Resets the whole view to the default of the {@link WsCredentialStore} and reloads view.
	 *
	 */
	public void resetAndReloadView() {
		WsCredentialStore.getInstance().resetAndReloadWsCredStore();	
		if(this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue()!=null) {
		this.getJPanelAssignedCredentials().fillAssignedCredentialJList(this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue());
		}
		this.getJPanelClientBundle().refillListModelRegisteredApis();
		this.getJPanelCredentials().fillCredentialJListAndRepaint();
		this.getJPanelServerURL().fillJListServerUrlAndRepaint();
	}
	
	/**
	 * Fills the {@link JList} of the {@link JPanelAssignedCredentials} with all assigned credentials of the corresponding {@link AwbApiRegistrationService}.
	 */
	private void fillJListAssignedCredentials() {
		this.getJPanelAssignedCredentials().getJListAssignedCredentials().clearSelection();
		this.getJPanelCredentials().getJListCredentials().clearSelection();
		this.getJPanelServerURL().getJListServerUrl().clearSelection();
		this.getJPanelAssignedCredentials().fillAssignedCredentialJList(this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue());
	}
	
	/**
	 * Fills the {@link JList} of the {@link JPanelCachedClientBundles} with all assigned credentials of the corresponding cached {@link AwbApiRegistrationService}.
	 */
	private void fillJListCachedAssignedCredentials() {
		if(this.getJPanelCachedClientBundles().getJListCachedApiRegistration().getSelectedValue()!=null) {
		this.getJPanelCachedCredentials().getJListAssignedCredentials().clearSelection();
		this.getJPanelCredentials().getJListCredentials().clearSelection();
		this.getJPanelServerURL().getJListServerUrl().clearSelection();
		this.getJPanelCachedCredentials().fillCachedAssignedCredentialJList(this.getJPanelCachedClientBundles().getJListCachedApiRegistration().getSelectedValue());
		}
	}
	
	
	/**
	 * After the assigned credential is selected the corresponding {@link ServerURL} in JList of the {@link JPanelServerURL} and the corresponding {@link AbstractCredential} in the {@link JList} of the {@link JPanelCredentials}
	 */
	private void selectAllElementsOfaCredentialAssignment() {
		if (this.getJPanelAssignedCredentials().getJListAssignedCredentials().getSelectedValue() != null) {
			
			AbstractCredential cred=this.getJPanelAssignedCredentials().getJListAssignedCredentials().getSelectedValue();
			this.getJPanelCredentials().getJListCredentials().setSelectedValue(cred, true);
			
			if (this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue() != null) {
				
				//Can not be == null because then the assignment list is empty but still checking
				ApiRegistration apiReg=this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue();
				List<CredentialAssignment> credAssgn=WsCredentialStore.getInstance().getCredentialAssignmentsWithOneCredential(apiReg, cred);
				
				//Get the corresponding ServerURL
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

	/**
	 * After the assigned credential is selected the corresponding {@link ServerURL}
	 * in JList of the {@link JPanelServerURL} and the corresponding
	 * {@link AbstractCredential} in the {@link JList} of the
	 * {@link JPanelCredentials}
	 */
	private void selectAllElementsOfaCachedCredentialAssignment() {
		if (this.getJPanelCachedCredentials().getJListAssignedCredentials().getSelectedValue() != null) {

			CredentialAssignment credAssignment = this.getJPanelCachedCredentials().getJListAssignedCredentials()
					.getSelectedValue();
			AbstractCredential cred =WsCredentialStore.getInstance().getCredential(credAssignment.getIdCredential());
			this.getJPanelCredentials().getJListCredentials().setSelectedValue(cred, true);
			
			// Get the corresponding ServerURL
			ServerURL server=WsCredentialStore.getInstance().getServerURL(credAssignment.getIdServerURL());
			this.getJPanelServerURL().getJListServerUrl().setSelectedValue(server, true);

			if (this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue() != null) {
				
				ListModel<String> bundleNames = this.getJPanelCachedClientBundles().getJListCachedApiRegistration().getModel();
				for (int i = 0; i < bundleNames.getSize(); i++) {
                    String bundleName=bundleNames.getElementAt(i);
                    if(bundleName.equals(credAssignment.getIdApiRegistrationDefaultBundleName())) {
                    	this.getJPanelCachedClientBundles().getJListCachedApiRegistration().setSelectedValue(bundleName, true);
                    }
				}
			}
		}
	}
	
	/**
	 * As a reaction the {@link JButton} Create a CredentialAssignment of the {@link JPanelAssignedCredentials}, this method creates A credential assignment.
	 */
	private void createACredAssignment() {
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
	}
	
	//-------------------------------------------------------------------------------------
	//--------------Overridden Methods and Non GUI Getter and Setters-----------------------
	//-------------------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		if(this.getJPanelAssignedCredentials().hasUnsavedChanges()) {
			this.getJPanelAssignedCredentials().revalidate();
			this.getJPanelAssignedCredentials().repaint();
			this.revalidate();
			this.repaint();
			return true;
		}
		if(this.getJPanelClientBundle().hasUnsavedChanges()) {
		   this.getJPanelClientBundle().revalidate();
		   this.getJPanelClientBundle().repaint();
		   this.revalidate();
		   this.repaint();
			return true;
		}
		if(this.getJPanelCredentials().hasUnsavedChanges()) {
			this.getJPanelCredentials().revalidate();
			this.getJPanelCredentials().repaint();
			this.revalidate();
			this.repaint();
			return true;
		}
		if(this.getJPanelServerURL().hasUnsavedChanges()) {
			this.getJPanelServerURL().revalidate();
			this.getJPanelServerURL().repaint();
			this.revalidate();
			this.repaint();
			return true;
		}
		if(this.getJPanelCachedClientBundles().hasUnsavedChanges()) {
			//Refresh
			this.getJPanelCachedClientBundles().revalidate();
			this.getJPanelCachedClientBundles().repaint();
			this.getJPanelCredentials().revalidate();
			this.getJPanelCredentials().repaint();
			this.revalidate();
			this.repaint();
			return true;
		}
		if(this.getJPanelCachedCredentials().hasUnsavedChanges()) {
			this.getJPanelCredentials().revalidate();
			this.getJPanelCredentials().repaint();
			this.revalidate();
			this.repaint();
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	 */
	@Override
	public boolean userConfirmedToChangeView() {

		if (this.hasUnsavedChanges()==false) return true;
		
		String title = "Save client settings?";
		String message = "Would you like to save the changes in the client settings ?";
		int userAnswer = JOptionPane.showConfirmDialog(this.getParent(), message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null);
		if (userAnswer==JOptionPane.YES_OPTION) {
			// --- Save the changes ---------------------------------------
			WsCredentialStore.getInstance().save();
			
		} else if (userAnswer==JOptionPane.NO_OPTION) {
			// --- Revert to last saved settings --------------------------
			this.resetAndReloadView();	
		} else if (userAnswer==JOptionPane.CANCEL_OPTION) {
			// --- Return to previous selection ---------------------------
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	* @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getJPanelAssignedCredentials().getJButtonCreateACredentialAssignment())) {
			createACredAssignment();
		} else if (e.getSource().equals(this.getJPanelAssignedCredentials().getJButtonDeleteCredentialAssignment())) {
			deleteCredentialAssignment();
		}else if(e.getSource().equals(this.getJPanelClientBundle().getJButtonCachedCredentialAssignmentsView())) {
			this.getJSplitPaneLeft().setLeftComponent(this.getJPanelCachedClientBundles());
			this.getJSplitPaneMiddle().setLeftComponent(this.getJPanelCachedCredentials());
			this.revalidate();
			this.repaint();
		}else if(e.getSource().equals(this.getJPanelCachedClientBundles().getJButtonBackToCredAssgnView())) {
			this.getJSplitPaneLeft().setLeftComponent(this.getJPanelClientBundle());
			this.getJSplitPaneMiddle().setLeftComponent(this.getJPanelAssignedCredentials());
			this.revalidate();
			this.repaint();
		}else if(e.getSource().equals(this.getJPanelCachedCredentials().getJButtonDeleteCredentialAssignment())) {
			this.deleteCachedCredentialAssignment();
		}else if(e.getSource().equals(this.getJPanelCachedClientBundles().getJButtonDeleteCredentialAssignment())){
			this.deleteAllCachedCredentialAssignments();
	        this.getJPanelCachedCredentials().refreshPanel();
			this.revalidate();
			this.repaint();
		}else {
			this.revalidate();
			this.repaint();
		}
	}
	
	/* (non-Javadoc)
	* @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	*/
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(this.getJPanelClientBundle().getJListApiRegistration())) {
			if (this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue() != null) {
				this.fillJListAssignedCredentials();
			}
		}else if (e.getSource().equals(this.getJPanelCachedClientBundles().getJListCachedApiRegistration())) {
			if (this.getJPanelCachedClientBundles().getJListCachedApiRegistration().getSelectedValue() != null) {
				this.fillJListCachedAssignedCredentials();
			}
		}else if(e.getSource().equals(getJPanelAssignedCredentials().getJListAssignedCredentials())) {
			this.selectAllElementsOfaCredentialAssignment();	
		}else if(e.getSource().equals(getJPanelCachedCredentials().getJListAssignedCredentials())) {
			this.selectAllElementsOfaCachedCredentialAssignment();
		}
	}
}
	
