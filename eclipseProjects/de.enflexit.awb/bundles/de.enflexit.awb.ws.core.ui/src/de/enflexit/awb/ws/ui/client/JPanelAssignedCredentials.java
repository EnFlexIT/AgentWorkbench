package de.enflexit.awb.ws.ui.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import agentgui.core.config.GlobalInfo;
import de.enflexit.awb.ws.client.ApiRegistration;
import de.enflexit.awb.ws.client.CredentialAssignment;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.AbstractCredential;

public class JPanelAssignedCredentials extends JPanel {

	private static final long serialVersionUID = 4478895396272005153L;
	private JScrollPane jScrollPaneAssigneCredentials;
	private JList<AbstractCredential> jListAssignedCredentials;
	private JLabel jLableCredAssignment;
	private JButton jButtonCreateACredentialAssignment;
	private JButton jButtonDeleteCredentialAssignment;
	private JPanel jPanelHeader;
	
	
	public JPanelAssignedCredentials() {
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
	
	private JScrollPane getJScrollPaneAssigneCredentials() {
		if (jScrollPaneAssigneCredentials == null) {
			jScrollPaneAssigneCredentials = new JScrollPane();
			jScrollPaneAssigneCredentials.setViewportView(getJListAssignedCredentials());
		}
		return jScrollPaneAssigneCredentials;
	}
	private JList<AbstractCredential> getJListAssignedCredentials() {
		if (jListAssignedCredentials == null) {
			jListAssignedCredentials = new JList<AbstractCredential>();
			jListAssignedCredentials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jListAssignedCredentials;
	}
	
	public void fillAssignedCredentialJList(ApiRegistration awbRegService) {

		List<CredentialAssignment> credAssgnList = WsCredentialStore.getInstance().getCredentialAssignmentList();
		List<CredentialAssignment> credAssgnOfSelectedApi = new ArrayList<CredentialAssignment>();
		List<AbstractCredential> assgnCredentials = new ArrayList<>();

		for (Iterator<CredentialAssignment> iterator = credAssgnList.iterator(); iterator.hasNext();) {
			CredentialAssignment credentialAssignment = (CredentialAssignment) iterator.next();
			String apiId = credentialAssignment.getIdApiRegistrationDefaultBundleName();
			if (awbRegService.getClientBundleName() == apiId) {
				credAssgnOfSelectedApi.add(credentialAssignment);
				AbstractCredential abstractCred = WsCredentialStore.getInstance().getCredential(credentialAssignment.getIdCredential());
				assgnCredentials.add(abstractCred);
			}
		}
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
	private JButton getJButtonCreateACredentialAssignment() {
		if (jButtonCreateACredentialAssignment == null) {
			jButtonCreateACredentialAssignment = new JButton(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonCreateACredentialAssignment.setToolTipText("Create a credential assignment");
			jButtonCreateACredentialAssignment.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
		}
		return jButtonCreateACredentialAssignment;
	}
	private JButton getJButtonDeleteCredentialAssignment() {
		if (jButtonDeleteCredentialAssignment == null) {
			jButtonDeleteCredentialAssignment = new JButton(GlobalInfo.getInternalImageIcon("Delete.png"));
			jButtonDeleteCredentialAssignment.setToolTipText("Delete a credential assignment");
			jButtonDeleteCredentialAssignment.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
		}
		return jButtonDeleteCredentialAssignment;
	}
	
	
}
