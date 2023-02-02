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
 * The Class JPanelAssignedCredential inteface for showing assigned Credentials.
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
public class JPanelAssignedCredentials extends JPanel implements WsConfigurationInterface{

	private static final long serialVersionUID = 4478895396272005153L;
	private JScrollPane jScrollPaneAssignedCredentials;
	private JList<AbstractCredential> jListAssignedCredentials;
	private JLabel jLableCredAssignment;
	private JButton jButtonCreateACredentialAssignment;
	private JButton jButtonDeleteCredentialAssignment;
	private JPanel jPanelHeader;
	
	//Non GUI-attributes
	private ApiRegistration apiRegCurr;
	private List<CredentialAssignment> listCacheCredAssignment;
	private List<CredentialAssignment> listCacheDeletedCredAssignment;
	
	public JPanelAssignedCredentials(ApiRegistration apiReg) {
		setApiRegCurr(apiReg);
		this.initialize();
	}
	
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{265, 0};
		gridBagLayout.rowHeights = new int[] {26, 75, 0};
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
	 * Gets the j scroll pane assigne credentials.
	 *
	 * @return the j scroll pane assigne credentials
	 */
	private JScrollPane getJScrollPaneAssigneCredentials() {
		if (jScrollPaneAssignedCredentials == null) {
			jScrollPaneAssignedCredentials = new JScrollPane();
			jScrollPaneAssignedCredentials.setViewportView(getJListAssignedCredentials());
		}
		return jScrollPaneAssignedCredentials;
	}
	
	/**
	 * Gets the j list assigned credentials, which stores all assigned Credentials of an {@link AwbApiRegistrationService}.
	 *
	 * @return the j list assigned credentials
	 */
	public JList<AbstractCredential> getJListAssignedCredentials() {
		if (jListAssignedCredentials == null) {
			jListAssignedCredentials = new JList<AbstractCredential>();
			jListAssignedCredentials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			if(getApiRegCurr()!=null) {
				fillAssignedCredentialJList(getApiRegCurr());
			}
		}
		return jListAssignedCredentials;
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
			
			GridBagConstraints gbc_jButtonCreateACredentialAssignment = new GridBagConstraints();
			gbc_jButtonCreateACredentialAssignment.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonCreateACredentialAssignment.gridx = 1;
			gbc_jButtonCreateACredentialAssignment.gridy = 0;
			jPanelHeader.add(this.getJButtonCreateACredentialAssignment(), gbc_jButtonCreateACredentialAssignment);
			
			GridBagConstraints gbc_jButtonDeleteCredentialAssignment = new GridBagConstraints();
			gbc_jButtonDeleteCredentialAssignment.gridx = 2;
			gbc_jButtonDeleteCredentialAssignment.gridy = 0;
			jPanelHeader.add(this.getJButtonDeleteCredentialAssignment(), gbc_jButtonDeleteCredentialAssignment);
		}
		return jPanelHeader;
	}
	
	private JLabel getJLableCredAssignment() {
		if (jLableCredAssignment == null) {
			jLableCredAssignment = new JLabel("Assign Credentials");
			jLableCredAssignment.setFont(new Font("Dialog", Font.BOLD, 12));
			jLableCredAssignment.setMinimumSize(new Dimension(150,  26));
			jLableCredAssignment.setPreferredSize(new Dimension(150,  26));
		}
		return jLableCredAssignment;
	}
	public JButton getJButtonCreateACredentialAssignment() {
		if (jButtonCreateACredentialAssignment == null) {
			jButtonCreateACredentialAssignment = new JButton(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonCreateACredentialAssignment.setToolTipText("Create a credential assignment");
			jButtonCreateACredentialAssignment.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCreateACredentialAssignment.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
		}
		return jButtonCreateACredentialAssignment;
	}
	public JButton getJButtonDeleteCredentialAssignment() {
		if (jButtonDeleteCredentialAssignment == null) {
			jButtonDeleteCredentialAssignment = new JButton(GlobalInfo.getInternalImageIcon("Delete.png"));
			jButtonDeleteCredentialAssignment.setToolTipText("Delete a credential assignment");
			jButtonDeleteCredentialAssignment.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonDeleteCredentialAssignment.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
		}
		return jButtonDeleteCredentialAssignment;
	}
	
	public ApiRegistration getApiRegCurr() {
		return apiRegCurr;
	}

	public void setApiRegCurr(ApiRegistration apiRegCurr) {
		this.apiRegCurr = apiRegCurr;
	}
	
	//-------------------------------------------------------------------------------------
	//--------------Helping methods to initialoie components-----------------------
	//-------------------------------------------------------------------------------------
	
	/**
	 * Fill assigned credential J list.
	 *
	 * @param awbRegService the corresponding {@link ApiRegistration}, which assigned Credentials should be shown
	 */
	public void fillAssignedCredentialJList(ApiRegistration awbRegService) {

		List<CredentialAssignment> credAssgnList = WsCredentialStore.getInstance().getCredentialAssignmentList();
		List<CredentialAssignment> credAssgnOfSelectedApi = new ArrayList<CredentialAssignment>();
		List<AbstractCredential> assgnCredentials = new ArrayList<>();

		for (Iterator<CredentialAssignment> iterator = credAssgnList.iterator(); iterator.hasNext();) {
			CredentialAssignment credentialAssignment = (CredentialAssignment) iterator.next();
			String apiId = credentialAssignment.getIdApiRegistrationDefaultBundleName();
			if (awbRegService.getClientBundleName().equals(apiId)) {
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


	public List<CredentialAssignment> getListCacheDeletedCredAssignment() {
		if(this.listCacheDeletedCredAssignment==null) {
			this.listCacheDeletedCredAssignment=new ArrayList<CredentialAssignment>();;
		}
		return listCacheDeletedCredAssignment;
	}

	/**
	 * Instantiates a new j panel assigned credentials.
	 */
	public JPanelAssignedCredentials() {
		this.initialize();
	}

	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	*/
	@Override
	public boolean hasUnsavedChanges() {
		if(this.getListCacheCredAssignment().size()>0) {
			return true;
		}
		
	    if(this.getListCacheCredAssignment().size()>0) {
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
