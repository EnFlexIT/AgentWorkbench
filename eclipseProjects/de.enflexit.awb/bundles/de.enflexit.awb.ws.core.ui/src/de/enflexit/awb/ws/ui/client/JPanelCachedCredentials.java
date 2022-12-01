package de.enflexit.awb.ws.ui.client;

import java.awt.Dimension;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import agentgui.core.config.GlobalInfo;
import de.enflexit.awb.ws.client.ApiRegistration;
import de.enflexit.awb.ws.client.AwbApiRegistrationService;
import de.enflexit.awb.ws.client.CredentialAssignment;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;

/**
 * The Class JPanelCachedCredentials.
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
public class JPanelCachedCredentials extends JPanel implements WsConfigurationInterface {

	private static final long serialVersionUID = -8432771480572412286L;
	private JScrollPane jScrollPanelCacheCredentials;
	private JList<AbstractCredential> jListCacheCredentials;
	private JLabel jLableCredAssignment;
	private JButton jButtonDeleteCredentialAssignment;
	private JPanel jPanelHeader;
	
	//Non GUI-attributes
	private String clientBundle;
	private List<CredentialAssignment> listCacheCredAssignment;
	private List<CredentialAssignment> deletedCachedCredAssignments;
	
	/**
	 * Instantiates a new j panel cached credentials.
	 *
	 * @param nameOfClientBundle the name of client bundle, can be <code>null</code>
	 */
	public JPanelCachedCredentials(String nameOfClientBundle) {
		if(nameOfClientBundle!=null) {
		  this.setClientBundleName(nameOfClientBundle);
		}
		this.initialize();
	}
	
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{265, 0};
		gridBagLayout.rowHeights = new int[]{26, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jPanelHeader = new GridBagConstraints();
		gbc_jPanelHeader.insets = new Insets(5, 0, 0, 0);
		gbc_jPanelHeader.fill = GridBagConstraints.BOTH;
		gbc_jPanelHeader.gridx = 0;
		gbc_jPanelHeader.gridy = 0;
		this.add(getJPanelHeader(), gbc_jPanelHeader);
		
		GridBagConstraints gbc_jScrollPaneAssigneCredentials = new GridBagConstraints();
		gbc_jScrollPaneAssigneCredentials.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneAssigneCredentials.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneAssigneCredentials.gridx = 0;
		gbc_jScrollPaneAssigneCredentials.gridy = 1;
		this.add(getJScrollPaneAssigneCredentials(), gbc_jScrollPaneAssigneCredentials);
	}
	
	/**
	 * Gets the {@link JScrollPane} assigned credentials.
	 *
	 * @return the {@link JScrollPane} assigned credentials
	 */
	private JScrollPane getJScrollPaneAssigneCredentials() {
		if (jScrollPanelCacheCredentials == null) {
			jScrollPanelCacheCredentials = new JScrollPane();
			jScrollPanelCacheCredentials.setViewportView(getJListAssignedCredentials());
		}
		return jScrollPanelCacheCredentials;
	}
	
	/**
	 * Gets the {@link JList} assigned credentials, which stores all assigned Credentials of an {@link AwbApiRegistrationService}.
	 *
	 * @return the {@link JList} assigned credentials
	 */
	public JList<AbstractCredential> getJListAssignedCredentials() {
		if (jListCacheCredentials == null) {
			jListCacheCredentials = new JList<AbstractCredential>();
			jListCacheCredentials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			if(getClientBundleName()!=null) {
				fillAssignedCredentialJList(getClientBundleName());
			}
		}
		return jListCacheCredentials;
	}
	
	/**
	 * Gets the list cache cred assignment.
	 *
	 * @return the list cache cred assignment
	 */
	public List<CredentialAssignment> getListCacheCredAssignment() {
		if(this.listCacheCredAssignment==null) {
			this.listCacheCredAssignment=new ArrayList<CredentialAssignment>();
		}
		return listCacheCredAssignment;
	}
	
	public List<CredentialAssignment> getDeletedCachedCredAssignments() {
		if(this.deletedCachedCredAssignments==null) {
			deletedCachedCredAssignments=new ArrayList<CredentialAssignment>();
		}
		return deletedCachedCredAssignments;
	}
	
	private JPanel getJPanelHeader() {
		if (jPanelHeader == null) {
			jPanelHeader = new JPanel();
			
			GridBagLayout gbl_jPanelHeader = new GridBagLayout();
			gbl_jPanelHeader.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelHeader.rowHeights = new int[]{0, 0};
			gbl_jPanelHeader.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelHeader.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelHeader.setLayout(gbl_jPanelHeader);
			
			GridBagConstraints gbc_jLableCredAssignment = new GridBagConstraints();
			gbc_jLableCredAssignment.anchor = GridBagConstraints.WEST;
			gbc_jLableCredAssignment.gridx = 0;
			gbc_jLableCredAssignment.gridy = 0;
			jPanelHeader.add(this.getJLableCredAssignment(), gbc_jLableCredAssignment);
					
			GridBagConstraints gbc_jButtonDeleteCredentialAssignment = new GridBagConstraints();
			gbc_jButtonDeleteCredentialAssignment.gridx = 2;
			gbc_jButtonDeleteCredentialAssignment.gridy = 0;
			jPanelHeader.add(this.getJButtonDeleteCredentialAssignment(), gbc_jButtonDeleteCredentialAssignment);
		}
		return jPanelHeader;
	}
	
	private JLabel getJLableCredAssignment() {
		if (jLableCredAssignment == null) {
			jLableCredAssignment = new JLabel("Cached Credentials");
			jLableCredAssignment.setFont(new Font("Dialog", Font.BOLD, 12));
			jLableCredAssignment.setMinimumSize(new Dimension(150,  26));
			jLableCredAssignment.setPreferredSize(new Dimension(150,  26));
		}
		return jLableCredAssignment;
	}

	
	public JButton getJButtonDeleteCredentialAssignment() {
		if (jButtonDeleteCredentialAssignment == null) {
			jButtonDeleteCredentialAssignment = new JButton(GlobalInfo.getInternalImageIcon("Delete.png"));
			jButtonDeleteCredentialAssignment.setToolTipText("Delete a credential assignment");
			jButtonDeleteCredentialAssignment.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
		}
		return jButtonDeleteCredentialAssignment;
	}
	
	public String getClientBundleName() {
		return clientBundle;
	}

	public void setClientBundleName(String clientBundleName) {
		this.clientBundle = clientBundleName;
	}
	
	//-------------------------------------------------------------------------------------
	//--------------Helping methods to initialize components-----------------------
	//-------------------------------------------------------------------------------------
	
	/**
	 * Fill assigned credential J list.
	 *
	 * @param awbRegService the corresponding {@link ApiRegistration}, which assigned Credentials should be shown
	 */
	public void fillAssignedCredentialJList(String clientBundleName) {

		List<CredentialAssignment> credAssgnList = WsCredentialStore.getInstance().getCacheCredentialAssignmentList();
		List<CredentialAssignment> credAssgnOfSelectedApi = new ArrayList<CredentialAssignment>();
		List<AbstractCredential> assgnCredentials = new ArrayList<>();

		for (Iterator<CredentialAssignment> iterator = credAssgnList.iterator(); iterator.hasNext();) {
			CredentialAssignment credentialAssignment = (CredentialAssignment) iterator.next();
			String apiId = credentialAssignment.getIdApiRegistrationDefaultBundleName();
			if (clientBundleName.equals(apiId)) {
				credAssgnOfSelectedApi.add(credentialAssignment);
				AbstractCredential abstractCred = WsCredentialStore.getInstance().getCredentialWithID(credentialAssignment.getIdCredential());
				assgnCredentials.add(abstractCred);
			}
		}
		DefaultListModel<AbstractCredential> defaultListModel= new DefaultListModel<>();
		defaultListModel.addAll(assgnCredentials);
		getJListAssignedCredentials().setModel(defaultListModel);
		this.revalidate();
		this.repaint();
	}
	

	//-------------------------------------------------------------------------------------
	//--------------Overriden Methods and Non GUI Getter and Setters-----------------------
	//-------------------------------------------------------------------------------------
	

	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	*/
	@Override
	public boolean hasUnsavedChanges() {
		if(this.getDeletedCachedCredAssignments().size()>0) {
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
}
