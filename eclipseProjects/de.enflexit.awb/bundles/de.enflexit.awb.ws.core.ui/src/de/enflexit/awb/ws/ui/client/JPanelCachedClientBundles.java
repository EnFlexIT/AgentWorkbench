package de.enflexit.awb.ws.ui.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import agentgui.core.config.GlobalInfo;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.client.ApiRegistration;
import de.enflexit.awb.ws.client.CredentialAssignment;
import de.enflexit.awb.ws.client.CredentialType;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.credential.ApiKeyCredential;
import de.enflexit.awb.ws.credential.BearerTokenCredential;
import de.enflexit.awb.ws.credential.UserPasswordCredential;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;

/**
 * The Class JPanelCachedClientBundles.
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
public class JPanelCachedClientBundles extends JPanel implements WsConfigurationInterface{

	private static final long serialVersionUID = 6774308085043520145L;
	private JScrollPane jScrollPaneBundleList;
	private JList<String> jListCachedApiRegistration;
	private DefaultListModel<String> listModelCachedRegisteredApis;
	private JButton jButtonDeleteCachedCredentialAssignment;
	private JButton jButtonBackToCredAssgnView;
	
	//Variables to track changes
	private List<String> deletedApiRegistrations;
	private List<AbstractCredential> deletedCredentials;
	private JPanel jPanelHeader;
	private JLabel jLableCachedClientBundles;
	

	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelCachedClientBundles() {
		this.initialize();
	}
	
	private void initialize() {		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{26, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(getJPanelHeader(), gbc_panel);
		GridBagConstraints gbc_jScrollPaneBundleList = new GridBagConstraints();
		gbc_jScrollPaneBundleList.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneBundleList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneBundleList.gridx = 0;
		gbc_jScrollPaneBundleList.gridy = 1;
		add(getJScrollPaneBundleList(), gbc_jScrollPaneBundleList);
	}
	
	private JPanel getJPanelHeader() {
		if (jPanelHeader == null) {
			jPanelHeader = new JPanel();
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{0, 0};
			gbl_panel.rowHeights = new int[]{0, 0};
			gbl_panel.columnWeights = new double[]{1.0, 0.0};
			gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelHeader.setLayout(gbl_panel);
			GridBagConstraints gbc_jLableCachedClientBundles = new GridBagConstraints();
			gbc_jLableCachedClientBundles.gridx = 0;
			gbc_jLableCachedClientBundles.gridy = 0;
			jPanelHeader.add(getJLableCachedClientBundles(), gbc_jLableCachedClientBundles);
			GridBagConstraints gbc_jButtonDeleteCredentialAssignment = new GridBagConstraints();
			gbc_jButtonDeleteCredentialAssignment.gridx = 2;
			gbc_jButtonDeleteCredentialAssignment.gridy = 0;
			jPanelHeader.add(this.getJButtonDeleteCredentialAssignment(), gbc_jButtonDeleteCredentialAssignment);
			GridBagConstraints gbc_jButtonCreateACredentialAssignment = new GridBagConstraints();
			gbc_jButtonCreateACredentialAssignment.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonCreateACredentialAssignment.gridx = 1;
			gbc_jButtonCreateACredentialAssignment.gridy = 0;
			jPanelHeader.add(this.getJButtonBackToCredAssgnView(), gbc_jButtonCreateACredentialAssignment);
		}
		return jPanelHeader;
	}
	
	public JButton getJButtonDeleteCredentialAssignment() {
		if (jButtonDeleteCachedCredentialAssignment == null) {
			jButtonDeleteCachedCredentialAssignment = new JButton(GlobalInfo.getInternalImageIcon("Delete.png"));
			jButtonDeleteCachedCredentialAssignment.setToolTipText("Delete a credential assignment");
			jButtonDeleteCachedCredentialAssignment.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
		}
		return jButtonDeleteCachedCredentialAssignment;
	}
	
	public JButton getJButtonBackToCredAssgnView() {
		if (jButtonBackToCredAssgnView == null) {
			jButtonBackToCredAssgnView = new JButton(BundleHelper.getImageIcon("MBreset.png"));
			jButtonBackToCredAssgnView.setToolTipText("Go back to normal CredentialAssignment and ServerAPI/Client-Bundle view");
			jButtonBackToCredAssgnView.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
		}
		return jButtonBackToCredAssgnView;
	}
	/**
	 * Gets all deleted api registrations.
	 *
	 * @return all deleted api registrations
	 */
	public List<String> getDeletedApiRegistrations() {
		if(deletedApiRegistrations==null) {
			deletedApiRegistrations=new ArrayList<String>();
		}
		return deletedApiRegistrations;
	}

	/**
	 * Sets the deleted api registrations.
	 *
	 * @param deletedApiRegistration the new deleted api registrations
	 */
	public void setDeletedApiRegistration(List<String> deletedApiRegistration) {
		this.deletedApiRegistrations = deletedApiRegistration;
	}
	
	/**
	 * Gets all deleted api registrations.
	 *
	 * @return all deleted api registrations
	 */
	public List<AbstractCredential> getDeletedCredentials() {
		if(deletedCredentials==null) {
			deletedCredentials=new ArrayList<AbstractCredential>();
		}
		return deletedCredentials;
	}

	/**
	 * Sets the deleted credential.
	 * @param deletedCredentials the new deleted credential
	 */
	public void setDeletedCredential(List<AbstractCredential> deletedCredentials) {
		this.deletedCredentials = deletedCredentials;
	}
	
	/**
	 * Gets the {@link JScrollPane} of the {@link ApiRegistration}-CachedBundle list.
	 *
	 * @return the {@link JScrollPane} bundle list
	 */
	private JScrollPane getJScrollPaneBundleList() {
		if (jScrollPaneBundleList == null) {
			jScrollPaneBundleList = new JScrollPane();
			jScrollPaneBundleList.setViewportView(getJListCachedApiRegistration());
		}
		return jScrollPaneBundleList;
	}
	
	/**
	 * Gets the corresponding {@link ListModel} of the JListCachedApiRegistration.
	 *
	 * @return the list model registered apis
	 */
	private DefaultListModel<String> getListModelCachedRegisteredApis() {
		if (listModelCachedRegisteredApis==null) {
			this.refillListModelCachedRegisteredApis();
		}
		return listModelCachedRegisteredApis;
	}

	/**
	 * Creates and fills a new {@link ListModel} for the JListAPIRegistration.
	 */
	public void refillListModelCachedRegisteredApis() {
		listModelCachedRegisteredApis = new DefaultListModel<String>();
		WsCredentialStore.getInstance().getBundleCredAssgnsMap().keySet();	
		List<String> apiRegServiceList=new ArrayList<String>(WsCredentialStore.getInstance().getBundleCredAssgnsMap().keySet());
		listModelCachedRegisteredApis.addAll(apiRegServiceList);
		this.getJListCachedApiRegistration().setModel(listModelCachedRegisteredApis);
		this.repaint();
		this.revalidate();
	}

	/**
	 * Gets the j list cached api registration.
	 *
	 * @return the j list cached api registration
	 */
	public JList<String> getJListCachedApiRegistration() {
		if (jListCachedApiRegistration == null) {
			jListCachedApiRegistration = new JList<String>(this.getListModelCachedRegisteredApis());
			jListCachedApiRegistration.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListCachedApiRegistration.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jListCachedApiRegistration;
	}

	
	/**
	 * Delete all cached credential assignments.
	 *
	 * @param clientBundleName the client bundle name
	 * @return the deleted CredentialAssignments as a list
	 */
	public List<CredentialAssignment> deleteAllCachedCredentialAssignments() {
		if(this.getJListCachedApiRegistration().isSelectionEmpty()) {
		 JOptionPane.showConfirmDialog(this, "Please select a credential assignment!","Select a credential assignment", JOptionPane.YES_OPTION);
		 return new ArrayList<CredentialAssignment>();
		}else {
	    int ret = JOptionPane.showConfirmDialog(this, "You want to delete all selected cached client bundles and their credential assignments?","Delete client bundles and credential assignments", JOptionPane.YES_OPTION);
		//Delete all credential assignments of the bundle
	    List<CredentialAssignment> deletedClientBundles= new ArrayList<CredentialAssignment>();
	    if(ret== JOptionPane.YES_OPTION) {
		    List<String> clientBundleNames= this.getJListCachedApiRegistration().getSelectedValuesList();
		    for (Iterator<String> iterator = clientBundleNames.iterator(); iterator.hasNext();) {
				String clientBundleName = (String) iterator.next();
				deleteEmptyCredentials(clientBundleName);
				WsCredentialStore.getInstance().getCacheCredentialAssignmentList().removeAll(WsCredentialStore.getInstance().getBundleCredAssgnsMap().get(clientBundleName));
				deletedClientBundles.addAll(WsCredentialStore.getInstance().getBundleCredAssgnsMap().remove(clientBundleName));
				this.getDeletedApiRegistrations().add(clientBundleName);
			}
	    }

		this.refillListModelCachedRegisteredApis();
	    //refresh
		this.revalidate();
		this.repaint();
		return deletedClientBundles;
		}
	}

	/**
	 * Delete empty credentials.
	 *
	 * @param clientBundleName the client bundle name
	 */
	private void deleteEmptyCredentials(String clientBundleName) {
		List<CredentialAssignment> credAssgns=WsCredentialStore.getInstance().getBundleCredAssgnsMap().get(clientBundleName);
		for (Iterator<CredentialAssignment> credAssignments = credAssgns.iterator(); credAssignments.hasNext();) {
			CredentialAssignment credentialAssignment = (CredentialAssignment) credAssignments.next();
			AbstractCredential credential=WsCredentialStore.getInstance().getCredential(credentialAssignment.getIdCredential());
		    if(credential!=null) {
			if (credential.getCredentialType() == CredentialType.API_KEY) {
				ApiKeyCredential apiKey=(ApiKeyCredential) credential;
				if(apiKey.isEmpty()) {
				  WsCredentialStore.getInstance().getCredentialList().remove(credential);
				  this.getDeletedCredentials().add(credential);
				}
			} else if (credential.getCredentialType() == CredentialType.BEARER_TOKEN) {
				BearerTokenCredential bearerToken=(BearerTokenCredential) credential;
		        if(bearerToken.isEmpty()) {
		        	WsCredentialStore.getInstance().getCredentialList().remove(credential);
		        	this.getDeletedCredentials().add(credential);
				}
			} else if (credential.getCredentialType() == CredentialType.USERNAME_PASSWORD) {
				UserPasswordCredential password=(UserPasswordCredential) credential;
		        if(password.isEmpty()) {
		        	WsCredentialStore.getInstance().getCredentialList().remove(credential);
		        	this.getDeletedCredentials().add(credential);
				}
			}
		   }
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		if(this.getDeletedApiRegistrations().size()>=1) {
			return true;
		}
		
		if(this.getDeletedCredentials().size()>=1) {
			return true;
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	 */
	@Override
	public boolean userConfirmedToChangeView() {
		return hasUnsavedChanges();
	}

	private JLabel getJLableCachedClientBundles() {
		if (jLableCachedClientBundles == null) {
			jLableCachedClientBundles = new JLabel("Cached Server-API / Client-Bundle");
			jLableCachedClientBundles.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableCachedClientBundles;
	}

}