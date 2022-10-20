package de.enflexit.awb.ws.ui.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
	
	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelClientConfiguration() {
		this.initialize();
	}
	
	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelClientConfiguration(ApiRegistration apiReg) {
		this.initialize();
	}
	
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
	
	
	public JPanelClientBundle getJPanelClientBundle() {
		if (jPanelClientBundle == null) {
			jPanelClientBundle = new JPanelClientBundle();
			jPanelClientBundle.getJListApiRegistration().addListSelectionListener(this);
		}
		return jPanelClientBundle;
	}

	public JPanelCredentials getJPanelCredentials() {
		if (jPanelCredentials == null) {
			jPanelCredentials = new JPanelCredentials();
			jPanelCredentials.getJListCredentials().addListSelectionListener(this);
		}		
		return jPanelCredentials;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	 */
	@Override
	public boolean userConfirmedToChangeView() {
		return false;
	}
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
	private JPanelServerURL getJPanelServerURL() {
		if (jPanelServerURL == null) {
			jPanelServerURL = new JPanelServerURL();
			jPanelServerURL.getJListServerUrl().addListSelectionListener(this);
		}
		return jPanelServerURL;
	}
	private JPanelAssignedCredentials getJPanelAssignedCredentials() {
		if (jPanelAssignedCredentials == null) {
			jPanelAssignedCredentials = new JPanelAssignedCredentials();
			jPanelAssignedCredentials.getJButtonCreateACredentialAssignment().addActionListener(this);
			jPanelAssignedCredentials.getJButtonDeleteCredentialAssignment().addActionListener(this);
			jPanelAssignedCredentials.getJListAssignedCredentials().getSelectionModel().addListSelectionListener(this);
		}
		return jPanelAssignedCredentials;
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
						if(WsCredentialStore.getInstance().getCredentialAssignment(apiReg, credential, serverURL)==null) {
							CredentialAssignment credAssgn = new CredentialAssignment();
							credAssgn.setIdApiRegistrationDefaultBundleName(apiReg.getClientBundleName());
							credAssgn.setIdCredential(credential.getID());
							credAssgn.setIdServerURL(serverURL.getID());
							if(WsCredentialStore.getInstance().putCredAssgnInCredAssgnList(credAssgn)) {
								WsCredentialStore.getInstance().save();
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
			if (getJPanelClientBundle().getJListApiRegistration().getSelectedValue() != null) {
					if (getJPanelAssignedCredentials().getJListAssignedCredentials().getSelectedValue() != null) {
						ApiRegistration apiReg = getJPanelClientBundle().getJListApiRegistration().getSelectedValue();
						AbstractCredential cred = this.getJPanelAssignedCredentials().getJListAssignedCredentials().getSelectedValue();
						int option = JOptionPane.showConfirmDialog(this,"Do you want to delete the Assignment of the credential with the name " + cred.getName()+ " of the type " + cred.getCredentialType() + "?","Deletion of a credential", JOptionPane.YES_NO_CANCEL_OPTION);
						if (option == JOptionPane.YES_OPTION) {
							List<CredentialAssignment> credAssgn = WsCredentialStore.getInstance().getCredentialAssignmentsWithOneCredential(apiReg, cred);
                            for (CredentialAssignment credentialAssignment : credAssgn) {
								credAssgn.remove(credentialAssignment);
							}
							this.getJPanelAssignedCredentials().fillAssignedCredentialJList(apiReg);
							this.getJPanelAssignedCredentials().getJListAssignedCredentials().revalidate();
							this.getJPanelAssignedCredentials().getJListAssignedCredentials().repaint();
							this.revalidate();
							this.repaint();
							WsCredentialStore.getInstance().save();
						}
					}else {
						JOptionPane.showMessageDialog(this,"Please select a Credential in order to delete a Credential Assignment");
					}
			}else {
				JOptionPane.showMessageDialog(this,"Please select a Client-Bundle in order to delete a Credential Assignment");
			}

		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(this.getJPanelServerURL().getJListServerUrl())) {

		} else if (e.getSource().equals(getJPanelClientBundle().getJListApiRegistration())) {
			if (this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue() != null) {
				this.getJPanelAssignedCredentials().fillAssignedCredentialJList(this.getJPanelClientBundle().getJListApiRegistration().getSelectedValue());
			}
		} else if (e.getSource().equals(getJPanelCredentials().getJListCredentials())) {

		}
	}
}
